package model;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class CombinedReader {
	
	private TableReader tableReader;
	private TextReader textReader;
	
	public CombinedReader(String filesPath) {
		
		textReader = new TextReader(filesPath);	
		tableReader = textReader.getTableReader();
		
	}
	
	public String getAllParagraphsWithTag(String tag, String translator, boolean showOtherTags, boolean isExactWords) {
		
		String allParagraphsWithTag = new String();
		
		ArrayList<Integer> virginiaParagraphNumbersWithTag = textReader.getParagraphNumbersWith(tag, "Virginia Woolf (1927)", isExactWords);
		
		ArrayList<Integer> translatorParagraphNumbersWithTag = new ArrayList<Integer>();
		
		for (int i = 0; i < virginiaParagraphNumbersWithTag.size(); ++i) {
			translatorParagraphNumbersWithTag.addAll(tableReader.getTranslatedParagraphNumbers(virginiaParagraphNumbersWithTag.get(i), translator));
		}
		
		for (int i = 0; i < translatorParagraphNumbersWithTag.size(); ++i) {
			allParagraphsWithTag += textReader.getWholeParagraph(translatorParagraphNumbersWithTag.get(i), translator, true);
		}
		
		if (!showOtherTags) {
			
			String tagWithoutBrackets = tag.replaceAll("<", "").replaceAll(">", "");
			
			allParagraphsWithTag = Pattern.compile("<(?=(?!" + tagWithoutBrackets + "))(?=(?!/" + tagWithoutBrackets + "))[^>]*>").matcher(allParagraphsWithTag).replaceAll("");
		}
		
		return allParagraphsWithTag;
	}
	
	public ArrayList<String> getAlignedParagraphsWithTag(String tag, ArrayList<String> translators, boolean showOtherTags, boolean isExactWords) {
		
		System.out.println("i");
		
//		String alignedParagraphsWithTag = new String();
		ArrayList<String> alignedParagraphsList= new ArrayList<String>();
		
		ArrayList<Integer> virginiaParagraphNumbersWithTag = textReader.getParagraphNumbersWith(tag, "Virginia Woolf (1927)", isExactWords);
		
		System.out.println("ii");
				
		for (int i = 0; i < virginiaParagraphNumbersWithTag.size(); ++i) {	
			
			System.out.println("counter beginning i = " + i);
			
			String alignedParagraphWithTags = new String();
			
			for (int j = 0; j < translators.size(); ++j) {
				
				System.out.println("counter beginning j = " + j);
				
				alignedParagraphWithTags += "\n" + translators.get(j) + "\n";
				
				ArrayList<Integer> translatorParagraphNumbersWithTag = new ArrayList<Integer>();
				
//				System.out.println(virginiaParagraphNumbersWithTag.get(i));
//				System.out.println(translators.get(j));
				
				translatorParagraphNumbersWithTag.addAll(tableReader.getTranslatedParagraphNumbers(virginiaParagraphNumbersWithTag.get(i), translators.get(j)));
				
				for (int k = 0; k < translatorParagraphNumbersWithTag.size(); ++k) {
					
					System.out.println("counter beginning k = " + k);
					
					alignedParagraphWithTags += textReader.getWholeParagraph(translatorParagraphNumbersWithTag.get(k), translators.get(j), true);
					
					System.out.println("counter end k = " + k);
				}
				
				System.out.println("counter end j = " + j);
			}
			
			alignedParagraphWithTags += "\n" + "===============================================================================================" + "\n";
			
			alignedParagraphsList.add(alignedParagraphWithTags);
			
			System.out.println("counter i = " + i);
			
		}
		
		if (!showOtherTags) {
			
			String tagWithoutBrackets = tag.replaceAll("<", "").replaceAll(">", "");
			
			for (int i = 0; i < alignedParagraphsList.size(); ++i) {
				alignedParagraphsList.set(i, Pattern.compile("<(?=(?!" + tagWithoutBrackets + "))(?=(?!/" + tagWithoutBrackets + "))[^>]*>").matcher(alignedParagraphsList.get(i)).replaceAll(""));
			}
		}
		
//		System.out.println("done");
		
		return alignedParagraphsList;
	}
	
	
	
	public String getAlignedParagraph(Integer paragraphNumber, ArrayList<String> translators, boolean showOtherTags) {
		
		String alignedParagraph = new String();
		
		for (int i = 0; i < translators.size(); ++i) {
			
			String translator = translators.get(i);
			
			alignedParagraph += "\n" + translator + "\n";
			
			ArrayList<Integer> translatedParagraphNumbers = tableReader.getTranslatedParagraphNumbers(paragraphNumber, translator);
			
			for (int j = 0; j < translatedParagraphNumbers.size(); ++j) {
				alignedParagraph += textReader.getWholeParagraph(translatedParagraphNumbers.get(j), translator, true);
			}
			
			
		}
		
		if (!showOtherTags) {
						
			alignedParagraph = Pattern.compile("<.*>").matcher(alignedParagraph).replaceAll("");
		}
		
		return alignedParagraph;
		
	}
	
	public TextReader getTextReader() {
		return textReader;
	}
}
