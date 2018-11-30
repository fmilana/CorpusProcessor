package tabs;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import application.Main;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import model.Pair;

public class AlignmentTab extends Tab {
	
	private Main main;
	
//	private TextArea alignmentTextArea;
	private TextFlow alignmentTextFlow;
	private BorderPane alignmentBorderPane;
	private ScrollPane alignmentScrollPane;
	
//	private String extractedText;
	private ArrayList<String> extractedTexts;
	
	private HashMap<String, CheckBox> alignmentCheckBoxes;
	
	private String selectedTagName;
	
	private Button saveButton;
	
	private HBox pageHBox;
	private Button backButton;
	private TextField pageTextField;
	private Button forwardButton;
	
	private Label totalPageNumberLabel;
	
	private int currentPageNumber;
	
	
	public AlignmentTab(Main main) {
		
		this.main = main;
		
		setText("Alignment");

//		alignmentTextArea = new TextArea();
//		alignmentTextArea.setEditable(false);
		
		alignmentTextFlow = new TextFlow();
		alignmentScrollPane = new ScrollPane();
		
		alignmentScrollPane.setContent(alignmentTextFlow);
		alignmentScrollPane.setPadding(new Insets(0, 15, 0, 15));

		alignmentBorderPane = new BorderPane();
		alignmentBorderPane.setLeft(createAlignmentVBox());
		alignmentBorderPane.setTop(createAlignmentHBox(alignmentCheckBoxes));
		

		alignmentBorderPane.setCenter(alignmentScrollPane);

		setContent(alignmentBorderPane);
	}
	
