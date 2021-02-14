package Memory;

public class Score {
    private TimeScore timeScore12; 
    private TimeScore timeScore24;
    private int playedTimes;

    @Override
    public String toString() {
        if(timeScore24 == null && timeScore12 == null) {
            return (String) ":;:;" + playedTimes;
        }
        if(timeScore24 == null) {
            return (String) timeScore12.toString() + ";" + ":" + ";" + playedTimes;
        }
        if(timeScore12 == null) {
            return (String) ":" + ";" + timeScore24.toString() + ";" + playedTimes;
        }

        return (String) timeScore12.toString() + ";" + timeScore24.toString() + ";" + playedTimes;
    }

    public String getScore12() {
        if(timeScore12 == null) {
            return "None";
        }

        return (String) timeScore12.toString();
    }

    public String getScore24() {
        if(timeScore24 == null) {
            return "None";
        }
        return (String) timeScore24.toString();
    }

    public int getPlayedTimes() {
        return playedTimes;
    }

    public Score() {
        this.timeScore12 = null;
        this.timeScore24 = null;
        this.playedTimes = 0;
    }

    public String toDisplay() {
        if(timeScore24 == null && timeScore12 == null) {
            return (String) "None | None |" + playedTimes;
        }
        if(timeScore24 == null) {
            return (String) timeScore12.toString() + " | " + "None" + " | " + playedTimes;
        }
        if(timeScore12 == null) {
            return (String) "None" + " | " + timeScore24.toString() + " | " + playedTimes;
        }

        return (String) timeScore12.toString() + " | " + timeScore24.toString() + " | " + playedTimes;
    }

    public Score(TimeScore score12, TimeScore score24, int playedTimes) {
        this.timeScore12 = score12;
        this.timeScore24 = score24;
        this.playedTimes = playedTimes;
    }

    public void checkBestScore12(TimeScore newScore) {
        if(newScore == null) {
            return;
        }
        if(timeScore12 == null) {
            timeScore12 = newScore;
            return;
        }

        if(newScore.lessThan(timeScore12)) {
            timeScore12 = newScore;
        }
    }

    public void checkBestScore24(TimeScore newScore) {
        if(newScore == null) {
            return;
        }

        if(timeScore24 == null) {
            timeScore24 = newScore;
            return;
        }

        if(newScore.lessThan(timeScore24)) {
            timeScore24 = newScore;
        }
    }

    public void addPlayedTimes() {
        this.playedTimes++;
    }
}
