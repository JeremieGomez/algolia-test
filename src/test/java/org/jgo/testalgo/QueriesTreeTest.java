package org.jgo.testalgo;

import org.jgo.testalgo.timestamp.Timestamp;
import org.jgo.testalgo.timestamp.TimestampInterval;
import org.jgo.testalgo.timestamp.TimestampParseException;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;

public class QueriesTreeTest {

    QueriesTree tree;
    Timestamp timestamp;
    TimestampInterval tsi;

    @Before
    public void createTree() throws IOException, TimestampParseException {
        tree = new QueriesTree(ClassLoader.getSystemResource("sample.tsv").getPath());
        timestamp = new Timestamp("2015-08-01 00:04:01");
        tsi = new TimestampInterval("2015-08-01 00:04");
    }

    @Test
    public void testConstructorNumberOfNodes() throws IOException, TimestampParseException {
        assertEquals(6, tree.size());
    }

    @Test
    public void testConstructorNumberOfQueries() {
        // 7 without duplicates
        assertEquals(7, tree.get(timestamp).size());
    }

    @Test
    public void testSecondConstructor() {
        // Act
        QueriesTree subQueriesTree = new QueriesTree(
                tree.subMap(tsi.getStartTimestamp(), true, tsi.getEndTimestamp(), true));

        // Assert
        assertEquals(5, subQueriesTree.size());
        assertEquals(7, tree.get(timestamp).size());
    }

    @Test(expected = DateTimeParseException.class)
    public void testWrongConstruction() throws IOException, TimestampParseException {
        new QueriesTree(ClassLoader.getSystemResource("sample_wrong.tsv").getPath());
    }

    @Test
    public void testCount() {
        // 10 without duplicates
        // Act
        Integer count = tree.countQueries(tsi);

        // Assert
        assertEquals(new Integer(10), count);
    }

    @Test
    public void testPopular() {
        // Prepare
        Map<String, Integer> expectedResults = new HashMap<>();
        expectedResults.put("dup", 4);
        expectedResults.put("%22http%3A%2F%2Fwww.npr.org%2F2015%2F07%2F15%2F423203008%2Fcartel-author", 3);
        expectedResults.put("https%3A%2F%2Fphoenix.craigslist.org%2Fsearch%2Ffoa%3Fs%3D400", 2);

        // Act
        Map<String, Integer> results = tree.getPopularQueries(tsi, 3);

        // Assert
        assertEquals(new Integer(4), results.get("dup"));
        assertEquals(new Integer(3), results.get("%22http%3A%2F%2Fwww.npr.org%2F2015%2F07%2F15%2F423203008%2Fcartel-author"));
        assertEquals(new Integer(2), results.get("https%3A%2F%2Fphoenix.craigslist.org%2Fsearch%2Ffoa%3Fs%3D400"));
    }
}
