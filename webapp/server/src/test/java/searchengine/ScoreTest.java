package searchengine;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ScoreTest {

    private Index hashIndex;
    private TFScore tfScore;
    private IDFScore idfScore;
    private BM25Score bm25Score;

    private List<Website> sites = new ArrayList<Website>();

    //For extra tests for the IDFScore
    private List<Website> sites2 = new ArrayList<>();
    private Index hashIndex2;

    // For TFIDFScore
    private TFIDFScore tfidfScore;

    // Crate empty list of websites
    private Index hashIndexempty;

    @BeforeEach
    void setUp() {
        sites.add(new Website("example1.com", "example1", Arrays.asList("word1", "word2")));
        sites.add(new Website("example2.com", "example2", Arrays.asList("word1", "word1")));
        sites.add(new Website("example3.com", "example3", Arrays.asList("")));
        hashIndex = new InvertedIndexHashMap();
        hashIndex.build(sites);
        tfScore = new TFScore();
        idfScore = new IDFScore();
        bm25Score = new BM25Score();

        // for extra tests for IDFScore and BM25
        sites2.add(new Website("example1.com", "example1", Arrays.asList("word1", "word2", "word2")));
        sites2.add(new Website("example2.com", "example2", Arrays.asList("word1", "word1", "word2")));
        sites2.add(new Website("example3.com", "example3", Arrays.asList("word1", "word3", "word3")));
        hashIndex2 = new InvertedIndexHashMap();
        hashIndex2.build(sites2);

        // for test for TFIDFScore
        tfidfScore = new TFIDFScore();

        // Crate empty list of websites
        hashIndexempty = new InvertedIndexHashMap();
        hashIndexempty.build(new ArrayList<Website>());

    }

    @Test
    void getTFScore() {
        // Get score for website containing 1 instance of the search word - expected return value = 1
        assertEquals(1, tfScore.getScore("word1", sites.get(0), hashIndex));
        // Get score for website containing 2 instances of the search words - expected return value = 2
        assertEquals(2, tfScore.getScore("word1", sites.get(1), hashIndex));
        // Get score for website containing 0 instances of the search words - expected return value = 0
        assertEquals(0, tfScore.getScore("word3", sites.get(0), hashIndex));
        // Get score for website containing 0 instances of the search words when Website contains 0 words
        // - expected return value = 0
        assertEquals(0, tfScore.getScore("word1", sites.get(2), hashIndex));
        // Get score based on an empty index. The index is not used in the TFScore, so it doesn't matter if the
        // Index is empty
        assertEquals(0, tfScore.getScore("word3", sites.get(0), hashIndexempty));
    }

    @Test
    void getIDFScore(){
        // test a word that is present on 1/3 websites - the variation allowed is 1%
        assertEquals(1.58, idfScore.getScore("word2", sites.get(0), hashIndex), 0.01);
        // test a word that is present on 2/3 websites - the variation allowed is 1%
        assertEquals(0.58, idfScore.getScore("word1", sites.get(0), hashIndex), 0.01);
        // test a word that is present on 3/3 websites - the variation allowed is 1%
        assertEquals(0, idfScore.getScore("word1", sites.get(0), hashIndex2), 0.01);
        // tests if the search word is contained in no websites of all the websites in the index
        assertEquals(0, idfScore.getScore("word3", sites.get(0), hashIndex));
        // Get score based on an empty index.
        assertEquals(0, idfScore.getScore("word1", sites.get(0), hashIndexempty));
    }

    @Test
    void getTFIDFScore(){
        // test word that is contained in all websites = this result is 0
        assertEquals(0, tfidfScore.getScore("word1", sites2.get(0), hashIndex2));
        // Test for a word that is included in 2/3 websites and once in the website- variation allowed 1%
        assertEquals(0.58, tfidfScore.getScore("word2", sites2.get(1), hashIndex2), 0.01);
        // Test for a word that is included in 2/3 websites and twice in the website- variation allowed 1%
        assertEquals(1.16, tfidfScore.getScore("word2", sites2.get(0), hashIndex2), 0.01);
        // Test for a word that is included in 1/3 websites and twice in the website - variation allowed 1%
        assertEquals(3.16, tfidfScore.getScore("word3", sites2.get(2), hashIndex2), 0.01);
        // Test for a word that is included in 0/3 websites
        assertEquals(0, tfidfScore.getScore("word4", sites2.get(2), hashIndex2));
        // Get score based on an empty index.

        // WHY DO WE DO THIS WHEN THE SITES IS NOT NULL, ANDERS LAURIDSEN?
        assertEquals(0, tfidfScore.getScore("word2", sites2.get(0), hashIndexempty));
    }

    @Test
    void getBM25Score() {
        // Test word3 that is contained on 1/3 websites and 2 times on the relevant website
        assertEquals(2.32, bm25Score.getScore("word3", sites2.get(2), hashIndex2), 0.01);
        // Test word2 that is contained on 2/3 websites and 2 times on the relevant website
        assertEquals(0.85, bm25Score.getScore("word2", sites2.get(0), hashIndex2), 0.01);
        // Test word1 that is contained on 3/3 websites and 1 time on the relevant website
        assertEquals(0, bm25Score.getScore("word1", sites2.get(0), hashIndex2), 0.01);
        // Test word2 that is contained on 2/3 websites and 1 time on the relevant website
        assertEquals(0.58, bm25Score.getScore("word2", sites2.get(1), hashIndex2), 0.01);
        // Test word4 that is not contained on any of the 3 websites or on the relevant website
        assertEquals(0, bm25Score.getScore("word4", sites2.get(0), hashIndex2), 0.01);

    }

}