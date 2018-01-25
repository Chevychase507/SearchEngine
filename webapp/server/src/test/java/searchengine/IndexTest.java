package searchengine;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class IndexTest {
    private Index simpleIndex = null;
    private Index hashIndex = null;
    private Index treeIndex = null;

    @BeforeEach
    void setUp() {
        List<Website> sites = new ArrayList<Website>();
        sites.add(new Website("example1.com", "example1", Arrays.asList("word1", "word2")));
        sites.add(new Website("example2.com", "example2", Arrays.asList("word2", "word3")));
        simpleIndex = new SimpleIndex();
        hashIndex = new InvertedIndexHashMap();
        treeIndex = new InvertedIndexTreeMap();
        simpleIndex.build(sites);
        hashIndex.build(sites);
        treeIndex.build(sites);
    }

    @AfterEach
    void tearDown() {
         simpleIndex = null;
         hashIndex = null;
         treeIndex = null;
    }

    @Test
    void build() {
        assertEquals("SimpleIndex{sites=[Website{title='example1', url='example1.com', words=[word1, word2]}, Website{title='example2', url='example2.com', words=[word2, word3]}]}", simpleIndex.toString());
        assertEquals("InvertedIndex{invertedMap={word1=[Website{title='example1', url='example1.com', words=[word1, word2]}], word3=[Website{title='example2', url='example2.com', words=[word2, word3]}], word2=[Website{title='example1', url='example1.com', words=[word1, word2]}, Website{title='example2', url='example2.com', words=[word2, word3]}]}}", hashIndex.toString());
        assertEquals("InvertedIndex{invertedMap={word1=[Website{title='example1', url='example1.com', words=[word1, word2]}], word2=[Website{title='example1', url='example1.com', words=[word1, word2]}, Website{title='example2', url='example2.com', words=[word2, word3]}], word3=[Website{title='example2', url='example2.com', words=[word2, word3]}]}}", treeIndex.toString());
    }

    @Test
    void lookupTest() {
        lookupHelper(simpleIndex);
        lookupHelper(hashIndex);
        lookupHelper(treeIndex);
    }

    private void lookupHelper(Index  index) {
        assertEquals(1, index.lookup("word1").size());
        assertEquals(2, index.lookup("word2").size());
        assertEquals(0, index.lookup("word4").size());
    }

}