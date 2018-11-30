package tabs;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import application.Main;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import model.Pair;

public class SearchTab extends Tab {

	private Main main;
	
	private ListView<TextFlow> searchListView;
	private BorderPane searchBorderPane;
	
	private TextField searchField;
	
	private String searchTerm;
	
	private CheckBox exactWordsCheckBox;
	
	private boolean noMatches;
	
	public SearchTab(Main main) {
		
		this.main = main;
		
		setText("Search");
		
		searchTerm = new String();
		
		searchListView = new ListView<TextFlow>();
//		searchListView.setStyle("-fx-font: 18 arial;");
		
		searchListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TextFlow>() {

			@Override
			public void changed(ObservableValue<? extends TextFlow> observable, TextFlow oldValue, TextFlow newValue) {
				
				if (!noMatches) {
				
					if (searchListView.getSelectionModel().getSelectedItem() != null) {
						
						main.getScene().setCursor(Cursor.DEFAULT);
						
						BorderPane alignmentBorderPane = new BorderPane();
						
						Integer paragraphNumber = main.getCombinedReader().getTextReader().getParagraphNumber(textFlowToString(newValue), "Virginia Woolf");
						
						Button backButton = new Button("Back");
						backButton.setOnAction(new EventHandler<ActionEvent>() {
							@Override
							public void handle(ActionEvent e) {
								searchListView.getSelectionModel().clearSelection();
								searchBorderPane.setCenter(searchListView);
								
								searchListView.setOnMouseEntered(new EventHandler<Event>() {
									@Override
									public void handle(Event event) {
										main.getScene().setCursor(Cursor.HAND);
									}
								});
								
								searchListView.setOnMouseExited(new EventHandler<Event>() {
								    public void handle(Event event) {
								    	main.getScene().setCursor(Cursor.DEFAULT);
								    }
								});								
							}
						});		
						
	//					TextArea alignmentTextArea = new TextArea();
	//					alignmentTextArea.setEditable(false);
						
						
						ArrayList<String> selectedTranslators = new ArrayList<>();
						selectedTranslators.add("Virginia Woolf (1927)");
						selectedTranslators.add("Celenza (1934)");
						selectedTranslators.add("Fusini (1992)");
						selectedTranslators.add("Cucciarelli (1993)");
						selectedTranslators.add("Malagò (1993)");
						selectedTranslators.add("Zazo (1994)");
						selectedTranslators.add("Bianciardi (1994)");
						selectedTranslators.add("Fusini (1998)");
						selectedTranslators.add("De Marinis (2012)");
						selectedTranslators.add("Fusini (2012)");
						selectedTranslators.add("Nadotti (2014)");
						selectedTranslators.add("Artioli (2017)");
						
						TextFlow alignmentTextFlow = stringToTextFlow(main.getCombinedReader().getAlignedParagraph(paragraphNumber, selectedTranslators, true));
						
	//					alignmentTextArea.setText(main.getCombinedReader().getAlignedParagraph(paragraphNumber, selectedTranslators, true));
	//					alignmentTextArea.setStyle("-fx-font: 18 arial;");
						
						
						HBox hbox = new HBox();				
						hbox.setPadding(new Insets(0, 20, 20, 20));
						hbox.setSpacing(10);
						hbox.setAlignment(Pos.CENTER_LEFT);
						
						Region region = new Region();
						HBox.setHgrow(region, Priority.ALWAYS);
						
						Button saveButton = new Button("Save As...");
						saveButton.setMinWidth(100);
						
						saveButton.setOnAction(new EventHandler<ActionEvent>() {
							@Override
							public void handle(ActionEvent e) {
								FileChooser fileChooser = new FileChooser();						
								
								fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("TXT file (*.txt)", "*.txt"));
								
								File file = fileChooser.showSaveDialog(main.getPrimaryStage());
								
								if (file != null) {
									
									System.out.println("================> " + textFlowToString(alignmentTextFlow));
									
									main.saveFile(textFlowToString(alignmentTextFlow), file);
								}
							}
						});		
						
						ScrollPane scrollPane = new ScrollPane();
						scrollPane.setContent(alignmentTextFlow);
						scrollPane.setPadding(new Insets(0, 15, 0, 15));
						
						hbox.getChildren().addAll(backButton, new Text("Paragraph " + paragraphNumber + ": \"" + searchTerm + "\""), region, saveButton);
						
						alignmentBorderPane.setTop(hbox);
						alignmentBorderPane.setCenter(scrollPane);
						
						searchBorderPane.setCenter(alignmentBorderPane);
					}
				}
			}
			
		});
		
		searchBorderPane = new BorderPane();
		searchBorderPane.setTop(createSearchHBox());
		
		searchBorderPane.setCenter(searchListView);
		
		setContent(searchBorderPane);
	}
	
	
	private HBox createSearchHBox() {
		
		HBox hbox = new HBox();
		hbox.setPadding(new Insets(20));
		hbox.setSpacing(10);
		hbox.setAlignment(Pos.CENTER_LEFT);
		
		searchField = new TextField();
		searchField.setPromptText("Search for a term or sentence");
		searchField.setMinWidth(300);
		
		searchField.setOnKeyPressed(new EventHandler<KeyEvent>() {
	        @Override
	        public void handle(KeyEvent keyEvent) {
	            if (keyEvent.getCode().equals(KeyCode.ENTER) && searchField.getText().length() > 0) {
	            	
	                search(searchField.getText());
	            }
	        }
	    });
		
		Button searchButton = new Button();
		searchButton.setText("Search");
		searchButton.setMinWidth(100);	
		searchButton.disableProperty().bind(searchField.textProperty().isEmpty());
		
		searchButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				
				search(searchField.getText());
			}
		});
		
		exactWordsCheckBox = new CheckBox("Exact Words");
		
		Region region = new Region();
		
		hbox.getChildren().addAll(searchField, searchButton, region, exactWordsCheckBox);
		
		return hbox;		
	}
	
	private void search(String text) {
		
		searchTerm = searchField.getText();
		
		ProgressBar progressBar = new ProgressBar();
		
		searchBorderPane.setCenter(progressBar);		
		
		final Task<Void> task = new Task<Void>() {

			@Override
			protected Void call() throws Exception {
				
				searchListView.getItems().removeAll(searchListView.getItems());
				
				ArrayList<Integer> paragraphNumbersWithText = main.getCombinedReader().getTextReader().getParagraphNumbersWith(text, "Virginia Woolf (1927)", exactWordsCheckBox.isSelected());
				
				if (paragraphNumbersWithText.size() > 0) {
					
					noMatches = false;
					
					for (int i = 0; i < paragraphNumbersWithText.size(); ++i) {
						
						searchListView.getItems().add(stringToTextFlow(main.getCombinedReader().getTextReader().getWholeParagraph(paragraphNumbersWithText.get(i), "Virginia Woolf (1927)", false) + "\n"));
					}
					
				} else {
					
					noMatches = true;
					
					Text noMatchesText = new Text("No matches");
					noMatchesText.setFont(Font.font(Font.getDefault().getName(), Font.getDefault().getSize() * 1.5));
					
					searchListView.getItems().add(new TextFlow(noMatchesText));
				}
				
				Platform.runLater(new Runnable(){
					@Override
					public void run() {	
						
						searchBorderPane.setCenter(searchListView);
						
						if (!noMatches) {
							
							searchListView.setMouseTransparent(false);
							searchListView.setFocusTraversable(true);
							
							searchListView.setOnMouseEntered(new EventHandler<Event>() {
								@Override
								public void handle(Event event) {
									main.getScene().setCursor(Cursor.HAND);
								}
							});
							
							searchListView.setOnMouseExited(new EventHandler<Event>() {
							    public void handle(Event event) {
							    	main.getScene().setCursor(Cursor.DEFAULT);
							    }
							});
						} else {
							searchListView.setMouseTransparent(true);
							searchListView.setFocusTraversable(false);
						}						
					}
				});
				
				return null;
			}
		};
		
		progressBar.progressProperty().bind(task.progressProperty());
		
		final Thread thread = new Thread(task);
        thread.start();
	}
	
	private String textFlowToString(TextFlow textFlow) {
		StringBuilder stringBuilder = new StringBuilder();
		
		for (Node node : textFlow.getChildren()) {
		    if (node instanceof Text) {
		    	
		    	System.out.println("=============================================> " + ((Text)node).getText());
		    	
		    	stringBuilder.append(((Text) node).getText());
		    } else {
		    	System.out.println("=============================================> NODE NOT INSTANCEOF TEXT");
		    }
		}
		
		return stringBuilder.toString();
	}
	
	private TextFlow stringToTextFlow(String string) {
		
		ArrayList<Integer> searchTermPositions = new ArrayList<Integer>();
		
		String lowerCaseString = string.toLowerCase();
		String lowerCaseSearchTerm = searchTerm.toLowerCase();
		
		if (exactWordsCheckBox.isSelected()) {
			
		    Pattern pattern = Pattern.compile("(?i)(?<![a-zA-Z]|-)" + lowerCaseSearchTerm + "(?![a-zA-Z]|-)");
		    Matcher matcher = pattern.matcher(lowerCaseString);
		    
		    while (matcher.find()) {
		    	searchTermPositions.add(matcher.start());
		    	
		    	System.out.println("=============================================================> " + matcher.start());
		    }
				
		} else {
			int i = lowerCaseString.indexOf(lowerCaseSearchTerm);
			
			if (i > 0) {
				searchTermPositions.add(i);
			}
			
			while (i >= 0) {
			     System.out.println(i);
			     i = lowerCaseString.indexOf(lowerCaseSearchTerm, i+1);
			     
			     if (i > 0) {
			     	searchTermPositions.add(i);
			     }
			}
		}
		
		ArrayList<Pair<String, Boolean>> stringsList = new ArrayList<Pair<String, Boolean>>();
		
		int restIndex = 0;
		
		for (int i = 0; i < searchTermPositions.size(); ++i) {
			
			stringsList.add(new Pair<String, Boolean>(string.substring(restIndex, searchTermPositions.get(i)), false));
		
			stringsList.add(new Pair<String, Boolean>(string.substring(searchTermPositions.get(i), searchTermPositions.get(i) + searchTerm.length()), true));
			
			restIndex = searchTermPositions.get(i) + searchTerm.length();
			
		}
		
		if (restIndex < string.length()-1) {
			stringsList.add(new Pair<String, Boolean>(string.substring(restIndex, string.length()-1), false));
		}
		
		TextFlow textFlow = new TextFlow();
		
		for (int i = 0; i < stringsList.size(); ++i) {
			
			textFlow.getChildren().add(highlightText(new Text(stringsList.get(i).getKey()), stringsList.get(i).getValue()));
		}
		
		return textFlow;		
	}
	
	private Text highlightText(Text text, boolean isSearchTerm) {
		
		if (isSearchTerm) {
			text.setFont(Font.font(Font.getDefault().getName(), FontWeight.BOLD, Font.getDefault().getSize() * 1.5));
			text.setUnderline(true);
			text.setFill(Color.rgb(103,47,87));
		} else {
			text.setFont(Font.font(Font.getDefault().getName(), Font.getDefault().getSize() * 1.5));
		}
		
		return text;
	}
}