	private VBox createAlignmentVBox() {
		
		alignmentCheckBoxes = new HashMap<String, CheckBox>();
		
		VBox vbox = new VBox();
		vbox.setPadding(new Insets(20));
		vbox.setSpacing(10);
//		vbox.setAlignment(Position.CENTER);
		
		CheckBox virginiaWoolfCheckBox = new CheckBox();
		virginiaWoolfCheckBox.setText("Virginia Woolf (1927)");
		virginiaWoolfCheckBox.setSelected(true);
		alignmentCheckBoxes.put("Virginia Woolf (1927)", virginiaWoolfCheckBox);
		
		CheckBox celenzaCheckBox = new CheckBox();
		celenzaCheckBox.setText("Celenza (1934)");
		celenzaCheckBox.setSelected(true);
		alignmentCheckBoxes.put("Celenza (1934)", celenzaCheckBox);
		
		CheckBox fusini1992CheckBox = new CheckBox();
		fusini1992CheckBox.setText("Fusini (1992)");
		fusini1992CheckBox.setSelected(true);
		alignmentCheckBoxes.put("Fusini (1992)", fusini1992CheckBox);
		
		CheckBox cucciarelliCheckBox = new CheckBox();
		cucciarelliCheckBox.setText("Cucciarelli (1993)");
		cucciarelliCheckBox.setSelected(true);
		alignmentCheckBoxes.put("Cucciarelli (1993)", cucciarelliCheckBox);
		
		CheckBox malagòCheckBox = new CheckBox();
		malagòCheckBox.setText("Malagò (1993)");
		malagòCheckBox.setSelected(true);
		alignmentCheckBoxes.put("Malagò (1993)", malagòCheckBox);
		
		CheckBox zazoCheckBox = new CheckBox();
		zazoCheckBox.setText("Zazo (1994)");
		zazoCheckBox.setSelected(true);
		alignmentCheckBoxes.put("Zazo (1994)", zazoCheckBox);
		
		CheckBox bianciardiCheckBox = new CheckBox();
		bianciardiCheckBox.setText("Bianciardi (1994)");
		bianciardiCheckBox.setSelected(true);
		alignmentCheckBoxes.put("Bianciardi (1994)", bianciardiCheckBox);
		
		CheckBox fusini1998CheckBox = new CheckBox();
		fusini1998CheckBox.setText("Fusini (1998)");
		fusini1998CheckBox.setSelected(true);
		alignmentCheckBoxes.put("Fusini (1998)", fusini1998CheckBox);
		
		CheckBox deMarinisCheckBox = new CheckBox();
		deMarinisCheckBox.setText("De Marinis (2012)");
		deMarinisCheckBox.setSelected(true);
		alignmentCheckBoxes.put("De Marinis (2012)", deMarinisCheckBox);
		
		CheckBox fusini2012CheckBox = new CheckBox();
		fusini2012CheckBox.setText("Fusini (2012)");
		fusini2012CheckBox.setSelected(true);
		alignmentCheckBoxes.put("Fusini (2012)", fusini1998CheckBox);
		
		CheckBox nadottiCheckBox = new CheckBox();
		nadottiCheckBox.setText("Nadotti (2014)");
		nadottiCheckBox.setSelected(true);
		alignmentCheckBoxes.put("Nadotti (2014)", nadottiCheckBox);
		
		CheckBox artioliCheckBox = new CheckBox();
		artioliCheckBox.setText("Artioli (2017)");
		artioliCheckBox.setSelected(true);
		alignmentCheckBoxes.put("Artioli (2017)", artioliCheckBox);
		
		
		Region region = new Region();
		VBox.setVgrow(region, Priority.ALWAYS);
		
		pageHBox = new HBox();
		pageHBox.setAlignment(Pos.CENTER);
		
		backButton = new Button("<");
		backButton.setMinHeight(30);
		backButton.setMinWidth(30);
		backButton.setDisable(true);
		
		backButton.setOnAction(new EventHandler<ActionEvent>() {
		    @Override public void handle(ActionEvent e) {
		    	
		    	--currentPageNumber;
		    	
		    	System.out.println("currentPageNumber = " + currentPageNumber);
		    	
		    	pageTextField.setText(Integer.toString(currentPageNumber));
		    	
		    	/////////////////////////////////
		    	///////////////////////////////
		    	/////////////////////////////////////!!!!!!!!!!!!
		    	
		    	if (forwardButton.isDisabled()) {
		    		forwardButton.setDisable(false);
		    	}
		    	
		    	if (currentPageNumber == 1) {
		    		backButton.setDisable(true);
		    	} 
		    	
		    	changePage();
		    }
		});
		
		Label pageLabel = new Label("Page ");
		pageLabel.setPadding(new Insets(0,0,0,10));
		
		pageTextField = new TextField();
		pageTextField.setText("1");
//		lastPageNumber = new String("1");
		pageTextField.setMinWidth(30);
		pageTextField.setMaxWidth(30);
		pageTextField.setAlignment(Pos.CENTER_RIGHT);
		
		pageTextField.textProperty().addListener(new ChangeListener<String>() {
		    @Override
		    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
		        if (!newValue.matches("\\d{0,2}")) {
		        	pageTextField.setText(oldValue);
		        }
		    }
		});
		
		pageTextField.setOnKeyPressed(new EventHandler<KeyEvent>() {
	        @Override
	        public void handle(KeyEvent keyEvent) {
	            if (keyEvent.getCode().equals(KeyCode.ENTER)) {
	            
	                if (pageTextField.getText().isEmpty() ||
	                		Integer.parseInt(pageTextField.getText()) < 1 ||
	                		Integer.parseInt(pageTextField.getText()) > extractedTexts.size()) {
            	
            			pageTextField.setText(Integer.toString(currentPageNumber));
            			
	            	} else {
	            		
	            		currentPageNumber = Integer.parseInt(pageTextField.getText());
                    	
                    	if (currentPageNumber == 1) {
		        			backButton.setDisable(true);
		        			forwardButton.setDisable(false);
		        			pageHBox.requestFocus();		        			
		        		} else if (currentPageNumber == extractedTexts.size()) {
		        			backButton.setDisable(false);
		        			forwardButton.setDisable(true);
		        		} else {
		        			backButton.setDisable(false);
		        			forwardButton.setDisable(false);
		        		}
                    	
                    	changePage();
                    	
                    	pageHBox.requestFocus();	            		
	            	}
	            }
	        }
	    });
		
