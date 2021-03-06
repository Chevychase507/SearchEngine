package searchengine;


import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

/**
 * The main class of our search engine program.
 *
 * @author Martin Aumüller
 * @author Leonid Rusnac
 */
@Configuration
@EnableAutoConfiguration
@Path("/")
public class SearchEngine extends ResourceConfig {
    private static QueryHelper queryHelper;
    private static SentimentMeasure sm;


    public SearchEngine() {
        packages("searchengine");
    }

    /**
     * The main method of our search engine program.
     * Expects exactly one argument being provided. This
     * argument is the filename of the file containing the
     * websites.
     *
     * @param args command line arguments.
     */
    public static void main(String[] args) {
        System.out.println("Welcome to the SearchEngine!");

        if (args.length != 1) {
            System.out.println("Error: Filename is missing");
            return;
        }

        Index idx = new InvertedIndexHashMap();
        queryHelper = new QueryHelper(idx, new BM25Score());
        long t1 = System.nanoTime();
        List<Website> sites = FileHelper.parseFile(args[0]);
        idx.build(sites);
        long t2 = System.nanoTime();
        System.out.println("Processing the data set and building the index took " +
                (t2 - t1) / 10e6 + " milliseconds.");


        sm = new SentimentMeasure(sites);


        // run the search engine
        SpringApplication.run(SearchEngine.class);
    }

    /**
     * This methods handles requests to GET requests at search.
     * It assumes that a GET request of the form "search?query=word" is made.
     * @param response Http response object
     * @param query the query string
     * @return the list of websites matching the query
     */

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("search")
    public List<Website> search(@Context HttpServletResponse response, @QueryParam("query") String query) {
        // Set crossdomain access. Otherwise your browser will complain that it does not want
        // to load code from a different location.
        response.setHeader("Access-Control-Allow-Origin", "*");

        if (query == null) {
            return new ArrayList<Website>();
        }

        String line = query;

        // Get and set the sentiment for each of the website objects in the result list

        System.out.println("Handling request for query word \"" + query + "\"");

        List<Website> resultList = queryHelper.getMatchingWebsites(line);

        resultList.forEach(w -> w.setSentiment(sm.getSentiment(w.getUrl())));

        System.out.println("Found " + resultList.size() + " websites.");
        return resultList;
    }
}
