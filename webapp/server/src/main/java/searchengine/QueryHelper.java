package searchengine;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * QueryHelper manages complex search strings and ranks the the matching websites according to their relevance
 *
 * @author All members of Group J
 */


public class QueryHelper {
    private Index idx;
    private Score score;

    public QueryHelper(Index idx, Score score) {
        this.idx = idx;
        this.score = score;
    }

    /**
     * Retrieves the websites matching the query string. Websites matching the query string
     * are ranked according to relevance by a given algorithm.
     * Searches can consist of single words, multiple words and multiple words split by an OR-operator.
     * @param queryString The query string passed web app - can contain the 'OR'-operator
     * @return A list of websites matching the query string in ranked order
     */

    public List<Website> getMatchingWebsites(String queryString) {
        List<Website> rankedSites;
        Map<Website, Double> foundAndRankedSites;
        String urlString = null;

        // finds urlString and cleans the query string if it contains "site:"
        if (queryString.contains("site:")) {
            urlString = findUrl(queryString);
            queryString = queryString.replaceAll("site:\\S+", "");
        }

        List<List<String>> nestedListOfQueries = new ArrayList<>();
        queryString = queryString.trim();

        // Splits query string into list of OR-searches
        List<String> queryList = new ArrayList<>(Arrays.asList(queryString.split("\\sOR")));

        // Converts strings to lowercase - method call inspired by a post on StackOverflow
        queryList = queryList.stream().map(String::toLowerCase).collect(Collectors.toList());

        // removes empty search words
        Iterator<String> it = queryList.iterator();
        while (it.hasNext()) {
            if (it.next().matches("\\s*")) {
                it.remove();
            }
        }

        // Splits into nested list of list(s) depending on how many OR searches has to be done
        for (String s : queryList) {
            s = s.trim();
            List<String> tempList = new ArrayList<>(Arrays.asList(s.split("\\s+")));
            nestedListOfQueries.add(tempList);
        }

        // Searches for websites matching the query and returns a map containing the matching websites
        foundAndRankedSites = searchAndRankWebsites(nestedListOfQueries);

        // Adds the websites to a List<Website> and sorts the List according to the score in descending order
        rankedSites = foundAndRankedSites.entrySet().stream().sorted((u, v) -> v.getValue().
                compareTo(u.getValue())).map(w -> w.getKey()).collect(Collectors.toList());

        // If the "site:" function is used, foundSites is filtered according to the url specified in the search.
        if (urlString != null) {
            return urlFilter(rankedSites, urlString);
        } else {
            return rankedSites;
        }
    }

    /** Takes a nested list of queries split by "OR" and performs a search for each query string.
     *  Ranks the websites according to a specified ranking algorithm
     * @param listOfQueries A nested list of lists of query strings
     * @return A map of websites and their ranking score
     */
    private Map<Website, Double> searchAndRankWebsites(List<List<String>> listOfQueries) {
        Map<Website, Double> searchResults = new HashMap<>();


        for (List<String> orQuery : listOfQueries) {

            // Conducts search on each list of queries
            Map<Website, Double> tempSearchResults = andSearch(orQuery);

            // Sums the subsets of returned websites to a total set of returned websites.
            for(Map.Entry<Website, Double> currentWebsiteEntry : tempSearchResults.entrySet()){
                if(!searchResults.containsKey(currentWebsiteEntry.getKey())){
                    searchResults.put(currentWebsiteEntry.getKey(), currentWebsiteEntry.getValue());
                }
                else if(currentWebsiteEntry.getValue() > searchResults.get(currentWebsiteEntry.getKey())) {
                    searchResults.replace(currentWebsiteEntry.getKey(), currentWebsiteEntry.getValue());
                }
            }
        }
        return searchResults;
    }


    /** Searches for websites based that contain all query words (AND-operation).
     *  Ranks the websites according to a specified ranking algorithm
     * @param queries A list of query words
     * @return A map of websites that contain all the query words and the score given to the websites
     */
    private  Map<Website, Double> andSearch(List<String> queries) {
        List<Website> foundSites = new ArrayList<>();

        // Adds all websites that contains the first query word
        foundSites.addAll(idx.lookup(queries.get(0)));

        // Removes found websites that doesn't contain the additional search words in the query
        for (int i = 1; i < queries.size(); i++) {
            // Intersection of lists - method call inspired by talk with member of Group F
            foundSites.retainAll(idx.lookup(queries.get(i)));
        }

        // Ranks sites and adds to map
        Map<Website, Double> rankedSites = new HashMap<>();
        for(Website website : foundSites){
            double tempScore = 0;
            // den summerer kun for alle words i querylist, men ikke for alle querylists i nestedListOfQueries
            for(String queryWord : queries){
                tempScore += score.getScore(queryWord, website, idx);
            }
            rankedSites.put(website, tempScore);
        }
        return rankedSites;
    }


    /**
     * Looks for the expression "site:" in the query string. If "site:" is present, the url following "site:" is
     * returned.
     * @param queryString the line of queries
     * @return A string containing the url after "site:"
     */

    private String findUrl(String queryString) {
        String urlString = null;
        Pattern pattern = Pattern.compile("site:\\s?(?<url>\\S+)");
        Matcher matcher = pattern.matcher(queryString);
        if(matcher.find()) urlString = matcher.group("url");
        return urlString;
    }


    /**
     * Iterates over the list of found websites and adds them to a new list of websites if the URLs the list of
     * found websites contains the url from the query.
     * @param foundSites the list of found websites
     * @param urlString the url which the URLs must contain
     * @return a list of websites, which URLs contains the url
     */

    private List<Website> urlFilter(List<Website> foundSites, String urlString) {
        List<Website> tempList = new ArrayList<>();
            for (Website w : foundSites) {
                if (w.getUrl().toLowerCase().contains(urlString.toLowerCase())) {
                    tempList.add(w);
                }
            }
            foundSites = tempList;
        return foundSites;
    }
}



