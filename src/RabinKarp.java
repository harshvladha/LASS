/**
 * Created by PrashantKumarRaj on 5/2/2016.
 */
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class RabinKarp {
    static int q = 101;
    static int d = 256;
    static int patternFreq;
    static void patternSearch(String Pattern) throws IOException {
        Scanner sc = new Scanner(new FileReader("../brown.txt"));

        int patternLength = Pattern.length();
        
        int i, j, p, t;
        int h = 1;
        for(i = 0; i < patternLength-1; i++){
        	h = (h*d)%q;
        }
        i = 0;p = 0; t = 0;
        while(sc.hasNextLine()){
        	String line = sc.nextLine();
        	if(patternLength > line.length()) continue;
        	p = 0; t = 0;
        	for(i = 0;i < patternLength; i++){
        		p = (d*p + (int)Pattern.charAt(i))%q;
        		t = (d*t + (int)line.charAt(i))%q;
        	}
        	for(i = 0; i <= line.length()-patternLength; i++){
        		if(p == t){
        			for(j=0;j<patternLength;j++){
        				if(line.charAt(i+j) != Pattern.charAt(j)){
        					break;
        				}
        			}
        			if(j==patternLength){
        				patternFreq++;
        			}
        		}
        		if(i < line.length()-patternLength){
        			t = (d*(t - (int)line.charAt(i) * h) +(int) line.charAt(i+patternLength))%q;
        			if(t < 0){
        				t = t+q;
        			}
        		}
        	}
        }
    }
}

