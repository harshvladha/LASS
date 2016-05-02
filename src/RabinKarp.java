/**
 * Created by PrashantKumarRaj on 3/30/2016.
 */
        import java.io.*;
        import java.nio.file.Files;
        import java.nio.file.Path;
        import java.nio.file.Paths;
        import java.io.IOException;

public class RabinKarp {

    private static int prime = 101;
    static int patternFreq=0;
    public static String patternSearch( String pattern) throws IOException {
        String index="";
        patternFreq=0;
        FileInputStream fin = new FileInputStream("C:\\Users\\PrashantKumarRaj\\Desktop\\brown.txt");
        Path pathOfTextFile = Paths.get("C:\\Users\\PrashantKumarRaj\\Desktop\\brown.txt");
        byte [] textInByte = Files.readAllBytes(pathOfTextFile);
        int textLength = textInByte.length;
        byte [] patternInByte=pattern.getBytes();
        int patternLength = patternInByte.length;
        long patternHash = createHash(patternInByte, patternLength-1);
        long textHash = createHash(textInByte, patternLength-1);
        for (int i = 1; i <=textLength-patternLength + 1; i++) {
            if (patternHash == textHash && checkEqual(textInByte, i - 1, i + patternLength - 2, patternInByte, 0, patternLength - 1)) {
                index+=i-1+",";
                patternFreq++;
            }
            if(i <textLength - patternLength + 1) {
                textHash = recalculateHash(textInByte, i-1, i + patternLength-1, textHash, patternLength);
            }
        }
        return index;
    }

    private static long recalculateHash(byte [] str, int oldIndex, int newIndex, long oldHash, int patternLen) {
        long newHash = oldHash - str[oldIndex];
        newHash = newHash / prime;
        newHash += str[newIndex] * Math.pow(prime, patternLen - 1);
        return newHash;
    }

    private static long createHash(byte[] str, int end) {
        long hash = 0;
        for (int i = 0; i <= end; i++) {
            hash += str[i] * Math.pow(prime, i);
        }
        return hash;
    }

    private static boolean checkEqual(byte str1[], int start1, int end1, byte str2[], int start2, int end2) {
        if (end1 - start1 != end2 - start2) {
            return false;
        }
        while (start1 <= end1 && start2 <= end2) {
            if (str2[start2] != str1[start1]) {
                return false;
            }
            start1++;
            start2++;
        }
        return true;
    }
}
