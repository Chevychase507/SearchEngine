package searchengine;
import java.util.*;

/**
 * Benchmarking tests the query time for different indices used to store and look up words on websites
 *
 * @author All members of Group J
 */

public class Benchmarking {

    /**
     * Benchmarks the time it takes to do a search query on an index.
     * First performs warm up for the Java Virtual Machine and then tests
     * the different indices with 1000 queries per word in a 20 word list.
     * Outputs average, maximum and minimum time in the console for each index.
     * Tests SimpleIndex and InvertedIndex with both TreeMap and HashMap.
     * @param args -Not used
     */
    public static void main(String[] args) {

        List<Index> indices = new ArrayList<>();

        indices.add(new SimpleIndex());
        indices.add(new InvertedIndexTreeMap());
        indices.add(new InvertedIndexHashMap());

        List<String> queryWords = new ArrayList<>();

        String[] strs = { "denmark", "usa", "states", "minor", "writing", "south", "family", "war", "word", "see",
                "auto", "in", "of", "north", "is", "punished", "asia", "day", "income", "the" };

        queryWords.addAll(Arrays.asList(strs));

        List<Website> sites = FileHelper.parseFile("../../data/enwiki-medium.txt");

        for(Index idx : indices) {
            idx.build(sites);
            System.out.println("Testing index with " + idx.getClass());
            warmUp(idx, queryWords);
            testTime(idx, queryWords, 20);

            System.out.println("---------------------------------------");
        }
    }
    /**
     * Times and outputs how many microseconds it takes to run a search on a list of query words with a specified
     * index implementation
     * @param idx the specified index implementation
     * @param queryWords list of words to query
     * @param testCount number of times to perform the test per word
     */

    private static void testTime(Index idx, List<String> queryWords, int testCount) {

        ArrayList<Long> timeResults = new ArrayList<>();
        long averageTime = 0;
        int foundWebsites = 0;

        for (int i = 0 ; i < testCount ; i++) {
            foundWebsites = 0;
            long startTime = System.nanoTime();
            for (String query : queryWords) {
                List<Website> tempResult = idx.lookup(query);
                if (tempResult != null) {
                    foundWebsites += tempResult.size();
                }
            }
            long elapsedTime = System.nanoTime() - startTime;
            timeResults.add(elapsedTime/1000);
        }

        for (Long t : timeResults) {
            averageTime += t;
        }
        averageTime = averageTime / testCount;

        System.out.println("Running queries with this index implementation based on " + (testCount * queryWords.size())
                + " tests took:" + "\nOn average: " + averageTime + " microseconds.\n" + "Maximum time was: "
                + Collections.max(timeResults) + "." + "\nMinimum time was: " + Collections.min(timeResults)
                + ".");

        System.out.println("Found " + foundWebsites + " websites.");

    }

    /**
     * Performs 1000 queries per word in the list of query words to warm up the Java Virtual Machine
     * @param idx - the specified index implementation
     * @param queryWords - list of words to query
     */

    private static void warmUp(Index idx, List<String> queryWords) {
        for (int i = 0; i < 5; i++) {
            for (String query : queryWords) {
                idx.lookup(query);

            }
        }

    }
}





