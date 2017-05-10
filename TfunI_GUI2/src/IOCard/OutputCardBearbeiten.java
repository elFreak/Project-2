package IOCard;

import JavaPlot.Plot;
import JavaPlot.Slider;
import userInterface.OutputPanel;
import userInterface.WindowContainer;

public class OutputCardBearbeiten extends WindowContainer {
	private static final long serialVersionUID = 1L;
	
	public Plot plotBearbeiten = new Plot();
	
	public OutputCardBearbeiten(OutputPanel outputPanel) {
		addComponent(plotBearbeiten);

		plotBearbeiten.addTrace(outputPanel.traceRaw);
		plotBearbeiten.addTrace(outputPanel.traceStep);
		plotBearbeiten.addSubplot();
		plotBearbeiten.addTrace(outputPanel.traceRaw);
		plotBearbeiten.addTrace(outputPanel.traceMean);
		outputPanel.traceStep.dataValid = false;
		outputPanel.traceRaw.dataValid = false;
		outputPanel.traceMean.dataValid = false;
		plotBearbeiten.setSubplot(0);
		plotBearbeiten.addSlider(Slider.HORIZONTAL,"1");
		plotBearbeiten.addSlider(Slider.VERTICAL,"2");
		plotBearbeiten.addSlider(Slider.VERTICAL,"3");
	}
}
