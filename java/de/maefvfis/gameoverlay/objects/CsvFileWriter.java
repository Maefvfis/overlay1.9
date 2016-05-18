package de.maefvfis.gameoverlay.objects;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ashraf
 * 
 */
public class CsvFileWriter {
	
	//Delimiter used in CSV file
	private static final String COMMA_DELIMITER = ";";
	private static final String NEW_LINE_SEPARATOR = "\n";
	
	//CSV file header
	private static final String FILE_HEADER = "ShopInhaber;Anzahl;Verkauf;Ankauf;Item;X;Y;Z";

	public static void writeCsvFile(String fileName, List<ShopSchild> shopschilder) {
		
		
		FileWriter fileWriter = null;
				
		try {
			fileWriter = new FileWriter(fileName);

			//Write the CSV file header
			fileWriter.append(FILE_HEADER.toString());
			
			//Add a new line separator after the header
			fileWriter.append(NEW_LINE_SEPARATOR);
			
			//Write a new student object list to the CSV file
			for (ShopSchild shopschild : shopschilder) {
				
				fileWriter.append(shopschild.getShopInhaber());
				fileWriter.append(COMMA_DELIMITER);
				
				fileWriter.append(String.valueOf(shopschild.getAnzahl()));
				fileWriter.append(COMMA_DELIMITER);
				
				fileWriter.append(String.valueOf(shopschild.getVerkauf()));
				fileWriter.append(COMMA_DELIMITER);
				
				fileWriter.append(String.valueOf(shopschild.getAnkauf()));
				fileWriter.append(COMMA_DELIMITER);
				
				fileWriter.append(String.valueOf(shopschild.getItem()));
				fileWriter.append(COMMA_DELIMITER);
				
				fileWriter.append(String.valueOf(shopschild.getX()));
				fileWriter.append(COMMA_DELIMITER);
				
				fileWriter.append(String.valueOf(shopschild.getY()));
				fileWriter.append(COMMA_DELIMITER);
				
				fileWriter.append(String.valueOf(shopschild.getZ()));
				fileWriter.append(COMMA_DELIMITER);
				
				fileWriter.append(String.valueOf(shopschild.getWarp()));
				fileWriter.append(COMMA_DELIMITER);
				
				fileWriter.append(String.valueOf(shopschild.getServer()));
				
				
				fileWriter.append(NEW_LINE_SEPARATOR);
			}

			
			
			System.out.println("CSV file was created successfully !!!");
			
		} catch (Exception e) {
			System.out.println("Error in CsvFileWriter !!!");
			e.printStackTrace();
		} finally {
			
			try {
				fileWriter.flush();
				fileWriter.close();
			} catch (IOException e) {
				System.out.println("Error while flushing/closing fileWriter !!!");
                e.printStackTrace();
			}
			
		}
	}
}