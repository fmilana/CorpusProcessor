package tabs;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import javax.imageio.ImageIO;

import com.kennycason.kumo.WordFrequency;

import application.Main;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import model.CloudMaker;

public class WordCloudTab extends Tab {

	private Main main;
	
	private CloudMaker cloudMaker;
	
	private BorderPane wordCloudBorderPane;
	
	private HashMap<String, CheckBox> wordCloudCheckBoxes;
	
	public WordCloudTab(Main main) {
		
		this.main = main;
		cloudMaker = main.getCloudMaker();
		
		setText("Word Cloud");
		
		wordCloudBorderPane = new BorderPane();
		
		wordCloudBorderPane.setLeft(createWordCloudVBox());
		wordCloudBorderPane.setTop(createWordCloudHBox(wordCloudCheckBoxes));
		
		setContent(wordCloudBorderPane);
	}
	
	private VBox createWordCloudVBox() {
		
		wordCloudCheckBoxes = new HashMap<String, CheckBox>();
		
		VBox vbox = new VBox();
		vbox.setPadding(new Insets(20));
		vbox.setSpacing(10);
		
		CheckBox virginiaWoolfCheckBox = new CheckBox();
		virginiaWoolfCheckBox.setText("Virginia Woolf (1927)");
		virginiaWoolfCheckBox.setSelected(true);
		wordCloudCheckBoxes.put("Virginia Woolf (1927)", virginiaWoolfCheckBox);
		
		CheckBox celenzaCheckBox = new CheckBox();
		celenzaCheckBox.setText("Celenza (1934)");
		celenzaCheckBox.setSelected(true);
		wordCloudCheckBoxes.put("Celenza (1934)", celenzaCheckBox);
		
		CheckBox fusini1992CheckBox = new CheckBox();
		fusini1992CheckBox.setText("Fusini (1992)");
		fusini1992CheckBox.setSelected(true);
		wordCloudCheckBoxes.put("Fusini (1992)", fusini1992CheckBox);
		
		CheckBox cucciarelliCheckBox = new CheckBox();
		cucciarelliCheckBox.setText("Cucciarelli (1993)");
		cucciarelliCheckBox.setSelected(true);
		wordCloudCheckBoxes.put("Cucciarelli (1993)", cucciarelliCheckBox);
		
		CheckBox malagòCheckBox = new CheckBox();
		malagòCheckBox.setText("Malagò (1993)");
		malagòCheckBox.setSelected(true);
		wordCloudCheckBoxes.put("Malagò (1993)", malagòCheckBox);
		
		CheckBox zazoCheckBox = new CheckBox();
		zazoCheckBox.setText("Zazo (1994)");
		zazoCheckBox.setSelected(true);
		wordCloudCheckBoxes.put("Zazo (1994)", zazoCheckBox);
		
		CheckBox bianciardiCheckBox = new CheckBox();
		bianciardiCheckBox.setText("Bianciardi (1994)");
		bianciardiCheckBox.setSelected(true);
		wordCloudCheckBoxes.put("Bianciardi (1994)", bianciardiCheckBox);
		
		CheckBox fusini1998CheckBox = new CheckBox();
		fusini1998CheckBox.setText("Fusini (1998)");
		fusini1998CheckBox.setSelected(true);
		wordCloudCheckBoxes.put("Fusini (1998)", fusini1998CheckBox);
		
		CheckBox deMarinisCheckBox = new CheckBox();
		deMarinisCheckBox.setText("De Marinis (2012)");
		deMarinisCheckBox.setSelected(true);
		wordCloudCheckBoxes.put("De Marinis (2012)", deMarinisCheckBox);
		
		CheckBox fusini2012CheckBox = new CheckBox();
		fusini2012CheckBox.setText("Fusini (2012)");
		fusini2012CheckBox.setSelected(true);
		wordCloudCheckBoxes.put("Fusini (2012)", fusini1998CheckBox);
		
		CheckBox nadottiCheckBox = new CheckBox();
		nadottiCheckBox.setText("Nadotti (2014)");
		nadottiCheckBox.setSelected(true);
		wordCloudCheckBoxes.put("Nadotti (2014)", nadottiCheckBox);
		
		CheckBox artioliCheckBox = new CheckBox();
		artioliCheckBox.setText("Artioli (2017)");
		artioliCheckBox.setSelected(true);
		wordCloudCheckBoxes.put("Artioli (2017)", artioliCheckBox);
		
		vbox.getChildren().addAll(
				virginiaWoolfCheckBox,
				celenzaCheckBox,
				fusini1992CheckBox,
				cucciarelliCheckBox,
				malagòCheckBox,
				zazoCheckBox,
				bianciardiCheckBox,
				fusini1998CheckBox,
				deMarinisCheckBox,
				fusini2012CheckBox,
				nadottiCheckBox,
				artioliCheckBox);
		
		return vbox;
	}
	
