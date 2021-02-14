package Memory;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ScorePage extends JFrame {

    private static final int HEIGHT = 400;
    private static final int WIDTH = 400;
    private final String iconPath = "assets\\put_logo.png";

    public ScorePage(HashMap<String, Score> scoreDatabase) {

        this.setTitle("Best scores");
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setResizable(false);
        this.setSize(WIDTH, HEIGHT);
        this.setIconImage(new ImageIcon(iconPath).getImage());

        JPanel panel = new JPanel(new GridLayout(0, 4, 5, 5));
        JScrollPane scrollPane = new JScrollPane(panel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        this.add(scrollPane, BorderLayout.CENTER);

        JLabel usernameLabel = new JLabel("Username");
        JLabel time12Label = new JLabel("Time 12");
        JLabel time24Label = new JLabel("Time 24");
        JLabel playedTimesLabel = new JLabel("Played Times");

        JPanel x1 = new JPanel();
        JPanel x2 = new JPanel();
        JPanel x3 = new JPanel();
        JPanel x4 = new JPanel();

        x1.add(usernameLabel);
        x2.add(time12Label);
        x3.add(time24Label);
        x4.add(playedTimesLabel);

        panel.add(x1);
        panel.add(x2);
        panel.add(x3);
        panel.add(x4);

        Iterator it = scoreDatabase.entrySet().iterator();
        while(it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            String username = (String) pair.getKey();
            Score score = (Score) pair.getValue();

            JPanel p1 = new JPanel();
            JPanel p2 = new JPanel();
            JPanel p3 = new JPanel();
            JPanel p4 = new JPanel();

            String score12 = score.getScore12();
            String score24 = score.getScore24();
            int playedTimes = score.getPlayedTimes();

            JLabel usernameL = new JLabel(username);
            JLabel score12L = new JLabel(score12);
            JLabel score24L = new JLabel(score24);
            JLabel playedTimesL = new JLabel(String.valueOf(playedTimes));

            p1.add(usernameL);
            p2.add(score12L);
            p3.add(score24L);
            p4.add(playedTimesL);

            panel.add(p1);
            panel.add(p2);
            panel.add(p3);
            panel.add(p4);
        }

        if(scoreDatabase.size() < 10) {
            this.pack();
        }

        this.setLocationByPlatform(true);
        this.setVisible(true);
    }
}
