import java.util.ArrayList;

/**
 * The Parser class. It is used for parsing the given lines of code (in the Boaz language).
 *
 * @author Andrei Constantin (ac2042)
 *
 */
public class Parser
{
    private ArrayList<String> lines;

    /**
     * The constructor of the Parser.
     * @param lines The lines of code
     */
    public Parser(ArrayList<String> lines)
    {
        this.lines = lines;
    }

    /**
     * Parse the lines of code
     */
    public void parse()
    {


        printDiagnostic(true);
    }

    /**
     *
     */
    public void printDiagnostic(boolean successful)
    {
        if(successful)
            System.out.println("ok");
        else
            System.out.println("error");
    }
}
