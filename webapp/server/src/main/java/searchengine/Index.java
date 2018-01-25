package searchengine;
import java.util.List;
import java.util.Map;

/**
 * The Index interface defines the methods required by InvertedIndex
 *
 * @author All members of Group J
 */

public interface Index {

    /**
     * Builds an index of websites. Can use SimpleIndex or InvertedIndex with either HashMap or TreeMap.
     * @param sites List of preprocessed websites.
     */
    void build(List<Website> sites);

    /**
     * Check if a query word is present an collection of websites
     * @param query the query word to search for
     * @return List of Websites where the query words is present. Returns an empty list if no matches are found
     */

    List<Website> lookup(String query);

    /** Gets the total numbers of websites that the index holds
     * @return the total count of websites
     */
    int getTotalNumberOfWebsites();

    /** Gets the average number of words on all the websites that the index holds
     * @return the average number of words
     */
    double getAverageNumberOfWords();

}
