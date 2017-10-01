package org.jgo.testalgo.timestamp;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;


public class Timestamp implements Comparable<Timestamp> {

    public static final String TIMESTAMP_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static final ZoneOffset ZONE_OFFSET = ZoneOffset.UTC;

    private long epochSecond;

    /**
     * Create a timestamp from a datetime string
     * @param datetimeStr the datetime string, with format yyyy-MM-dd HH:mm:ss
     */
    public Timestamp(String datetimeStr) {
        LocalDateTime ldt = LocalDateTime.parse(datetimeStr, DateTimeFormatter.ofPattern(TIMESTAMP_PATTERN));
        this.epochSecond = ldt.toEpochSecond(ZONE_OFFSET);
    }

    /**
     * Create a timestamp from a LocalDateTime object
     * @param ldt the LocalDateTime to transform to a Timestamp
     */
    public Timestamp(LocalDateTime ldt) {
        this.epochSecond = ldt.toEpochSecond(ZONE_OFFSET);
    }

    /**
     * Retrieve epochSecond associated with timestamp
     * @return epochSecond, as a long
     */
    public long getEpochSecond() {
        return epochSecond;
    }


    @Override
    public int compareTo(Timestamp t) {
        return Long.compare(this.epochSecond, t.epochSecond);
    }


    @Override
    public String toString() {
        return LocalDateTime.ofEpochSecond(this.epochSecond, 0, ZONE_OFFSET).toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Timestamp timestamp = (Timestamp) o;

        return epochSecond == timestamp.epochSecond;
    }

    @Override
    public int hashCode() {
        return (int) (epochSecond ^ (epochSecond >>> 32));
    }
}
