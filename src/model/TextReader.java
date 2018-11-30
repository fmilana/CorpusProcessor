package model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextReader {
	
	private String fileAddress; 
	private String filesPath;
	
	private TableReader tableReader;
	
	private HashMap<String, String> allTexts;

	public TextReader(String filesPath) {
		tableReader = new TableReader(filesPath);
		
		this.filesPath = filesPath;
		
		allTexts = new HashMap<String, String>();
	}
	
	private void setFileAddress(String translator) {
		switch (translator) {
		case "Virginia Woolf (1927)": fileAddress = filesPath + "/XMLFile ST VW 1927.txt";
			break;
		case "Celenza (1934)": fileAddress = filesPath + "/XMLFile TT1 CELENZA 1934.txt";
			break;
		case "Fusini (1992)": fileAddress = filesPath + "/XMLFile TT2 FUSINI 1992.txt";
			break;
		case "Cucciarelli (1993)": fileAddress = filesPath + "/XMLFile TT3 CUCCIARELLI 1993.txt";
			break;
		case "Malagò (1993)": fileAddress = filesPath + "/XMLFile TT4 MALAGO 1993.txt";
			break;
		case "Zazo (1994)": fileAddress = filesPath + "/XMLFile TT5 ZAZO 1994.txt";
			break;
		case "Bianciardi (1994)": fileAddress = filesPath + "/XMLFile TT6 BIANCIARDI 1994.txt";
			break;
		case "Fusini (1998)": fileAddress = filesPath + "/XMLFile TT7 FUSINI 1998.txt";
			break;	
		case "De Marinis (2012)": fileAddress = filesPath + "/XMLFile TT8 DE MARINIS 2012.txt";
			break;
		case "Fusini (2012)": fileAddress = filesPath + "/XMLFile TT9 FUSINI 2012.txt";
			break;
		case "Nadotti (2014)": fileAddress = filesPath + "/XMLFile TT10 NADOTTI 2014.txt";
			break;
		case "Artioli (2017)": fileAddress = filesPath + "/XMLFile TT11 ARTIOLI 2017.txt";
			break;
		default: fileAddress = filesPath + "/XMLFile ST VW 1927.txt";
			break;
		}
	}
	
	public ArrayList<Integer> getParagraphNumbersWith(String text, String translator, boolean isExactWords) {
		
		ArrayList<Integer> paragraphsWithTag = new ArrayList<Integer>();
		
		setFileAddress(translator);
		
		try {
			BufferedReader bufferedReader = new BufferedReader(new FileReader(fileAddress));
			
			try {
				String currentLine = bufferedReader.readLine();
				
				boolean lookingForParagraphEnd = false;
				
				Pattern pattern = Pattern.compile(".*</P[0-9]{3}>.*");
				
				while (currentLine != null) {
					
					if (isExactWords) {
						if (!lookingForParagraphEnd && Pattern.compile("(?i)(?<![a-zA-Z]|-)" + text + "(?![a-zA-Z]|-)").matcher(currentLine).find()) {
							
							
							lookingForParagraphEnd = true;
						}
					} else {
						if (!lookingForParagraphEnd && Pattern.compile(Pattern.quote(text), Pattern.CASE_INSENSITIVE).matcher(currentLine).find()) {
							
							
							lookingForParagraphEnd = true;
						}
					}
					
					if (lookingForParagraphEnd) {
						
						Matcher matcher = pattern.matcher(currentLine);
						
						if (matcher.matches()) {
							
							String matchedLineNumbers = matcher.group().replaceAll("[^0-9]", "");
							
							//avoids 10:30 error
							if (matchedLineNumbers.length() > 3) {
								matchedLineNumbers = matchedLineNumbers.substring(matchedLineNumbers.length() - 3);
							}
							
							paragraphsWithTag.add(Integer.parseInt(matchedLineNumbers));
						
							lookingForParagraphEnd = false;
						} 
					}
					
					currentLine = bufferedReader.readLine();
				}
				
				
				bufferedReader.close();
				
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return paragraphsWithTag;
	}
		
	public String getWholeParagraph(int paragraphNumber, String translator, boolean withLineNumbers) {
		
		System.out.println(translator + ", " + "paragraph " + paragraphNumber);
		
		String paragraph = "\n"; 
		
		setFileAddress(translator);
		
		System.out.println(".");
		
		try {
			BufferedReader bufferedReader = new BufferedReader(new FileReader(fileAddress));
			
			try {
				String currentLine = bufferedReader.readLine();
				
				Pattern openingPattern = Pattern.compile(".*<P[0-9]{3}>.*");
				Pattern closingPattern = Pattern.compile(".*</P[0-9]{3}>.*");		
				
				boolean lookingForParagraphEnd = false;
				
				System.out.println("..");
				
				
				while (currentLine != null) {
					
						
					Matcher openingMatcher = openingPattern.matcher(currentLine);
					
					if (openingMatcher.matches()) {
						
						System.out.println(currentLine);
					
						if (Integer.parseInt(getLastThreeCharacters(openingMatcher.group().replaceAll("[^0-9]", ""))) == paragraphNumber) {
							
							System.out.println("getting last three characters of " + paragraphNumber);
							
							lookingForParagraphEnd = true;
						}
							
					}
					
					if (lookingForParagraphEnd) {	
						
						if (withLineNumbers) {
							
							System.out.println("here for " + paragraphNumber);
							
							paragraph += tableReader.getOriginalParagraphNumber(paragraphNumber, translator) + "    " + paragraphNumber + "            " + currentLine + "\n";
							
							System.out.println("done");
							
						} else {
							paragraph += currentLine + "\n";
						}
						
						Matcher closingMatcher = closingPattern.matcher(currentLine);
						
						System.out.println("looking for closing tag in line: "+ currentLine);
						
						if (closingMatcher.matches()) {
							
							System.out.println("matched end of " + paragraphNumber);
							
							if (Integer.parseInt(getLastThreeCharacters(closingMatcher.group().replaceAll("[^0-9]", ""))) == paragraphNumber) {
								break;
							}
						}
						
						
					}
					
					currentLine = bufferedReader.readLine();
				}
				
				System.out.println("....");
				
				bufferedReader.close();
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return paragraph;
	}
	
	public Integer getParagraphNumber(String paragraph, String translator) {
		
		Integer paragraphNumber = 1;
		
		String[] lines = paragraph.split("\n");
		
		Pattern closingPattern = Pattern.compile(".*</P[0-9]{3}>.*");
		
		for (int i = 0; i < lines.length; ++i) {
			
			Matcher closingMatcher = closingPattern.matcher(lines[i]);
			
			if (closingMatcher.matches()) {
				
				paragraphNumber = Integer.parseInt(closingMatcher.group().replaceAll("[^0-9]", ""));
				
				break;
			}
			
		}
		
		
		return paragraphNumber;
	}
	
	public String getWholeFilteredText(String translator, boolean removeStopWords) throws FileNotFoundException {
		
		String wholeText = new String();
		
		String dataTextName = translator;
//		String filteredTextFilePath= "data/filteredtext/" + translator;
		
		if (removeStopWords) {
			dataTextName += " without stopwords";
//			filteredTextFilePath += " without stopwords.txt";
		} else {
//			filteredTextFilePath += ".txt";
		}
		
		if (allTexts.containsKey(dataTextName)) {
			
//			System.out.println("reading text from local variable");
		
			wholeText = allTexts.get(dataTextName.replaceAll("\n", " "));
			
		} else {
			
//			System.out.println("local variable does not contain text");
		
//			File filteredTextFile = new File(filteredTextFilePath);
//			
//			if (filteredTextFile.exists()) {
//				
//				System.out.println("filtered text exists locally");
//				
//				try {
//					
//					System.out.println("reading filtered txt from storage");
//					
//					wholeText = new String(Files.readAllBytes(Paths.get(filteredTextFilePath)), StandardCharsets.UTF_8);
//				} catch (UnsupportedEncodingException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				
//			} else {	
				
//				System.out.println("filtered text does not exist locally");
				
				setFileAddress(translator);
				
				try {
					
//					System.out.println("reading from unfiltered text");
					
					BufferedReader bufferedReader = new BufferedReader(new FileReader(fileAddress));
					
					try {
						String currentLine = bufferedReader.readLine();
						
						while (currentLine != null) {
							
							wholeText += currentLine + "\n";
							
							currentLine = bufferedReader.readLine();
							
						}
						
						
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					try {
						bufferedReader.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}			
					
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				wholeText = wholeText.replaceAll("'", " ")
						.replaceAll("’", " ")
						.replaceAll("<[^>]+>", "")
						.replaceAll("--", " ")
						.replaceAll("[^A-zÀ-ú ]", "")
						.replaceAll("\n", " ")
						.replaceAll("[ ]{2,}", " ");
				
				
				if (removeStopWords) {
					
					ArrayList<String> stopWords = new ArrayList<String>();
					String stopWordsFilePath = new String();
					
					if (translator.equals("Virginia Woolf (1927)")) {
						stopWordsFilePath = filesPath + "/English Stopwords.txt";
					} else {
						stopWordsFilePath = filesPath + "/Italian Stopwords.txt";
					}
					
					@SuppressWarnings("resource")
					Scanner scanner = new Scanner(new File(stopWordsFilePath)).useDelimiter(", ");
					
					while (scanner.hasNext()){
						stopWords.add(scanner.next());
					}
					scanner.close();
					
					
					for (String stopWord : stopWords) {
						wholeText = wholeText.replaceAll(" \\b(?i)" + stopWord + "\\b", "");
					}				
				} 
				
//				System.out.println("writing filtered text to storage");
//				
//				PrintWriter printWriter = null;
//				
//				try {
//					printWriter = new PrintWriter(filteredTextFilePath, StandardCharsets.UTF_8);
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				
//				System.out.println("created printwriter");
//				
//				printWriter.print(wholeText);
//				
//				System.out.println("written filtered text to storage");
//				
//				printWriter.close();
//			}
		
		}
		
		String wholeTextLowerCase = wholeText.toLowerCase();
		
		allTexts.put(dataTextName, wholeTextLowerCase);
		
		return wholeTextLowerCase;
		
	}
	
	private String getLastThreeCharacters(String string) {
		
		if (string.length() > 3) {
			return string.substring(string.length() - 3);
		} else {
			return string;
		}
	}	
	
	public TableReader getTableReader() {
		return tableReader;
	}
	
}
