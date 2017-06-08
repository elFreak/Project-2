package model;

/**
 * Representiert eine gemessene Sprungantwort. Diese kann auf verschiedene Arten
 * in Echtzeit bearbeited werden.
 * 
 * @author Team 1
 *
 */
public class MeasurementData {

	// ---Atribute
	// -------------------------------------------------------------------------------------------------------
	// Konstanten um auf die entsprechenden Daten zuzugreiffen
	public static final int XAXIS = 0;
	public static final int MEASUREMENTS = 1;
	public static final int STEP = 2;

	private Model model;

	private double rawData[][];
	private double meanData[][];
	private double finalData[][]; // Data f�r weitere Berechnungen

	private double offset = 0;
	private int filter = 0;
	private double tail = 0;
	private double stepTime = 0;
	private double originalStepTime = 0;
	private double stepHeight = 1;
	private double originalStepHeight = 1;
	private double[][] stepData;
	private double[][] originalStep;
	private double[] timeFullNormed;
	private double[] stepFullNormed;
	private double[] timeLenghtNormed;
	private double timeScaleFactor;
	private int normNumberOfData = 200;
	// -------------------------------------------------------------------------------------------------------

	// Konstruktor
	// -------------------------------------------------------------------------------------------------------
	/**
	 * 
	 * Erzeugt das Objekt anhand der �bergebenen Daten.
	 * 
	 * @param model
	 * @param data
	 * @throws IllegalArgumentException
	 */
	public MeasurementData(Model model, double data[][]) throws IllegalArgumentException {
		this.model = model;

		// check for exceptions
		if (data.length > 3 || data.length < 1 || data[0].length < 1)
			throw new IllegalArgumentException(data.length + " " + data[1].length);

		// update attributes
		rawData = new double[2][data[XAXIS].length];
		meanData = new double[2][data[XAXIS].length];
		finalData = new double[2][data[XAXIS].length];
		stepData = new double[2][data[XAXIS].length];
		originalStep = new double[2][data[XAXIS].length];
		for (int i = 0; i < data[XAXIS].length; i++) {
			rawData[XAXIS][i] = data[XAXIS][i];
			meanData[XAXIS][i] = data[XAXIS][i];
			finalData[XAXIS][i] = data[XAXIS][i];
			stepData[XAXIS][i] = data[XAXIS][i];
			originalStep[XAXIS][i] = data[XAXIS][i];

			rawData[MEASUREMENTS][i] = data[MEASUREMENTS][i];
			meanData[MEASUREMENTS][i] = data[MEASUREMENTS][i];
			finalData[MEASUREMENTS][i] = data[MEASUREMENTS][i];

			// Sprung berechnen
			// Wenn Sprung vorhanden
			if (data.length == 3) {
				stepData[MEASUREMENTS][i] = data[STEP][i];
				originalStep[MEASUREMENTS][i] = data[STEP][i];

				// stepTime und stepHeight berechnen
				if (i != 0) {
					if (data[STEP][i] != 0 && data[STEP][i - 1] == 0) {
						stepTime = data[XAXIS][i];
						originalStepTime = data[XAXIS][i];
						stepHeight = data[STEP][i];
						originalStepHeight = data[STEP][i];
					}
				}
			}
			// Wenn kein Sprung vorhanden
			else {
				stepData[MEASUREMENTS][i] = 1;
				originalStep[MEASUREMENTS][i] = 1;

			}

		}
		updateFinalData();

	}
	// -------------------------------------------------------------------------------------------------------

	// Set Methoden
	// -------------------------------------------------------------------------------------------------------
	/**
	 * Ver�ndert die L�nge auf welche das Signal zu berechnungszwecken
	 * geschrumpft werden soll.
	 * 
	 * @param norm
	 */
	public void setNorm(int norm) {
		this.normNumberOfData = norm;
		updateFinalData();
		model.notifyObservers(Model.NOTIFY_REASON_THRESHOLD_OR_NORM_CHANGED);
	}

