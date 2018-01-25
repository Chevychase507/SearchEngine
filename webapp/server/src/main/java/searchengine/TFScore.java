package searchengine;

/**
 * TF implements score and returns a score value for a website according to its term frequency: The higher frequency
 * of a word, the higher ranking.
 *
 * @author All members of Group J
 */


public class TFScore implements Score {

    /**
     * Scores a single website according the frequency of the query word
     * @param word word the user searched for
     * @param website website which contains the word
     * @param index index used in the search
     * @return a double with the score for the website
     */

    @Override
    public double getScore(String word, Website website, Index index) {
        double wordCount = 0;
        for(String wordInWebsite : website.getListWord()){
            if(word.equals(wordInWebsite)) wordCount++;
        }
        return wordCount;
    }
}
