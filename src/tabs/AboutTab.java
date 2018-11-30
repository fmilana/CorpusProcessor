package tabs;

import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontSmoothingType;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;

public class AboutTab extends Tab {
	
	private BorderPane aboutBorderPane;

	public AboutTab() {
		
		setText("About this software");
		
		Text VWCorpusProcessorText = new Text("VW Corpus Processor");
		VWCorpusProcessorText.setFont(Font.font(Font.getDefault().getName(), FontWeight.BOLD, FontPosture.ITALIC, Font.getDefault().getSize() * 1.5));
		VWCorpusProcessorText.setFill(Color.rgb(103,47,87));
		VWCorpusProcessorText.setFontSmoothingType(FontSmoothingType.LCD);
		
		Text versionText = new Text("Version 1.0.0.0");
		versionText.setFont(Font.font(Font.getDefault().getName(), Font.getDefault().getSize() * 1.5));
		versionText.setFontSmoothingType(FontSmoothingType.LCD);
		
		Text javaText = new Text("JavaFX");
		javaText.setFont(Font.font(Font.getDefault().getName(), FontPosture.ITALIC, Font.getDefault().getSize() * 1.5));
		javaText.setFontSmoothingType(FontSmoothingType.LCD);
		
		Text toTheLightHouseText = new Text("To The Lighthouse");
		toTheLightHouseText.setFont(Font.font(Font.getDefault().getName(), FontPosture.ITALIC, Font.getDefault().getSize() * 1.5));
		toTheLightHouseText.setFontSmoothingType(FontSmoothingType.LCD);
		
		Text description1Text = new Text("Created in September 2018 using "); 
		description1Text.setFont(Font.font(Font.getDefault().getName(), Font.getDefault().getSize() * 1.5));
		description1Text.setFontSmoothingType(FontSmoothingType.LCD);
		
		Text description2Text = new Text(" by Federico Milana, BSc in Computer Science (Software Engineering),\n"
				+ "King's College London, and candidate MSc in Human-Computer Interaction at the Division of Psychology and Language Sciences,\n"
				+ "University College London.\n");
		description2Text.setFont(Font.font(Font.getDefault().getName(), Font.getDefault().getSize() * 1.5));
		description2Text.setFontSmoothingType(FontSmoothingType.LCD);
		
		Text description3Text = new Text("Developed under the direction of Anna Maria Cipriani to enable corpus-based computations on translated texts\n"
				+ "of Virginia Woolf's ");
		description3Text.setFont(Font.font(Font.getDefault().getName(), Font.getDefault().getSize() * 1.5));
		description3Text.setFontSmoothingType(FontSmoothingType.LCD);
		
		Text description4Text = new Text(" for her PhD dissertation at the Centre for Translation Studies,\n" + 
				"University College London.");
		description4Text.setFont(Font.font(Font.getDefault().getName(), Font.getDefault().getSize() * 1.5));
		description4Text.setFontSmoothingType(FontSmoothingType.LCD);		
		
		TextFlow textFlow = new TextFlow(
				VWCorpusProcessorText, 
				new Text("\n"),
				versionText,
				new Text("\n\n"),
				description1Text, 
				javaText, 
				description2Text,
				new Text("\n"),
				description3Text,
				toTheLightHouseText,
				description4Text);
		
		textFlow.setLineSpacing(10);
		
		Label aboutLabel = new Label(null, textFlow);
		
		aboutBorderPane = new BorderPane();	    
		
		aboutBorderPane.setCenter(aboutLabel);
		
		setContent(aboutBorderPane);
	}
	
}
