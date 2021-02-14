package Memory;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ScoreDatabase {
    private static final String databasePath = "database\\scores.txt";
    private static HashMap<String, Score> scoreDatabase;

    public HashMap<String, Score> getScoreDatabase() {
        return scoreDatabase;
    }

    public void addToDatabase(String userName, TimeScore score12, TimeScore score24) {
        Score currentScore = null;
        if (scoreDatabase.containsKey(userName)) {
            currentScore = scoreDatabase.get(userName);
            currentScore.checkBestScore12(score12);
            currentScore.checkBestScore24(score24);
        }
        if(currentScore == null) {
            currentScore = new Score(score12, score24, 0);
        }
        currentScore.addPlayedTimes();
        scoreDatabase.put(userName, currentScore);
    }

    public void readDatabase() {
        scoreDatabase = new HashMap<>();
        String line;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(databasePath));
            while ((line = reader.readLine()) != null)
            {
                String[] parts = line.split(";", 4);
                String username = parts[0];

                TimeScore t12 = null;
                TimeScore t24 = null;

                String[] partTime1 = parts[1].split(":", 2);

                // Check for empty split
                if(partTime1[0] != "" || partTime1[1] != "") {
                    int minutes1 = Integer.parseInt(partTime1[0]);
                    int seconds1 = Integer.parseInt(partTime1[1]);
                    t12 = new TimeScore(minutes1, seconds1);
                }

                String[] partTime2 = parts[2].split(":", 2);

                // Check for empty split
                if(partTime2[0] != "" || partTime2[1] != "") {
                    int minutes2 = Integer.parseInt(partTime2[0]);
                    int seconds2 = Integer.parseInt(partTime2[1]);
                    t24 = new TimeScore(minutes2, seconds2);
                }

                int playedTimes = Integer.parseInt(parts[3]);
                Score score = new Score(t12, t24, playedTimes);
                scoreDatabase.put(username, score);
            }
            reader.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            return;
        }
    }

    public void safeDatabaseToFile() {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(databasePath, false));
            Iterator it = scoreDatabase.entrySet().iterator();
            while(it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                String username = (String) pair.getKey();
                Score score = (Score) pair.getValue();
                bw.write(username);
                bw.write(";");
                bw.write(score.toString());
                bw.newLine();
            }
            bw.close();
        } catch(Exception ex) {
            ex.printStackTrace();
            return;
        }
    }
}
