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
    public static float modifiedAllCharacterPairCount[][] = new float[128][128];
	public static int scoringMatrix[][] = new int[128][128];
	public static float bigramCountArray[];  //Nc
    public static float modfiedBigramCountArray[];
	public static void main(String[] args) throws UnsupportedEncodingException {
		HashInput(args[0]);
		CountBigramFrequencies(10);

	}

/*
* This function is for Nc.
* */
	private static void BigramProbability(int countThreshold){
		for(int i=0;i<128;i++){
			for(int j=0;j<128;j++){
				if(allCharacterPairCount[i][j]<countThreshold){

				}
			}
		}

	}

	/*
	* This function is for smoothing.
	* */
	private static void CountBigramFrequencies(int countThreshold){
		bigramCountArray = new float[countThreshold+2]; // +2 for 0 , 1 to k , k+1
		for(int i=0;i<countThreshold+2;i++){
			bigramCountArray[i]=0;
		}
		for(int i=0;i<128;i++){
			for(int j=0;j<128;j++){
				if(allCharacterPairCount[i][j]<countThreshold+2)
					bigramCountArray[allCharacterPairCount[i][j]]++;
			}
        }
        System.out.println(Arrays.toString(bigramCountArray));

        //Smoothing Bigram Count Array
        float N1 = bigramCountArray[1];
        for(int c=0;c<bigramCountArray.length-1;c++)
        {
            if(bigramCountArray[c]!=0)
            {
                float x = (c+1)*bigramCountArray[c+1]/bigramCountArray[c];
                float y =  (countThreshold+1)*bigramCountArray[countThreshold+1]/N1;
                bigramCountArray[c] = ((x-c*y)/(1-y));
            }
        }

        System.out.println(Arrays.toString(bigramCountArray));

        // modifying the allCharacterPairCount Matrix
        for(int i=0;i<128;i++){
            for(int j=0;j<128;j++){
                if(allCharacterPairCount[i][j]<=countThreshold) {
                  //  allCharacterPairCount[i][j] =  bigramCountArray[allCharacterPairCount[i][j]];
                }
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
			System.out.println(Arrays.toString(allCharacterCount));
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
