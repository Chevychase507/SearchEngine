package searchengine;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class QueryHelperTest {
    private Index idx, idx2;
    private QueryHelper qh;

    @BeforeEach
    void setUp() {
        List<Website> sites = new ArrayList<>();
        sites.add(new Website("example1.com", "example1", Arrays.asList("word1", "word2")));
        sites.add(new Website("example2.com", "example2", Arrays.asList("word2", "word3")));
        sites.add(new Website("example3.com", "example3", Arrays.asList("word3", "word4", "word5")));
        idx = new SimpleIndex();
        idx.build(sites);
        qh = new QueryHelper(idx, new TFScore());

        List<Website> sites2 = new ArrayList<>();
        sites2.add(new Website("example1.com", "example1", Arrays.asList("word1", "word1", "word2")));
        sites2.add(new Website("example2.com", "example2", Arrays.asList("word1", "word1", "word1")));
        sites2.add(new Website("example3.com", "example3", Arrays.asList("word1", "word2", "word2")));
        sites2.add(new Website("example4.com", "example4", Arrays.asList("word1", "word2", "word5")));
        sites2.add(new Website("example5.com", "example5", Arrays.asList("word1", "word6", "word7")));
        sites2.add(new Website("example6.com", "example6", Arrays.asList("word7", "word9", "word10")));
        idx2 = new SimpleIndex();
        idx2.build(sites2);

    }

    @AfterEach
    void tearDown() {
        idx = null;
        qh = null;
    }

    @Test
     void testSingleWord() {
        assertEquals(1, qh.getMatchingWebsites("word1").size());
        assertEquals("example1", qh.getMatchingWebsites("word1").get(0).getTitle());
        assertEquals(2, qh.getMatchingWebsites("word2").size());

    }
    @Test
    void testMultipleWords() {
        assertEquals(0, qh.getMatchingWebsites("word1 word3").size());
        assertEquals(1, qh.getMatchingWebsites("word1 word2").size());
        assertEquals(1, qh.getMatchingWebsites("word3 word4 word5").size());
    }

    @Test
    void testORQueries() {
        assertEquals(2, qh.getMatchingWebsites("word1 OR word2").size());
        assertEquals(3, qh.getMatchingWebsites("word2 OR word3").size());
        assertEquals(1, qh.getMatchingWebsites("word1 OR word1").size());

    }

    @Test
    void testCornerCases() {
        // Does it return 0 when expected?
        assertEquals(0, qh.getMatchingWebsites("word6").size());
        // Does it ignore invalid OR queries?
        assertEquals(1, qh.getMatchingWebsites("word1 OR ").size());
        // Does it take queries with multiple ORs?
        assertEquals(3, qh.getMatchingWebsites("word1 OR word2 OR word3").size());
        // Does it handle whitespace properly?
        assertEquals(1, qh.getMatchingWebsites("    word1       ").size());
    }




    @Test
    void testTFScoreAndGetMatchingWebsites() {
        qh = new QueryHelper(idx2, new TFScore());

        // Tests if it gives the websites with 3 and 2 instances of word1 the right scores and returns them in
        // correct order
        assertTrue(qh.getMatchingWebsites("word1").toString().startsWith("[Website{title='example2', " +
                "url='example2.com', words=[word1, word1, word1]}, Website{title='example1'," +
                " url='example1.com', words=[word1, word1, word2]}"));

        // Tests if it gives the websites with 2 and 1 instance of word1 the right score and returns
        // them in correct order
        assertTrue(qh.getMatchingWebsites("word2").toString().startsWith("[Website{title='example3', " +
                "url='example3.com', words=[word1, word2, word2]}"));

        // Tests AND-search: It should score example 4 lowest, since it has the lowest frequency of word1 and word2
        assertTrue(qh.getMatchingWebsites("word1 word2").toString().endsWith("Website{title='example4'," +
                " url='example4.com', words=[word1, word2, word5]}]"));

        // Tests OR-search: It should score example2 highest, since it has the highest frequency of word1 OR word2
        assertTrue(qh.getMatchingWebsites("word1 OR word2").toString().
                startsWith("[Website{title='example2', url='example2.com', words=[word1, word1, word1]}"));

        // Tests OR-search and AND-search: It should score example6 lowest, since it has the lowest frequency of
        // word1 word2 OR word9
        assertTrue(qh.getMatchingWebsites("word1 word2 OR word9").toString().
                endsWith("url='example6.com', words=[word7, word9, word10]}]"));

        // Tests multiple OR-searches and AND-search: It should score example 6 lowest.
        assertTrue(qh.getMatchingWebsites("word1 word2 OR word2 word5 OR word9").toString()
                .endsWith("Website{title='example6', url='example6.com', words=[word7, word9, word10]}]"));
    }

    @Test
    void testIDFScoreAndGetMatchingWebsites() {
        // The ranking of IDFScore can only be tested on OR-searches, since it is only at OR-searches that websites
        //gets different scores

        qh = new QueryHelper(idx2, new IDFScore());

        //Tests OR-search with single words: It should score example5 or example6 highest, since they contain word7,
        // which gives them a high idf score
        assertTrue(qh.getMatchingWebsites("word1 OR word7").toString().startsWith(
                "[Website{title='example6', url='example6.com', words=[word7, word9, word10]}") ||
                qh.getMatchingWebsites("word1 OR word7").toString().startsWith(
                        "[Website{title='example5', url='example5.com', words=[word1, word6, word7]}"));

        //Tests OR-search combined with AND-search: It should score example5 highest, since it contains both word5 and
        //word7, but none of them are frequent on other websites.
        assertTrue(qh.getMatchingWebsites("word1 word2 OR word6 word7").toString().startsWith(
                "[Website{title='example5', url='example5.com', words=[word1, word6, word7]}"));
    }

    @Test
    void testTFIDFScoreAndGetMatchingWebsites() {
        qh = new QueryHelper(idx2, new TFIDFScore());

        // Tests if it gives the websites with 3 and 2 instances of word1 the right scores and returns them in
        // correct order
        assertTrue(qh.getMatchingWebsites("word1").toString().startsWith("[Website{title='example2', " +
                "url='example2.com', words=[word1, word1, word1]}, Website{title='example1'," +
                " url='example1.com', words=[word1, word1, word2]}"));

        // Tests if it gives the websites with 2 and 1 instance of word1 the right score and returns
        // them in correct order
        assertTrue(qh.getMatchingWebsites("word2").toString().startsWith("[Website{title='example3', " +
                "url='example3.com', words=[word1, word2, word2]}"));

        // Tests AND-search: It should score example 4 lowest, since it has the lowest frequency of word1 and word2
        assertTrue(qh.getMatchingWebsites("word1 word2").toString().endsWith("Website{title='example4'," +
                " url='example4.com', words=[word1, word2, word5]}]"));

        // Tests OR-search: It should score example3 highest, since it has the highest frequency of word2, which has a
        // lower frequence than word1 and therefore higher relevance
        assertTrue(qh.getMatchingWebsites("word1 OR word2").toString()
                .startsWith("[Website{title='example3', url='example3.com', words=[word1, word2, word2]}"));

        // Tests OR-search and AND-search: It should score example6 highest, since it gets the highest idf score, then
        // example3, since it has the highest term frequency
        assertTrue(qh.getMatchingWebsites("word1 word2 OR word9").toString()
                .startsWith("[Website{title='example6', url='example6.com', words=[word7, word9, word10]}," +
                        " Website{title='example3', url='example3.com'"));

        // Tests multiple OR-searches and AND-search: It should score example4 highest, since it gets a high idf score
        // and has the term frequency 2. Example6 should be second, since it gets a high idf score but has low term
        // frequency.
        assertTrue(qh.getMatchingWebsites("word1 word2 OR word2 word5 OR word9").toString()
                .startsWith("[Website{title='example4', url='example4.com', words=[word1, word2, word5]}, " +
                        "Website{title='example6', url='example6.com', words=[word7, word9, word10]}"));

    }

    @Test
    void testBM25ScoreAndGetMatchingWebsites() {
        qh = new QueryHelper(idx2, new BM25Score());

        // Tests if it gives the websites with 3 and 2 instances of word1 the right scores and returns them in
        // correct order
        assertTrue(qh.getMatchingWebsites("word1").toString().startsWith("[Website{title='example2', " +
                "url='example2.com', words=[word1, word1, word1]}, Website{title='example1'," +
                " url='example1.com', words=[word1, word1, word2]}"));

        // Tests if it gives the websites with 2 and 1 instance of word1 the right score and returns
        // them in correct order
        assertTrue(qh.getMatchingWebsites("word2").toString().startsWith("[Website{title='example3', " +
                "url='example3.com', words=[word1, word2, word2]}"));

        // Tests AND-search: It should score example 4 lowest, since it has the lowest frequency of word1 and word2
        assertTrue(qh.getMatchingWebsites("word1 word2").toString().endsWith("Website{title='example4'," +
                " url='example4.com', words=[word1, word2, word5]}]"));

        // Tests OR-search: It should score example3 highest, since it has the highest frequency of word2, which has a
        // lower frequence than word1 and therefore higher relevance
        assertTrue(qh.getMatchingWebsites("word1 OR word2").toString()
                .startsWith("[Website{title='example3', url='example3.com', words=[word1, word2, word2]}"));

        // Tests OR-search and AND-search: It should score example6 highest, since it gets the highest idf score, then
        // example3, since it has the highest term frequency
        assertTrue(qh.getMatchingWebsites("word1 word2 OR word9").toString()
                .startsWith("[Website{title='example6', url='example6.com', words=[word7, word9, word10]}," +
                        " Website{title='example3', url='example3.com'"));

        // Tests multiple OR-searches and AND-search: It should score example4 highest, since it gets a high idf score
        // and has the term frequency 2. Example6 should be second, since it gets a high idf score but has low term
        // frequency.
        assertTrue(qh.getMatchingWebsites("word1 word2 OR word2 word5 OR word9").toString()
                .startsWith("[Website{title='example4', url='example4.com', words=[word1, word2, word5]}, " +
                        "Website{title='example6', url='example6.com', words=[word7, word9, word10]}"));
    }

}