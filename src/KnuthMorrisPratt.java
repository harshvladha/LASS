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
        int i=0,j=0;
        prefixTable(P);
        String index="";
        int m = P.length();
        try {
            FileInputStream fin=new FileInputStream("../brown.txt");
            Scanner scanner = new Scanner(new FileReader("../brown.txt"));
            int c=0;
            patternFreq=0;
            while((c=fin.read())!=-1){
                if((char)c== P.charAt(j)){
                    if(j==m-1){
                        patternFreq++;
                        index += i-j+",";
                    }

                    else{
                        i++;
                        j++;
                    }
                }
                else if(j>0){
                    j=F[j-1];
                }
                else{
                    i++;
                }
            }
        }

        catch (Exception e) {
            e.printStackTrace();
        }
        return index;
    }
}


