package searchengine;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * FileHelper reads website data from a text file
 *
 * @author All members of Group J
 */

public class FileHelper {

    /**
     * Parses the text file and loads it into the list of websites objects.
     * Each object in sites gets a title, a url and a list of words.
     * Throws an en exception error message if the text file cannot be found.
     * @param filename is the name of the file, which it receives in program arguments
     * @return the list of website objects.
     */

    public static List<Website> parseFile(String filename) {
        List<Website> sites = new ArrayList<Website>();
        String url = null, title = null;
        List<String> listOfWords = null;


        try {
            Scanner sc = new Scanner(new File(filename), "UTF-8");
            while (sc.hasNext()) {
                String line = sc.nextLine();
                if (line.startsWith("*PAGE:")) {
                    // Create previous website from data gathered
                    if (isWebsiteReadyForAdding(url, title, listOfWords)) {
                        sites.add(new Website(url, title, listOfWords));
                    }
                    // New website starts
                    url = line.substring(6);
                    title = null;
                    listOfWords = null;
                } else if (title == null) {
                    title = line;
                } else {
                    // And that's a word!
                    if (listOfWords == null) {
                        listOfWords = new ArrayList<>();
                    }
                    listOfWords.add(line);
                }
            }
            if (isWebsiteReadyForAdding(url, title, listOfWords)) {
                sites.add(new Website(url, title, listOfWords));
            }
        } catch (FileNotFoundException e) {
            System.out.println("Couldn't load the given file");
            e.printStackTrace();
        }

        return sites;
    }

    /** Helper method to check if seperate parts of a website is ready to add to a list of sites
     * @param url the websites url
     * @param title the websites title
     * @param listOfWords the words on the website
     * @return true if arguments are not null
     */
    private static boolean isWebsiteReadyForAdding(String url, String title, List<String> listOfWords)
    {
        if(url != null && title != null && listOfWords != null)
        {
            return true;
        }
        return false;
    }

}
