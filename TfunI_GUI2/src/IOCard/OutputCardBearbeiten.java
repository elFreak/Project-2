package IOCard;

import JavaPlot.Plot;
import JavaPlot.Slider;
import model.Model;
import userInterface.Controller;
import userInterface.WindowContainer;

public class OutputCardBearbeiten extends WindowContainer {
	private static final long serialVersionUID = 1L;
	
	OutputPanel outputPanel;
	
	public final static String KEY_START = "Start";
	public final static String KEY_END = "End";
	public final static String KEY_OFFSET = "Offset";
	
	private Plot plotBearbeiten;
	private boolean plotBearbeitenTraceStepAdded = false;
	private boolean plotBearbeitenTraceRawAdded = false;
	private boolean plotBearbeitenTracePreprocessedAdded = false;
	
	public OutputCardBearbeiten(OutputPanel outputPanel, Controller controller) {
		this.outputPanel = outputPanel;
		plotBearbeiten = new Plot(controller);
		addComponent(plotBearbeiten);

		plotBearbeiten.addSubplot();
	
		plotBearbeiten.setSubplot(0);
		plotBearbeiten.addSlider(Slider.HORIZONTAL,KEY_OFFSET);
		plotBearbeiten.addSlider(Slider.VERTICAL,KEY_START);
		plotBearbeiten.addSlider(Slider.VERTICAL,KEY_END);
		plotBearbeiten.setSliderPosition(KEY_OFFSET, 0);
		plotBearbeiten.setSliderPosition(KEY_START, -1000);
		plotBearbeiten.setSliderPosition(KEY_END, 1000);
	}
	
	
	public void update(java.util.Observable obs, Object obj) {
		plotBearbeiten.setSubplot(0);
		plotBearbeiten.setStepPosition(((Model) obs).measurementData.getstepTime());
		plotBearbeiten.setSliderPosition(KEY_OFFSET, ((Model) obs).measurementData.getOffset());
		plotBearbeiten.setSliderPosition(KEY_START, ((Model) obs).measurementData.getDeadTime());
		plotBearbeiten.setSliderPosition(KEY_END, ((Model) obs).measurementData.getTail());
		
		plotBearbeiten.setSubplot(0);
		plotBearbeiten.setStepPosition(((Model) obs).measurementData.getstepTime());
		
		plotBearbeiten.setSubplot(1);
		plotBearbeiten.setRangeIdeal();
		
		if(outputPanel.traceRaw.dataValid&&!plotBearbeitenTraceStepAdded) {
			plotBearbeiten.setSubplot(0);
			plotBearbeiten.addTrace(outputPanel.traceStep);
		}
		if(outputPanel.traceRaw.dataValid&&!plotBearbeitenTraceRawAdded) {
			plotBearbeiten.setSubplot(0);
			plotBearbeiten.addTrace(outputPanel.traceRaw);
		}
		if(outputPanel.traceRaw.dataValid&&!plotBearbeitenTracePreprocessedAdded) {
			plotBearbeiten.setSubplot(1);
			plotBearbeiten.addTrace(outputPanel.tracePreprocessed);
		}
	}
}
