import edu.duke.*;
import org.apache.commons.csv.*;
import java.io.File;
/**
 * Write a description of part1 here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class part1 {
    public CSVRecord coldestHourInFile(CSVParser parser){
        CSVRecord coldesthoursofar = null;
        for(CSVRecord currentRow : parser){
            if(coldesthoursofar == null){
                coldesthoursofar = currentRow;
            }
            else{
                double currenttemp = Double.parseDouble(currentRow.get("TemperatureF"));
                double smallesttemp = Double.parseDouble(coldesthoursofar.get("TemperatureF"));
                if(currenttemp < smallesttemp && currenttemp != -9999){
                    coldesthoursofar = currentRow;
                }
            }
        }
        return coldesthoursofar; 
    }
    
    public String fileWithColdestTemperature(){
        //select multiple files to parse through
        CSVRecord smallestsofar = null;
        DirectoryResource dr = new DirectoryResource();
        File fsmallest = null;
        String canonical = null;
        //loop through each file
        for(File f : dr.selectedFiles()){
            FileResource fr = new FileResource(f);
            CSVRecord currentRow = coldestHourInFile(fr.getCSVParser());
            if(smallestsofar == null){
                smallestsofar = currentRow;
            }
            if(fsmallest == null){
                fsmallest = f;
            }
            else{
                 //check each record
                double currenttemp = Double.parseDouble(currentRow.get("TemperatureF"));
                double smallesttemp = Double.parseDouble(smallestsofar.get("TemperatureF"));
                if(currenttemp < smallesttemp && currenttemp != -9999){
                    //if the record is smaller than the previous record, updated smallest record
                    smallestsofar = currentRow;
                    if(fsmallest != f){
                        fsmallest = f;
                        canonical = fsmallest.getPath();
                    }
                }
            }
        }
        //return the coldest hour out of all files selected
        
        return canonical;
    }
    
    public CSVRecord lowestHumidityInFile(CSVParser parser){
        CSVRecord lowestsofar = null;
        for(CSVRecord currentRow : parser){
            if(lowestsofar == null){
                lowestsofar = currentRow;
            }
            else{
                if(currentRow.get("Humidity").contains("N/A")){
                    lowestsofar = lowestsofar;
                }
                else{
                    double currenthumid = Double.parseDouble(currentRow.get("Humidity"));
                    double lowest = Double.parseDouble(lowestsofar.get("Humidity"));
                
                    if(currenthumid < lowest){
                        lowestsofar = currentRow;
                    }
                }
            }
        }
        
        return lowestsofar;
    }
    
    public CSVRecord lowestHumidityInManyFiles(){
        DirectoryResource dr = new DirectoryResource();
        CSVRecord lowestsofar = null;
        for(File f : dr.selectedFiles()){
            FileResource fr = new FileResource(f);
            CSVRecord currentRow = lowestHumidityInFile(fr.getCSVParser());
            
            if(lowestsofar == null){
                lowestsofar = currentRow;
            }
            if(currentRow.get("Humidity").contains("N/A")){
                    System.out.println("There's an instance of N/A");
            }
            else{
                 //check each record
                
                double currenthumid = Double.parseDouble(currentRow.get("Humidity"));
                double smallesthumid = Double.parseDouble(lowestsofar.get("Humidity"));
                if(currenthumid < smallesthumid){
                    //if the record is smaller than the previous record, updated smallest record
                    lowestsofar = currentRow;
                }
            }
        }
        return lowestsofar;
    }
    
    public double averageTemperatureInFile(CSVParser parser){
        double sum = 0;
        int count = 0;
        for(CSVRecord currentRow : parser){
            double currenttemp = Double.parseDouble(currentRow.get("TemperatureF"));
            if(currenttemp == -9999){
                sum += 0;
                count += 1;
            }
            else{
                sum += currenttemp;
                count += 1;
            }
        }
        double avg = sum/count;
        return avg;
    }
    
    public double averageTemperatureWithHighHumidityInFile(CSVParser parser, int value){
        int count = 0;
        double sum = 0;
        for (CSVRecord currentRow : parser){
            double currenttemp = Double.parseDouble(currentRow.get("TemperatureF"));
            if(currentRow.get("Humidity") == "N/A"){
                sum += 0;
                count += 0;
            }
            else{
                double currenthumid = Double.parseDouble(currentRow.get("Humidity"));
                if(currenthumid >= value){
                    if(value == -9999){
                        count += 0;
                        sum += 0;
                    }
                    else{
                        count += 1;
                        sum += currenttemp;
                    }
                }
            }
        }
        if(count == 0){
            return 0;
        }
        double avg = sum/count;
        return avg;
    }
    
    public void testAverageTemperatureWithHighHumidityInFile(){
        FileResource fr = new FileResource();
        double test = averageTemperatureWithHighHumidityInFile(fr.getCSVParser(), 80);
        if(test == 0){
            System.out.println("No temperatures with that humidity");
        }
        else{
            System.out.println("Average Temp when high Humidity is " + test);
        }
    }
    
    public void testAverageTemperatureInFile(){
        FileResource fr = new FileResource();
        double test = averageTemperatureInFile(fr.getCSVParser());
        System.out.println("The average temperature in file is " + test);
    }
    
    public void testLowestHumidityInManyFiles(){
        CSVRecord test = lowestHumidityInManyFiles();
        System.out.println("The lowest humidity was " + test.get("Humidity") + " at " + test.get("DateUTC"));
        
    }
    
    public void testLowestHumidityInFile(){
        FileResource fr = new FileResource();
        CSVParser parser = fr.getCSVParser();
        CSVRecord csv = lowestHumidityInFile(parser);
        System.out.println("The lowest humidity was " + csv.get("Humidity") + " at " + csv.get("DateUTC"));
        
    }
    
    public void testFileWithColdestTemperature(){
        String test = fileWithColdestTemperature();
        System.out.println("The coldest day was in file " + test.substring(test.length() - 22));
        FileResource fr = new FileResource(test);
        CSVRecord coldest = coldestHourInFile(fr.getCSVParser());
        //System.out.println(test);
        System.out.println("The coldest temperature on that day was " + coldest.get("TemperatureF"));
        System.out.println("All the Temperatures on the coldest day were: ");
        for(CSVRecord record : fr.getCSVParser()){
            System.out.println(record.get("DateUTC") + ": " + record.get("TemperatureF"));
        }
    }
    
    public void testColdestHourInFile(){
        FileResource fr = new FileResource();
        CSVRecord coldesthour = coldestHourInFile(fr.getCSVParser());
        System.out.print("The coldest temperature on " + coldesthour.get("DateUTC").substring(0,10));
        System.out.println(" was " + coldesthour.get("TemperatureF"));
        
    }
}
