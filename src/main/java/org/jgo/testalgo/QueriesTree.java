package org.jgo.testalgo;

import org.jgo.testalgo.timestamp.Timestamp;
import org.jgo.testalgo.timestamp.TimestampInterval;
import org.jgo.testalgo.timestamp.TimestampParseException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

/**
 * QueriesTree uses the red-black tree TreeMap implementation.
 * Each node key is a Long corresponding to a timestamp epoch, and the value is a map of query:count for this timestamp
 */
public class QueriesTree extends TreeMap<Timestamp, HashMap<String, Integer>> {

    /**
     * Creates a QueriesTree based on a NavigableMap, like a TreeMap
     * @param tree a tree of class NavigableMap
     */
    public QueriesTree(NavigableMap<Timestamp, HashMap<String, Integer>> tree) {
        super(tree);
    }

    /**
     * Create a QueriesTree from a tsv file
     * @param tsvPath full path to tsv file
     * @throws IOException exception with reading tsv file
     * @throws TimestampParseException exception with timestamp parsing
     */
    public QueriesTree(String tsvPath) throws IOException, TimestampParseException {

        Files.lines(Paths.get(tsvPath)).forEach(line -> {
            String[] lineElements = line.split("\\s");
            Timestamp timestamp = new Timestamp(lineElements[0] + " "+ lineElements[1]);
            String query = lineElements[2];

            HashMap<String, Integer> queriesForTimestamp = this.getOrDefault(timestamp, new HashMap<>());
            int queryCount = queriesForTimestamp.getOrDefault(query, 0);
            queriesForTimestamp.put(query, ++queryCount);

            this.put(timestamp, queriesForTimestamp);
        });
    }


    /**
     * Finds all nodes for the passed interval, and iterates on each node. Uses a set to store and count unique queries.
     * @param timestampInterval the timestamp interval on which to count the queries
     * @return the number of unique queries on the timestamp interval
     */
    public Integer countQueries(TimestampInterval timestampInterval) {

        Set<String> uniqueQueries = new HashSet<>();

        // Get only nodes that are in the timestamp interval
        QueriesTree subTree = new QueriesTree(
                this.subMap(timestampInterval.getStartTimestamp(),true,
                timestampInterval.getEndTimestamp(), true));

        // Iterate on all nodes and populate the set to have unique queries
        subTree.forEach((timestamp, queryAndCount) -> {
            queryAndCount.forEach((query, count) -> {
                uniqueQueries.add(query);
            });
        });
        return uniqueQueries.size();
    }

    /**
     * Finds all nodes for the passed interval, and iterates on each node. Builds a hashmap to aggregate counts for each
     * query, and then sort by count, and keep top n entries.
     * @param timestampInterval the timestamp interval on which to get queries
     * @param size the number of queries to return
     * @return a map of {query:count}
     */
    public Map<String, Integer> getPopularQueries(TimestampInterval timestampInterval, Integer size) {
        HashMap<String, Integer> allQueries = new HashMap<>();

        // Get only nodes that are in the timestamp interval
        QueriesTree subTree = new QueriesTree(
                this.subMap(timestampInterval.getStartTimestamp(),true,
                        timestampInterval.getEndTimestamp(), true));

        // Iterate on all nodes and populate a hashmap of queries with their total count
        subTree.forEach((timestamp, queryAndCount) -> {
            queryAndCount.forEach((query, count) -> {
                Integer previousCountForQuery = allQueries.getOrDefault(query, 0);
                allQueries.put(query, previousCountForQuery + count);
            });
        });

        // Create a linked hash map sorted by counts and limit to the top n (defined by the size argument) entries
        LinkedHashMap<String, Integer> topNQueries = allQueries.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Collections.reverseOrder()))
                .limit(size)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));

        return topNQueries;
    }


}

