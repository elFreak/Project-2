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
	Controller controller;
	private CardLayout cardLayout = new CardLayout();

	/**
	 * Traces
	 */

	public Trace traceStep;
	public Trace traceRaw;
	public Trace tracePreprocessed;
	public Trace traceMean;
	public Trace[] tracesSolution = new Trace[10];
	public Trace[] tracesPole = new Trace[10];
	public Trace traceKorKoeffCompare;
	public Trace[] traceKorKoeffPoints = new Trace[10];

	/**
	 * Cards:
	 */
	private OutputCardEinlesen cardEinlesen;
	private OutputCardBearbeiten cardBearbeiten;
	private OutputCardBerechnen cardBerechnen;
	private OutputCardVerifizieren cardVerifizieren;

	public OutputPanel(Controller controller) {
		this.controller = controller;

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

		traceMean = new Trace();
		traceMean.usePreferedColor = true;
		traceMean.preferedColor = GlobalSettings.colorTraceOrange;

		traceKorKoeffCompare = new Trace();
		traceKorKoeffCompare.usePreferedColor = true;
		traceKorKoeffCompare.preferedColor = GlobalSettings.colorTraceGrey;

		for (int i = 0; i < traceKorKoeffPoints.length; i++) {
			traceKorKoeffPoints[i] = new Trace();
			traceKorKoeffPoints[i].usePreferedColor = true;
			traceKorKoeffPoints[i].preferedColor = GlobalSettings.colorsTraceSolution[i];
			traceKorKoeffPoints[i].lineType = Trace.LINE_NONE;
			traceKorKoeffPoints[i].pointType = Trace.POINT_BULLET;
		}

		for (int i = 0; i < tracesSolution.length; i++) {
			tracesSolution[i] = new Trace();
			tracesSolution[i].usePreferedColor = true;
			tracesSolution[i].preferedColor = GlobalSettings.colorsTraceSolution[i];
			tracesPole[i] = new Trace();
			tracesPole[i].usePreferedColor = true;
			tracesPole[i].preferedColor = GlobalSettings.colorsTraceSolution[i];
			tracesPole[i].lineType = Trace.LINE_NONE;
			tracesPole[i].pointType = Trace.POINT_CROSS;
		}

		// Init Cards:
		cardEinlesen = new OutputCardEinlesen(this);
		cardBearbeiten = new OutputCardBearbeiten(this, controller);
		cardBerechnen = new OutputCardBerechnen(this);
		cardVerifizieren = new OutputCardVerifizieren(this, controller);

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
			cardEinlesen.setAllRangeIdeal();
			break;
		case Controller.BEARBEITEN:
			cardLayout.show(this, Controller.KEY_BEARBEITEN);
			cardBearbeiten.setAllRangeIdeal();
			break;
		case Controller.BERECHNEN:
			cardLayout.show(this, Controller.KEY_BERECHNEN);
			cardBerechnen.setAllRangeIdeal();
			break;
		case Controller.VERIFIZIEREN:
			cardLayout.show(this, Controller.KEY_VERIFIZIEREN);
			cardVerifizieren.setAllRangeIdeal();
			break;
		}
	}

	public void update(java.util.Observable obs, Object obj) {

		if (!(obs instanceof Model) || !(obj instanceof Integer)) {
			throw (new IllegalArgumentException());
		}

		Model model = (Model) obs;
		int reason = (int) obj;

		switch (reason) {
		case Model.NOTIFY_REASON_MEASUREMENT_CHANGED:
			traceStep.data = model.measurementData.getstep();
			traceStep.dataValid = true;
			traceRaw.data = model.measurementData.getRawData();
			traceRaw.dataValid = true;
			tracePreprocessed.data = model.measurementData.getFinalData();
			tracePreprocessed.dataValid = true;
			traceMean.data = model.measurementData.getMeanData();
			traceMean.dataValid = true;
			break;
		case Model.NOTIFY_REASON_APPROXIMATION_UPDATE:
			traceKorKoeffCompare.data = model.network.getKorrelationComparison();
			traceKorKoeffCompare.dataValid = true;

			for (int i = 0; i < tracesSolution.length; i++) {
				if (model.network.getApprox(i + 1) != null) {
					if (model.network.getApprox(i + 1).getPole()[0] != null
							&& controller.getBerechnenCBActive()[i] == true) {

						tracesSolution[i].data = model.network.getApprox(i + 1).getStepResponse();
						tracesSolution[i].dataValid = true;
						tracesPole[i].data = new double[][] { model.network.getApprox(i + 1).getPole()[0].getPoint(),
								model.network.getApprox(i + 1).getPole()[1].getPoint() };
						tracesPole[i].dataValid = true;
					}
					else {
						traceKorKoeffPoints[i].dataValid = false;
						tracesSolution[i].dataValid = false;
					}
					if (model.network.getApprox(i + 1).getPole()[0] != null) {
						if (model.network.getApprox(i + 1).getPole()[0] != null) {
							traceKorKoeffPoints[i].data = model.network.getKorrelationComparisonPoins()[i];
							traceKorKoeffPoints[i].dataValid = true;
						}
					} else {
						tracesPole[i].dataValid = false;
					}
				} else {
					traceKorKoeffPoints[i].dataValid = false;
					tracesSolution[i].dataValid = false;
					tracesPole[i].dataValid = false;
				}
			}
			break;
		}

		cardEinlesen.update(obs, obj);
		cardBearbeiten.update(obs, obj);
		cardBerechnen.update(obs, obj);
		cardVerifizieren.update(obs, obj);
	}

}
