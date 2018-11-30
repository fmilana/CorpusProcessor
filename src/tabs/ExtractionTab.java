package tabs;

import java.io.File;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import application.Main;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import model.Pair;

public class ExtractionTab extends Tab {
	
	private Main main;
	
	private BorderPane extractionBorderPane;
	private ScrollPane extractionScrollPane;
	private TextFlow extractionTextFlow;
	
	private String selectedTagName;
	
	private String extractedText;
	
	public ExtractionTab(Main main) {
		
		this.main = main;
		
		setText("Extraction");
		
		extractedText = new String();

		extractionTextFlow = new TextFlow();
//		extractionTextArea.setEditable(false);
		
		extractionScrollPane = new ScrollPane();
		
		extractionScrollPane.setContent(extractionTextFlow);
		extractionScrollPane.setPadding(new Insets(0, 15, 0, 15));

		extractionBorderPane = new BorderPane();
		extractionBorderPane.setTop(createExtractionHBox());

		extractionBorderPane.setCenter(extractionScrollPane);

		setContent(extractionBorderPane);
	}
	
	private HBox createExtractionHBox() {
		
		HBox hbox = new HBox();

		hbox.setPadding(new Insets(20));
		hbox.setSpacing(10);
		hbox.setAlignment(Pos.CENTER_LEFT);

		ComboBox<String> translatorComboBox = main.createTranslatorComboBox();
		translatorComboBox.setVisibleRowCount(12);
		ComboBox<String> tagComboBox = main.createTagComboBox();
		
		ProgressBar progressBar = new ProgressBar();
		
		CheckBox showOtherTagsCheckBox = new CheckBox();
		showOtherTagsCheckBox.setText("Show Other Tags");
		showOtherTagsCheckBox.setSelected(true);
		
		Button saveButton = new Button("Save As...");
		saveButton.setDisable(true);
		saveButton.setMinWidth(100);
//		saveButton.disableProperty().bind(extractionScrollPane.proper);

		Button extractButton = new Button("Extract");
		extractButton.setMinWidth(100);
		extractButton.disableProperty()
				.bind(translatorComboBox.valueProperty().isNull().or(tagComboBox.valueProperty().isNull()));

		extractButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				
				selectedTagName = tagComboBox.getValue().replaceAll("<", "").replaceAll(">", "");
				

				String selectedTranslator = translatorComboBox.getValue();
				String selectedTag = tagComboBox.getValue();

				if (selectedTranslator != null && selectedTag != null) {
					
					extractionBorderPane.setCenter(progressBar);

					final Task<Void> task = new Task<Void>() {
					
//						extractedText = new String();

						@Override
						protected Void call() throws Exception {
							
							extractedText = main.getCombinedReader().getAllParagraphsWithTag(selectedTag, selectedTranslator, showOtherTagsCheckBox.isSelected(), false);
							
							if (extractedText.length() > 0) {
								saveButton.setDisable(false);
							} else {
								extractedText = "No results..";
								
								extractionTextFlow.setTextAlignment(TextAlignment.CENTER);
							}
							
							Platform.runLater(new Runnable(){
								@Override
								public void run() {	
									
									extractionTextFlow.getChildren().clear();
									
									extractionTextFlow.getChildren().add(stringToTextFlow(extractedText));
//									extractionTextFlow.setStyle("-fx-font: 18 arial;");
									
									extractionBorderPane.setCenter(extractionScrollPane);
								}
							});
							
							return null;
						}
					};
					
					progressBar.progressProperty().bind(task.progressProperty());
					
					final Thread thread = new Thread(task);
			        thread.start();
				}
			}
		});		

		

		saveButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				FileChooser fileChooser = new FileChooser();

				fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("TXT file (*.txt)", "*.txt"));

				File file = fileChooser.showSaveDialog(main.getPrimaryStage());

				if (file != null) {
					main.saveFile(extractedText, file);
				}
			}
		});

		Region region = new Region();
		HBox.setHgrow(region, Priority.ALWAYS);
		
		Region region2 = new Region();

		hbox.getChildren().addAll(translatorComboBox, tagComboBox, extractButton, region, showOtherTagsCheckBox, region2, saveButton);

		return hbox;
	}
	
