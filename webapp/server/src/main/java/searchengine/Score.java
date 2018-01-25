package searchengine;

/**
 * Score gives the interface for the Score classes TFScore, IDFScore, TFIDFScore and BM25Score.
 *
 * @author All members of Group J
 */

public interface Score {

    /**
     * Scores a single website according to its relevance and returns a score value, which is used to rank the websites.
     * @param word word the user searched for
     * @param website website which contains the word
     * @param index index used in the search
     * @return a double with the score for the website
     */
    double getScore(String word, Website website, Index index);
}
