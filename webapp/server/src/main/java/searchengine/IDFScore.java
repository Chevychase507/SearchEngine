package searchengine;

/**
 * Implements the inverse document frequency score
 *
 * @author All members of Group J
 */

public class IDFScore implements Score {
    @Override

    /** Scores a website according to the inverse document frequency score
     * @param word word the user searched for
     * @param website website which contains the word
     * @param index index used in the search
     * @return a double with the score for the website
     */
    public double getScore(String word, Website website, Index index) {

        double numberOfWebsites = index.getTotalNumberOfWebsites();
        double numberOfWebsitesWordOccursOn = index.lookup(word).size();

        // Avoid divide by zero if a word is passed which occurs on zero websites in the index.
        if(numberOfWebsitesWordOccursOn == 0.0) return 0.0;

        return Math.log10(numberOfWebsites/numberOfWebsitesWordOccursOn) / Math.log10(2.);
    }
}
