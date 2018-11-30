package model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

import application.Main;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.shape.Path;

public class ChartMaker {
	
	private TextReader textReader;
	
	private HashMap<String, XYChart.Series<Number, Number>> allSeries;
	
	private LinkedHashMap<String, HashMap<Integer, Integer>> totalOccurrenceMaps;

	public ChartMaker(TextReader textReader) {
		this.textReader = textReader;
		allSeries = new HashMap<String, XYChart.Series<Number, Number>>();
	}
	
	public void createLineChart(String typeOfChart, ArrayList<String> translators, boolean removeStopWords, BorderPane borderPane, ProgressBar progressBar, Button saveButton) {
		
		final NumberAxis xAxis = new NumberAxis();
		xAxis.setLabel("Tokens");
		final NumberAxis yAxis = new NumberAxis();
		
		if (typeOfChart.equals("Type vs Token")) {
			yAxis.setLabel("Types");
		} else if (typeOfChart.equals("Type-Token Ratio")) {
			yAxis.setLabel("Ratio");
			xAxis.setForceZeroInRange(false);
		}
		
		final LineChart<Number, Number>  lineChart = new LineChart<Number, Number>(xAxis, yAxis);
		
		String title = new String();
		
		if (removeStopWords) {
			title = typeOfChart + " Without Stopwords";
		} else {
			title = typeOfChart;
		}
		
		lineChart.setTitle(title);
		lineChart.setCreateSymbols(false);
		
		final Task<Void> task = new Task<Void>() {

			@Override
			protected Void call() throws Exception {
				
				int totalNumberOfWords = 0;
				int wordCounter = 0;
				
				LinkedHashMap<String, String> translatorsTextMap = new LinkedHashMap<String,String>();
				
//				String dataFilePath = new String();
				String savedSeriesName = new String();
				
				int progressCounter = 1;
				
//				//calculates total number of words for progress bar
				for (String translator : translators) {
					
					if (removeStopWords) {
//						dataFilePath = "data/charts/" + typeOfChart + "/" + translator + " without stopwords.bin";
						savedSeriesName = translator + " " + typeOfChart + " without stopwords";
					} else {
//						dataFilePath = "data/charts/" + typeOfChart + "/" + translator + ".bin";
						savedSeriesName = translator + " " + typeOfChart;
					}
					
					
					///////////////////////////////if series already saved locally////////////////////////
//					if (allSeries.containsKey(savedSeriesName)) {
//						
//						System.out.println(savedSeriesName + " saved locally");
//						
//						lineChart.getData().add(allSeries.get(savedSeriesName));
//						
//					} else {		
						
						System.out.println(savedSeriesName + " not saved locally");
						
//						File dataFile = new File(dataFilePath);

						///////////////////try to read from data file//////////////////////////////////////
//						if (dataFile.exists()) {
//							
//							ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(dataFilePath));
//							
//	//						ObservableList<XYChart.Data<Number, Number>> observableList = FXCollections.observableArrayList((ArrayList<XYChart.Data<Number, Number>>) objectInputStream.readObject());
//							
//							XYChart.Series<Number, Number> series = new XYChart.Series<Number, Number>();
//							series.setName(translator);
//	//						series.setData(observableList);
//							
//							HashMap<Number, Number> dataHashMap = (HashMap<Number, Number>) objectInputStream.readObject();
//							
//							for (Map.Entry<Number, Number> entry : dataHashMap.entrySet()) {
//								
//								series.getData().add(new XYChart.Data<Number, Number>(entry.getKey(), entry.getValue()));
//								
//								System.out.println("reading ("+ entry.getKey() + "," + entry.getValue() + ") for " + translator);
//							}
//							
//							
//							
//							if (removeStopWords) {
//								allSeries.put(translator + " " + typeOfChart + " without stopwords", series);
//							} else {
//								allSeries.put(translator + " " + typeOfChart, series);
//							}
//							lineChart.getData().add(series);
//							
//							try {
//								objectInputStream.close();
//							} catch (IOException e) {
//								e.printStackTrace();
//							}
//							
//							updateProgress(progressCounter, translators.size());
//							++progressCounter;
//							
//						} else {
							
							System.out.println("SERIES NOT FOUND IN DATA");
							
							String translatorsWholeText = textReader.getWholeFilteredText(translator, removeStopWords);
							
							translatorsTextMap.put(translator, translatorsWholeText);
							
							totalNumberOfWords += translatorsWholeText.split(" ").length;
							
							
							
//						}
					}
//				}
				
				
				////////////////////////for each translator not found in data////////////////
				for (Map.Entry<String, String> entry : translatorsTextMap.entrySet()) {
					
					String translator = entry.getKey();
					String wholeTextOnlyWords = entry.getValue();
					
					XYChart.Series<Number, Number> series = new XYChart.Series<Number, Number>();
					series.setName(translator);
					
					String[] words = wholeTextOnlyWords.split(" ");					
					
					ArrayList<String> tokens = new ArrayList<String>();
					
					
					for (int j = 0; j < words.length; ++j) {
							
						String word = words[j];
						
						if (tokens.stream().noneMatch(token -> token.equalsIgnoreCase(word))) {
							
							tokens.add(word);
						} 
						
						if (typeOfChart.equals("Type vs Token")) {
							
							series.getData().add(new XYChart.Data<Number, Number>(j+1, tokens.size()));
							
						} else if (typeOfChart.equals("Type-Token Ratio")) {
												
							series.getData().add(new XYChart.Data<Number, Number>(j+1, (double)tokens.size()/(j+1)));
						}
						
						++wordCounter;
						
						System.out.println(wordCounter + "/" + totalNumberOfWords);
//						updateProgress(wordCounter, totalNumberOfWords);
						
					}
					
					
					
//					 System.out.println("ATTEMPTING TO WRITE TO FILE");
					
					//////////////////////////////writing series from data file/////////////////////////////
//					if (removeStopWords) {
//						dataFilePath = "data/charts/" + typeOfChart + "/" + translator + " without stopwords.bin";
//					} else {
//						dataFilePath = "data/charts/" + typeOfChart + "/"+ translator + ".bin";
//					}
//					
//					System.out.println("0");
//					
//					File dataFile = new File(dataFilePath);
//					dataFile.createNewFile();
//					
//					System.out.println("1");
//					
//					ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(dataFile));
//					
//					System.out.println("2");
//					
////					ArrayList<XYChart.Data<Number, Number>> dataArrayList = (ArrayList<Data<Number, Number>>) series.getData().stream().collect(Collectors.toList());
//					
//					HashMap<Number, Number> dataHashMap = new HashMap<Number, Number>();
//					
//					for (XYChart.Data<Number, Number> data : series.getData()) {
//						
//						dataHashMap.put(data.getXValue(), data.getYValue());
//						
//						System.out.println("writing for " + translator);
//					}
//					
//					
//					try {
////			        	objectOutputStream.writeObject(series);
//						objectOutputStream.writeObject(dataHashMap);
//					} catch (Exception e) {
//						System.out.println(e.getMessage());
//					}
//			        
//			        System.out.println("DATA WRITTEN IN FILE");
//			        
//			        try {
//			        	objectOutputStream.close();
//			        } catch (IOException e) {
//			        	e.printStackTrace();
//			        }
//					///////////////////////////////////////////////////////////////////////////////////////////
					
					
					
			        if (removeStopWords) {
						allSeries.put(translator + " " + typeOfChart + " without stopwords", series);
					} else {
						allSeries.put(translator + " " + typeOfChart, series);
					}
			        
					lineChart.getData().add(series);
					
					updateProgress(progressCounter, translators.size());
					++progressCounter;
					
				}
				//////////////////////////////////////////////////////////////////////////////////
				
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						borderPane.setCenter(lineChart);
						boldenLineChartSeriesOnHover(lineChart, allSeries);
						boldenLineChartSeriesOnLegendHover(lineChart);
						saveButton.setDisable(false);
						
						System.out.println("allSeries.size() ============> " + allSeries.size());
					}
				});
				
