package userInterface.IOCard;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;

import projectT_Fun_I.GlobalSettings;
import userInterface.MyBorderFactory;
import userInterface.WindowContainer;
import userInterface.JavaPlot.Plot;

public class OutputCardBerechnen extends WindowContainer {
	private static final long serialVersionUID = 1L;

	private OutputPanel outputPanel;

	private Plot plotBerechnen = new Plot();
	private boolean plotBerechnenTracePreprocessedAdded = false;
	private boolean plotBerechnenTraceSolutionAdded = false;
	private boolean plotBerechnenTraceZwischenResultAdded = false;

	private Plot plotNullstellen = new Plot();
	private JPanel panelBerechnen = new JPanel(new GridBagLayout());

	public OutputCardBerechnen(OutputPanel outputPanel) {
		this.outputPanel = outputPanel;

		addComponent(plotBerechnen);
		addComponent(plotNullstellen);
		addComponent(panelBerechnen);

		// Funktion
		panelBerechnen.setBackground(GlobalSettings.colorBackground);
		panelBerechnen.setBorder(MyBorderFactory.createMyBorder("Funktion"));

		panelBerechnen.add(new JLabel("Formel: "), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
				GridBagConstraints.FIRST_LINE_START, GridBagConstraints.HORIZONTAL, new Insets(20, 10, 10, 10), 0, 0));
		panelBerechnen.add(new JLabel("Qp: "), new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
				GridBagConstraints.FIRST_LINE_START, GridBagConstraints.HORIZONTAL, new Insets(20, 10, 10, 10), 0, 0));
		panelBerechnen.add(new JLabel("Wp: "), new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0,
				GridBagConstraints.FIRST_LINE_START, GridBagConstraints.HORIZONTAL, new Insets(20, 10, 10, 10), 0, 0));
		panelBerechnen.add(new JLabel("K: "), new GridBagConstraints(0, 3, 1, 1, 1.0, 0.0,
				GridBagConstraints.FIRST_LINE_START, GridBagConstraints.HORIZONTAL, new Insets(20, 10, 10, 10), 0, 0));
		panelBerechnen.add(new JLabel("K: "), new GridBagConstraints(0, 4, 1, 1, 1.0, 0.0,
				GridBagConstraints.FIRST_LINE_START, GridBagConstraints.HORIZONTAL, new Insets(20, 10, 10, 10), 0, 0));
		panelBerechnen.add(new JLabel("K: "), new GridBagConstraints(0, 5, 1, 1, 1.0, 0.0,
				GridBagConstraints.FIRST_LINE_START, GridBagConstraints.HORIZONTAL, new Insets(20, 10, 10, 10), 0, 0));


	}

	public void update(java.util.Observable obs, Object obj) {

		if (!outputPanel.traceZwischenResultat.dataValid) {
			plotBerechnenTraceZwischenResultAdded = false;
		}
		if (outputPanel.tracePreprocessed.dataValid && !plotBerechnenTracePreprocessedAdded) {
			plotBerechnen.setSubplot(0);
			plotBerechnenTracePreprocessedAdded = true;
			plotBerechnen.addTrace(outputPanel.tracePreprocessed);
			plotBerechnen.setRangeIdeal();
		}
		if (outputPanel.traceSolution.dataValid && !plotBerechnenTraceSolutionAdded) {
			plotBerechnen.setSubplot(0);
			plotBerechnenTraceSolutionAdded = true;
			plotBerechnen.addTrace(outputPanel.traceSolution);
			plotBerechnen.setRangeIdeal();
		}
		if (outputPanel.traceZwischenResultat.dataValid && !plotBerechnenTraceZwischenResultAdded) {
			plotBerechnen.setSubplot(0);
			plotBerechnenTraceZwischenResultAdded = true;
			plotBerechnen.addTrace(outputPanel.traceZwischenResultat);
			plotBerechnen.setRangeIdeal();
		}

	}
}