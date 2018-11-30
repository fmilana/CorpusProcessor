package application;

import java.awt.Dimension;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Properties;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.DirectoryChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.ChartMaker;
import model.CloudMaker;
import model.CombinedReader;
import tabs.AboutTab;
import tabs.AlignmentTab;
import tabs.ExtractionTab;
import tabs.LexicalFrequencyTab;
import tabs.SearchTab;
import tabs.TypeTokenTab;
import tabs.WordCloudTab;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TabPane;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;

public class Main extends Application {

	private Properties properties;

	private CombinedReader combinedReader;
	private ChartMaker chartMaker;
	private CloudMaker cloudMaker;
	
	private Scene scene;

	private Stage primaryStage;
	private Stage popupStage;
	
	private ExtractionTab extractionTab;
	private AlignmentTab alignmentTab;
	private SearchTab searchTab;
	private TypeTokenTab typeTokenTab;
	private LexicalFrequencyTab lexicalFrequencyTab;
	private WordCloudTab wordCloudTab;
	private AboutTab aboutTab;


	public static void main(String[] args) {
		launch(args);
	}              

	@Override
	public void start(Stage primaryStage) throws Exception {
		
		extractionTab = new ExtractionTab(this);
		alignmentTab = new AlignmentTab(this);
		searchTab = new SearchTab(this);
		typeTokenTab = new TypeTokenTab(this);
		lexicalFrequencyTab = new LexicalFrequencyTab(this);
		wordCloudTab = new WordCloudTab(this);
		aboutTab = new AboutTab();
		
		loadOrCreateProperties();
		
		if (properties.getProperty("filesPath") != null) {
			
			System.out.println("FILEPATH = " + properties.getProperty("filesPath"));
			
			String filesPath = properties.getProperty("filesPath");
			
			combinedReader = new CombinedReader(filesPath);
			chartMaker = new ChartMaker(combinedReader.getTextReader());
			cloudMaker = new CloudMaker(filesPath);
			
			updateTabMakers();
		}

		this.primaryStage = primaryStage;

		primaryStage.setTitle("VW Corpus Processor");
		///////////////////////////IDE.///////////////////////////////
		primaryStage.getIcons().add(new Image("file:images/icon.png"));
		
		////////////////////////////JAR///////////////////////////////
//		primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/icon.png")));


		TabPane tabPane = new TabPane();
		tabPane.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);
		
		tabPane.getTabs().addAll(
				extractionTab,
				alignmentTab,
				searchTab,
				typeTokenTab,
				lexicalFrequencyTab,
				wordCloudTab,
				aboutTab);
		
		Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		double width = screenSize.getWidth() / 1.35;
		double height = screenSize.getHeight() / 1.35;

