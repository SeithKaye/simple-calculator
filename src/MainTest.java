import java.io.*;

/**
 * Created by Shine Kaye on 2017/3/24.
 * test the calculator
 */
public class MainTest {

    private static String readFile(File file) throws IOException {

        FileReader fileReader = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String str;
        StringBuilder strs = new StringBuilder();
        while((str = bufferedReader.readLine()) != null)
        {
            strs.append(str + "\n");
        }
        fileReader.close();
        return strs.toString();
    }

    public static void main(String[] args) throws Exception {

        String fileName = args[0];
        File file = new File(fileName);
        String sourceStr = "";
        try {
            sourceStr = readFile(file);
        } catch (IOException e) {
            System.err.println("File not found!");
        }
        Analyzer.analyze(sourceStr);
    }
}