	/**
	 * Gleitender Mittelwert der Daten berechnen. Parameter n bestimmt �ber wie
	 * viele Werte gemittelt wird. Falls n=0 dann ist der Filter deaktiviert.
	 * �ndert das Signal in Echtzeit.
	 * 
	 * @param n
	 */
	public void setMovingMean(int n) {
		// Kein Mittelwert bilden
		filter = n;
		if (n == 0) {
			for (int i = 0; i < rawData[MEASUREMENTS].length; i++) {
				meanData[MEASUREMENTS][i] = rawData[MEASUREMENTS][i];
			}
		}
		// Mittelwert bilden
		else {
			for (int i = 0; i < rawData[MEASUREMENTS].length; i++) {
				int count = 0;
				meanData[MEASUREMENTS][i] = 0;
				for (int j = i - n; j <= i + n; j++) {
					if (j >= 0 && j < rawData[MEASUREMENTS].length) {
						meanData[MEASUREMENTS][i] += rawData[MEASUREMENTS][j];
						count = count + 1;
					}
				}
				meanData[MEASUREMENTS][i] /= count;
			}
		}

		updateFinalData();

		model.notifyObservers(Model.NOTIFY_REASON_MEASUREMENT_CHANGED);

	}

	/**
	 * Setzt den Offset und wieviel vom Signal hinten abgeschnitten wird. �ndert
	 * das Signal in Echtzeit.
	 * 
	 * @param offset
	 * @param tail
	 */
	public void setLimits(double offset, double tail) {

		this.offset = offset;

		// tail vom hintersten Datenpunkt aus gemessen.
		if (tail > meanData[XAXIS][meanData[XAXIS].length - 1])
			this.tail = 0;
		else
			this.tail = meanData[XAXIS][meanData[XAXIS].length - 1] - tail;

		updateFinalData();

		model.notifyObservers(Model.NOTIFY_REASON_MEASUREMENT_CHANGED);
	}

	/**
	 * Setzt den Offset und wieviel vom Signal hinten abgeschnitten wird
	 * automatisch. �ndert das Signal in Echtzeit.
	 * 
	 */
	public void autoLimits() {
		int n;
		double delta;
		int frontIndex = 0;
		int tailIndex = 0;
		double offset;
		double meanOffsetData[][];

		// n bestimmen
		n = 0;
		for (int i = 0; i < this.meanData[XAXIS].length - 1; i++) {
			if (this.meanData[XAXIS][n] <= this.meanData[XAXIS][this.meanData[XAXIS].length - 1] / 100)
				n++;
			else
				break;
		}
		n = n < 5 ? 5 : n;

		// Automatische Erkennung des Offsets
		// -------------------------------------------------------------------------------------------------------
		offset = 0;
		for (int i = 0; i < n; i++) {
			offset += this.meanData[MEASUREMENTS][i];
		}

		offset /= n;

		meanOffsetData = new double[this.meanData.length][this.meanData[MEASUREMENTS].length];
		for (int i = 0; i < meanOffsetData[MEASUREMENTS].length; i++) {
			meanOffsetData[XAXIS][i] = this.meanData[XAXIS][i];
			meanOffsetData[MEASUREMENTS][i] = this.meanData[MEASUREMENTS][i] - offset;
		}

		// Automatische Erkennung des Endes
		// -------------------------------------------------------------------------------------------------------
		// delta bestimmen
		int iMinD = 0;
		for (int i = 1; i < meanOffsetData[MEASUREMENTS].length; i++) {
			if (meanOffsetData[MEASUREMENTS][i] < meanOffsetData[MEASUREMENTS][iMinD])
				iMinD = i;
		}
		int iMaxD = 0;
		for (int i = 1; i < meanOffsetData[MEASUREMENTS].length; i++) {
			if (meanOffsetData[MEASUREMENTS][i] > meanOffsetData[MEASUREMENTS][iMaxD])
				iMaxD = i;
		}

		double end = 0;
		for (int i = 0; i < n; i++) {
			end += meanOffsetData[MEASUREMENTS][meanOffsetData[MEASUREMENTS].length - 1 - i];
		}

		end /= n;

		delta = Math.abs(meanOffsetData[MEASUREMENTS][iMaxD] - end) > Math
				.abs(meanOffsetData[MEASUREMENTS][iMinD] - end) ? Math.abs(meanOffsetData[MEASUREMENTS][iMaxD] - end)
						: Math.abs(meanOffsetData[MEASUREMENTS][iMinD] - end);

		// noise bestimmen
		int iMinN = 0;
		for (int i = 1; i < n; i++) {
			if (meanOffsetData[MEASUREMENTS][i] < meanOffsetData[MEASUREMENTS][iMinN])
				iMinN = i;
		}
		int iMaxN = 0;
		for (int i = 1; i < n; i++) {
			if (meanOffsetData[MEASUREMENTS][i] > meanOffsetData[MEASUREMENTS][iMaxN])
				iMaxN = i;
		}

		double noise = Math.abs(meanOffsetData[MEASUREMENTS][iMaxN] - meanOffsetData[MEASUREMENTS][iMinN]);

		// tail bestimmen

		for (int i = 0; i < meanOffsetData[MEASUREMENTS].length; i++) {
			if (Math.abs(meanOffsetData[MEASUREMENTS][meanOffsetData[MEASUREMENTS].length - 1 - i] - end) < noise * 0.75
					+ delta * 0.01)
				tailIndex++;
			else
				break;

		}

		tailIndex = meanOffsetData[MEASUREMENTS].length - 1 - tailIndex;

		if (tailIndex <= frontIndex)
			tailIndex = frontIndex + 1;
		if (tailIndex >= meanOffsetData[XAXIS].length) {
			tailIndex--;
			frontIndex--;
		}

		// Data aktualisieren
		// ---------------------------------------------------------------------------------------------
		this.tail = meanOffsetData[XAXIS][meanOffsetData[XAXIS].length - 1] - meanOffsetData[XAXIS][tailIndex];
		this.offset = offset;

		updateFinalData();

		model.notifyObservers(Model.NOTIFY_REASON_MEASUREMENT_CHANGED);
	}

