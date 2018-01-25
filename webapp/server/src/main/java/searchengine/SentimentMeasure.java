package searchengine;

import com.google.cloud.language.v1.AnalyzeSentimentResponse;
import com.google.cloud.language.v1.Document;
import com.google.cloud.language.v1.Document.Type;
import com.google.cloud.language.v1.LanguageServiceClient;
import com.google.cloud.language.v1.Sentiment;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Loads or builds a map with a URL as key and a sentiment score as value
 * Uses Google's Language API to generate the sentiment score
 *
 * @author Mathias Koch Stræde
 */

public class SentimentMeasure {

    private final String FILENAME = "sentimentWebsiteMap.ser";
    private Map<String, Float> sentimentWebsiteMap = new HashMap<>();

    /**
     * Builds a map of sentiment scores based on the given websites. If a serialized file
     * is already in the current working directory with the title in FILENAME, the constructor
     * simply loads this serialized file as the map with sentiment scores
     * @param websites the websites to generate sentiment scores for
     */

    public SentimentMeasure(List<Website> websites) {
        File file = new File(FILENAME);

        // Test if a file with sentiment score exists and that the file is not a directory
        if (file.exists() && !file.isDirectory()) {
            sentimentWebsiteMap = loadSentimentDatabase();
        }
        // Otherwise, build and then load the database
        else {

            // Iterate through websites and get sentiment score via Google API
            for (Website w : websites) {
                try {
                    // API call: Serve the website list of words as a joined string
                    Sentiment sentiment = analyzeSentimentText(String.join(" ", w.getListWord()));

                    // Save sentiment score in map with website url as key
                    sentimentWebsiteMap.put(w.getUrl(), sentiment.getScore());
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    // Serialize the map for later use every time
                    // If we get interrupted in the process, e.g. by an API error
                    // Then we at least have some sentiment scores
                    serializeDatabase(sentimentWebsiteMap);
                }
            }
        }
    }

    /***
     * Serializes the given map to disk in a file with the name given by FILENAME
     * @param sentimentWebsiteMap the map to serialize
     */

    private void serializeDatabase(Map<String, Float> sentimentWebsiteMap) {

        // Check that map is not empty
        if (sentimentWebsiteMap.keySet().size() > 0) {

            try {
                FileOutputStream fileOut = new FileOutputStream(FILENAME);
                ObjectOutputStream out = new ObjectOutputStream(fileOut);

                // Serializing the map
                out.writeObject(sentimentWebsiteMap);

                out.close();
                fileOut.close();

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * De-serializes a map with sentiment scores and URL of websites from a file with the name in FILENAME
     * @return
     */

    private Map<String, Float> loadSentimentDatabase() {

        Map<String, Float> tempSentimentWebsiteMap = new HashMap<>();

        try {
            FileInputStream fileIn = new FileInputStream(FILENAME);
            ObjectInputStream in = new ObjectInputStream(fileIn);

            // The read object has to be casted to the right type to behave properly
            tempSentimentWebsiteMap = (Map<String, Float>) in.readObject();

            in.close();
            fileIn.close();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return tempSentimentWebsiteMap;

    }

    /**
     * Generates a Sentiment object (with a score value) from a block of text
     * Uses the Google Language API to generate the sentiment object.
     * @param text the block of text to be analyzed
     * @return a Sentiment object
     */

    private Sentiment analyzeSentimentText(String text) throws Exception {
        try (LanguageServiceClient language = LanguageServiceClient.create()) {
            Document doc = Document.newBuilder()
                    .setContent(text)
                    .setType(Type.PLAIN_TEXT)
                    .build();
            AnalyzeSentimentResponse response = language.analyzeSentiment(doc);
            Sentiment sentiment = response.getDocumentSentiment();
            if (sentiment == null) {
                return null;
            } else {

            }
            return sentiment;
        }
    }

    /**
     * Retrieves a sentiment score based on a given URL
     * @param url the websites url
     * @return a score between -1 and 1 if a sentiment score is available. Returns -2 if no sentiment score is available
     */

    public Float getSentiment(String url) {

        // If the sentiment map contains the score, then return
        if (sentimentWebsiteMap.containsKey(url)) {
            return sentimentWebsiteMap.get(url);
        }
        // Else return -2 as an error which is off scale, thus serving as an error
        return (float) -2;
    }

}