		scene = new Scene(tabPane, width, height);
		
		
//		scene.getStylesheets().add(getClass().getResource("/application.css").toExternalForm());
		
////////////////////////////////MOVE APPLICATION.CSS TO APPLICATION PACKAGE AND USE THIS BELOW INSTEAD IF LAUNCHING FROM IDE////////
	            scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());                 ////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////		
		

		
		primaryStage.setScene(scene);
		primaryStage.setMinHeight(600);
		primaryStage.setMinWidth(1000);
		
		primaryStage.show();		
		
		if (popupStage != null) {
			popupStage.initOwner(primaryStage);
			popupStage.show();
		}
	}
		
	private void loadOrCreateProperties() throws IOException {
		
		properties = new Properties();
		InputStream inputStream = null;
		

		File propertiesFile = new File("config.properties");
		
		
		if (propertiesFile.exists()) {
		
			inputStream = new FileInputStream(propertiesFile);
	
			// load a properties file
			properties.load(inputStream);
		
//		System.out.println("FILE EXISTS!");


//		} catch (FileNotFoundException e) {
		
		} else {
			
			popupStage = new Stage();
			popupStage.setTitle("Select Files Path");
			popupStage.initOwner(primaryStage);
			popupStage.setResizable(false);
			popupStage.initStyle(StageStyle.UTILITY);
			
			popupStage.setOnCloseRequest(event -> {
				popupStage.close();
		        primaryStage.close();
			});
			
			popupStage.initModality(Modality.WINDOW_MODAL);
			popupStage.initOwner(primaryStage);
			
			Button chooseFileDirectoryButton = new Button("Choose Files Directory");
			chooseFileDirectoryButton.setMinWidth(200);
			
			Button doneButton = new Button("Done");
			doneButton.setDisable(true);
			doneButton.setMinWidth(200);
			
			chooseFileDirectoryButton.setOnAction(new EventHandler<ActionEvent>() {
			    @Override
			    public void handle(ActionEvent event) {
			    	DirectoryChooser directoryChooser = new DirectoryChooser();
			    	File selectedDirectory = directoryChooser.showDialog(popupStage);
			    	
			    	if (selectedDirectory != null) {
			    		OutputStream outputStream = null;
			    		
						try {
							outputStream = new FileOutputStream(propertiesFile);
							
						} catch (FileNotFoundException e1) {
							// TODO Auto-generated catch block
							
							e1.printStackTrace();
						}
						
			    		properties.setProperty("filesPath", selectedDirectory.getAbsolutePath());
			    		
			    		try {
							properties.store(outputStream, null);
							
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
			    		
			    		doneButton.setDisable(false);
			    		
			    		try {
							outputStream.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
			    	}
			    }
			});
			
			BorderPane borderPane = new BorderPane();
			borderPane.setTop(chooseFileDirectoryButton);
			borderPane.setBottom(doneButton);
			
			BorderPane.setAlignment(chooseFileDirectoryButton, Pos.CENTER);
			BorderPane.setAlignment(doneButton, Pos.CENTER);
			
			borderPane.setPadding(new Insets(10,10,10,10));
			
			Scene scene = new Scene(borderPane, 230, 100);
			
			popupStage.setScene(scene);
			
			doneButton.setOnAction(new EventHandler<ActionEvent>() {
			    @Override
			    public void handle(ActionEvent event) {
			    	
			    	combinedReader = new CombinedReader(properties.getProperty("filesPath"));
					chartMaker = new ChartMaker(combinedReader.getTextReader());
					cloudMaker = new CloudMaker(properties.getProperty("filesPath"));
			    	
					updateTabMakers();
					
					popupStage.close();
			    }
			});
			
			
		} 
//		finally {
		if (inputStream != null) {
			try {
				inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
//		}
	}
	
	public ComboBox<String> createTranslatorComboBox() {

		ObservableList<String> translatorOptions = FXCollections.observableArrayList("Virginia Woolf (1927)", "Celenza (1934)", 
				"Fusini (1992)", "Cucciarelli (1993)", "Malagò (1993)", "Zazo (1994)", "Bianciardi (1994)", "Fusini (1998)",
				"De Marinis (2012)", "Fusini (2012)", "Nadotti (2014)", "Artioli (2017)");

		ComboBox<String> translatorComboBox = new ComboBox<String>(translatorOptions);
		translatorComboBox.setPromptText("Translator");

		return translatorComboBox;
	}

	public ComboBox<String> createTagComboBox() {

		ObservableList<String> tagOptions = FXCollections.observableArrayList("<DD>", "<FID>", "<IIM>", "<LS>",
				"<NCE>", "<NCP>", "<NCPDD>", "<RW>", "<SC>", "<UP>");

		ComboBox<String> tagComboBox = new ComboBox<String>(tagOptions);
		tagComboBox.setPromptText("Tag");

		return tagComboBox;
	}

	public void saveFile(String text, File file) {

		String[] splitText = text.split("\n");

		try {
			
			FileWriter fileWriter = new FileWriter(file);

			for (int i = 0; i < splitText.length; ++i) {
				fileWriter.write(splitText[i]);
				fileWriter.write(System.lineSeparator());
			}

			fileWriter.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public CombinedReader getCombinedReader() {
		return combinedReader;
	}
	
	public ChartMaker getChartMaker() {
		return chartMaker;
	}
	
	public CloudMaker getCloudMaker() {
		return cloudMaker;
	}
	
	public Stage getPrimaryStage() {
		return primaryStage;
	}
	
//	public Properties getProperties() {
//		return properties;
//	}
	
	private void updateTabMakers() {
		typeTokenTab.updateChartMaker(chartMaker);
		lexicalFrequencyTab.updateChartMaker(chartMaker);
		wordCloudTab.updateCloudMaker(cloudMaker);
	}
	
	public Scene getScene() {
		return scene;
	}
}