	/**
	 * Setzt die Sprungzeit. �ndert das Signal in Echtzeit.
	 * 
	 * @param stepTime
	 */
	public void setStepTime(double stepTime) {
		this.stepTime = stepTime;

		// stepData aktualisieren
		for (int i = 0; i < rawData[XAXIS].length; i++) {
			if (rawData[XAXIS][i] < stepTime) {
				stepData[MEASUREMENTS][i] = 0;
			} else {
				stepData[MEASUREMENTS][i] = stepHeight;
			}
		}

		updateFinalData();

		model.notifyObservers(Model.NOTIFY_REASON_MEASUREMENT_CHANGED);
	}

	/**
	 * Setzt die Sprungh�he. �ndert das Signal in Echtzeit.
	 * 
	 * @param stepHeight
	 */
	public void setStepHeight(double stepHeight) {
		this.stepHeight = stepHeight;

		// stepData aktualisieren
		for (int i = 0; i < stepData[MEASUREMENTS].length; i++) {
			if (stepData[MEASUREMENTS][i] != 0) {
				stepData[MEASUREMENTS][i] = stepHeight;
			}
		}

		model.notifyObservers(Model.NOTIFY_REASON_MEASUREMENT_CHANGED);

	}

	/**
	 * Setzt den Schritt zur�ck auf die urspr�nglichen Werte. Falls kein Werte
	 * vorhanden sind, wird die Sprungzeit auf 0s und die Sprungh�he auf 1V
	 * gesetzt.
	 * 
	 */
	public void setOriginalStep() {
		for (int i = 0; i < stepData[MEASUREMENTS].length; i++) {
			stepData[MEASUREMENTS][i] = originalStep[MEASUREMENTS][i];
		}
		stepHeight = originalStepHeight;
		stepTime = originalStepTime;

		updateFinalData();

		model.notifyObservers(Model.NOTIFY_REASON_MEASUREMENT_CHANGED);
	}

	private void updateFinalData() {
		int frontIndex = 0;
		int tailIndex = 0;
		// Indexe der Zeiten berechnen
		for (int i = 0; i < meanData[XAXIS].length; i++) {
			// Index des stepTimes bestimmen
			if (meanData[XAXIS][i] < stepTime) {
				frontIndex++;
			}
			// Index des Tails bestimmen
			if (meanData[XAXIS][i] < meanData[XAXIS][meanData[XAXIS].length - 1] - tail) {
				tailIndex++;
			}
		}
		// finalData aktualisieren
		if (tailIndex <= frontIndex)
			tailIndex = frontIndex + 1;
		if (tailIndex >= meanData[XAXIS].length) {
			tailIndex--;
			frontIndex--;
		}

		finalData = new double[meanData.length][tailIndex - frontIndex + 1];
		for (int i = 0; i < finalData[XAXIS].length; i++) {
			finalData[XAXIS][i] = meanData[XAXIS][i + frontIndex] - meanData[XAXIS][frontIndex];
			finalData[MEASUREMENTS][i] = meanData[MEASUREMENTS][i + frontIndex] - offset;
		}

		// Final Daten normieren:
		double[][] stepResponseMeasurement = scalingStepResponse(getFinalData());
		timeFullNormed = stepResponseMeasurement[0];
		stepFullNormed = stepResponseMeasurement[1];
		timeLenghtNormed = stepResponseMeasurement[2];

	}