//	private String textFlowToString(TextFlow textFlow) {
//		StringBuilder stringBuilder = new StringBuilder();
//		
//		for (Node node : textFlow.getChildren()) {
//		    if (node instanceof Text) {
//		    	
//		    	stringBuilder.append(((Text) node).getText());
//		    } 
//		}
//		
//		return stringBuilder.toString();
//	}
	
	private TextFlow stringToTextFlow(String string) {
		
		ArrayList<Pair<Integer, Integer>> highlightedTextStartAndEndList = new ArrayList<Pair<Integer, Integer>>();
		
		TextFlow textFlow = new TextFlow();
		
		Pattern pattern = Pattern.compile("<" + selectedTagName + ">(?s).*?</" + selectedTagName+ ">");
		
		System.out.println("matching tag " + selectedTagName);
		
	    Matcher matcher = pattern.matcher(string);
	    
	    while (matcher.find()) {
	    	highlightedTextStartAndEndList.add(new Pair<Integer, Integer>(matcher.start(), matcher.end()));
	    }
	    
	    int restIndex = 0;
	    
	    for (int i = 0; i < highlightedTextStartAndEndList.size(); ++i) {
	    	textFlow.getChildren().addAll(highlightedTextFlow(string.substring(restIndex, highlightedTextStartAndEndList.get(i).getKey()), false));
	    	
	    	textFlow.getChildren().addAll(highlightedTextFlow(string.substring(highlightedTextStartAndEndList.get(i).getKey(), highlightedTextStartAndEndList.get(i).getValue()), true));
	    	
	    	restIndex = highlightedTextStartAndEndList.get(i).getValue();
	    }
	    
	    if (restIndex < string.length()-1) {
			textFlow.getChildren().addAll(highlightedTextFlow(string.substring(restIndex, string.length()-1), false));
		}
	    
	    return textFlow;
	}
	
	private ArrayList<Text> highlightedTextFlow(String string, boolean toHighlight) {
		
//		TextFlow textFlow = new TextFlow();
		ArrayList<Text> textList = new ArrayList<Text>();
		
		if (toHighlight) {
			
			ArrayList<Pair<Integer, Integer>> numberPositions = new ArrayList<Pair<Integer, Integer>>();
			
			Pattern pattern = Pattern.compile("\\d+    \\d+            ");
			Matcher matcher = pattern.matcher(string);
			
			while (matcher.find()) {
				numberPositions.add(new Pair<Integer, Integer>(matcher.start(), matcher.end()));
			}
			
			Integer restIndex = 0;
			
			for (int i = 0; i < numberPositions.size(); ++i) {
				
				Text text = new Text(string.substring(restIndex, numberPositions.get(i).getKey()));
				text.setFont(Font.font(Font.getDefault().getName(), FontWeight.BOLD, Font.getDefault().getSize() * 1.5));
				text.setFill(Color.rgb(103,47,87));
				
//				textFlow.getChildren().add(text);
				textList.add(text);
				
				text = new Text(string.substring(numberPositions.get(i).getKey(), numberPositions.get(i).getValue()));
				
				text.setFont(Font.font(Font.getDefault().getName(), Font.getDefault().getSize() * 1.5));
//				textFlow.getChildren().add(text);
				textList.add(text);
				
				restIndex = numberPositions.get(i).getValue();
			}
			
			if (restIndex < string.length()-1) {
				
				Text text = new Text(string.substring(restIndex, string.length()));
				text.setFont(Font.font(Font.getDefault().getName(), FontWeight.BOLD, Font.getDefault().getSize() * 1.5));
				text.setFill(Color.rgb(103,47,87));
				
				textList.add(text);
			}
			
			
		} else {
			
			Text text = new Text(string);
			
			text.setFont(Font.font(Font.getDefault().getName(), Font.getDefault().getSize() * 1.5));
			
//			textFlow.getChildren().add(text);
			textList.add(text);
		}
		
//		return textFlow;
		return textList;
	}
	
//	private Text highlightText(String string, boolean toHighlight) {
//		
//		Text text = new Text(string);
//		
//		if (toHighlight) {
//			text.setFont(Font.font(Font.getDefault().getName(), FontWeight.BOLD, Font.getDefault().getSize() * 1.5));
//			text.setFill(Color.rgb(103,47,87));
//		} else {
//			text.setFont(Font.font(Font.getDefault().getName(), Font.getDefault().getSize() * 1.5));
//		}
//		
//		return text;
//	}
}