		pageTextField.focusedProperty().addListener(new ChangeListener<Boolean>() {
	        @Override
	        public void changed(ObservableValue observableValue, Boolean wasFocused, Boolean isFocused) {
	            Platform.runLater(new Runnable() {
	                @Override
	                public void run() {
	                    if (isFocused && !pageTextField.getText().isEmpty()) {
	                    	pageTextField.selectAll();
	                    } else if (!isFocused && (pageTextField.getText().isEmpty() ||
	                    		Integer.parseInt(pageTextField.getText()) < 1 ||
	                    		Integer.parseInt(pageTextField.getText()) > extractedTexts.size())) {
	                    	
	                    	pageTextField.setText(Integer.toString(currentPageNumber));
	                    	
	                    } else if (!isFocused) {
	                    	
	                    	currentPageNumber = Integer.parseInt(pageTextField.getText());
	                    	
	                    	if (currentPageNumber == 1) {
			        			backButton.setDisable(true);
			        			forwardButton.setDisable(false);
			        			pageHBox.requestFocus();
			        		} else if (currentPageNumber == extractedTexts.size()) {
			        			backButton.setDisable(false);
			        			forwardButton.setDisable(true);
			        		} else {
			        			backButton.setDisable(false);
			        			forwardButton.setDisable(false);
			        		}
	                    	
	                    	changePage();
	                    }
	                }
	            });
	        }
	    });
		
		totalPageNumberLabel = new Label("/X");
		totalPageNumberLabel.setPadding(new Insets(0,10,0,0));
		
		forwardButton = new Button(">");
		forwardButton.setMinHeight(30);
		forwardButton.setMinWidth(30);
		
		forwardButton.setOnAction(new EventHandler<ActionEvent>() {
		    @Override public void handle(ActionEvent e) {
		    	
		    	++currentPageNumber;
		    	
		    	pageTextField.setText(Integer.toString(currentPageNumber));
		    	
		    	/////////////////////////////////
		    	///////////////////////////////
		    	/////////////////////////////////////!!!!!!!!!!!!
		    	
		    	if (backButton.isDisabled()) {
		    		backButton.setDisable(false);
		    	}
		    	
		    	if (currentPageNumber == extractedTexts.size()) {
		    		forwardButton.setDisable(true);
		    	}
		    	
		    	changePage();
		    }
		});
		
//		Region hRegion1 = new Region();
//		HBox.setHgrow(hRegion1, Priority.ALWAYS);
		
//		Region hRegion2 = new Region();
//		HBox.setHgrow(hRegion2, Priority.ALWAYS);
		