	private double[][] scalingStepResponse(double[][] stepResponseOriginal) {

		// Berechnet den Faktor um die Anzahl der Messpunkte zu normieren:
		int originalLenght = stepResponseOriginal[0].length;
		int factor = originalLenght / normNumberOfData;
		factor = (int) Math.max(1.0, factor);

		// Berechnet den Faktor um die Zeitachse zu normieren:
		timeScaleFactor = Math.log10(stepResponseOriginal[0][stepResponseOriginal[0].length - 1]) - 1;

		// Erzeugt die Strucktur der normierten Daten:
		int newLenght = originalLenght / factor;
		double[][] stepResponseNew = new double[3][newLenght];

		// Erstellt die normierten Daten:
		for (int i = 0; i < newLenght; i++) {
			stepResponseNew[0][i] = stepResponseOriginal[0][i * factor] * Math.pow(10.0, -timeScaleFactor); // Zeit
																											// normiert
																											// und
																											// Anzahl
																											// Messwerte
																											// normiert.
			stepResponseNew[1][i] = stepResponseOriginal[1][i * factor]; // Anzahl
																			// Messwerte
																			// normiert.
			stepResponseNew[2][i] = stepResponseOriginal[0][i * factor]; // Anzahl
																			// Messwerte
																			// normiert.
		}

		return stepResponseNew;
	}

	// Get Methoden
	// -------------------------------------------------------------------------------------------------------
	public double[][] getRawData() {
		double rawData[][] = new double[this.rawData.length][this.rawData[XAXIS].length];
		for (int i = 0; i < rawData[0].length; i++) {
			rawData[XAXIS][i] = this.rawData[XAXIS][i];
			rawData[MEASUREMENTS][i] = this.rawData[MEASUREMENTS][i];

		}
		return rawData;
	}

	public double[][] getFinalData() {
		double finalData[][] = new double[this.finalData.length][this.finalData[XAXIS].length];
		for (int i = 0; i < finalData[0].length; i++) {
			finalData[XAXIS][i] = this.finalData[XAXIS][i];
			finalData[MEASUREMENTS][i] = this.finalData[MEASUREMENTS][i];
		}

		return finalData;
	}

	public double[][] getMeanData() {
		double meanData[][] = new double[this.meanData.length][this.meanData[XAXIS].length];
		for (int i = 0; i < meanData[0].length; i++) {
			meanData[XAXIS][i] = this.meanData[XAXIS][i];
			meanData[MEASUREMENTS][i] = this.meanData[MEASUREMENTS][i];
		}
		return meanData;
	}

	public double getOffset() {
		return offset;
	}

	public double getTail() {
		return meanData[XAXIS][meanData[XAXIS].length - 1] - tail;
	}

	public double[][] getstep() {
		double stepData[][] = new double[this.stepData.length][this.stepData[XAXIS].length];
		for (int i = 0; i < stepData[0].length; i++) {
			stepData[XAXIS][i] = this.stepData[XAXIS][i];
			stepData[MEASUREMENTS][i] = this.stepData[MEASUREMENTS][i];
		}
		return stepData;
	}

	public double getstepTime() {
		return stepTime;
	}

	public double getstepHeight() {
		return stepHeight;
	}

	public double[] getTimeFullNormed() {
		return timeFullNormed;
	}

	public double[] getStepFullNormed() {
		return stepFullNormed;
	}

	public double[] getTimeLenghtNormed() {
		return timeLenghtNormed;
	}

	public int getNormNumberOfData() {
		return normNumberOfData;
	}

	public double getTimeScaleFactor() {
		return timeScaleFactor;
	}

	public int getFilter() {
		return filter;
	}
	// -------------------------------------------------------------------------------------------------------

}
