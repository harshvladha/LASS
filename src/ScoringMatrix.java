import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Scanner;
import java.util.Arrays;

 /*
  * Created on 30-01-2016.
  */

/*
 *class to calculate the Scoring Matrix
 */
public class ScoringMatrix
{
    public static int allCharacterCount[] = new int[128];
    public static float allCharacterPairCount[][] = new float[128][128];
    public static float scoringMatrix[][] = new float[128][128]; //declaration of the Scoring matrix
    public static int bigramCountArray[];
    /*
     * main function
     */
    public static void main(String[] args) throws UnsupportedEncodingException, FileNotFoundException
    {
        HashInput(args[0]);          //hash of characters in corpus
        CountBigramFrequencies(5);   //count of bigrams
        BigramProbability(5);        //smoothing of scoring matrix
        CountFinalScores();          //final probability score
    }
    /*
     *function to calculate final probability count for the Scoring Matrix.
     */
    private static void CountFinalScores() throws FileNotFoundException, UnsupportedEncodingException
    {
        for(int i=0;i<128;i++)
        {
            for(int j=0;j<128;j++)
            {
                scoringMatrix[i][j] = (float)0.00; //initializing the Scoring Matrix
            }
        }
        for(int i=0;i<128;i++)
        {
            for(int j=0;j<128;j++)
            {
                try
                {
                    if(allCharacterCount[i]!=0)
                        scoringMatrix[i][j] = allCharacterPairCount[i][j]/(float)allCharacterCount[i]; //final score in scoring matrix
                }
                catch(ArithmeticException e)
                {
                    System.out.println(e.getMessage());
                }
            }
        }
        PrintWriter writer = new PrintWriter("ScoringMatric.txt");  //declaration of file to store the Scoring Matrix
        for(int i = 0; i < scoringMatrix.length; i++)
        {
            for (int j = 0; j < scoringMatrix[0].length; j++)
            {
                writer.print(scoringMatrix[i][j] + " ");  //storing the scores in the Scoring Matrix
            }
            writer.println();
        }
        writer.close();
    }
    /*
     *function is for smoothing of Scoring matrix
     */
    private static void BigramProbability(int countThreshold)
    {
        int count;
        float newCount;
        float smoothNumerator1, smoothNumerator2, smoothDenominator;
        for(int i=0;i<128;i++)
        {
            for(int j=0;j<128;j++)
            {
                if(allCharacterPairCount[i][j]<countThreshold)
                {
                    count =(int)allCharacterPairCount[i][j];
                    smoothNumerator1 = ((count+1)*bigramCountArray[count+1])/bigramCountArray[count];  //smoothing neumerator
                    smoothNumerator2 = (count*(countThreshold+1)*bigramCountArray[countThreshold])/bigramCountArray[0];  //smoothing neumerator
                    smoothDenominator = 1 - ((countThreshold+1)*bigramCountArray[countThreshold]/bigramCountArray[0]);  //smoothing denominator
                    newCount = (smoothNumerator1 - smoothNumerator2)/smoothDenominator;  //result of smoothing
                    allCharacterPairCount[i][j] = newCount;  //storing the smoothing result
                }
            }
        }
    }
    /*
     *function for frequencies of bigrams and checking sparsity
     */
    private static void CountBigramFrequencies(int countThreshold)
    {
        bigramCountArray = new int[countThreshold+1];
        for(int i=0;i<=countThreshold;i++)
        {
            bigramCountArray[i]=0;
        }
        for(int i=0;i<128;i++)
        {
            for(int j=0;j<128;j++)
            {
                if(allCharacterPairCount[i][j]<countThreshold)
                    bigramCountArray[(int)allCharacterPairCount[i][j]]++;  //removing sparsity
            }
        }
    }
    /*
     *function for hashing of characters in corpus
     */
    private static void HashInput(String filename) throws UnsupportedEncodingException
    {
        for(int i=0;i<128;i++)
        {
            allCharacterCount[i] = 0; //initializing the count of character array
        }
        for(int i=0;i<128;i++)
        {
            for(int j=0;j<128;j++)
            {
                allCharacterPairCount[i][j]=0; //initializing the count of pair
            }
        }
        try
        {
            Scanner scanner = new Scanner(new FileReader(filename)); //load the text file
            while (scanner.hasNextLine())  //run the loop till the end of text file
            {
                String columns = scanner.nextLine(); //store each line in columns
                if(columns.length() >0)
                {
                    char last_char = columns.charAt(0);
                    allCharacterCount[(int) columns.charAt(0)]++;
                    for (int i = 1; i < columns.length(); i++)
                    {
                        allCharacterCount[(int) columns.charAt(i)]++; //hashing of each character
                        allCharacterPairCount[(int) last_char][columns.charAt(i)]++;  //hashing of each pair
                        last_char = columns.charAt(i);
                    }
                }
            }
            PrintWriter writer = new PrintWriter("Count-of-pairs.txt"); //declaration of file to store counts
            for(int i = 0; i < allCharacterPairCount.length; i++)
            {
                for (int j = 0; j < allCharacterPairCount[0].length; j++)
                {
                    writer.print(allCharacterPairCount[i][j] + " ");  //write counts in file
                }
                writer.println();
            }
            writer.close();
        }
        catch (FileNotFoundException e)
        {
            System.out.println(e);
        }
    }
}