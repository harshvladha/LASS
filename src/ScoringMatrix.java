import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

public class ScoringMatrix {
	
	public static int allCharacters[] = new int[128];
	public static void main(String[] args) {
		HashInput(args[0]);
	}
	private static void HashInput(String filename){
		for(int i=0;i<128;i++){
			allCharacters[i] = 0;
		}
		try {
			Scanner scanner = new Scanner(new FileReader(filename));
			while (scanner.hasNextLine()) {
			    String columns = scanner.nextLine();
			    for(int i=0;i<columns.length();i++){
			    	allCharacters[(int)columns.charAt(i)]++;
			    }
			}
		} catch (FileNotFoundException e) {
			System.out.println("File Not FOund!!,\n" + e);
			
		}
		/*
		for(int i=0;i<128;i++){
			System.out.println(allCharacters[i]);
		}
		*/
		return;
		
	}

}
