package Memory;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SettingsPage extends JFrame implements ActionListener {
    final private static String TITLE = "Settings Panel";
    final private String iconPath = "assets\\put_logo.png";

    private JButton playButton;
    private JPanel mainPanel;
    private JLabel welcomeLabel;


    private JRadioButton deckType1;
    private JRadioButton deckType2;
    private JRadioButton gameTypeSolo;
    private JRadioButton gameTypeAI;
    private JRadioButton gameTypeVS;
    private JRadioButton cardNumber12;
    private JRadioButton cardNumber24;

    private ButtonGroup cardTypeGroup;
    private ButtonGroup gameTypeGroup;
    private ButtonGroup cardNumberGroup;

    private JLabel cardOneLabel;
    private JLabel cardTwoLabel;
    private JLabel cardOneIconLabel;
    private JLabel cardTwoIconLabel;
    private JLabel playError;

    private static ImageIcon cardOne = new ImageIcon("images_pack_normal/backImage.png");
    private static ImageIcon cardTwo = new ImageIcon("images_pack_special/backImage.png");

    private int cardsChosen;
    private GameType gameTypeChosen;
    private ImagePack imagePackChosen;

    boolean setDeckType = false;
    boolean setGameType = false;
    boolean setCardNumber = false;

    SettingsPage(String userName) {
        this.setTitle(TITLE + " - " + userName);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(600, 350);
        this.setIconImage(new ImageIcon(iconPath).getImage());

        cardTypeGroup = new ButtonGroup();
        cardTypeGroup.clearSelection();
        gameTypeGroup = new ButtonGroup();
        gameTypeGroup.clearSelection();
        cardNumberGroup = new ButtonGroup();
        cardNumberGroup.clearSelection();

        cardNumber12 = new JRadioButton("12 cards");
        cardNumber24 = new JRadioButton("24 cards");
        cardNumber12.setFocusPainted(false);
        cardNumber24.setFocusPainted(false);

        cardNumberGroup.add(cardNumber12);
        cardNumberGroup.add(cardNumber24);

        gameTypeSolo = new JRadioButton("Singleplayer");
        gameTypeAI = new JRadioButton("vs Computer");
        gameTypeVS = new JRadioButton("vs Player");
        gameTypeSolo.setFocusPainted(false);
        gameTypeAI.setFocusPainted(false);
        gameTypeVS.setFocusPainted(false);

        gameTypeGroup.add(gameTypeSolo);
        gameTypeGroup.add(gameTypeAI);
        gameTypeGroup.add(gameTypeVS);


        deckType1 = new JRadioButton("Standard Deck");
        deckType2 = new JRadioButton("Special Deck");
        deckType1.setFocusPainted(false);
        deckType2.setFocusPainted(false);

        cardTypeGroup.add(deckType1);
        cardTypeGroup.add(deckType2);

        playError = new JLabel("");

        welcomeLabel = new JLabel();
        welcomeLabel.setText("Welcome " + userName + "!");


        mainPanel = new JPanel();


        mainPanel.setBounds(0,0,450,199);


        playError = new JLabel("");
        playError.setBounds(20,280, 200, 15);

        cardTypeGroup = new ButtonGroup();
        cardTypeGroup.clearSelection();
        gameTypeGroup = new ButtonGroup();
        gameTypeGroup.clearSelection();
        cardNumberGroup = new ButtonGroup();
        cardNumberGroup.clearSelection();

        welcomeLabel = new JLabel();
        welcomeLabel.setBounds(45,220,100,15);
        welcomeLabel.setText("Welcome " + userName + "!");
        mainPanel.add(welcomeLabel);
        mainPanel.add(playError);

        playButton = new JButton("Play");
        playButton.setFocusPainted(false);
        playButton.setBounds(55,245,80,25);
        mainPanel.add(playButton);


        deckType1.setBounds(20,10,150,25);
        deckType2.setBounds(20,30,150,25);

        mainPanel.add(deckType1);
        mainPanel.add(deckType2);

        gameTypeSolo.setBounds(20,70,150,25);
        gameTypeAI.setBounds(20,90,150,25);
        gameTypeVS.setBounds(20,110,150,25);

        mainPanel.add(gameTypeSolo);
        mainPanel.add(gameTypeAI);
        mainPanel.add(gameTypeVS);


        cardNumber12.setBounds(20,150,150,25);
        cardNumber24.setBounds(20,170,150,25);

        mainPanel.add(cardNumber12);
        mainPanel.add(cardNumber24);

        cardOneLabel = new JLabel("Standard Deck");
        cardTwoLabel = new JLabel("Special Deck");
        cardOneIconLabel = new JLabel();
        cardTwoIconLabel = new JLabel();

        cardOneLabel.setBounds(245,10, 100, 20);
        cardTwoLabel.setBounds(452,10,100,20);

        cardOneIconLabel.setBounds(200,40,200,270);
        cardTwoIconLabel.setBounds(400,40,200,270);

        cardOneIconLabel.setIcon(cardOne);
        cardTwoIconLabel.setIcon(cardTwo);

        mainPanel.add(cardOneLabel);
        mainPanel.add(cardTwoLabel);
        mainPanel.add(cardOneIconLabel);
        mainPanel.add(cardTwoIconLabel);

        mainPanel.setLayout(null);
        this.add(mainPanel);


        // Add actions listeners for radio buttons
        deckType1.addActionListener(this);
        deckType2.addActionListener(this);

        cardNumber12.addActionListener(this);
        cardNumber24.addActionListener(this);

        gameTypeSolo.addActionListener(this);
        gameTypeAI.addActionListener(this);
        gameTypeVS.addActionListener(this);

        playButton.addActionListener(e -> {
            if(setDeckType && setGameType && setCardNumber) {
                this.dispose();
                try {
                    new MemoryGame(userName, gameTypeChosen, cardsChosen, imagePackChosen);
                } catch (InterruptedException interruptedException) {
                    interruptedException.printStackTrace();
                }
            }
            else {
                playError.setText("Please check radio buttons");
            }
        });

        this.setResizable(false);
        this.setLocationByPlatform(true);
        this.setVisible(true);
    }

    @Override // ActionListener for Radio Buttons
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == deckType1) {
            imagePackChosen = ImagePack.NORMAL;
            setDeckType = true;
        }
        if(e.getSource() == deckType2) {
            imagePackChosen = ImagePack.SPECIAL;
            setDeckType = true;
        }
        if(e.getSource() == gameTypeSolo) {
            gameTypeChosen = GameType.SOLO;
            setGameType = true;
        }
        if(e.getSource() == gameTypeAI) {
            gameTypeChosen = GameType.AI;
            setGameType = true;
        }

        if(e.getSource() == gameTypeVS) {
            gameTypeChosen = GameType.VERSUS;
            setGameType = true;
        }

        if(e.getSource() == cardNumber12) {
            cardsChosen = 12;
            setCardNumber = true;
        }

        if(e.getSource() == cardNumber24) {
            cardsChosen = 24;
            setCardNumber = true;
        }
    }
}
