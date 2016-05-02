import java.io.FileInputStream;
import java.io.FileReader;
import java.util.Scanner;
/**
 * Created by PrashantKumarRaj on 3/16/2016.
 */
/**
 ** Java Program to implement Knuth Morris Pratt Algorithm
 **/

/** Class KnuthMorrisPratt **/
public class KnuthMorrisPratt{
    static int F[];
    static char T[];
    static char P[];
    static int patternFreq;
    public static void prefixTable(String P)
    {
        F=new int[P.length()];
        F[0]=0;
        int i=1,j=0;
        while(i<P.length()){
            if(P.charAt(i)==P.charAt(j)){
                F[i]=j+1;
                i++;
                j++;
            }
            else if(j>0){
                j=F[j-1];
            }
            else{
                F[i]=0;
                i++;
            }

        }
    }

    public static String KMPALGO(String P){
        int i,j;
        prefixTable(P);
        String index="";
        int m = P.length();
        try {
            //FileInputStream fin=new FileInputStream("../brown.txt");
            Scanner scanner = new Scanner(new FileReader("../brown.txt"));
            patternFreq=0;
            char c;
            while(scanner.hasNextLine()){
            	i = 0; j = 0;
            	String line = scanner.nextLine();
            	for(int i1 = 0; i1 < line.length(); i1++){
            		if(line.charAt(i1) == P.charAt(j)){
            			if(j==m-1){
                            patternFreq++;
                            index += i-j+",";
                        }

                        else{
                            i++;
                            j++;
                        }
            		}else if(j > 0){
            			j = F[j-1];
            		}else{
            			i++;
            		}
            	}
            }
        }

        catch (Exception e) {
            e.printStackTrace();
        }
        return index;
    }
}


