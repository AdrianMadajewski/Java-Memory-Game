package Memory;

public class TimeScore implements Comparable<TimeScore> {
    long minutes;
    long seconds;

    private static final String TIME_FORMAT = "%02d:%02d";

    public TimeScore(long minutes, long seconds) {
        this.minutes = minutes;
        this.seconds = seconds;
    }

    @Override
    public String toString() {
        return String.format(TIME_FORMAT, minutes, seconds);
    }

    @Override
    public int compareTo(TimeScore o) {
        // < 0 - other < this
        // > 0 - this > other
        // = 0 - same
        int sec1 = (int) (this.minutes * 60 + this.seconds);
        int sec2 = (int) (o.minutes * 60 + o.seconds);

        return sec1 - sec2;
    }

    public boolean lessThan(TimeScore o) {
        return compareTo(o) < 0;
    }

    public boolean greaterThan(TimeScore o) {
        return compareTo(o) > 0;
    }

    public boolean equal(TimeScore o) {
        return compareTo(o) == 0;
    }
}
