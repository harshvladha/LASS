import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Scanner;
import java.util.Arrays;
public class LASS {
	public static float scoringMatrix[][] = new float[128][128];
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
		/*
		for(int i=0;i<128;i++){
			for(int j=0;j<128;j++){
				System.out.print(scoringMatrix[i][j] + " ");
			}
			System.out.println();
		}
		*/
		String Seeds[] = new String[args[1].length()-1];
		searchPattern = args[1];
		Seeds = createSeeds(searchPattern, 3);
		//System.out.println(Seeds.length);
		float score = 0;
		float min_score = 100000; // make it 0 for taking HSS 
		float threshold = Float.parseFloat(args[3]);
		int j = 0;
		//System.out.println(searchPattern.length());
		for(int i=0;i<searchPattern.length()-2;i++){
			score = ScoreOfEachSeed(Seeds[i]);
			System.out.println(Seeds[i] + " score : " + score);
			if(score > threshold && score < min_score){ // make => score > min_score for taking HSS
				min_score = score;
				LSS = Seeds[i];
				LSSPos = i;
			}
		}
		//System.out.println(LSS + " score : "+ min_score);
		exactmatchoflss(args[0], LSS, min_score);
	}
	private static String[] createSeeds(String pattern, int seedLength) {
		String seeds[] = new String[pattern.length()-seedLength+1];
		char seed[] = new char[seedLength];
		for(int i=0;i<pattern.length()-seedLength+1;i++){
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
					scoringMatrix[i][j] = scanner.nextFloat();
				}
			}
		} catch (FileNotFoundException e) {
			System.out.println(e);
		}
		
	}
	private static float ScoreOfEachSeed(String seed){
        int k=seed.length();
        float score=0;
		for(int p=0;p<k-1;p++){
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
	private static void exactmatchoflss(String filename, String lsseed, float seedscore){
		try {
			Scanner scanner = new Scanner(new FileReader(filename));
			/* scan document for the match */
			int lineNumber = 0, count = 0, countLSS = 0; 
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				//System.out.println(line);

				lineNumber++;
				int s=lsseed.length();
				int L = line.length();				
				float currentscore=0;
				int i = 0;
				int j = 1;
				if(line.length() < s){
					continue;
				}
				/*
				 * seed length traversal should happen for all characters in this line.
				 */
				while(i+j < L){
					while(j<s)
					{
						currentscore=currentscore+scoringMatrix[(int)line.charAt(i+j-1)][(int)line.charAt(i+j)];
						//System.out.println(currentscore);
						j++;
					}
					if((int)(currentscore*100000)==(int)(seedscore*100000)){
						/* 
						 * Score matched, now we can extend left and right to make an EXACT match						 
						 * 
						 */
						
						//System.out.println("LSS found at Line " + lineNumber );
						countLSS++;
						boolean foundFlag = false;
						int k = i+j-s;
						//System.out.println("value of K here is : " + k + " and LSS pos in search string is : " + LSSPos);
						int i1=k, i2=k;
						int lsspos1 = LSSPos;
						int lsspos2 = LSSPos;
						//System.out.println(LSSPos + " " + i + " " + j);
						while((lsspos1 > -1 || lsspos2 < searchPattern.length()) && (i1 > -1 || i2 < line.length())){
							if((lsspos1 > -1 && i1 > -1 && searchPattern.charAt(lsspos1) == line.charAt(i1)) ){
								lsspos1--;
								i1--;
							}
							else if((lsspos2 < searchPattern.length() && i2 < line.length() && searchPattern.charAt(lsspos2) == line.charAt(i2))){
								lsspos2++;
								i2++;
							}
							else{
								break;
							}
							if(lsspos1 == -1 && lsspos2 == searchPattern.length()){
								foundFlag = true;
							}
						}
						if(foundFlag == true){
							System.out.println("Pattern found at index " + (i1+2) +  " of Line : " + lineNumber);
							count++;
						}
					}
					currentscore=currentscore-scoringMatrix[(int)line.charAt(i+j-s)][(int)line.charAt(i+j-s+1)];
					i++;
					j = s-1;	
				}
				//break;
			}
			System.out.println("Total found instances : " + count + " and LSS = " + countLSS + " LSS is " + LSS);
		} catch (FileNotFoundException e) {
			System.out.println(e);
		}
	}
}
