/**
 * Created by PrashantKumarRaj on 5/2/2016.
 */


    import java.io.File;
    import java.io.FileReader;
    import java.io.IOException;
    import java.util.Scanner;

    public class R {
        static int q = 101;
        static int d = 256;
        static int patternFreq;
        static void patternSearch(String Pattern) throws IOException {
            File file=new File("C:\\Users\\PrashantKumarRaj\\Desktop\\brown.txt");
            FileReader fr=new FileReader(file);
            Scanner sc=new Scanner(fr);

            int patternLength = Pattern.length();
            long textLength = file.length();
            int i, j;
            int patternHash = 0; // hash value for pattern
            int textHash = 0; // hash value for txt
            int h = 1;

            // The value of h would be "pow(d, M-1)%q"
            for (i = 0; i < patternLength - 1; i++)
                h = (h * d) % q;

            // Calculate the hash value of pattern and first
            // window of text

            String columns = sc.nextLine();
            for(i=0;i < patternLength;i++) {
                patternHash = (d * patternHash + Pattern.charAt(i)) % q;
                textHash = (d * textHash +columns.charAt(i)) % q;

            }

            // Slide the pattern over text one by one
            i = 0;
            while( i <= textLength - patternLength && sc.hasNextLine()) {
                // Check the hash values of current window of text
                // and pattern. If the hash values match then only
                // check for characters on by one
                columns = sc.nextLine();
                if (patternHash == textHash) {
            /* Check for characters one by one */

                    for (j=0;j < patternLength;j++) {
                        if (columns.charAt(i + j) != Pattern.charAt(j))
                            break;
                    }

                    // if p == t and pat[0...M-1] = txt[i, i+1, ...i+M-1]
                    if (j == patternLength)
                        patternFreq++;
                }

                // Calculate hash value for next window of text: Remove
                // leading digit, add trailing digit
                if (i < textLength - patternLength) {
                    textHash = (d * (textHash - columns.charAt(i) * h) + columns.charAt(i + patternLength)) % q;

                    // We might get negative value of t, converting it
                    // to positive
                    if (textHash < 0)
                        textHash = (textHash + q);
                }
               i++;
            }
        }
    }

