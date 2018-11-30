package tabs;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.ImageIO;

import application.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Tab;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import model.ChartMaker;

public class TypeTokenTab extends Tab {

	private Main main;
	
	private ChartMaker chartMaker;
	
	private BorderPane typeTokenBorderPane;
	
	private HashMap<String, CheckBox> typeTokenCheckBoxes;
	
	public TypeTokenTab(Main main) {
		
		this.main = main;
		chartMaker = main.getChartMaker();
		
		setText("Type Token");
		
		typeTokenBorderPane = new BorderPane();
		
		typeTokenBorderPane.setLeft(createTypeTokenVBox());
		typeTokenBorderPane.setTop(createTypeTokenHBox(typeTokenCheckBoxes));
		typeTokenBorderPane.setCenter(new Pane());
		
		setContent(typeTokenBorderPane);
	}
	
	private VBox createTypeTokenVBox() {
		
		typeTokenCheckBoxes = new HashMap<String, CheckBox>();
		
		VBox vbox = new VBox();
		vbox.setPadding(new Insets(20));
		vbox.setSpacing(10);
		
		CheckBox virginiaWoolfCheckBox = new CheckBox();
		virginiaWoolfCheckBox.setText("Virginia Woolf (1927)");
		virginiaWoolfCheckBox.setSelected(true);
		typeTokenCheckBoxes.put("Virginia Woolf (1927)", virginiaWoolfCheckBox);
		
		CheckBox celenzaCheckBox = new CheckBox();
		celenzaCheckBox.setText("Celenza (1934)");
		celenzaCheckBox.setSelected(true);
		typeTokenCheckBoxes.put("Celenza (1934)", celenzaCheckBox);
		
		CheckBox fusini1992CheckBox = new CheckBox();
		fusini1992CheckBox.setText("Fusini (1992)");
		fusini1992CheckBox.setSelected(true);
		typeTokenCheckBoxes.put("Fusini (1992)", fusini1992CheckBox);
		
		CheckBox cucciarelliCheckBox = new CheckBox();
		cucciarelliCheckBox.setText("Cucciarelli (1993)");
		cucciarelliCheckBox.setSelected(true);
		typeTokenCheckBoxes.put("Cucciarelli (1993)", cucciarelliCheckBox);
		
		CheckBox malagòCheckBox = new CheckBox();
		malagòCheckBox.setText("Malagò (1993)");
		malagòCheckBox.setSelected(true);
		typeTokenCheckBoxes.put("Malagò (1993)", malagòCheckBox);
		
		CheckBox zazoCheckBox = new CheckBox();
		zazoCheckBox.setText("Zazo (1994)");
		zazoCheckBox.setSelected(true);
		typeTokenCheckBoxes.put("Zazo (1994)", zazoCheckBox);
		
		CheckBox bianciardiCheckBox = new CheckBox();
		bianciardiCheckBox.setText("Bianciardi (1994)");
		bianciardiCheckBox.setSelected(true);
		typeTokenCheckBoxes.put("Bianciardi (1994)", bianciardiCheckBox);
		
		CheckBox fusini1998CheckBox = new CheckBox();
		fusini1998CheckBox.setText("Fusini (1998)");
		fusini1998CheckBox.setSelected(true);
		typeTokenCheckBoxes.put("Fusini (1998)", fusini1998CheckBox);
		
		CheckBox deMarinisCheckBox = new CheckBox();
		deMarinisCheckBox.setText("De Marinis (2012)");
		deMarinisCheckBox.setSelected(true);
		typeTokenCheckBoxes.put("De Marinis (2012)", deMarinisCheckBox);
		
		CheckBox fusini2012CheckBox = new CheckBox();
		fusini2012CheckBox.setText("Fusini (2012)");
		fusini2012CheckBox.setSelected(true);
		typeTokenCheckBoxes.put("Fusini (2012)", fusini1998CheckBox);
		
		CheckBox nadottiCheckBox = new CheckBox();
		nadottiCheckBox.setText("Nadotti (2014)");
		nadottiCheckBox.setSelected(true);
		typeTokenCheckBoxes.put("Nadotti (2014)", nadottiCheckBox);
		
		CheckBox artioliCheckBox = new CheckBox();
		artioliCheckBox.setText("Artioli (2017)");
		artioliCheckBox.setSelected(true);
		typeTokenCheckBoxes.put("Artioli (2017)", artioliCheckBox);
		
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
	
	private HBox createTypeTokenHBox(HashMap<String, CheckBox> typeTokenCheckBoxes) {
	
		HBox hbox = new HBox();
		hbox.setPadding(new Insets(20));
		hbox.setSpacing(10);
		hbox.setAlignment(Pos.CENTER_LEFT);
		
		Button calculateButton = new Button("Calculate");
		calculateButton.setMinWidth(100);		
		
		ComboBox<String> typeTokenComboBox = new ComboBox<String>(FXCollections.observableArrayList("Type vs Token", "Type-Token Ratio"));
		typeTokenComboBox.getSelectionModel().selectFirst();
		
		
		CheckBox virginiaWoolfCheckBox = typeTokenCheckBoxes.get("Virginia Woolf (1927)");
		CheckBox celenzaCheckBox = typeTokenCheckBoxes.get("Celenza (1934)");
		CheckBox fusini1992CheckBox = typeTokenCheckBoxes.get("Fusini (1992)");
		CheckBox cucciarelliCheckBox = typeTokenCheckBoxes.get("Cucciarelli (1993)");
		CheckBox malagòCheckBox = typeTokenCheckBoxes.get("Malagò (1993)");
		CheckBox zazoCheckBox = typeTokenCheckBoxes.get("Zazo (1994)");
		CheckBox bianciardiCheckBox = typeTokenCheckBoxes.get("Bianciardi (1994)");
		CheckBox fusini1998CheckBox = typeTokenCheckBoxes.get("Fusini (1998)");
		CheckBox deMarinisCheckBox = typeTokenCheckBoxes.get("De Marinis (2012)");
		CheckBox fusini2012CheckBox = typeTokenCheckBoxes.get("Fusini (2012)");
		CheckBox nadottiCheckBox = typeTokenCheckBoxes.get("Nadotti (2014)");
		CheckBox artioliCheckBox = typeTokenCheckBoxes.get("Artioli (2017)");
		
		
		CheckBox removeStopWordsCheckBox = new CheckBox();
		removeStopWordsCheckBox.setText("Remove Stop Words");
		removeStopWordsCheckBox.setSelected(true);
		
		Region region3 = new Region();
		
		
		
		ProgressBar progressBar = new ProgressBar();
		
		Button saveButton = new Button("Save As...");
		saveButton.setMinWidth(100);
		saveButton.setDisable(true);
		
		saveButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				FileChooser fileChooser = new FileChooser();	
				
				fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG file (*.png)", "*.png"));
				fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("TXT file (*.txt)", "*.txt"));
				
				File file = fileChooser.showSaveDialog(main.getPrimaryStage());
				
				if (file != null) {
					saveLineChart(file);
				}
				
			}
			
		});
		
		calculateButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				
				if (!saveButton.isDisabled()) {
					saveButton.setDisable(true);
				}
				
				typeTokenBorderPane.setCenter(progressBar);
				
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
				
				chartMaker.createLineChart(typeTokenComboBox.getValue(), selectedTranslators, removeStopWordsCheckBox.isSelected(), typeTokenBorderPane, progressBar, saveButton);
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
		
				
		
		Region region2 = new Region();
		HBox.setHgrow(region2, Priority.ALWAYS);
		
		hbox.getChildren().addAll(typeTokenComboBox,
				calculateButton,
				region2,
				removeStopWordsCheckBox,
				region3,
				saveButton);
		
		
		return hbox;
	}
	
	@SuppressWarnings("unchecked")
	private void saveLineChart(File file) {
		
		Node typeTokenBorderPaneContent = typeTokenBorderPane.getChildren().get(2);
		
		if (typeTokenBorderPaneContent instanceof LineChart) {
			
			LineChart<Number, Number> lineChart = (LineChart<Number, Number>)typeTokenBorderPaneContent;
			lineChart.setAnimated(false);
		
			try {
				
				FileWriter fileWriter = new FileWriter(file);
				
				String fileExtension = file.getName().substring(file.getName().lastIndexOf(".") + 1, file.getName().length());
				
				System.out.println(fileExtension);
				
				if (fileExtension.equals("png")) {
				
					WritableImage image = lineChart.snapshot(new SnapshotParameters(), null);
					
					try {					
		                ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
		                
		            } catch (IOException e) {
		                e.printStackTrace();
		            }
					
				} else if (fileExtension.equals("txt")) {
					
					ObservableList<Series<Number, Number>> allSeries = lineChart.getData();
					
					int maxNumberOfTokens = 0;
					
					for (int i = 0; i < allSeries.size(); ++i) {
						
						XYChart.Series<Number, Number> series = allSeries.get(i);
						
						int lastTokenNumber = (int) series.getData().get(series.getData().size()-1).getXValue();
						
						if (lastTokenNumber > maxNumberOfTokens) {
							
							maxNumberOfTokens = lastTokenNumber;							
						}						
					}

					fileWriter.write("Token,");
					
					for (int i = 0; i < allSeries.size(); ++i) {
						
						fileWriter.write(allSeries.get(i).getName());
						
						if (i < allSeries.size() - 1) {
							fileWriter.write(",");
						} 
					}
					
					fileWriter.write(System.lineSeparator());
					
					
					for (int i = 0; i < maxNumberOfTokens; ++i) {
						
						fileWriter.write(i + 1 + ",");
						
						for (int j = 0; j < allSeries.size(); j ++) {
							
							if (i < allSeries.get(j).getData().size()) {
							
								if (lineChart.getTitle().equals("Type-Token Ratio")) {
									
									fileWriter.write(Double.toString((double)allSeries.get(j).getData().get(i).getYValue()));	
									
								} else {
									
									fileWriter.write(Integer.toString((int)allSeries.get(j).getData().get(i).getYValue()));	
									
								}
								
							} else {
								fileWriter.write(" ");
							}
							
							if  (j < allSeries.size() - 1) {
								fileWriter.write(",");
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
	}
	
	public void updateChartMaker(ChartMaker chartMaker) {
		this.chartMaker = chartMaker;
	}
}