	private HBox createWordCloudHBox(HashMap<String, CheckBox> wordCloudCheckBoxes) {
		
		HBox hbox = new HBox();
		hbox.setPadding(new Insets(20));
		hbox.setSpacing(10);
		hbox.setAlignment(Pos.CENTER_LEFT);
		
		Button generateButton = new Button("Generate");
		generateButton.setMinWidth(100);
		
		Button saveButton = new Button("Save As...");
		saveButton.setMinWidth(100);
		saveButton.setDisable(true);
		
		saveButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				
				FileChooser fileChooser = new FileChooser();	
				
				fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG file (*.png)", "*.png"));
				fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("TXT file (*.txt)", "*.txt"));
				
				File file = fileChooser.showSaveDialog(main.getPrimaryStage());
				
				if (file != null) {
					saveWordCloud(file);
				}
				
			}
		});
			
//		generateButton.disableProperty().bind(translatorComboBox.valueProperty().isNull());
		
		ProgressBar progressBar = new ProgressBar();
		
		CheckBox removeStopWordsCheckBox = new CheckBox("Remove Stop Words");
		removeStopWordsCheckBox.setSelected(true);
		
		Region region = new Region();
		HBox.setHgrow(region, Priority.ALWAYS);
		
		
		CheckBox virginiaWoolfCheckBox = wordCloudCheckBoxes.get("Virginia Woolf (1927)");
		CheckBox celenzaCheckBox = wordCloudCheckBoxes.get("Celenza (1934)");
		CheckBox fusini1992CheckBox = wordCloudCheckBoxes.get("Fusini (1992)");
		CheckBox cucciarelliCheckBox = wordCloudCheckBoxes.get("Cucciarelli (1993)");
		CheckBox malagòCheckBox = wordCloudCheckBoxes.get("Malagò (1993)");
		CheckBox zazoCheckBox = wordCloudCheckBoxes.get("Zazo (1994)");
		CheckBox bianciardiCheckBox = wordCloudCheckBoxes.get("Bianciardi (1994)");
		CheckBox fusini1998CheckBox = wordCloudCheckBoxes.get("Fusini (1998)");
		CheckBox deMarinisCheckBox = wordCloudCheckBoxes.get("De Marinis (2012)");
		CheckBox fusini2012CheckBox = wordCloudCheckBoxes.get("Fusini (2012)");
		CheckBox nadottiCheckBox = wordCloudCheckBoxes.get("Nadotti (2014)");
		CheckBox artioliCheckBox = wordCloudCheckBoxes.get("Artioli (2017)");
		
		generateButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				
				saveButton.setDisable(true);
				
				wordCloudBorderPane.setCenter(progressBar);
				
				ArrayList<String> selectedTranslators = new ArrayList<String>();
				
				if (virginiaWoolfCheckBox.isSelected()) {
					selectedTranslators.add("Virginia Woolf (1927)");
				}
				if (celenzaCheckBox.isSelected()) {
					selectedTranslators.add("Celenza (1934)");
				}
				if (fusini1992CheckBox.isSelected()) {
					selectedTranslators.add("Fusini (1992)");
				}
				if (cucciarelliCheckBox.isSelected()) {
					selectedTranslators.add("Cucciarelli (1993)");
				}
				if (malagòCheckBox.isSelected()) {
					selectedTranslators.add("Malagò (1993)");
				}
				if (zazoCheckBox.isSelected()) {
					selectedTranslators.add("Zazo (1994)");
				}
				if (bianciardiCheckBox.isSelected()) {
					selectedTranslators.add("Bianciardi (1994)");
				}
				if (fusini1998CheckBox.isSelected()) {
					selectedTranslators.add("Fusini (1998)");
				}
				if (deMarinisCheckBox.isSelected()) {
					selectedTranslators.add("De Marinis (2012)");
				}
				if (fusini2012CheckBox.isSelected()) {
					selectedTranslators.add("Fusini (2012)");
				}
				if (nadottiCheckBox.isSelected()) {
					selectedTranslators.add("Nadotti (2014)");
				}
				if (artioliCheckBox.isSelected()) {
					selectedTranslators.add("Artioli (2017)");
				}
				
				try {
					
					cloudMaker.generateWordCloud(selectedTranslators, removeStopWordsCheckBox.isSelected(), wordCloudBorderPane, progressBar, saveButton);
					
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
		});		
		
		
		Region region3 = new Region();
		
		hbox.getChildren().addAll(generateButton,
				region,
				removeStopWordsCheckBox,
				region3,
				saveButton);
		
		return hbox;		
	}
	
	private void saveWordCloud(File file) {
		
		try {
			FileWriter fileWriter = new FileWriter(file);
			
			String fileExtension = file.getName().substring(file.getName().lastIndexOf(".") + 1, file.getName().length());
			
			System.out.println(fileExtension);
			
			if (fileExtension.equals("png")) {
				
				GridPane gridPane = (GridPane) ((ScrollPane) wordCloudBorderPane.getChildren().get(2)).getContent();
				
				WritableImage image = gridPane.snapshot(new SnapshotParameters(), null);
				
				ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
				
			} else if (fileExtension.equals("txt")) {
				
				LinkedHashMap<String, List<WordFrequency>> wordFrequencyMap = cloudMaker.getWordFrequencyMap();
				
				ArrayList<String> allWords = new ArrayList<String>();
				ArrayList<String> translators = new ArrayList<String>();
				
				ArrayList<List<WordFrequency>> allWordFrequencies = new ArrayList<List<WordFrequency>>();
				
				fileWriter.write("Word");
		        
				
				for (Entry<String, List<WordFrequency>> pair : wordFrequencyMap.entrySet()) {
					
					String translator = pair.getKey();
					List<WordFrequency> wordFrequencies = pair.getValue();
					
					for (WordFrequency wordFrequency : wordFrequencies) {
						
						String word = wordFrequency.getWord();
						
						if (!allWords.contains(word) && !cloudMaker.getTranslatorSkippedWords().get(translator).contains(word)) {
							allWords.add(word);
						}
					}
					
					translators.add(translator);
				}
				
				for (String translator : translators) {
					fileWriter.write(", " + translator);
					
					
					allWordFrequencies.add(wordFrequencyMap.get(translator));
					
				}
				
				fileWriter.write(System.lineSeparator());
				
				Collections.sort(allWords);
				
				
				for (String word : allWords) {
					
					fileWriter.write(word);
					
					
					for (int i = 0; i < translators.size(); ++i) {
						
						List<WordFrequency> wordFrequencyList = allWordFrequencies.get(i);
						
						for (int j = 0; j < wordFrequencyList.size(); ++j) {
							
							WordFrequency wordFrequency = wordFrequencyList.get(j);
							
							if (wordFrequency.getWord().equals(word)) {
								
								fileWriter.write(", " + wordFrequency.getFrequency());
								
								break;		
							}
							
							if (j == wordFrequencyList.size()-1) {
								fileWriter.write(", 0");
							}
						}
						
					}
					
					
					fileWriter.write(System.lineSeparator());
					
					
				}
				
				
			}
			
			fileWriter.close();
			
		} catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	public void updateCloudMaker(CloudMaker cloudMaker) {
		this.cloudMaker = cloudMaker;
	}
}
