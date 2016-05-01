import java.io.*;
import java.util.Scanner;

/**
 * Created by PrashantKumarRaj on 4/30/2016.
 */
public class Comparison {

    public static void main(String [] args){
        int seedLength = 3;
        String format1 = "%-20s %-20s %-20s %-20s %-20s %-20s";
        String format2 = "%-20s %-20d %-20d %-20d %-20d %-20d";
        System.out.printf(format1, "Pattern", "Pattern Length", "Seed Length", "Total Found", "Total LSS Found", "Time Taken (ms)");
        Scanner scanner = new Scanner(System.in);
        String pattern = scanner.next();
        int pattern_length = pattern.length();
        System.out.println("LASS Algorithm");
        while(seedLength <= pattern_length){
        	System.out.println();
            try{
                long startTime = System.currentTimeMillis();
                String[] argsLASS = {"../brown.txt", pattern , "../ScoringMatric.txt", "0.0", Integer.toString(seedLength)};
                int[] output = new int[2];
                output = LASS.main(argsLASS);
                
                long stopTime = System.currentTimeMillis();
                long elapsedTime = stopTime - startTime;
                System.out.printf(format2, pattern, pattern_length, seedLength, output[0], output[1], elapsedTime);
                seedLength++;
            }catch(IOException e){	
                e.printStackTrace();
            }
        }        
        System.out.println("\nKMP Algorithm");
        long startTime = System.currentTimeMillis();
        KnuthMorrisPratt.KMPALGO(pattern);
        long stopTime = System.currentTimeMillis();
        long elapsedTime = stopTime - startTime;
        System.out.printf(format2, pattern, pattern_length, pattern_length, KnuthMorrisPratt.patternFreq, 0, elapsedTime);

    }
}
