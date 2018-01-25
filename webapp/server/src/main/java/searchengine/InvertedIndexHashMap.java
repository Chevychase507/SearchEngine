package searchengine;
import java.util.HashMap;

/**
 * Inverted index that uses Javas HashMap
 *
 * @author All members of Group J
 */
public class InvertedIndexHashMap extends InvertedIndex {

    /**
     * Initializes the inverted map data structure to a HashMap.
     */
    public InvertedIndexHashMap (){
        this.invertedMap = new HashMap<>();
    }
}
