package org.jgo.testalgo.timestamp;

import org.junit.Test;
import static org.junit.Assert.*;

public class TestTimestampInterval {


    @Test
    public void testEndWithoutSeconds() throws TimestampParseException {
        // Prepare
        Timestamp startTs = new Timestamp("2015-08-01 00:03:00");
        Timestamp endTs = new Timestamp("2015-08-01 00:03:59");

        // Act
        TimestampInterval tsi = new TimestampInterval("2015-08-01 00:03");

        // Assert
        assertEquals(startTs, tsi.getStartTimestamp());
        assertEquals(endTs, tsi.getEndTimestamp());
    }

    @Test
    public void testEndWithoutMinutes() throws TimestampParseException {
        // Prepare
        Timestamp startTs = new Timestamp("2015-08-01 01:00:00");
        Timestamp endTs = new Timestamp("2015-08-01 01:59:59");

        // Act
        TimestampInterval tsi = new TimestampInterval("2015-08-01 01");

        // Assert
        assertEquals(startTs, tsi.getStartTimestamp());
        assertEquals(endTs, tsi.getEndTimestamp());
    }

    @Test
    public void testEndWithoutHours() throws TimestampParseException {
        // Prepare
        Timestamp startTs = new Timestamp("2015-08-01 00:00:00");
        Timestamp endTs = new Timestamp("2015-08-01 23:59:59");

        // Act
        TimestampInterval tsi = new TimestampInterval("2015-08-01");

        // Assert
        assertEquals(startTs, tsi.getStartTimestamp());
        assertEquals(endTs, tsi.getEndTimestamp());
    }

    @Test
    public void testEndWithoutDays() throws TimestampParseException {
        // Prepare
        Timestamp startTs = new Timestamp("2015-08-01 00:00:00");
        Timestamp endTs = new Timestamp("2015-08-31 23:59:59");

        // Act
        TimestampInterval tsi = new TimestampInterval("2015-08");

        // Assert
        assertEquals(startTs, tsi.getStartTimestamp());
        assertEquals(endTs, tsi.getEndTimestamp());
    }

    @Test
    public void testEndWithoutMonths() throws TimestampParseException {
        // Prepare
        Timestamp startTs = new Timestamp("2015-01-01 00:00:00");
        Timestamp endTs = new Timestamp("2015-12-31 23:59:59");

        // Act
        TimestampInterval tsi = new TimestampInterval("2015");

        // Assert
        assertEquals(startTs, tsi.getStartTimestamp());
        assertEquals(endTs, tsi.getEndTimestamp());
    }

    @Test(expected = TimestampParseException.class)
    public void testWrongFormat() throws TimestampParseException {
        TimestampInterval tsi = new TimestampInterval("2018:39");
    }
}
