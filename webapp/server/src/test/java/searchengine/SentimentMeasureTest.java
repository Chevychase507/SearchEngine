package searchengine;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SentimentMeasureTest {
    @Test
    void getSentiment() {
        List<Website> websites = new ArrayList<>();

        SentimentMeasure sm = new SentimentMeasure(websites);

        assertEquals(-0.8, sm.getSentiment("https://en.wikipedia.org/wiki/Marilyn_Monroe"), 0.01);

        assertEquals(-2.0, sm.getSentiment("foobar.foo"), 0.01);

        assertEquals(-2.0, sm.getSentiment(null), 0.01);

    }

}