		pageHBox.getChildren().addAll(backButton, pageLabel, pageTextField, totalPageNumberLabel, forwardButton);
		pageHBox.setVisible(false);
		
		
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
				artioliCheckBox,
				region,
				pageHBox);
		
		
		return vbox;
	}

	private HBox createAlignmentHBox(HashMap<String, CheckBox> alignmentCheckBoxes) {
		
		HBox hbox = new HBox();
		
		hbox.setPadding(new Insets(20));
		hbox.setSpacing(10);
		hbox.setAlignment(Pos.CENTER_LEFT);
		
		ComboBox<String> tagComboBox = main.createTagComboBox();
		
		CheckBox virginiaWoolfCheckBox = alignmentCheckBoxes.get("Virginia Woolf (1927)");
		CheckBox celenzaCheckBox = alignmentCheckBoxes.get("Celenza (1934)");
		CheckBox fusini1992CheckBox = alignmentCheckBoxes.get("Fusini (1992)");
		CheckBox cucciarelliCheckBox = alignmentCheckBoxes.get("Cucciarelli (1993)");
		CheckBox malagòCheckBox = alignmentCheckBoxes.get("Malagò (1993)");
		CheckBox zazoCheckBox = alignmentCheckBoxes.get("Zazo (1994)");
		CheckBox bianciardiCheckBox = alignmentCheckBoxes.get("Bianciardi (1994)");
		CheckBox fusini1998CheckBox = alignmentCheckBoxes.get("Fusini (1998)");
		CheckBox deMarinisCheckBox = alignmentCheckBoxes.get("De Marinis (2012)");
		CheckBox fusini2012CheckBox = alignmentCheckBoxes.get("Fusini (2012)");
		CheckBox nadottiCheckBox = alignmentCheckBoxes.get("Nadotti (2014)");
		CheckBox artioliCheckBox = alignmentCheckBoxes.get("Artioli (2017)");
		
		
		CheckBox showOtherTagsCheckBox = new CheckBox();
		showOtherTagsCheckBox.setText("Show Other Tags");
		showOtherTagsCheckBox.setSelected(true);
		
		ProgressBar progressBar = new ProgressBar();
		
		
		Button alignButton = new Button("Align");
		alignButton.setMinWidth(100);	
		alignButton.disableProperty().bind(tagComboBox.valueProperty().isNull().or(
				virginiaWoolfCheckBox.selectedProperty().not().and(
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
				nadottiCheckBox.selectedProperty().not())));
		
		saveButton = new Button("Save As...");
		saveButton.setMinWidth(100);
		
		alignButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				
				pageHBox.setVisible(false);
				saveButton.setDisable(true);
				
				selectedTagName = tagComboBox.getValue().replaceAll("<", "").replaceAll(">", "");
				
				ArrayList<String> selectedTranslators = new ArrayList<>();
				
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
				
//				System.out.println("0");
				
				if (selectedTagName != null && selectedTranslators.size() > 0) {
					
					alignmentBorderPane.setCenter(progressBar);
					
//					System.out.println("0.5");
					
					final Task<Void> task = new Task<Void>() {
//						
						@Override
						protected Void call() throws Exception {
							
//							System.out.println("0.5.5");
							
//							extractedText = main.getCombinedReader().getAlignedParagraphsWithTag("<"+selectedTagName+">", selectedTranslators, showOtherTagsCheckBox.isSelected(), false);
							
							extractedTexts = main.getCombinedReader().getAlignedParagraphsWithTag("<"+selectedTagName+">", selectedTranslators, showOtherTagsCheckBox.isSelected(), false);
							
//							if (extractedText.length() > 0) {
//								saveButton.setDisable(false);
//								pageHBox.setVisible(true);
//							}
							
//							System.out.println("1");
							
							if (extractedTexts.size() > 0) {
								Platform.runLater(new Runnable(){
									@Override
									public void run() {	
										
//										System.out.println("2");
										
										alignmentTextFlow.getChildren().clear();
										
										alignmentTextFlow.getChildren().add(stringToTextFlow(extractedTexts.get(0)));
										
										currentPageNumber = 1;
										
//										System.out.println("3");
										
										resetPageHBox();
										
										alignmentBorderPane.setCenter(alignmentScrollPane);
																				
										saveButton.setDisable(false);
										pageHBox.setVisible(true);
										
										alignmentScrollPane.setVvalue(0); 
										
									}
								});
								
							} else {
								
								Platform.runLater(new Runnable(){
									@Override
									public void run() {	
										
										Text noResultsText = new Text("No results");
										noResultsText.setFont(Font.font(Font.getDefault().getName(), Font.getDefault().getSize() * 1.5));
										
										alignmentBorderPane.setCenter(noResultsText);
										
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
			}
		});
		
		
//		saveButton.disableProperty().bind(alignmentTextFlow.textProperty().isEmpty());
		
		saveButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				FileChooser fileChooser = new FileChooser();
				
				fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("TXT file (*.txt)", "*.txt"));
				
				File file = fileChooser.showSaveDialog(main.getPrimaryStage());
				
				if (file != null) {
					main.saveFile(String.join("", extractedTexts), file);
				}
			}
		});
		
		Region region1 = new Region();
		
		Region region2 = new Region();
		HBox.setHgrow(region2, Priority.ALWAYS);
		
		Region region3 = new Region();
		
		hbox.getChildren().addAll(tagComboBox,
				alignButton,
				region1,
				region2,
				showOtherTagsCheckBox,
				region3,
				saveButton);
		
		return hbox;
	}	
	
	private TextFlow stringToTextFlow(String string) {
		
		ArrayList<Pair<Integer, Integer>> highlightedTextStartAndEndList = new ArrayList<Pair<Integer, Integer>>();
		
		TextFlow textFlow = new TextFlow();
		
		Pattern pattern = Pattern.compile("<" + selectedTagName + ">(?s).*?</" + selectedTagName+ ">");
		
//		System.out.println("matching tag " + selectedTagName);
		
	    Matcher matcher = pattern.matcher(string);
	    
//	    System.out.println("0");
	    
	    while (matcher.find()) {
	    	highlightedTextStartAndEndList.add(new Pair<Integer, Integer>(matcher.start(), matcher.end()));
	    }
	    
//	    System.out.println("1");
	    
	    int restIndex = 0;
	    
//	    System.out.println("2");
	    
	    for (int i = 0; i < highlightedTextStartAndEndList.size(); ++i) {
	    	textFlow.getChildren().addAll(highlightedTexts(string.substring(restIndex, highlightedTextStartAndEndList.get(i).getKey()), false));
	    	
	    	textFlow.getChildren().addAll(highlightedTexts(string.substring(highlightedTextStartAndEndList.get(i).getKey(), highlightedTextStartAndEndList.get(i).getValue()), true));
	    	
	    	restIndex = highlightedTextStartAndEndList.get(i).getValue();
	    }
	    
//	    System.out.println("3");
	    
	    if (restIndex < string.length()-1) {
			textFlow.getChildren().addAll(highlightedTexts(string.substring(restIndex, string.length()-1), false));
		}
//	    System.out.println("4");
	    
	    return textFlow;
	}

	
	private ArrayList<Text> highlightedTexts(String string, boolean toHighlight) {
		
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
				
				textList.add(text);
				
				text = new Text(string.substring(numberPositions.get(i).getKey(), numberPositions.get(i).getValue()));
				
				text.setFont(Font.font(Font.getDefault().getName(), Font.getDefault().getSize() * 1.5));
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
			
			textList.add(text);
		}
		
