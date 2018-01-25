package searchengine;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * SimpleIndex provides a simple build method and a simple lookup method
 *
 * @author All members of Group J
 */

public class SimpleIndex implements Index {
    private List<Website> sites;
    private double averageWordsOnAllWebsites;

    /**
     * Builds a  simple index of websites.
     * @param sites a list of websites.
     */
    @Override
    public void build(List<Website> sites) {
        this.sites = sites;
        for (int i = 0; i < sites.size(); i++){
            int temp = sites.get(i).getListWord().size();
            averageWordsOnAllWebsites += temp;
        }
        averageWordsOnAllWebsites = averageWordsOnAllWebsites /sites.size();
    }


    /**
     * Iterates through the list of all websites and searches for websites matching the query word. If a website matches
     * the query word, it is added to a list of found sites.
     * @param query A query word
     * @return A list of websites where the query words is present.
     */
    @Override
    public List<Website> lookup(String query) {
        List<Website> foundSites = new ArrayList<>();
        for (Website w : sites) {
            if (w.containsWord(query)) {
                foundSites.add(w);
            }
        }
        return foundSites;
    }


    /** Gets the total numbers of websites that the index holds
     * @return the total count of websites
     */
    @Override
    public int getTotalNumberOfWebsites() {
        return sites.size();
    }

    /** Gets the average number of words on all the websites that the index holds
     * @return the average number of words
     */
    @Override
    public double getAverageNumberOfWords (){
        return averageWordsOnAllWebsites;
    }

    @Override
    public String toString() {
        return "SimpleIndex{" +
                "sites=" + sites +
                '}';
    }

}
