package model;

import java.util.Arrays;
import java.util.List;

import javax.swing.SwingWorker;

import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.NelderMeadSimplex;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.SimplexOptimizer;

import com.sun.swing.internal.plaf.metal.resources.metal;

import speicher.StartValueSaver;
import userInterface.StatusBar;

public class Approximation extends SwingWorker<Object, Integer> {

	MeasurementData measurementData;
	Model model;

	public double[][] stepAnswer;

	public Approximation(MeasurementData measurementData, Model model) {
		this.measurementData = measurementData;
		this.model = model;
	}

	public void berechne() {

		// Target target = new
		// Target(scalingTime(measurementData.getMeanData())[0],
		// measurementData.getMeanData()[1], 3);

		// Zeitvektor normieren
		double[] time_normiert = scalingTime(measurementData.getFinalData())[0];

		//Target ist die Funktion, welche minimiert werden soll
		Target target = new Target(time_normiert, measurementData.getFinalData()[1]);
		System.out.println("" + time_normiert.length);
		for (int i = 0; i < time_normiert.length; i++) {
			System.out.println("" + time_normiert[i]);
		}
		

		// Optimierer1 starten
		SimplexOptimizer optimizer = new SimplexOptimizer(1e-10, 1e-6); // (relativer Threshold,absoluter Threshold)
			
		/**
		 *  Max Eval: Anzahl Iterationen?
		 *  ObjectiveFunction: Zielfunktion (target) �bergeben
		 *  GoalType: Minimeren weil Minimum gesucht
		 *  InitialGuess: Array mit Startwerten 	
		 *  [K wp1 qp1 wp2 qp2 wp3 qp3]        gerade Ordnung
		 *  [K wp1 qp1 wp2 qp2 wp3 qp3 sig]    ungerade Ordnung
		 *  NelderMeadSimplex: Schrittgr�sse
		 *  
		 */
		PointValuePair optimum = optimizer.optimize(new MaxEval(10000), new ObjectiveFunction(target),
				GoalType.MINIMIZE,
				new InitialGuess(getStartingValues(measurementData.getFinalData(),
						new boolean[] { false, false, false, false, true, false, false, false, false, false })),
				new NelderMeadSimplex(new double[] { 0.2, 0.2, 0.2, 0.2, 0.2, 0.2 }));

		// Zweiter Durchgang mit optimierer
		SimplexOptimizer optimizer2 = new SimplexOptimizer(1e-12, 1e-16);
		PointValuePair optimum2 = optimizer.optimize(new MaxEval(10000), new ObjectiveFunction(target),
				GoalType.MINIMIZE, new InitialGuess(optimum.getPoint()),
				new NelderMeadSimplex(new double[] { 0.005, 0.005, 0.005, 0.005, 0.005, 0.005 }));

		// double[] step_approx = target.omega2polstep(4, optimum.getPoint(),
		// measurementData.getMeanData()[0]);

		//K wp qp und sigma des ersten und zweiten Durchgangs
		System.out.println(Arrays.toString(optimum.getPoint()) + " : " + optimum.getSecond());
		System.out.println(Arrays.toString(optimum2.getPoint()) + " : " + optimum2.getSecond());
		
		//matlabfunction.Matlab.print("error", target.error);

		//Schrittantwort mit den gefundenen Koeffizienten
		stepAnswer = new double[][] { time_normiert, target.omega2polstep(optimum2.getPoint(), time_normiert) };
		//storeData(stepAnswer);

		model.notifyObservers();
	}

	public void stopp() {
	}

	@Override
	public Object doInBackground() {
		int zaeler = 0;
		try {
			for (zaeler = 1; zaeler <= 10; zaeler++) {
				Thread.sleep(500);
				publish(zaeler);
			}

		} catch (InterruptedException e) {
		}

		return 0;
	}

	@Override
	protected void process(List<Integer> zaeler) {
		super.process(zaeler);
		StatusBar.showStatus("Z�hler: " + zaeler.get(0), StatusBar.INFO);
	}

	@Override
	public void done() {
		super.done();
		StatusBar.showStatus("Fertig ", StatusBar.INFO);
	}
/**
 * Funktion skalliert Zeitvektor
 * @param step_response_m
 * @return
 */
	private static double[][] scalingTime(double[][] step_response_m) {
		double[] t_old = step_response_m[0];
		double scalefactor = Math.log10(t_old[t_old.length - 1]) - 1;
		double[][] step_response_scaled_m = new double[2][step_response_m[0].length];
		double[] t_scaled = new double[t_old.length];

		for (int i = 0; i < t_old.length; i++) {
			t_scaled[i] = t_old[i] * Math.pow(10.0, -scalefactor);
			step_response_scaled_m[0][i] = t_scaled[i];
		}
		return step_response_scaled_m;
	}

	private static void storeData(double[][] store) {
		byte ordnung = 5;
		UTFDatatype utfDatatype = new UTFDatatype();
		utfDatatype.zaehler = 0.1;
		double[] h1 = new double[] { 2.2, 2, 4.2, 1, -1, -1, -1, -1, -1, -1 };
		double[] h2 = new double[10];
		utfDatatype.koeffWQ = h1;
		utfDatatype.sigma = -2.3;
		double[][] saveIt = new double[2][2500];
		saveIt = store;
		StartValueSaver.addUTF(ordnung, utfDatatype, saveIt);
	}
/**
 * Funktion gibt Startwerte
 * @param plot
 * @param ordnung
 * @return
 */
	private static double[] getStartingValues(double[][] plot, boolean[] ordnung) {
		double[] startingValues = new double[] { 0.1, 2.2, 2.0, 4.2, 1.0, -2.3 };
		// UTFDatatype utfDatatype = new UTFDatatype();
		// double[] startingValues = new double[5];
		// utfDatatype = StartValueSaver.getSimilarUTF(plot, ordnung);
		// startingValues[0] = utfDatatype.zaehler;
		// for (int i = 1; i < 5; i++) {
		// startingValues[i] = utfDatatype.koeffWQ[i - 1]/10.0;
		// }
		// startingValues[4]=utfDatatype.sigma;
		return startingValues;
	}
	/**
	 * Funktion gibt Schrittgr�sse
	 * @param startingValues
	 * @return
	 */
	private static double[] getStepSize(double[] startingValues){
		double[] steps = new double[startingValues.length];
		for (int i = 0; i < startingValues.length; i++) {
			steps[i] = 0.005;
		}
		return steps;
	}

}
