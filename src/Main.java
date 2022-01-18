import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

                String sanitized = sanitizeInput(lines);

                try {
                    Tokenizer tokenizer = new Tokenizer(sanitized);
                    var tokens = tokenizer.getTokens();
                    Parser parser = new Parser(tokens);
                    //parser.parse();

                    System.out.println("ok");
                }catch(Exception ex)
                {
                    System.err.println(ex.getMessage());
                    System.out.println("error");
                }

            }catch(IOException ex)
            {
                System.err.println("Unable to read "+file);
            }
        }
        else
            System.err.println("Usage: sham file.boaz");
    }

    /**
     * Sanitizes the input lines.
     * @param lines The lines to be sanitized
     *
     * @return The sanitized input
     */
    private static String sanitizeInput(List<String> lines)
    {
        StringBuilder sanitizedInput = new StringBuilder();
        for(var line: lines)
        {
            String sanitizedLine = line.strip().replaceAll("[\\n\\t]", "").replaceAll("\\s+", " ");
            sanitizedLine = sanitizedLine.replaceAll("(?i)([;,])", " $1");
            sanitizedInput.append(sanitizedLine).append(" ");
        }

        return sanitizedInput.toString();
    }


    /**
     * Print the diagnostic of the Parser
     *
     * @param successful true if the parsing was successful, false otherwise
     */
    public void printDiagnostic(boolean successful)
    {
        if(successful)
            System.out.println("ok");
        else
            System.out.println("error");
    }
}
