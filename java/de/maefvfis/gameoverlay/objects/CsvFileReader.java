package de.maefvfis.gameoverlay.objects;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.maefvfis.gameoverlay.client.gui.ChunckViewer.choords;

/**
 * @author ashraf_sarhan
 *
 */
public class CsvFileReader {
	
	//Delimiter used in CSV file
	private static final String COMMA_DELIMITER = ";";
	
	
	
	
	public static List<ShopVglPreis> readCsvShopImportFile(String fileName) {
		
		
		BufferedReader fileReader = null;
		List<ShopVglPreis> ShopSchilder = null;
        try {
        	
        	//Create a new list of student to be filled by CSV file data 
        	ShopSchilder = new ArrayList();
        	
            String line = "";
            
            //Create the file reader
            fileReader = new BufferedReader(new FileReader(fileName));
            
            //Read the CSV file header to skip it
            fileReader.readLine();
            
            //Read the file line by line starting from the second line
            while ((line = fileReader.readLine()) != null) {
                //Get all tokens available in line
                String[] tokens = line.split(COMMA_DELIMITER);
                if (tokens.length > 0) {
                	//Create a new student object and fill his  data
                	ShopVglPreis shopschild = new ShopVglPreis(tokens[ShopVergleich.INDItem],Float.parseFloat(tokens[ShopVergleich.INDVerkauf]),Float.parseFloat(tokens[ShopVergleich.INDAnkauf]));
					ShopSchilder.add(shopschild);
				}
            }
            
            //Print the new student list
            for (ShopVglPreis shopschild : ShopSchilder) {
				System.out.println(shopschild.toString());
			}
        } 
        catch (Exception e) {
        	System.out.println("Error in CsvFileReader !!!");
            e.printStackTrace();
        } finally {
            try {
                fileReader.close();
            } catch (IOException e) {
            	System.out.println("Error while closing fileReader !!!");
                e.printStackTrace();
            }
        }
        
        return ShopSchilder;
		
	
	
	}
	
	
	
	

	public static List<ShopSchild> readCsvFile(String fileName) {

		BufferedReader fileReader = null;
		List<ShopSchild> ShopSchilder = null;
        try {
        	
        	//Create a new list of student to be filled by CSV file data 
        	ShopSchilder = new ArrayList();
        	
            String line = "";
            
            //Create the file reader
            fileReader = new BufferedReader(new FileReader(fileName));
            
            //Read the CSV file header to skip it
            fileReader.readLine();
            
            //Read the file line by line starting from the second line
            while ((line = fileReader.readLine()) != null) {
                //Get all tokens available in line
                String[] tokens = line.split(COMMA_DELIMITER);
                if (tokens.length > 0) {
                	//Create a new student object and fill his  data
					ShopSchild shopschild = new ShopSchild(tokens[ShopSchild.INDShopInhaber], Integer.parseInt(tokens[ShopSchild.INDAnzahl]), Float.parseFloat(tokens[ShopSchild.INDVerkauf]), Float.parseFloat(tokens[ShopSchild.INDAnkauf]), tokens[ShopSchild.INDItem], Integer.parseInt(tokens[ShopSchild.INDX]), Integer.parseInt(tokens[ShopSchild.INDY]), Integer.parseInt(tokens[ShopSchild.INDZ]),tokens[ShopSchild.INDWarp],tokens[ShopSchild.INDServer]);
					ShopSchilder.add(shopschild);
				}
            }
            
            //Print the new student list
            for (ShopSchild shopschild : ShopSchilder) {
				System.out.println(shopschild.toString());
			}
        } 
        catch (Exception e) {
        	System.out.println("Error in CsvFileReader !!!");
            e.printStackTrace();
        } finally {
            try {
                fileReader.close();
            } catch (IOException e) {
            	System.out.println("Error while closing fileReader !!!");
                e.printStackTrace();
            }
        }
        
        return ShopSchilder;

	}

}