import java.io.*;

/**
 * Created by PrashantKumarRaj on 4/30/2016.
 */
public class Comparison {

    public static void main(String [] args){
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        for(int i=0;i<5;i++){
            try{
                System.out.println("\nEnter Pattern\n");
                String Pattern = br.readLine();
                long patternLength=Pattern.length();
                long startTime = System.currentTimeMillis();
                String pos;
                //pos=KnuthMorrisPratt.KMPALGO(Pattern);

                pos = RabinKarp.patternSearch(Pattern);
                long stopTime = System.currentTimeMillis();
                long elapsedTime = stopTime - startTime;


                File file =new File("Comparison.txt");

                //if file does not exists, then create it
                if(!file.exists()){
                    file.createNewFile();
                }

                //true = append file
                FileWriter fileWritter = new FileWriter(file.getName(),true);
                BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
                String Result=String.format("%-30s%-10d %-40s %10d %10d \n",Pattern,patternLength,pos,RabinKarp.patternFreq,elapsedTime);
                bufferWritter.write(Result);
                bufferWritter.close();
            }catch(IOException e){
                e.printStackTrace();
            }
        }

    }
}
