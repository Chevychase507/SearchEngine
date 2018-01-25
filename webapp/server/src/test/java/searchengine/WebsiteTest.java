package searchengine;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by maks on 17/10/17.
 */
class WebsiteTest {
    @Test
    void getTitle() {
        List<String> testList = new ArrayList<>();
        testList.add("foo");
        Website w = new Website("www.foo.bar","foobar", testList);

        assertEquals("foobar", w.getTitle());
    }

    @Test
    void getUrl() {
        List<String> testList = new ArrayList<>();
        testList.add("foo");
        Website w = new Website("www.foo.bar","foobar", testList);

        assertEquals("www.foo.bar", w.getUrl());
    }

    @Test
    void containsWord() {
        List<String> testList = new ArrayList<>();
        testList.add("foo");
        Website w = new Website("www.foo.bar","foobar", testList);

        assertTrue(w.containsWord("foo"));

    }

    @Test
    void getListWord() {

        List<String> testList = new ArrayList<>();
        testList.add("foo");
        testList.add("bar");
        Website w = new Website("www.foo.bar","foobar", testList);

        assertEquals(testList, w.getListWord());
    }

}