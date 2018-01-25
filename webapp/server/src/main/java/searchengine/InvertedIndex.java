package searchengine;
import java.util.*;

/**
 * Defines build and lookup methods for inverted indices.
 * Functions as an abstract super class for InvertedIndexHashMap and InvertedIndexTreeMap
 *
 * @author All members of Group J
 */

public abstract class InvertedIndex implements Index {

    protected Map<String, List<Website>> invertedMap;
    private int numberOFWebsites;
    private double averageWordsOnAllWebsites;

    /**
     * Builds an index of websites.
     * @param sites List of preprocessed websites.
     */
    public void build(List<Website> sites) {
        // Calculate the average amounts of words across all websites (for BM25  ranking score)
        for (int i = 0; i < sites.size(); i++){
            int temp = sites.get(i).getListWord().size();
            averageWordsOnAllWebsites += temp;
        }
        averageWordsOnAllWebsites = averageWordsOnAllWebsites /sites.size();
        //build list
        numberOFWebsites = sites.size();
        for(Website w : sites) {
            List<String> listOfwords;
            listOfwords = w.getListWord();
            for (String word : listOfwords) {
                if (this.invertedMap.containsKey(word)) {
                    if (!this.invertedMap.get(word).contains(w)) {
                        this.invertedMap.get(word).add(w);
                    }
                }
                else {
                    List<Website> tempSites = new ArrayList<>();
                    tempSites.add(w);
                    this.invertedMap.put(word, tempSites);
                    }
                }
            }
    }

    /**
     * Check if a query word is present an collection of websites
     * @param query the query word to search for
     * @return List of Websites where the query words is present. Returns an empty list if no matches are found
     */
    @Override
    public List<Website> lookup(String query) {
        if(invertedMap.containsKey(query)) {

            return this.invertedMap.get(query);
        }
        else{
            return new ArrayList<>();
        }
    }

    /** Gets the total numbers of websites that the index holds
     * @return the total count of websites
     */
    @Override
    public int getTotalNumberOfWebsites() {
        return numberOFWebsites;
    }

    /** Gets the average number of words on all the websites that the index holds
     * @return the average number of words
     */
    @Override
    public double getAverageNumberOfWords(){
        return averageWordsOnAllWebsites;
    }

    @Override
    public String toString() {
        return "InvertedIndex{" +
                "invertedMap=" + invertedMap +
                '}';
    }

}

