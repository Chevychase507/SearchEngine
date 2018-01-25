package searchengine;
import java.util.Map;
import java.util.TreeMap;

/**
 * Inverted index that uses Javas TreeMap
 *
 * @author All members of Group J
 *
 */

public class InvertedIndexTreeMap extends InvertedIndex {

    /**
     * Initializes the inverted map data structure to a TreeMap.
     */
    public InvertedIndexTreeMap (){
        this.invertedMap = new TreeMap<>();
    }
}
