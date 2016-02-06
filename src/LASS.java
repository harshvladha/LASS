import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Scanner;
import java.util.Arrays;
public class LASS {
	public static double scoringMatrix[][] = new double[128][128];
	public static void main(String[] args) throws UnsupportedEncodingException {
		/*
		 * first argument is file/database to search in (args[0)
		 * second argument is pattern which we have to find (args[1)
		 * third argument is file of scoring matrix (args[2)
		 */
		ImportScoringMatrix(args[2]);
		String Seeds[] = new String[args[1].length()-1];
		Seeds = createSeeds(args[1], 3);
	}
	private static String[] createSeeds(String pattern, int seedLength) {
		String seeds[] = new String[pattern.length()-1];
		char seed[] = new char[seedLength];
		for(int i=0;i<pattern.length()-seedLength;i++){
			for(int j=i;j<seedLength;j++){
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
	
}
