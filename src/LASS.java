import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Scanner;
import java.util.Arrays;

 /*
  * Created on 06-02-2016.
  */

		/*
		 * first argument is file/database to search in (args[0])
		 * second argument is pattern which we have to find (args[1])
		 * third argument is file of scoring matrix (args[2])
		 * fourth argument is threshold (args[3])
		 * fifth argument is seed lenght (args[4])
		 */

/*
 *class of main search algo
 */
public class LASS
{
	public static float scoringMatrix[][] = new float[128][128];
	public static String searchPattern;  // pattern which we need to find
	public static String LSS;  //least scoring seed
	public static int LSSPos;  //position of least scoring seed
	public static int seedLength;  //seed length

	/*
	 *main function
	 */
	public static int[] main(String[] args) throws UnsupportedEncodingException
	{
		searchPattern = args[1];
		seedLength = Integer.parseInt(args[4]);
		ImportScoringMatrix(args[2]);  //scoring matrix import
		float threshold = Float.parseFloat(args[3]);  //threshold value
		int[] RETURN = new int[2]; // index 0 = total found instances . index 1 = total LSS found 
		if(searchPattern.length() < seedLength) //if pattern-length is less than seed-length ERROR !!
		{
			System.out.println("ERROR !! : Pattern length is less than seed length.");
			return RETURN;
		}
		String Seeds[] = new String[args[1].length()-1];
		Seeds = createSeeds(searchPattern);
		float score = 0;
		float min_score = 100000;  // make it 0 for taking HSS
		int j = 0;
		for(int i=0;i<searchPattern.length()-seedLength+1;i++)
		{
			score = ScoreOfEachSeed(Seeds[i]);
			//System.out.println(Seeds[i] + " score : " + score); //Score of each seed
			if(score > threshold && score < min_score)  // make => score > min_score for taking HSS
			{
				min_score = score;
				LSS = Seeds[i];  //taking the seed with minimum score
				LSSPos = i;
			}
		}
		RETURN = exactmatchoflss(args[0], LSS, min_score);  // Exact match of the pattern on both sides of LSS
		
		return RETURN;
	}

	/*
	 *function create seeds of given length from the search pattern
	 */
	private static String[] createSeeds(String pattern)
	{
		String seeds[] = new String[pattern.length()-seedLength+1];
		char seed[] = new char[seedLength];
		for(int i=0;i<pattern.length()-seedLength+1;i++)
		{
			for(int j=i;j<i+seedLength;j++)  //creating the seeds
			{
				seed[j-i] = pattern.charAt(j);
			}
			seeds[i] = String.copyValueOf(seed);
		}
		return seeds;
	}
	/*
	 *function to import the scoring matrix
	 */
	private static void ImportScoringMatrix(String filename)
	{
		try
		{
			Scanner scanner = new Scanner(new FileReader(filename));
			for(int i=0;i<128;i++)
			{
				for(int j=0;j<128;j++)
				{
					scoringMatrix[i][j] = scanner.nextFloat();  //build scoring matrix from text file
				}
			}
		}
		catch (FileNotFoundException e)
		{
			System.out.println(e);
		}
	}
	/*
	 *function to calculate the score of each seed
	 */
	private static float ScoreOfEachSeed(String seed)
	{
        int k=seed.length();
        float score=0;
		for(int p=0;p<k-1;p++)
		{
            score=score+scoringMatrix[(int)seed.charAt(p)][(int)seed.charAt(p+1)];  //score of each seed
        }
        return score;
	}
	/*
	 *function for exact matching after comparing LSS score
	 */
	private static int[] exactmatchoflss(String filename, String lsseed, float seedscore)
	{
		int output[] = new int[2];
		try
		{
			Scanner scanner = new Scanner(new FileReader(filename));  // scan document for the match
			int lineNumber = 0, count = 0, countLSS = 0; 
			while (scanner.hasNextLine())
			{
				String line = scanner.nextLine();
				lineNumber++;
				int s=lsseed.length();
				int L = line.length();				
				float currentscore=0;
				int i = 0;
				int j = 1;
				if(line.length() < s)
				{
					continue;
				}
				while(i+j < L)   //seed length traversal should happen for all characters in this line.
				{
					while(j<s)
					{
						currentscore=currentscore+scoringMatrix[(int)line.charAt(i+j-1)][(int)line.charAt(i+j)];
						j++;
					}
					if((int)(currentscore*100000)==(int)(seedscore*100000))
					{
						/* 
						 * Score matched, now we can extend left and right to make an EXACT match
						 */
						countLSS++;
						boolean foundFlag = false;
						int k = i+j-s;
						int i1=k, i2=k;
						int lsspos1 = LSSPos;
						int lsspos2 = LSSPos;
						while((lsspos1 > -1 || lsspos2 < searchPattern.length()) && (i1 > -1 || i2 < line.length()))
						{
							if((lsspos1 > -1 && i1 > -1 && searchPattern.charAt(lsspos1) == line.charAt(i1)) )
							{
								lsspos1--;
								i1--;
							}
							else if((lsspos2 < searchPattern.length() && i2 < line.length() && searchPattern.charAt(lsspos2) == line.charAt(i2)))
							{
								lsspos2++;
								i2++;
							}
							else
							{
								break;
							}
							if(lsspos1 == -1 && lsspos2 == searchPattern.length())
							{
								foundFlag = true;
							}
						}
						if(foundFlag == true)
						{
							//System.out.println("Pattern found at index " + (i1+2) +  " of Line : " + lineNumber);
							count++;
						}
					}
					currentscore=currentscore-scoringMatrix[(int)line.charAt(i+j-s)][(int)line.charAt(i+j-s+1)]; // Score at each instance
					i++;
					j = s-1;	
				}
			}
			//System.out.println("Total found instances : " + count + " and LSS = " + countLSS + " LSS is " + LSS);
			output[0] = count;
			output[1] = countLSS;
		}
		catch (FileNotFoundException e)
		{
		System.out.println(e);
		}
		return output;
	}
}