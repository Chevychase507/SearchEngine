package searchengine;

/**
 * Implements the Okapi BM25 ranking algorithm
 *
 * @author Anders Lauridsen
 */

public class BM25Score implements Score {

    /** Scores a website according to the Okapi BM25 ranking algorithm
     * @param word word the user searched for
     * @param website website which contains the word
     * @param index index used in the search
     * @return a double with the score for the website
     */
    @Override
    public double getScore(String word, Website website, Index index) {

        int documentLength;
        double averageDocumentLength; // average length of all websites
        double k = 1.75; // constant used in the algorithm
        double b = 0.75; // constant used in the algorithm
        double tfScore;
        double idfScore;

        documentLength = website.getListWord().size();
        averageDocumentLength = index.getAverageNumberOfWords();

        tfScore = new TFScore().getScore(word, website, index);
        idfScore = new IDFScore().getScore(word, website, index);

        return tfScore * ((k + 1)/(k * (1 - b + b * documentLength / averageDocumentLength) + tfScore)) * idfScore;
    }
}
