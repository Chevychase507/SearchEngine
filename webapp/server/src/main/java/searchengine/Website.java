package searchengine;
import java.util.ArrayList;
import java.util.List;

/**
 * Website is the class of each website object. It consists of an url, a title and a list of words.
 *
 * @author Martin Aumüller - with minor adjustments by Group J
 */

public class Website {
    private String title;
    private String url;
    private List<String> words;
    private Float sentiment;

    /**
     * Creates new website object with url, title and a list of words
     * @param url A website's url
     * @param title A website's title
     * @param words A website's list of words
     */

    public Website(String url, String title, List<String> words) {
        this.url = url;
        this.title = title;
        this.words = words;
    }


    /**
     * Checks if a website contains a specific word
     * @param word
     * @return True if the word is present, false otherwise
     */

    public Boolean containsWord(String word) {
        return words.contains(word);
    }


    /**
     * Gets the url of the website
     * @return A url
     */

    public String getUrl() {
        return url;
    }

    /**
     * Gets the title of the website
     * @return A title
     */

    public String getTitle() {
        return title;
    }

    /**
     * Gets all words on the website
     * @return a list of all words on the website
     */

    public List<String> getListWord(){
        return words;
    }

    public Float getSentiment() {
        return sentiment;
    }

    public void setSentiment(Float sentiment) {
        this.sentiment = sentiment;
    }

    @Override
    public String toString() {
        return "Website{" +
                "title='" + title + '\'' +
                ", url='" + url + '\'' +
                ", words=" + words +
                '}';
    }
}