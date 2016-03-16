import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Scanner;
import java.util.Arrays;
public class LASS {
	public static double scoringMatrix[][] = new double[128][128];
	public static String searchPattern; // pattern which we need to find
	public static String LSS;
	public static int LSSPos;
	public static void main(String[] args) throws UnsupportedEncodingException {
		/*
		 * first argument is file/database to search in (args[0)
		 * second argument is pattern which we have to find (args[1)
		 * third argument is file of scoring matrix (args[2)
		 * fourth argument is threshold (args[3)
		 */
		ImportScoringMatrix(args[2]);
		String Seeds[] = new String[args[1].length()-1];
		searchPattern = args[1];
		Seeds = createSeeds(searchPattern, 3);
		double score = 0;
		double min_score = 1000000.00;
		double threshold = Double.parseDouble(args[3]);
		int j = 0;
		for(int i=0;i<searchPattern.length()-2;i++){
			score = ScoreOfEachSeed(Seeds[i]);
			if(score > threshold && score < min_score){
				min_score = score;
				LSS = Seeds[i];
				LSSPos = i;
			}
		}
		exactmatchoflss(args[0], LSS, min_score);
	}
	private static String[] createSeeds(String pattern, int seedLength) {
		String seeds[] = new String[pattern.length()-seedLength+1];
		char seed[] = new char[seedLength];
		for(int i=0;i<pattern.length()-seedLength;i++){
			for(int j=i;j<i+seedLength;j++){
				seed[j-i] = pattern.charAt(j);
			}
			seeds[i] = String.copyValueOf(seed);
		}
		return seeds;
	}
	private static void ImportScoringMatrix(String filename) {
		try {
			Scanner scanner = new Scanner(new FileReader(filename));
			/* build scoring matrix from text file */
			for(int i=0;i<128;i++){
				for(int j=0;j<128;j++){
					scoringMatrix[i][j] = scanner.nextDouble();
				}
			}
		} catch (FileNotFoundException e) {
			System.out.println(e);
		}
		
	}
	private static double ScoreOfEachSeed(String seed){
        int k=seed.length();
        double score=0;
		for(int p=0;p<k;p++){
            score=score+scoringMatrix[(int)seed.charAt(p)][(int)seed.charAt(p+1)];
        }
        return score;
	}
	/*
	private static String leastscoringseeds(String lseed,int threshold){
		if(ScoreOfEachSeed(lseed)>threshold){
			return lseed;
		}
		else
			return "";
	}
	*/
	private static void exactmatchoflss(String filename, String lsseed, double seedscore){
		try {
			Scanner scanner = new Scanner(new FileReader(filename));
			/* build scoring matrix from text file */
			int lineNumber = 0, count = 0;
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				lineNumber++;
				int s=lsseed.length();
				int L = line.length();				
				double currentscore=0;
				int i = 0;
				int j = 1;
				/*
				 * seed length traversal should happen for all characters in this line.
				 */
				while(i < L){
					while(j<s)
					{
						currentscore=currentscore+scoringMatrix[(int)line.charAt(i+j-1)][(int)line.charAt(i+j)];
						j++;
					}
					if(currentscore==seedscore){
						/* 
						 * Score matched, now we can extend left and right to make a EXACT match						 * 
						 */
						boolean foundFlag = true;
						int k = i+j-s+1;
						int i1=k, i2=k;
						int lsspos1 = LSSPos;
						int lsspos2 = LSSPos;
						while(lsspos1 > 0 || lsspos2 < searchPattern.length()){
							if(lsspos1 > 0 && searchPattern.charAt(lsspos1) == line.charAt(i1)){
								lsspos1--;
								i1--;
							}
							else if(lsspos2 < searchPattern.length() && searchPattern.charAt(lsspos2) == line.charAt(i2)){
								lsspos2++;
								i2++;
							}
							else{
								foundFlag = false;
								break;
							}
						}
						if(foundFlag == true){
							System.out.println("Pattern found at "+ (k - i1+1) + "Line : " + lineNumber);
							count++;
						}
						
					}
					else{
						currentscore=currentscore-scoringMatrix[(int)line.charAt(i+j-s)][(int)line.charAt(i+j-s+1)];
					}
					i++;
					j = s-1;
				}

			}
			System.out.println("Total found instances : " + count);
		} catch (FileNotFoundException e) {
			System.out.println(e);
		}
	}
}
