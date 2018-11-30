package tabs;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import application.Main;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Tab;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import model.ChartMaker;

public class LexicalFrequencyTab extends Tab {
	
	private Main main;
	
	private ChartMaker chartMaker;
	
	private BorderPane lexicalFrequencyBorderPane;
	
	private HashMap<String, CheckBox> lexicalFrequencyCheckBoxes;
	
	public LexicalFrequencyTab(Main main) {
		
		this.main = main;
		
		chartMaker = main.getChartMaker();
		
		setText("Lexical Frequency");
		
		lexicalFrequencyBorderPane = new BorderPane();
		
		lexicalFrequencyBorderPane.setLeft(createLexicalFerquencyVBox());
		lexicalFrequencyBorderPane.setTop(createLexicalFrequencyHBox(lexicalFrequencyCheckBoxes));
		
		setContent(lexicalFrequencyBorderPane);
	}
	
	private VBox createLexicalFerquencyVBox() {
		
		lexicalFrequencyCheckBoxes = new HashMap<String, CheckBox>();
		
		VBox vbox = new VBox();
		vbox.setPadding(new Insets(20));
		vbox.setSpacing(10);
		
		CheckBox virginiaWoolfCheckBox = new CheckBox();
		virginiaWoolfCheckBox.setText("Virginia Woolf (1927)");
		virginiaWoolfCheckBox.setSelected(true);
		lexicalFrequencyCheckBoxes.put("Virginia Woolf (1927)", virginiaWoolfCheckBox);
		
		CheckBox celenzaCheckBox = new CheckBox();
		celenzaCheckBox.setText("Celenza (1934)");
		celenzaCheckBox.setSelected(true);
		lexicalFrequencyCheckBoxes.put("Celenza (1934)", celenzaCheckBox);
		
		CheckBox fusini1992CheckBox = new CheckBox();
		fusini1992CheckBox.setText("Fusini (1992)");
		fusini1992CheckBox.setSelected(true);
		lexicalFrequencyCheckBoxes.put("Fusini (1992)", fusini1992CheckBox);
		
		CheckBox cucciarelliCheckBox = new CheckBox();
		cucciarelliCheckBox.setText("Cucciarelli (1993)");
		cucciarelliCheckBox.setSelected(true);
		lexicalFrequencyCheckBoxes.put("Cucciarelli (1993)", cucciarelliCheckBox);
		
		CheckBox malagòCheckBox = new CheckBox();
		malagòCheckBox.setText("Malagò (1993)");
		malagòCheckBox.setSelected(true);
		lexicalFrequencyCheckBoxes.put("Malagò (1993)", malagòCheckBox);
		
		CheckBox zazoCheckBox = new CheckBox();
		zazoCheckBox.setText("Zazo (1994)");
		zazoCheckBox.setSelected(true);
		lexicalFrequencyCheckBoxes.put("Zazo (1994)", zazoCheckBox);
		
		CheckBox bianciardiCheckBox = new CheckBox();
		bianciardiCheckBox.setText("Bianciardi (1994)");
		bianciardiCheckBox.setSelected(true);
		lexicalFrequencyCheckBoxes.put("Bianciardi (1994)", bianciardiCheckBox);
		
		CheckBox fusini1998CheckBox = new CheckBox();
		fusini1998CheckBox.setText("Fusini (1998)");
		fusini1998CheckBox.setSelected(true);
		lexicalFrequencyCheckBoxes.put("Fusini (1998)", fusini1998CheckBox);
		
		CheckBox deMarinisCheckBox = new CheckBox();
		deMarinisCheckBox.setText("De Marinis (2012)");
		deMarinisCheckBox.setSelected(true);
		lexicalFrequencyCheckBoxes.put("De Marinis (2012)", deMarinisCheckBox);
		
		CheckBox fusini2012CheckBox = new CheckBox();
		fusini2012CheckBox.setText("Fusini (2012)");
		fusini2012CheckBox.setSelected(true);
		lexicalFrequencyCheckBoxes.put("Fusini (2012)", fusini1998CheckBox);
		
		CheckBox nadottiCheckBox = new CheckBox();
		nadottiCheckBox.setText("Nadotti (2014)");
		nadottiCheckBox.setSelected(true);
		lexicalFrequencyCheckBoxes.put("Nadotti (2014)", nadottiCheckBox);
		
		CheckBox artioliCheckBox = new CheckBox();
		artioliCheckBox.setText("Artioli (2017)");
		artioliCheckBox.setSelected(true);
		lexicalFrequencyCheckBoxes.put("Artioli (2017)", artioliCheckBox);
		
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
	
	private HBox createLexicalFrequencyHBox(HashMap<String, CheckBox> lexicalFrequencyCheckBoxes) {
		
		HBox hbox = new HBox();
		hbox.setPadding(new Insets(20));
		hbox.setSpacing(10);
		hbox.setAlignment(Pos.CENTER_LEFT);
		
		CheckBox normaliseFrequencyCheckBox = new CheckBox("Normalise Frequency");
		normaliseFrequencyCheckBox.setSelected(true);
		normaliseFrequencyCheckBox.setVisible(false);
		
		ComboBox<String> typeOfChartComboBox = new ComboBox<String>(FXCollections.observableArrayList("Lexical Frequency", "Frequency of Repeated Words", "Frequency Distribution of Word Occurrence"));
		typeOfChartComboBox.getSelectionModel().selectFirst();
		
		typeOfChartComboBox.valueProperty().addListener(new ChangeListener<String>() {
			
			@Override public void changed(ObservableValue observableValue, String oldString, String newString) {
				
				if (newString.equals("Frequency Distribution of Word Occurrence")) {
					
					normaliseFrequencyCheckBox.setVisible(true);
				} else {
					
					normaliseFrequencyCheckBox.setVisible(false);
				}
				
	        }    
	    });
		
		Button calculateButton = new Button("Calculate");
		calculateButton.setMinWidth(100);
		
		
		CheckBox virginiaWoolfCheckBox = lexicalFrequencyCheckBoxes.get("Virginia Woolf (1927)");
		CheckBox celenzaCheckBox = lexicalFrequencyCheckBoxes.get("Celenza (1934)");
		CheckBox fusini1992CheckBox = lexicalFrequencyCheckBoxes.get("Fusini (1992)");
		CheckBox cucciarelliCheckBox = lexicalFrequencyCheckBoxes.get("Cucciarelli (1993)");
		CheckBox malagòCheckBox = lexicalFrequencyCheckBoxes.get("Malagò (1993)");
		CheckBox zazoCheckBox = lexicalFrequencyCheckBoxes.get("Zazo (1994)");
		CheckBox bianciardiCheckBox = lexicalFrequencyCheckBoxes.get("Bianciardi (1994)");
		CheckBox fusini1998CheckBox = lexicalFrequencyCheckBoxes.get("Fusini (1998)");
		CheckBox deMarinisCheckBox = lexicalFrequencyCheckBoxes.get("De Marinis (2012)");
		CheckBox fusini2012CheckBox = lexicalFrequencyCheckBoxes.get("Fusini (2012)");
		CheckBox nadottiCheckBox = lexicalFrequencyCheckBoxes.get("Nadotti (2014)");
		CheckBox artioliCheckBox = lexicalFrequencyCheckBoxes.get("Artioli (2017)");
		
		
		Region region2 = new Region();
		HBox.setHgrow(region2, Priority.ALWAYS);
		
		CheckBox removeStopWordsCheckBox = new CheckBox("Remove Stop Words");
		removeStopWordsCheckBox.setSelected(true);
		
		Region region3 = new Region();
		
		Button saveButton = new Button("Save As...");
		saveButton.setMinWidth(100);
		saveButton.setDisable(true);
		
		ProgressBar progressBar = new ProgressBar();
		
		
		calculateButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				
				if (!saveButton.isDisabled()) {
					saveButton.setDisable(true);
				}
				
				lexicalFrequencyBorderPane.setCenter(progressBar);
				
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
				
				chartMaker.createLexicalFrequencyChart(typeOfChartComboBox.getSelectionModel().getSelectedItem(), selectedTranslators, normaliseFrequencyCheckBox.isSelected(), removeStopWordsCheckBox.isSelected(), lexicalFrequencyBorderPane, progressBar, saveButton);
				
			}
		});
		
		calculateButton.disableProperty().bind(
				(virginiaWoolfCheckBox.selectedProperty().not().and(
				celenzaCheckBox.selectedProperty().not()).and(
				fusini1992CheckBox.selectedProperty().not()).and(
				cucciarelliCheckBox.selectedProperty().not()).and(
				malagòCheckBox.selectedProperty().not()).and(
				zazoCheckBox.selectedProperty().not()).and(
				bianciardiCheckBox.selectedProperty().not()).and(
				fusini1998CheckBox.selectedProperty().not()).and(
				deMarinisCheckBox.selectedProperty().not()).and(
				fusini2012CheckBox.selectedProperty().not()).and(
				artioliCheckBox.selectedProperty().not()).and(
				nadottiCheckBox.selectedProperty().not()))
				.or(progressBar.progressProperty().greaterThan(1)));
		
		
		
		saveButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				FileChooser fileChooser = new FileChooser();	
				
				fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG file (*.png)", "*.png"));
				fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("TXT file (*.txt)", "*.txt"));
				
				File file = fileChooser.showSaveDialog(main.getPrimaryStage());
				
				if (file != null) {
					saveLexicalChart(file);
				}
			}
		});
		
		
		hbox.getChildren().addAll(typeOfChartComboBox,
				calculateButton,
				region2,
				normaliseFrequencyCheckBox,
				new Region(),
				removeStopWordsCheckBox,
				region3,
				saveButton);
		
		return hbox;
		
	}
	
	@SuppressWarnings("unchecked")
	private void saveLexicalChart(File file) {
		
		Node lexicalFrequencyBorderPaneContent = lexicalFrequencyBorderPane.getChildren().get(2);
		
		if (lexicalFrequencyBorderPaneContent instanceof BarChart) {
			
			BarChart<String, Number> barChart = (BarChart<String, Number>)lexicalFrequencyBorderPaneContent;
			barChart.setAnimated(false);
		
			try {
				
				FileWriter fileWriter = new FileWriter(file);
				
				String fileExtension = file.getName().substring(file.getName().lastIndexOf(".") + 1, file.getName().length());
				
				System.out.println(fileExtension);
				
				if (fileExtension.equals("png")) {
				
					WritableImage image = barChart.snapshot(new SnapshotParameters(), null);
					
					try {					
		                ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
		                
		            } catch (IOException e) {
		                e.printStackTrace();
		            }
					
				} else if (fileExtension.equals("txt")) {
					
					ObservableList<Series<String, Number>> data = barChart.getData();				
					
					fileWriter.write("Translator");
					
					
					for (Series<String, Number> series : data) {
						
						fileWriter.write("," + series.getName());
						
					}
					
					fileWriter.write(System.lineSeparator());
					
					
					for (int i = 0; i < data.get(0).getData().size(); ++i) {
						
						fileWriter.write(data.get(0).getData().get(i).getXValue());
						
						for (int j = 0; j < data.size(); ++j) {
							
							fileWriter.write("," + data.get(j).getData().get(i).getYValue());
						}
						
						fileWriter.write(System.lineSeparator());
					}
					
				}
				
				fileWriter.close();
				
				
				
				
			} catch (IOException e) {
				e.printStackTrace();
			}	
			
		} else if (lexicalFrequencyBorderPaneContent instanceof LineChart) {
			
			LineChart<Number, Number> lineChart = (LineChart<Number, Number>)lexicalFrequencyBorderPaneContent;
			lineChart.setAnimated(false);
			
			try {
				
				FileWriter fileWriter = new FileWriter(file);
				
				String fileExtension = file.getName().substring(file.getName().lastIndexOf(".") + 1, file.getName().length());
				
				if (fileExtension.equals("png")) {
					
					WritableImage image = lineChart.snapshot(new SnapshotParameters(), null);
					
					try {					
		                ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
		                
		            } catch (IOException e) {
		                e.printStackTrace();
		            }
					
				} else if (fileExtension.equals("txt")) {
					
					HashMap<String, HashMap<Integer, Integer>> totalOccurrenceMaps = chartMaker.getTotalOccurrenceMaps();
					
					fileWriter.write("Occurrence");
					
					
					for (Map.Entry<String, HashMap<Integer, Integer>> entry : totalOccurrenceMaps.entrySet()) {
						
						fileWriter.write("," + entry.getKey());
					}
					
					fileWriter.write(System.lineSeparator());
					
					
					int maxOcc = 0;
					
					for (Map.Entry<String, HashMap<Integer, Integer>> entry : totalOccurrenceMaps.entrySet()) {
											
						for (Map.Entry<Integer, Integer> mapEntry : entry.getValue().entrySet()) {
							
							if (mapEntry.getKey() > maxOcc) {
								maxOcc = mapEntry.getKey();
							}
						}
					}
					
					for (int i = 1; i < maxOcc+1; ++i) {
						
						fileWriter.write(Integer.toString(i));
						
						for (Map.Entry<String, HashMap<Integer, Integer>> entry : totalOccurrenceMaps.entrySet()) {
								
							if (entry.getValue().get(i) != null) {
								
								fileWriter.write("," + entry.getValue().get(i));
								
							} else {
								
								fileWriter.write(",0");
							}
						}
						
						fileWriter.write(System.lineSeparator());
					}
					
					fileWriter.close();
				}
				
			} catch (IOException e) {
				e.printStackTrace();
			}	
		} 
	}
	
	public void updateChartMaker(ChartMaker chartMaker) {
		this.chartMaker = chartMaker;
	}
}
