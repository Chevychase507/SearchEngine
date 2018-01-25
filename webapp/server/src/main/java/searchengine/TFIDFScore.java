package searchengine;

/**
 * TFIDFScore implements score and returns a score value for a website following the "Term Frequency Inverse
 * Document Frequency" method
 *
 * @author All members of Group J
 */

public class TFIDFScore implements Score {

    /**
     * Scores a single website according to the "Term Frequency Inverse Document Frequency" method.
     * @param word word the user searched for
     * @param website website which contains the word
     * @param index index used in the search
     * @return a double with the score for the website
     */

    @Override
    public double getScore(String word, Website website, Index index) {
        Score TF = new TFScore();
        Score IDF = new IDFScore();
        return TF.getScore(word, website, index) * IDF.getScore(word, website, index);

    }
}
