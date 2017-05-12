package IOCard;

import java.awt.CardLayout;

import javax.swing.JPanel;

import JavaPlot.Trace;
import model.Model;
import projectT_Fun_I.GlobalSettings;
import projectT_Fun_I.Utility;
import userInterface.Controller;
import userInterface.MyBorderFactory;

public class OutputPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	/**
	 * General
	 */
	private CardLayout cardLayout = new CardLayout();

	/**
	 * Traces
	 */
//	public static final int TRACE_STEP = 0;
//	public static final int TRACE_RAW = 1;
//	public static final int TRACE_PREPROCESSED = 2;
//	public static final int TRACE_SOLUTION = 3;
	
	public JavaPlot.Trace traceStep;
	public JavaPlot.Trace traceRaw;
	public JavaPlot.Trace tracePreprocessed;
	public JavaPlot.Trace traceSolution;

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
		// Traces Aktualisieren:
		traceStep.data = ((Model) obs).measurementData.getstep();
		traceStep.dataValid=true;
		traceRaw.data = ((Model) obs).measurementData.getRawData();
		traceRaw.dataValid=true;
		tracePreprocessed.data = ((Model) obs).measurementData.getFinalData();
		tracePreprocessed.dataValid=true;
		traceSolution.data = ((Model) obs).approximation.stepAnswer;
		traceSolution.dataValid=true;
		
		cardEinlesen.update(obs, obj);
		cardBearbeiten.update(obs, obj);
	}
}
