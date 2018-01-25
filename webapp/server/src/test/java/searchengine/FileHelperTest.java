package searchengine;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Mathias Koch Stræde
 * @version 1.1
 */
class FileHelperTest {
    @Test
    void parseGoodFile() {
        List<Website> sites = FileHelper.parseFile("test-resources/test-file.txt");

        assertEquals(2, sites.size());

        assertEquals("title1", sites.get(0).getTitle());
        assertEquals("title2", sites.get(1).getTitle());

        assertTrue(sites.get(0).containsWord("word1"));
        assertTrue(sites.get(0).containsWord("word2"));
        assertFalse(sites.get(0).containsWord("word3"));

        assertTrue(sites.get(1).containsWord("word1"));
        assertTrue(sites.get(1).containsWord("word3"));
        assertFalse(sites.get(1).containsWord("word2"));

        assertEquals("http://page1.com", sites.get(0).getUrl());
        assertEquals("http://page2.com", sites.get(1).getUrl());


    }
    @Test
    void parseBadFile() {
        List<Website> sites = FileHelper.parseFile("test-resources/test-file-with-errors.txt");

        assertEquals(2, sites.size());

        assertEquals("title1", sites.get(0).getTitle());
        assertEquals("title2", sites.get(1).getTitle());

        assertTrue(sites.get(0).containsWord("word1"));
        assertTrue(sites.get(0).containsWord("word2"));
        assertFalse(sites.get(0).containsWord("word3"));

        assertTrue(sites.get(1).containsWord("word1"));
        assertTrue(sites.get(1).containsWord("word3"));
        assertFalse(sites.get(1).containsWord("word2"));

        assertEquals("http://page1.com", sites.get(0).getUrl());
        assertEquals("http://page2.com", sites.get(1).getUrl());


    }
}