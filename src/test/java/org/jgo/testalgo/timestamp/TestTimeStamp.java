package org.jgo.testalgo.timestamp;

import org.junit.Test;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import static org.junit.Assert.*;

public class TestTimeStamp {

    @Test
    public void testConstructorStr() {
        // Prepare
        String datetimeStr = "2012-08-07 12:09:08";
        LocalDateTime ldt = LocalDateTime.parse(datetimeStr, DateTimeFormatter.ofPattern(Timestamp.TIMESTAMP_PATTERN));

        // Act
        Timestamp ts = new Timestamp(datetimeStr);

        // Assert
        assertEquals(ldt.toEpochSecond(Timestamp.ZONE_OFFSET), ts.getEpochSecond());
    }

    @Test
    public void testConstructorLdt() {
        // Prepare
        String datetimeStr = "2012-08-07 12:09:08";
        LocalDateTime ldt = LocalDateTime.parse(datetimeStr, DateTimeFormatter.ofPattern(Timestamp.TIMESTAMP_PATTERN));

        // Act
        Timestamp ts = new Timestamp(ldt);

        // Assert
        assertEquals(ldt.toEpochSecond(Timestamp.ZONE_OFFSET), ts.getEpochSecond());
    }


    @Test
    public void testHashCode() {
        // Prepare
        String datetimeStr = "2012-08-07 12:09:08";

        // Act
        Timestamp ts1 = new Timestamp(datetimeStr);
        Timestamp ts2 = new Timestamp(datetimeStr);

        // Assert
        assertEquals(ts1, ts2);
    }

    @Test
    public void testHashCodeInMap() {
        // Prepare
        String datetimeStr = "2012-08-07 12:09:08";
        Timestamp ts1 = new Timestamp(datetimeStr);
        Timestamp ts2 = new Timestamp(datetimeStr);
        Map<Timestamp, String> map = new HashMap();

        // Act
        map.put(ts1, "somestring");
        map.put(ts2, "somestring");

        // Assert
        assertEquals(1, map.size());
    }

    @Test
    public void testEquals() {
        // Prepare
        String datetimeStr = "2012-08-07 12:09:08";

        // Act
        Timestamp ts1 = new Timestamp(datetimeStr);
        Timestamp ts2 = new Timestamp(datetimeStr);

        // Assert
        assertEquals(ts1, ts2);
    }

    @Test
    public void testOrderingInSortedMap() {
        // Prepare
        String datetimeStr1 = "2012-08-07 12:09:08";
        String datetimeStr2 = "2012-08-07 12:09:09";
        Timestamp ts1 = new Timestamp(datetimeStr1);
        Timestamp ts2 = new Timestamp(datetimeStr2);

        // Act
        TreeMap map = new TreeMap<Timestamp, String>();
        map.put(ts2, "somestuff");
        map.put(ts1, "somestuff");

        // Assert
        assertEquals(map.firstEntry().getKey(), ts1);
        assertEquals(map.lastEntry().getKey(), ts2);
    }
}
