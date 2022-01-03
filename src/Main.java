import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * The Main class of the Parser. Reads the file from arguments, creates a new Parser object and
 * calls the parse() method.
 *
 * @author Andrei Constantin (ac2042)
 */
public class Main {

    public static void main(String[] args)
    {
        String file = args[0];
        //Test if a file has been given and if it has the appropriate suffix.
        if(file!=null && !file.isEmpty() && file.endsWith(".boaz"))
        {
            try{
                //Create an assembler with the same name as the given file.

                BufferedReader reader = new BufferedReader(new FileReader(file));
                ArrayList<String> lines = new ArrayList<>();
                String line;
                while((line=reader.readLine()) != null)
                {
                    lines.add(line);
                }
                reader.close();

                Parser parser = new Parser(lines);

            }catch(IOException ex)
            {
                System.err.println("Unable to read "+file);
            }
        }
        else
            System.err.println("Usage: sham file.boaz");
    }
}
