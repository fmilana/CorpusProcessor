package model;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TableReader {

	private Map<String, Integer> translatorToColumn;

	private String filesPath;

	public TableReader(String filesPath) {

		populateTranslatorToColumnMap();

		this.filesPath = filesPath;
	}

	private void populateTranslatorToColumnMap() {
		translatorToColumn = new HashMap<>();

		translatorToColumn.put("Virginia Woolf (1927)", 0);
		translatorToColumn.put("Celenza (1934)", 1);
		translatorToColumn.put("Fusini (1992)", 2);
		translatorToColumn.put("Cucciarelli (1993)", 3);
		translatorToColumn.put("Malagò (1993)", 4);
		translatorToColumn.put("Zazo (1994)", 5);
		translatorToColumn.put("Bianciardi (1994)", 6);
		translatorToColumn.put("Fusini (1998)", 7);
		translatorToColumn.put("De Marinis (2012)", 8);
		translatorToColumn.put("Fusini (2012)", 9);
		translatorToColumn.put("Nadotti (2014)", 10);
		translatorToColumn.put("Artioli (2017)", 11);
	}

	public ArrayList<Integer> getTranslatedParagraphNumbers(Integer originalParagraphNumber, String translator) {

		ArrayList<Integer> paragraphNumbers = new ArrayList<Integer>();

		try {
			BufferedReader bufferedReader = new BufferedReader(new FileReader(filesPath + "/0 Concordance Table.txt"));

			try {
				String currentLine = bufferedReader.readLine();
				currentLine = bufferedReader.readLine();
				currentLine = bufferedReader.readLine();
				currentLine = bufferedReader.readLine();
				currentLine = bufferedReader.readLine();
				
				
				ArrayList<String> previousLines = new ArrayList<String>();
				

				boolean stillAdding = false;

				while (currentLine != null) {

					String[] values = currentLine.split("  ");

					if (!values[0].equals("----") && Integer.parseInt(values[0].replaceAll("[^0-9]", "")) == originalParagraphNumber) {
						
						if (!values[translatorToColumn.get(translator)].equals("----")) {						
							
							paragraphNumbers.add(Integer.parseInt(values[translatorToColumn.get(translator)].replaceAll("[^0-9]", "")));
							
						} else {
							
							for (int i = previousLines.size(); i > 0; --i) {
								
								String previousValue = previousLines.get(i-1).split("  ")[translatorToColumn.get(translator)];
								
								if (!previousValue.equals("----")) {
									
									paragraphNumbers.add(Integer.parseInt(previousValue.replaceAll("[^0-9]", "")));
									
									break;
								}
								
							}
						}
						
						stillAdding = true;

					} else if (stillAdding && values[0].equals("----") && !values[translatorToColumn.get(translator)].equals("----")) {
						
						paragraphNumbers.add(Integer.parseInt(values[translatorToColumn.get(translator)].replaceAll("[^0-9]", "")));

					} else if (stillAdding && !values[0].equals("----")) {

						break;
						
					} 

					previousLines.add(currentLine);
					
					currentLine = bufferedReader.readLine();
				}

			} catch (IOException e) {
				// TODO Auto-generated catch block

				e.printStackTrace();
			}

			bufferedReader.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block

			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block

			e.printStackTrace();
		}

		return paragraphNumbers;

	}

	public Integer getOriginalParagraphNumber(Integer translatedParagraphNumber, String translator) {

		System.out.println(translatedParagraphNumber + ", " + translator);
		
		String lastOriginalParagraphString = "-1";

		try {
			BufferedReader bufferedReader = new BufferedReader(new FileReader(filesPath + "/0 Concordance Table.txt"));

			System.out.println("1");
			
			try {
				
				System.out.println("2");

				String currentLine = bufferedReader.readLine();
				currentLine = bufferedReader.readLine();
				currentLine = bufferedReader.readLine();
				currentLine = bufferedReader.readLine();
				currentLine = bufferedReader.readLine();
				
				System.out.println("3");

				while (currentLine != null) {

					String[] values = currentLine.split("  ");

					String translatedParagraphValue = values[translatorToColumn.get(translator)];

					if (!values[0].equals("----")) {
						lastOriginalParagraphString = values[0];
					}

					if (!translatedParagraphValue.equals("----") && Integer
							.parseInt(translatedParagraphValue.replaceAll("[^0-9]", "")) == translatedParagraphNumber) {
						
						System.out.println("FOUND LINE");
						
						break;
					}

					System.out.println(translatedParagraphValue);
					
					currentLine = bufferedReader.readLine();
					
					System.out.println();
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
		
		System.out.println("CONVERTING LINE");

		return Integer.parseInt(lastOriginalParagraphString.replaceAll("[^0-9]", ""));

	}

}
