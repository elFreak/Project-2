package userInterface;

import java.util.Observable;

import model.Model;
import model.UTFDatatype;
import speicher.DataFile;
import sun.awt.RepaintArea;

/**
 * 
 * @author Team 1
 *
 */
public class Controller {

	// --------------------------------------------------------------------
	// General:
	private View view;
	private Model model;

	// --------------------------------------------------------------------
	// Programm-Flow:
	// --------------------------------------------------------------------
	// ProgrammFlow:
	public final static int EINLESEN = 0;
	public final static String KEY_EINLESEN = "EINLESEN";
	public final static int BEARBEITEN = 1;
	public final static String KEY_BEARBEITEN = "BEARBEITEN";
	public final static int BERECHNEN = 2;
	public final static String KEY_BERECHNEN = "BERECHNEN";
	public final static int VERIFIZIEREN = 3;
	public final static String KEY_VERIFIZIEREN = "VERIFIZIEREN";
	
	
	private boolean[] berechnenCBActive = new boolean[9];

	public Controller(Model model) {
		this.model = model;
	}

	/**
	 * 
	 * @param view
	 */
	public void setView(View view) {
		this.view = view;
		view.inputPanel.setActualMode(EINLESEN);
		view.outputPanel.setActualMode(EINLESEN);
	}

	/**
	 * 
	 * @param mode
	 */
	public void setActualMode(int mode) {
		view.inputPanel.setActualMode(mode);
		view.outputPanel.setActualMode(mode);

		switch (mode) {
		case EINLESEN:

			break;
		case BEARBEITEN:

			break;
		case BERECHNEN:
			model.creatNetwork();
			break;
		case VERIFIZIEREN:

			break;
		}
	}

	public void setMesuredData(double[][] data) {
		view.inputPanel.berechneClearAllCB();
		for(int i=0;i<berechnenCBActive.length;i++) {
			berechnenCBActive[i] = false;
		}
		model.setMesuredData(data);
		model.measurementData.autoLimits();
		StatusBar.showStatus("Neue Sprungantwort wurde eingelesen.", StatusBar.INFO);
	}

	public void filterChanged(int n) {
		model.measurementData.setMovingMean(n);
	}

	public void setRange(double tail, double offset) {
		if (tail < model.measurementData.getstepTime()) {
			if (tail != model.measurementData.getTail()) {
				tail = model.measurementData.getstepTime();
			}
		}

		model.measurementData.setLimits(offset, tail);
	}

	public void setStep(double stepTime, double stepHeight) {
		if (stepTime > model.measurementData.getTail()) {
			stepTime = model.measurementData.getTail();
		}

		model.measurementData.setStepHeight(stepHeight);
		model.measurementData.setStepTime(stepTime);
	}

	public void setStepTime(double stepTime) {
		if (stepTime > model.measurementData.getTail()) {
			stepTime = model.measurementData.getTail();
		}

		model.measurementData.setStepTime(stepTime);
	}

	public void setOriginalStep() {
		model.measurementData.setOriginalStep();
	}

	public void calculateUTF(int order) {
		model.berechneUTF(order);
	}

	public void autoLimmits() {
		model.measurementData.autoLimits();
	}

	public void setBerechnenCBActive(boolean[] value) {
		this.berechnenCBActive = value;
		for (int i = 0; i < value.length; i++) {
			view.outputPanel.tracesSolution[i].dataValid = value[i];
			view.outputPanel.tracesPole[i].dataValid = value[i];
		}
		model.notifyObservers(Model.NOTIFY_REASON_APPROXIMATION_DONE);
	}
	
	public boolean[] getBerechnenCBActive() {
		return berechnenCBActive;
	}
	
	public void changeApproximation(UTFDatatype utf) {
		model.network.getApprox(utf.ordnung).setUtf(utf);
	}
	
	public void berechnungLoeschen() {
		view.inputPanel.berechneClearAllCB();
		model.deleteNetwork();
	}
	
	public void setThreshold(double threshold) {
		model.setThreshold(threshold);
	}
}
