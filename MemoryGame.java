package Memory;

import javax.swing.*;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class MemoryGame {

    private final static String IMAGES_JUGI = "images_pack_special";
    private final static String IMAGES_NORMAL = "images_pack_normal";
    final private String iconPath = "assets\\put_logo.png";

    // Constructor types
    private String IMAGE_FOLDER;
    private GameType GAME_TYPE;
    private int CARDS;
    private String USERNAME;
    private ImagePack IMAGEPACK;
    
    private int numClicks;
    private int oddClickIndex;
    private int currentIndex;
    private long startClock;
    private int cardsLeft;
    private int playerTurn = 0;
    private int p1Points;
    private int p2Points;
    private boolean finished = false;

    private static final String TIME_FORMAT = "%02d:%02d";
    private final int MAINTHREAD_SLEEP = 1000; // 1-second change

    private final int CHECKCARDS_SLEEP = 1000;
    private final int AI_SLEEP = 1000;
    private final int MAX_CARDS_PER_COLUMN = 3;
    private final String TITLE = "Memory Game";
    private final String IMAGE_PREFIX = "image";
    private final String EXTENSION = ".png";

    private int COLUMNS;
    private String FILES[];
    private String BACK_IMAGE;
    private ScoreDatabase scoreDatabase;

    private Card cards[];
    private ImageIcon backIcon;
    private ImageIcon icons[];

    private Timer cardsTimer;
    private Timer mainClock;
    private Timer AI;
    private Random random;

    // Components
    private JFrame mainFrame;
    private JPanel topPanel;
    private JPanel topLeftPanel;
    private JPanel topRightPanel;
    private JPanel topMainPanel;
    private JLabel mainTextLabel;
    private JPanel bottomPanel;
    private JLabel player1Label;
    private JLabel player2Label;
    private JLabel player1Points;
    private JLabel player2Points;
    private JMenuBar topMenuBar;
    private JMenu options;
    private JMenuItem newGame;
    private JMenuItem quit;
    private JMenu scoresMenu;
    private JMenuItem scores;
    private JLabel turnPlayer1;
    private JLabel turnPlayer2;
    private ImageIcon turnReady;
    private ImageIcon turnStop;

    static Font globalFont;

    MemoryGame(String userName, GameType gameType, int CARDS, ImagePack imagePack) throws InterruptedException {
        this.USERNAME = userName;
        this.CARDS = CARDS;
        this.GAME_TYPE = gameType;
        this.IMAGEPACK = imagePack;


        initFields();
        initComponents();
        scoreDatabase.readDatabase();

        mainFrame.setTitle(TITLE + " - " + USERNAME);
        mainFrame.setIconImage(new ImageIcon(iconPath).getImage());
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        mainTextLabel.setText("Time: " + String.format(TIME_FORMAT, 0, 0));
        //mainTextLabel.setFont(globalFont);

        // Options menu bar
        newGame.addActionListener(new menuListener());
        quit.addActionListener(new menuListener());

        options.add(newGame);
        options.add(quit);
        scoresMenu.add(scores);

        topMenuBar.add(options);
        topMenuBar.add(scoresMenu);

        // Set alignment for players
        player1Label.setHorizontalAlignment(SwingConstants.CENTER);
        //player1Label.setFont(globalFont);
        player2Label.setHorizontalAlignment(SwingConstants.CENTER);
        //player2Label.setFont(globalFont);

        // Top Panel settings with left - mid - right panels
        topPanel.setLayout(new GridLayout());
        topPanel.setBorder(new EmptyBorder(20, 0, 20, 0));

        topMainPanel.add(mainTextLabel);

        topLeftPanel.add(turnPlayer1);
        topLeftPanel.add(player1Label);
        topLeftPanel.add(player1Points);

        topRightPanel.add(player2Label);
        topRightPanel.add(player2Points);
        topRightPanel.add(turnPlayer2);

        //player1Points.setFont(globalFont);
        //player2Points.setFont(globalFont);

        topPanel.add(topLeftPanel);
        topPanel.add(topMainPanel);
        topPanel.add(topRightPanel);

        // Add to JFrame
        mainFrame.add(topPanel, BorderLayout.NORTH);
        mainFrame.setJMenuBar(topMenuBar);

        // Main panel to hold cards
        bottomPanel.setLayout(new GridLayout(MAX_CARDS_PER_COLUMN, COLUMNS));
        mainFrame.add(bottomPanel);
        mainFrame.addWindowListener(new WindowCloseListener());

        initCards();
        cardsLeft = CARDS;

        // Frame visibility
        mainFrame.setLocationByPlatform(true);
        mainFrame.setResizable(false);
        mainFrame.pack();
        mainFrame.setVisible(true);

        scores.addActionListener(e -> {
            if(e.getSource() == scores) {
                new ScorePage(scoreDatabase.getScoreDatabase());
            }
        });

        // Timers
        cardsTimer = new Timer(CHECKCARDS_SLEEP, new TimerListener());
        mainClock = new Timer(MAINTHREAD_SLEEP, new MainClockListener());
        AI = new Timer(AI_SLEEP, new AIListener());
    }

    private String[] initFiles(int files) {
        String[] fileTable = new String[files];
        for(int i = 0; i < files; ++i) {
            String file = IMAGE_FOLDER + "/" + IMAGE_PREFIX + i + EXTENSION;
            fileTable[i] = file;
        }
        return fileTable;
    }

    public void initComponents() {
        mainFrame = new JFrame();
        topPanel = new JPanel();
        mainTextLabel = new JLabel();
        topMenuBar = new JMenuBar();
        options = new JMenu("Options");
        newGame = new JMenuItem("New Game");
        quit = new JMenuItem("Quit");
        topLeftPanel = new JPanel();
        topRightPanel = new JPanel();
        topMainPanel = new JPanel();
        bottomPanel = new JPanel();
        backIcon = new ImageIcon(BACK_IMAGE);
        cards = new Card[CARDS];
        icons = new ImageIcon[CARDS];
        player1Label = new JLabel((GAME_TYPE == GameType.SOLO ? "" : USERNAME + " |"));
        player2Label = new JLabel((GAME_TYPE == GameType.SOLO ? "" : "Player 2 |"));
        player1Points = new JLabel((GAME_TYPE == GameType.SOLO ? "" : "Points: 0"));
        player2Points = new JLabel((GAME_TYPE == GameType.SOLO ? "" : "Points: 0"));
        turnReady = new ImageIcon("assets\\ready.png");
        turnStop = new ImageIcon("assets\\stop.png");
        turnPlayer1 = new JLabel(turnReady);
        turnPlayer2 = new JLabel(turnStop);
        scoresMenu = new JMenu("Scores");
        scores = new JMenuItem("Scores");
        scoreDatabase = new ScoreDatabase();
        random = new Random();
        globalFont = new Font("Dialog", Font.BOLD, 16);

        if(GAME_TYPE == GameType.SOLO) {
            turnPlayer1.setVisible(false);
            turnPlayer2.setVisible(false);
        }
    }

    public void initFields() {

        switch(IMAGEPACK) {
            case NORMAL -> IMAGE_FOLDER = IMAGES_NORMAL;
            case SPECIAL -> IMAGE_FOLDER = IMAGES_JUGI;
        }

        COLUMNS = (int)Math.ceil((float) CARDS / MAX_CARDS_PER_COLUMN);
        BACK_IMAGE = IMAGE_FOLDER + "/" + "backImage" + EXTENSION;
        FILES = initFiles(CARDS / 2);
    }

    // Load cards' pictures
    private void initCards() {
        for(int i = 0, j = 0; i < FILES.length; i++) {

            // Create and setup new icon image and add to bottom panel
            icons[j] = new ImageIcon(FILES[i]);
            setUpCard(j);
            bottomPanel.add(cards[j++].getButton());

            // The next one is the same cause we have 2 same cards
            icons[j] = icons[j - 1];
            setUpCard(j);
            bottomPanel.add(cards[j++].getButton());
        }
        Collections.shuffle(Arrays.asList(cards));
    }

    // Single Card setup function
    private void setUpCard(int index) {
        cards[index] = new Card();

        // CardButton properties
        JButton cardButton = new JButton("");
        cardButton.setBorderPainted(false);
        cardButton.setContentAreaFilled(false);
        cardButton.setFocusPainted(false);

        cards[index].setButton(cardButton);
        cards[index].getButton().addActionListener(new ImageButtonListener());
        cards[index].getButton().setIcon(backIcon);
    }

    // AI Timer Listener
    private class AIListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            // Prevent player from switching
            cardsTimer.start();

            playerTurn %= 2;

            if (GAME_TYPE == GameType.AI && playerTurn != 0 && cardsLeft != 0) {

                currentIndex = getRandomNumber(CARDS);

                while (!cards[currentIndex].isPlayable()) {
                    currentIndex = getRandomNumber(CARDS);
                }

                oddClickIndex = currentIndex;

                while (oddClickIndex == currentIndex || !cards[oddClickIndex].isPlayable()) {
                    oddClickIndex = getRandomNumber(CARDS);
                }


                // Set open icons
                cards[currentIndex].getButton().setIcon(icons[currentIndex]);
                cards[oddClickIndex].getButton().setIcon(icons[oddClickIndex]);

                // Check if matched
                if (icons[currentIndex] == icons[oddClickIndex]) {
                    cards[currentIndex].setOpen(true);
                    cards[oddClickIndex].setOpen(true);
                    cardsLeft -= 2;
                }
                AI.stop();
            }
        }
    }

    // Embedded class for WindowClose Listener
    private class WindowCloseListener extends WindowAdapter {
        @Override
        public void windowClosing(WindowEvent e) {
            int confirmed = JOptionPane.showConfirmDialog(mainFrame,
                    "Quit game?", "Quit menu", JOptionPane.YES_NO_OPTION);

            if (confirmed == JOptionPane.YES_OPTION) {
                mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            } else {
                mainFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            }
        }
    }

    public int getRandomNumber(int cards) {
        return random.nextInt(cards);
    }

    // Embedded classes for flipTime listener
    private class TimerListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            boolean scored = false;

            playerTurn %= 2;
            if(playerTurn == 0) {
                turnPlayer2.setIcon(turnReady);
                turnPlayer1.setIcon(turnStop);
            } else {
                turnPlayer1.setIcon(turnReady);
                turnPlayer2.setIcon(turnStop);
            }

            if(cardsLeft == 0) {
                finished = true;
                AI.stop();
                cardsTimer.stop();
            }

            if(!cards[currentIndex].isOpen()) {
                cards[currentIndex].getButton().setIcon(backIcon);
            }

            if(!cards[oddClickIndex].isOpen()) {
                cards[oddClickIndex].getButton().setIcon(backIcon);
            }

            if(icons[currentIndex] == icons[oddClickIndex]) {
                cards[oddClickIndex].getButton().setEnabled(false);
                cards[currentIndex].getButton().setEnabled(false);
                cards[oddClickIndex].setPlayable(false);
                cards[currentIndex].setPlayable(false);
                scored = true;
            }

            if(scored && GAME_TYPE == GAME_TYPE.SOLO) {
                p1Points++;
            }

            if(scored && (GAME_TYPE == GAME_TYPE.VERSUS || GAME_TYPE == GAME_TYPE.AI)) {
                // Player's turn

                if(playerTurn == 0) {
                    p1Points++;
                    player1Points.setText("Points: " + p1Points);
                } else {
                    p2Points++;
                    player2Points.setText("Points: " + p2Points);
                }
            }

            playerTurn++;

            cardsTimer.stop();
        }
    }

    // Embedded classes for menu listener
    private class menuListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if(e.getSource() == quit) {
                int res = JOptionPane.showConfirmDialog(null,
                        "Quit game?", "Quit menu", JOptionPane.YES_NO_OPTION);
                if(res == JOptionPane.YES_OPTION) {
                    mainFrame.dispose();
                    System.exit(0);
                }
            }
            if(e.getSource() == newGame) {
                System.out.println("New Settings Panel");
                mainFrame.removeAll();
                mainFrame.dispose();
                new SettingsPage(USERNAME);
            }
        }
    }


    // Embedded class for ImageButtonListener
    private class ImageButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            if(AI.isRunning()) {
                return;
            }

            // Start mainTimer
            if(numClicks == 0) {
                startClock = System.currentTimeMillis();
                mainClock.start();
            }

            // If flip event happening
            if(cardsTimer.isRunning()) {
                return;
            }
            numClicks++;

            // Check actions for Cards' buttons
            for(int i = 0; i < CARDS; i++) {
                if(e.getSource() == cards[i].getButton()) {
                    cards[i].getButton().setIcon(icons[i]);
                    currentIndex = i;
                }
            }

            // Check for even distribution of clicks
            if(numClicks % 2 == 0) {

                // Prevents double click action
                if (currentIndex == oddClickIndex) {
                    numClicks--;
                    return;
                }

                // Check for the same card
                if(icons[currentIndex] == icons[oddClickIndex]) {
                    cards[currentIndex].setOpen(true);
                    cards[oddClickIndex].setOpen(true);
                    cardsLeft -= 2;
                }

                cardsTimer.start();

                if(GAME_TYPE == GameType.AI) {
                    AI.start();
                }

            } else {
                oddClickIndex = currentIndex;
            }
        }
    }

    private class MainClockListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {

            long currentClock = System.currentTimeMillis();
            long minutes = TimeUnit.MILLISECONDS.toMinutes(currentClock - startClock) % 60;
            long seconds = TimeUnit.MILLISECONDS.toSeconds(currentClock - startClock) % 60;

            TimeScore score = new TimeScore(minutes, seconds);
            mainTextLabel.setText("Time: " + score);

            if(finished) {
                mainClock.stop();
                String message = "";

                if(GAME_TYPE == GAME_TYPE.VERSUS || GAME_TYPE == GAME_TYPE.AI) {
                    scoreDatabase.addToDatabase(USERNAME, null, null);
                    if(p2Points == p1Points) {
                        message = "It's a draw!";
                        mainTextLabel.setText(message);

                    } else {
                        message = (p1Points > p2Points ? "You won!" : "Player 2 won!");
                        mainTextLabel.setText(message);
                    }
                }
                else {
                    message = "You finished in " + score + " - nice!";
                    mainTextLabel.setText(message);
                    if(CARDS == 12) {
                        scoreDatabase.addToDatabase(USERNAME, score, null);
                    }
                    if(CARDS == 24) {
                        scoreDatabase.addToDatabase(USERNAME, null, score);
                    }
                }
                JOptionPane.showMessageDialog(mainFrame, message, "Congratz!", JOptionPane.INFORMATION_MESSAGE);
                scoreDatabase.safeDatabaseToFile();
            }
        }
    }
}
