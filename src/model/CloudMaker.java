package model;

import java.awt.Color;
import java.awt.Dimension;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import org.apache.log4j.BasicConfigurator;

import com.kennycason.kumo.CollisionMode;
import com.kennycason.kumo.Word;
import com.kennycason.kumo.WordCloud;
import com.kennycason.kumo.WordFrequency;
import com.kennycason.kumo.bg.PixelBoundryBackground;
import com.kennycason.kumo.font.scale.LinearFontScalar;
import com.kennycason.kumo.nlp.FrequencyAnalyzer;
import com.kennycason.kumo.nlp.normalize.LowerCaseNormalizer;
import com.kennycason.kumo.palette.ColorPalette;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;

public class CloudMaker {
	
	private TextReader textReader;
//	private List<WordFrequency> wordFrequencies;
	private LinkedHashMap<WordCloud, String> wordCloudMap;
	private LinkedHashMap<String, List<WordFrequency>> wordFrequencyMap;
	
	private HashMap<String, ArrayList<String>> translatorSkippedWords;

	public CloudMaker(String filesPath) {
		textReader = new TextReader(filesPath);
	}
	
	
	public void generateWordCloud(ArrayList<String> selectedTranslators, boolean removeStopWords, BorderPane borderPane, ProgressBar progressBar, Button saveButton) throws IOException {
		
		final Task<Void> task = new Task<Void>() {

			@Override
			protected Void call() throws Exception {
				
//				ArrayList<WordCloud> wordClouds = new ArrayList<WordCloud>();
				wordCloudMap = new LinkedHashMap<WordCloud, String>();
				wordFrequencyMap = new LinkedHashMap<String, List<WordFrequency>>();
				
				translatorSkippedWords = new HashMap<String, ArrayList<String>>();
				
				for (int i = 0; i < selectedTranslators.size(); ++i) {
					
					System.out.println("1");
					
					String translator = selectedTranslators.get(i);
					
					final FrequencyAnalyzer frequencyAnalyzer = new FrequencyAnalyzer();
					frequencyAnalyzer.setWordFrequenciesToReturn(200);
					frequencyAnalyzer.setNormalizer(new LowerCaseNormalizer());
			
					BasicConfigurator.configure();	
					
					String text = textReader.getWholeFilteredText(translator, removeStopWords);
					
					System.out.println("2");
					
					List<WordFrequency> wordFrequencies = frequencyAnalyzer.load(new ByteArrayInputStream(text.getBytes(StandardCharsets.UTF_8)));
					
					System.out.println("3");
					
					wordFrequencyMap.put(translator, wordFrequencies);
					
					System.out.println("4");
					
					Dimension dimension = new Dimension(250,600);
					
					System.out.println("5");
					
					final WordCloud wordCloud = new WordCloud(dimension, CollisionMode.PIXEL_PERFECT);
					
					System.out.println("6");
					
					wordCloud.setPadding(2);
					
					System.out.println("7");
					
					///////////////////////////////////IDE/////////////////////////////////////
//					wordCloud.setBackground(new PixelBoundryBackground("images/lighthouse.png"));
					///////////////////////////////////////////////////////////////////////////
					
					//////////////////////////////////JAR//////////////////////////////////////
					wordCloud.setBackground(new PixelBoundryBackground(getClass().getResourceAsStream("/lighthouse.png")));
					///////////////////////////////////////////////////////////////////////////
					
					
					System.out.println("8");
					
					wordCloud.setBackgroundColor(new Color(0xFFFFFF));
					
					System.out.println("9");
					
					wordCloud.setColorPalette(new ColorPalette(new Color(0x4055F1), new Color(0x408DF1), new Color(0x40AAF1), new Color(0x40C5F1), new Color(0x40D3F1), new Color(0x663096)));
					System.out.println("10");
					
					wordCloud.setFontScalar(new LinearFontScalar(10, 40));
	
					System.out.println("11");
					
					wordCloud.build(wordFrequencies);
					
					System.out.println("12");
					
					wordCloudMap.put(wordCloud, translator);
					
					System.out.println("13");
					
					ArrayList<String> skippedWords = new ArrayList<String>();
					
					for (Word word : wordCloud.getSkipped()) {
						skippedWords.add(word.getWord());
					}
					
					translatorSkippedWords.put(translator, skippedWords);
					
					updateProgress(i+1, selectedTranslators.size());
				}
				
//				System.out.println(wordFrequencies);
				
				Platform.runLater(new Runnable(){
					@Override
					public void run() {				
						
						ScrollPane scrollPane = new ScrollPane();
						
						GridPane gridPane = new GridPane();
						
						gridPane.minWidthProperty().bind(Bindings.createDoubleBinding(() -> 
						scrollPane.getViewportBounds().getWidth(), scrollPane.viewportBoundsProperty()));
						
						gridPane.setPadding(new Insets(20,20,20,20));
						
						int counter = 0;
						
						for (Entry<WordCloud, String> pair : wordCloudMap.entrySet()) { 
							
							ColumnConstraints columnConstraints = new ColumnConstraints();
				            columnConstraints.setPercentWidth(100.0 / wordCloudMap.size());
				            gridPane.getColumnConstraints().add(columnConstraints);
				            
							WordCloud wordCloud = pair.getKey();
							
							ImageView imageView = new ImageView();
							imageView.setImage(SwingFXUtils.toFXImage(wordCloud.getBufferedImage(), null));
							
							GridPane.setHalignment(imageView, HPos.CENTER);
							
							Label translatorLabel = new Label(wordCloudMap.get(wordCloud));
							translatorLabel.setFont(new Font(15));
							translatorLabel.setPadding(new Insets(20,0,0,0));
							
							GridPane.setHalignment(translatorLabel, HPos.CENTER);
							
							gridPane.add(imageView, counter, 0);
							gridPane.add(translatorLabel, counter, 1);
														
							
							++counter;
							
						}
						
						scrollPane.setContent(gridPane);
						
						borderPane.setCenter(scrollPane);
						
						saveButton.setDisable(false);
					}
				});

				return null;
			}
		};
		
		progressBar.progressProperty().bind(task.progressProperty());
		
		final Thread thread = new Thread(task);
        thread.start();
	}
	
//	public List<WordFrequency> getWordFrequencies() {
//		return wordFrequencies;
//	}
	
	public LinkedHashMap<WordCloud, String> getWordCloudMap() {
		
		return wordCloudMap;
	}
	
	public LinkedHashMap<String, List<WordFrequency>> getWordFrequencyMap() {
		
		return wordFrequencyMap;
		
	}
	
	public HashMap<String, ArrayList<String>> getTranslatorSkippedWords() {
		
		return translatorSkippedWords;
		
	}
}
