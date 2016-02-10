import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Scanner;
import java.util.Arrays;

/**
 * Created by Kaancha on 30-01-2016.
 */
public class ScoringMatrix {

    public static int allCharacterCount[] = new int[128];
    public static int allCharacterPairCount[][] = new int[128][128];
    public static int scoringMatrix[][] = new int[128][128];
    public static int bigramCountArray[];
    public static void main(String[] args) throws UnsupportedEncodingException, FileNotFoundException {
        HashInput(args[0]);
        CountBigramFrequencies(10);
        BigramProbability(10);
        CountFinalScores();

    }

    /*
    * This function is for final probability count for the Scoring Matrix.
    * */
    private static void CountFinalScores() throws FileNotFoundException, UnsupportedEncodingException {
        for(int i=0;i<128;i++){
            for(int j=0;j<128;j++){
                scoringMatrix[i][j] = 0;
            }
        }
        for(int i=0;i<128;i++){
            for(int j=0;j<128;j++){
                try{
                    scoringMatrix[i][j] = allCharacterPairCount[i][j]/allCharacterCount[i];
                }
                catch(ArithmeticException e){
                    System.out.println(e);
                }
            }
        }
        PrintWriter writer = new PrintWriter("ScoringMatric.txt", "UTF-8");
        writer.println(Arrays.deepToString(scoringMatrix));

    }

    /*
        This function is for Nc.
    * */
    private static void BigramProbability(int countThreshold){
		/*   This for C*
		* */
        int count, newCount;
        int smoothNumerator1, smoothNumerator2, smoothDenominator;


        for(int i=0;i<128;i++){
            for(int j=0;j<128;j++){
                if(allCharacterPairCount[i][j]<countThreshold){
                    count = allCharacterPairCount[i][j]; //Nc

                    smoothNumerator1 = ((count+1)*bigramCountArray[count+1])/bigramCountArray[count];
                    smoothNumerator2 = (count*(countThreshold+1)*bigramCountArray[countThreshold])/bigramCountArray[0];
                    smoothDenominator = 1 - ((countThreshold+1)*bigramCountArray[countThreshold]/bigramCountArray[0]);
                    newCount = (smoothNumerator1 - smoothNumerator2)/smoothDenominator;
                    allCharacterPairCount[i][j] = newCount;
                }
            }
        }

    }

    /*
    * This function is for smoothing.
    * */
    private static void CountBigramFrequencies(int countThreshold){
        bigramCountArray = new int[countThreshold+1];
        for(int i=0;i<=countThreshold;i++){
            bigramCountArray[i]=0;
        }
        for(int i=0;i<128;i++){
            for(int j=0;j<128;j++){
                if(allCharacterPairCount[i][j]<countThreshold)
                    bigramCountArray[allCharacterPairCount[i][j]]++;
            }
        }
    }

    private static void HashInput(String filename) throws UnsupportedEncodingException {
        for(int i=0;i<128;i++){
            allCharacterCount[i] = 0;
        }
        for(int i=0;i<128;i++){
            for(int j=0;j<128;j++){
                allCharacterPairCount[i][j]=0;
            }
        }
        try {
            Scanner scanner = new Scanner(new FileReader(filename));
            while (scanner.hasNextLine()) {
                String columns = scanner.nextLine();
                if(columns.length() >0) {
                    char last_char = columns.charAt(0);
                    allCharacterCount[(int) columns.charAt(0)]++;
                    for (int i = 1; i < columns.length(); i++) {
                        allCharacterCount[(int) columns.charAt(i)]++;
                        allCharacterPairCount[(int) last_char][columns.charAt(i)]++;
                        last_char = columns.charAt(i);
                    }
                }
            }
            //System.out.println(Arrays.toString(allCharacterCount));
            //System.out.println(Arrays.deepToString(allCharacterPairCount));
            PrintWriter writer = new PrintWriter("the-file-name.txt", "UTF-8");
            writer.println(Arrays.deepToString(allCharacterPairCount));
            //writer.println("The second line");
            writer.close();
        } catch (FileNotFoundException e) {
            System.out.println(e);
        }

    }
}