				return null;
			}
			
		};
		
		progressBar.progressProperty().bind(task.progressProperty());
		
		final Thread thread = new Thread(task);
        thread.start();
	}
	
	private void boldenLineChartSeriesOnHover(LineChart lineChart, HashMap<String, XYChart.Series<Number, Number>> allSeries) {
        
		for (Map.Entry<String, XYChart.Series<Number, Number>> entry : allSeries.entrySet()) {
			
			XYChart.Series<Number, Number> series = entry.getValue();
			
			if (lineChart.getData().contains(series)) {
				
		        Node seriesNode = series.getNode();
		        //seriesNode will be null if this method is called before the scene CSS has been applied
		        
		        if (seriesNode != null && seriesNode instanceof Path) {
		        	
		            Path seriesPath = (Path) seriesNode;
		            
		            double initialStrokeWidth = seriesPath.getStrokeWidth();
		
		            seriesPath.setOnMouseEntered(event -> {
		            	
		            	seriesPath.setStrokeWidth(initialStrokeWidth * 10);
		            	seriesPath.toFront();
		            	
		            });
		            
		            seriesPath.setOnMouseExited(event -> {
		     
		            	seriesPath.setStrokeWidth(initialStrokeWidth * 3);
		            });
		            
		            //tooltip
		            Set<Node> legendItems = lineChart.lookupAll("Label.chart-legend-item");
	            	
	            	for (Node legendItem : legendItems) {
	            		
	            		Label legend = (Label) legendItem;
	            		
	            		if (series.getName().equals(legend.getText())) {
	            			
	            				
	        				Tooltip.install(seriesNode, new Tooltip(legend.getText()));    
	            		}
	            	}
		        } 
			}
		}   
    }
	
	private void boldenLineChartSeriesOnLegendHover(LineChart lineChart) {
		
		Set<Node> legendItems = lineChart.lookupAll("Label.chart-legend-item");
		
		List<XYChart.Series> seriesList = lineChart.getData();
		
		if (legendItems.isEmpty()) {
			return;
		}
			
		for (Node legendItem : legendItems) {
			
			Label legend = (Label) legendItem;
			
			XYChart.Series matchingSeries = new XYChart.Series<>();
			
			for (XYChart.Series series : seriesList){
				
	            if (series.getName().equals(legend.getText())) {
	            	
	            	matchingSeries =  series;
	            }
			}
            
            Node seriesNode = matchingSeries.getNode();
            
            if (seriesNode != null && seriesNode instanceof Path) {
            	
                Path seriesPath = (Path) seriesNode;
                
                double initialStrokeWidth = seriesPath.getStrokeWidth();

                legendItem.setOnMouseEntered(event -> {
                	
                	seriesPath.setStrokeWidth(initialStrokeWidth * 10);
	            	seriesPath.toFront();
                });
                
                legendItem.setOnMouseExited(event -> {
                	
                	seriesPath.setStrokeWidth(initialStrokeWidth * 3);
                });
                
            }
			
		}
	}
	
	public void createLexicalFrequencyChart(String typeOfChart, ArrayList<String> translators, boolean normaliseFrequency, boolean removeStopWords, BorderPane borderPane, ProgressBar progressBar, Button saveButton) {
        
        final Task<Void> task = new Task<Void>() {

			@SuppressWarnings("unchecked")
			@Override
			protected Void call() throws Exception {
							
				
				HashMap<String, String> translatorsTextMap = new HashMap<String,String>();
				
				
				for (int i = 0; i < translators.size(); ++i) {
					
					String translator = translators.get(i);
					
					String wholeFilteredText = textReader.getWholeFilteredText(translator, removeStopWords);
					
					translatorsTextMap.put(translator, wholeFilteredText);
					
					updateProgress(i+1, translators.size());
				}
				
				
				if (!typeOfChart.equals("Frequency Distribution of Word Occurrence")) {
					
					final CategoryAxis xAxis = new CategoryAxis();
			        final NumberAxis yAxis = new NumberAxis();
			        
			        BarChart<String,Number> barChart = new BarChart<String,Number>(xAxis,yAxis);
			        
			        String title = new String();
			        
			        if (removeStopWords) {
						title = typeOfChart + " Without Stopwords";
					} else {
						title = typeOfChart;
					}
			        
			        barChart.setTitle(title);
			        xAxis.setLabel("Translator");
					
					XYChart.Series<String, Number> ALFSeries = new XYChart.Series<String, Number>();
					ALFSeries.setName("Average Lexical Frequency");
					
					XYChart.Series<String, Number> variationRatioSeries = new XYChart.Series<String, Number>();
					variationRatioSeries.setName("Coefficient of Variation");
					
					XYChart.Series<String, Number> leftVariationRatioSeries = new XYChart.Series<String, Number>();
					leftVariationRatioSeries.setName("Left Coefficient of Variation");
					
					XYChart.Series<String, Number> rightVariationRatioSeries = new XYChart.Series<String, Number>();
					rightVariationRatioSeries.setName("Right Coefficient of Variation");
								
					for (int i = 0; i < translators.size(); ++i) {
						
						String translator = translators.get(i);
						
						HashMap<String, Integer> repetitionMap = new HashMap<String, Integer>();
				        
				        for (String word : translatorsTextMap.get(translator).split(" ")) {
				        	
				        	if (repetitionMap.containsKey(word)) {
				        		repetitionMap.put(word, repetitionMap.get(word) + 1);
				        	} else {
				        		repetitionMap.put(word, 1);
				        	}
				        	
				        }
				        
				        
				        if (typeOfChart.equals("Frequency of Repeated Words")) {			        
					        ////////removing 1//////
					        
					        repetitionMap.entrySet().removeIf(pair -> pair.getValue() == 1);
					        
					        //////////////////////////////////////////////////////
				        }
				      
				        
				        
				        int numberOfWords = repetitionMap.size();
				        
				        int sumOfRepetitions = 0;
				        
				        for (Map.Entry<String, Integer> pair : repetitionMap.entrySet()) {
				        	
				        	sumOfRepetitions += pair.getValue();
				        	
				        	
				        }
				        
				        double ALF = (double)sumOfRepetitions/numberOfWords;
				        
				        
				        ALFSeries.getData().add(new XYChart.Data<String, Number>(translator, ALF));
				        
				        
				        
				        double sumOfError = 0;
				        
				        int leftNCounter = 0;
				        int rightNCounter = 0;
				        
				        double leftSumOfError = 0;
				        double rightSumOfError = 0;
				        
				        for (Map.Entry<String, Integer> pair : repetitionMap.entrySet()) {
				        	
				        	int frequency = pair.getValue();
				        	
				        	sumOfError += Math.pow((frequency - ALF), 2);
				        	
				        	if (frequency <= ALF) {
				        		++leftNCounter;
				        		
				        		leftSumOfError += Math.pow((frequency - ALF), 2);
				        		
				        	} else {
				        		++rightNCounter;
				        		
				        		rightSumOfError += Math.pow((frequency - ALF), 2);
				        	}
				        	
				        }
				        
				        double sigma = Math.sqrt(sumOfError/repetitionMap.size());
				        
				        variationRatioSeries.getData().add(new XYChart.Data<String, Number>(translator, sigma/ALF));
				        
				        
				        double leftSigma = Math.sqrt(leftSumOfError/leftNCounter);
				        
				        leftVariationRatioSeries.getData().add(new XYChart.Data<String, Number>(translator, leftSigma/ALF));
				       
				        
				        double rightSigma = Math.sqrt(rightSumOfError/rightNCounter);
				        
				        rightVariationRatioSeries.getData().add(new XYChart.Data<String, Number>(translator, rightSigma/ALF));
				        
				        
					}
				
				
					barChart.getData().addAll(ALFSeries, variationRatioSeries, leftVariationRatioSeries, rightVariationRatioSeries);
					
					Platform.runLater(new Runnable(){
						@Override
						public void run() {
							
							borderPane.setCenter(barChart);
							
							saveButton.setDisable(false);
						}
					});
					
				} else {
					
					////////////////////////Frequency Distribution of Word Occurrence///////////////////////////
					
					final NumberAxis xAxis = new NumberAxis(1, 15, 1);
			        final NumberAxis yAxis = new NumberAxis();
			        
			        LineChart<Number,Number> lineChart = new LineChart<Number,Number>(xAxis,yAxis);
			        
			        String title = new String();
			        
			        if (removeStopWords) {
						title = typeOfChart + " Without Stopwords";
					} else {
						title = typeOfChart;
					}
			        
			        lineChart.setTitle(title);
			        xAxis.setLabel("Word Occurrence");
			        
			        if (normaliseFrequency) {
			        	yAxis.setLabel("Frequency (%)");
			        } else {
			        	yAxis.setLabel("Frequency");
			        }
					
					allSeries = new HashMap<String, XYChart.Series<Number, Number>>();
										
//					FileWriter fileWriter = new FileWriter("txt/maxWordDump.txt");
					
					totalOccurrenceMaps = new LinkedHashMap<String, HashMap<Integer, Integer>>();
					
					for (int i = 0; i < translators.size(); ++i) {
						
						String translator = translators.get(i);
						
						HashMap<String, Integer> repetitionMap = new HashMap<String, Integer>();
						
//						fileWriter.write(translator + System.lineSeparator());
				        
				        for (String word : translatorsTextMap.get(translator).split(" ")) {
				        	
				        	if (repetitionMap.containsKey(word)) {
				        		repetitionMap.put(word, repetitionMap.get(word) + 1);
				        	} else {
				        		repetitionMap.put(word, 1);
				        	}
				        	
				        }
				        
				        
				        HashMap<Integer, Integer> occurrenceMap = new HashMap<Integer, Integer>();
				        HashMap<Integer, Integer> totalOccurrenceMap = new HashMap<Integer, Integer>();
				        
//				        String maxWord = new String();
				        int maxOcc = 0;
				        
				        for (Map.Entry<String,Integer> entry : repetitionMap.entrySet()) {
				        	
				        	int occurrence = entry.getValue();
				        	
				        	if (occurrence > maxOcc) {
//				        		maxWord = entry.getKey();
				        		maxOcc = occurrence;
				        	}
				        	
				        	if (occurrence <= 15 && occurrenceMap.containsKey(occurrence)) {
				        		
				        		occurrenceMap.put(occurrence, occurrenceMap.get(occurrence) + 1);
				        		
				        	} else if (occurrence <= 15) {
				        		
				        		occurrenceMap.put(occurrence, 1);
				        	} 
				        	
				        	if (totalOccurrenceMap.containsKey(occurrence)) {
				        		
				        		totalOccurrenceMap.put(occurrence, totalOccurrenceMap.get(occurrence) + 1);
				        		
				        	} else {
				        		
				        		totalOccurrenceMap.put(occurrence, 1);
				        		
				        	}
				        	
				        }
				        
				        totalOccurrenceMaps.put(translator, totalOccurrenceMap);
				        
				        
				        XYChart.Series<Number, Number> series = new XYChart.Series<Number, Number>();
						series.setName(translator);		
						
						
						if (normaliseFrequency) {
							for (Map.Entry<Integer, Integer> entry : occurrenceMap.entrySet()) {
								
								series.getData().add(new XYChart.Data<Number, Number>(entry.getKey(), (double)entry.getValue()/translatorsTextMap.get(translator).split(" ").length*100));
							}
						} else {
							for (Map.Entry<Integer, Integer> entry : occurrenceMap.entrySet()) {
								
								series.getData().add(new XYChart.Data<Number, Number>(entry.getKey(), entry.getValue()));
							}
							
						}
						
						
						if (removeStopWords) {
							allSeries.put(translator + " " + typeOfChart + " without stopwords", series);
						} else {
							allSeries.put(translator + " " + typeOfChart, series);
						}
						lineChart.getData().add(series);
				        
				        
					}				
					
					Platform.runLater(new Runnable(){
						@Override
						public void run() {
							
							boldenLineChartSeriesOnHover(lineChart, allSeries);
							boldenLineChartSeriesOnLegendHover(lineChart);
							
							borderPane.setCenter(lineChart);
							
							saveButton.setDisable(false);
						}
					});
					
					
				}
				
				return null;
				
			}
			
        };
        
        progressBar.progressProperty().bind(task.progressProperty());
		
		final Thread thread = new Thread(task);
        thread.start();
        
	}
	
	public HashMap<String, HashMap<Integer, Integer>> getTotalOccurrenceMaps() {
		
		return totalOccurrenceMaps;
	}

}
