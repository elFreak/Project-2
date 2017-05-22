package userInterface.IOCard;

import java.awt.CardLayout;

import javax.swing.JPanel;

import model.Model;
import projectT_Fun_I.GlobalSettings;
import projectT_Fun_I.Utility;
import userInterface.Controller;
import userInterface.MyBorderFactory;
import userInterface.JavaPlot.Trace;

public class OutputPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	/**
	 * General
	 */
	private CardLayout cardLayout = new CardLayout();

	/**
	 * Traces
	 */

	public Trace traceStep;
	public Trace traceRaw;
	public Trace tracePreprocessed;
	public Trace traceSolution;
	public Trace traceMean;
	public Trace traceZwischenResultat;

	/**
	 * Cards:
	 */
	private OutputCardEinlesen cardEinlesen;
	private OutputCardBearbeiten cardBearbeiten;
	private OutputCardBerechnen cardBerechnen;
	private OutputCardVerifizieren cardVerifizieren;

	public OutputPanel(Controller controller) {

		// Output-Panel Design:
		setBorder(MyBorderFactory.createMyBorder("  Ausgabe  "));
		Utility.setAllBackgrounds(this, GlobalSettings.colorBackground);
		setOpaque(true);
		setBackground(GlobalSettings.colorBackgroundBlueBright);

		// Init Traces:
		traceStep = new Trace();
		traceStep.usePreferedColor = true;
		traceStep.preferedColor = GlobalSettings.colorTraceGreen;
		traceRaw = new Trace();
		traceRaw.usePreferedColor = true;
		traceRaw.preferedColor = GlobalSettings.colorTraceYellow;
		tracePreprocessed = new Trace();
		tracePreprocessed.usePreferedColor = true;
		tracePreprocessed.preferedColor = GlobalSettings.colorTraceOrange;
		traceSolution = new Trace();
		traceSolution.usePreferedColor = true;
		traceSolution.preferedColor = GlobalSettings.colorTracePink;
		traceMean = new Trace();
		traceMean.usePreferedColor = true;
		traceMean.preferedColor = GlobalSettings.colorTraceOrange;
		traceZwischenResultat = new Trace();
		traceZwischenResultat.usePreferedColor = true;
		traceZwischenResultat.preferedColor = GlobalSettings.colorTracePink;

		// Init Cards:
		cardEinlesen = new OutputCardEinlesen(this);
		cardBearbeiten = new OutputCardBearbeiten(this, controller);
		cardBerechnen = new OutputCardBerechnen(this);
		cardVerifizieren = new OutputCardVerifizieren(this);

		// Card Layout:
		setLayout(cardLayout);
		add(cardEinlesen, Controller.KEY_EINLESEN);
		add(cardBearbeiten, Controller.KEY_BEARBEITEN);
		add(cardBerechnen, Controller.KEY_BERECHNEN);
		add(cardVerifizieren, Controller.KEY_VERIFIZIEREN);

	}

	public void setActualMode(int mode) {
		switch (mode) {
		case Controller.EINLESEN:
			cardLayout.show(this, Controller.KEY_EINLESEN);
			break;
		case Controller.BEARBEITEN:
			cardLayout.show(this, Controller.KEY_BEARBEITEN);
			break;
		case Controller.BERECHNEN:
			cardLayout.show(this, Controller.KEY_BERECHNEN);
			break;
		case Controller.VERIFIZIEREN:
			cardLayout.show(this, Controller.KEY_VERIFIZIEREN);
			break;
		}
	}

	public void update(java.util.Observable obs, Object obj) {
		if(!(obs instanceof Model)||!(obj instanceof Integer)) {
			throw(new IllegalArgumentException());
		}
		
		switch ((int) obj) {
		case Model.NOTIFY_REASON_MEASUREMENT_CHANGED:
			traceStep.data = ((Model) obs).measurementData.getstep();
			traceStep.dataValid = true;
			traceRaw.data = ((Model) obs).measurementData.getRawData();
			traceRaw.dataValid = true;
			tracePreprocessed.data = ((Model) obs).measurementData.getFinalData();
			tracePreprocessed.dataValid = true;
			traceMean.data = ((Model) obs).measurementData.getMeanData();
			traceMean.dataValid = true;
			break;
		case Model.NOTIFY_REASON_APPROXIMATION_DONE:
			traceSolution.data = ((Model) obs).approximation.getSolutionSignal();
			traceSolution.dataValid = true;
			
			traceZwischenResultat.dataValid = false;
			break;
		case Model.NOTIFY_REASON_APPROXIMATION_ZWISCHENWERT:
			traceZwischenResultat.data = ((Model) obs).approximation.getZwischenSignal();
			traceZwischenResultat.dataValid = true;
			
			break;
		}

		cardEinlesen.update(obs, obj);
		cardBearbeiten.update(obs, obj);
		cardBerechnen.update(obs, obj);
	}
}