//		System.out.println("highlighted");
		
		return textList;
	}
	
	private void changePage() {
		
		ProgressBar progressBar = new ProgressBar();
		
		alignmentBorderPane.setCenter(progressBar);
		
		boolean backButtonWasDisabled = backButton.isDisabled();
		boolean forwardButtonWasDisabled = forwardButton.isDisabled();
		
		saveButton.setDisable(true);
		backButton.setDisable(true);
		pageTextField.setDisable(true);
		forwardButton.setDisable(true);
		
		final Task<Void> task = new Task<Void>() {
			
			@Override
			protected Void call() throws Exception {
				
				Platform.runLater(new Runnable(){
					@Override
					public void run() {	
						
						alignmentTextFlow.getChildren().clear();
						
						alignmentTextFlow.getChildren().add(stringToTextFlow(extractedTexts.get(currentPageNumber-1)));
												
						alignmentBorderPane.setCenter(alignmentScrollPane);
						
						saveButton.setDisable(false);
						backButton.setDisable(backButtonWasDisabled);
						pageTextField.setDisable(false);
						forwardButton.setDisable(forwardButtonWasDisabled);
						
						alignmentScrollPane.setVvalue(0); 
					}
				});
				
				return null;
			}
		};
		
		progressBar.progressProperty().bind(task.progressProperty());
		
		final Thread thread = new Thread(task);
		
        thread.start();
	}
	
	private void resetPageHBox() {
		
		backButton.setDisable(true);
		forwardButton.setDisable(false);
		
		pageTextField.setText("1");
		
		totalPageNumberLabel.setText("/" + extractedTexts.size());
	}
}
