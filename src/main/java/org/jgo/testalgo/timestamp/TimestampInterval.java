package org.jgo.testalgo.timestamp;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoField;
import java.util.Arrays;
import java.util.List;


public class TimestampInterval {

    private Timestamp startTimestamp;
    private Timestamp endTimestamp;

    /**
     * Create a TimestampInterval from a full timestamp (including seconds) or a partial timestamp
     * (from specifying a year only, up to specifying a particular minute)
     * @param datetimeStr, datetime in one of acceptable formats
     * @throws TimestampParseException parsing exception with a message
     */
    public TimestampInterval(String datetimeStr) throws TimestampParseException {

        List<String> acceptableFormats = Arrays.asList(
                "yyyy-MM-dd HH:mm:ss",
                "yyyy-MM-dd HH:mm",
                "yyyy-MM-dd HH",
                "yyyy-MM-dd",
                "yyyy-MM",
                "yyyy"
                );

        LocalDateTime ldt = null;
        for (String format: acceptableFormats) {
            try {
                DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                        .appendPattern(format)
                        .parseDefaulting(ChronoField.MONTH_OF_YEAR, 1)
                        .parseDefaulting(ChronoField.DAY_OF_MONTH, 1)
                        .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
                        .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
                        .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
                        .toFormatter();

                ldt = LocalDateTime.parse(datetimeStr, formatter);
                this.startTimestamp = new Timestamp(ldt);
                this.endTimestamp = new Timestamp(computeEndLdt(ldt, format));

            }
            catch (DateTimeParseException e) {
                continue;
            }
        }
        // If none of the supported formats could correctly parse the datime, throw an exception
        if (ldt == null) {
            throw new TimestampParseException("Could not parse " + datetimeStr);
        }

    }

    /**
     * Retrieve timestamp at beginning of interval
     * @return returns Timestamp object
     */
    public Timestamp getStartTimestamp() {
        return startTimestamp;
    }

    /**
     * Retrieve timestamp at end of interval
     * @return returns Timestamp object
     */
    public Timestamp getEndTimestamp() {
        return endTimestamp;
    }

    /**
     * Computes the end of the interval, given a localdatetime and a format
     * @param ldt
     * @param format
     * @return
     * @throws TimestampParseException
     */
    private LocalDateTime computeEndLdt(LocalDateTime ldt, String format) throws TimestampParseException {
        LocalDateTime endLdt;
        switch (format) {
            case "yyyy-MM-dd HH:mm:ss":
                endLdt = ldt;
                break;
            case "yyyy-MM-dd HH:mm":
                endLdt = ldt.plusSeconds(59);
                break;
            case "yyyy-MM-dd HH":
                endLdt = ldt.plusMinutes(59).plusSeconds(59);
                break;
            case "yyyy-MM-dd":
                endLdt = ldt.plusDays(1).minusSeconds(1);
                break;
            case "yyyy-MM":
                endLdt = ldt.plusMonths(1).minusSeconds(1);
                break;
            case "yyyy":
                endLdt = ldt.plusYears(1).minusSeconds(1);
                break;
            default:
                throw new TimestampParseException("Could not compute end datetime. Format unsupported: " + format);
        }
        return endLdt;
    }

    @Override
    public String toString() {
        return this.startTimestamp + " " + this.endTimestamp;
    }

}
