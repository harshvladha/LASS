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
