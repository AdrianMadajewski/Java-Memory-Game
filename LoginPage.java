package Memory;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import java.io.*;
import java.util.HashMap;
import java.util.Set;

public class LoginPage extends JFrame implements ActionListener, FocusListener {
	
	private JButton loginButton = new JButton("Login");
	private JButton registerButton = new JButton("Register");
	
	private JPasswordField passwordField = new JPasswordField("Password:");
	private JTextField loginTextField = new JTextField("Login:");
	
	final private String databasePath = "database\\users.txt";
	final private String iconPath = "assets\\put_logo.png";
	final private String title = "Memory Launch Page";
	final private String defaultHintLogin = "Login:";
	final private String defaultHintPassword = "Password:";
	
	private HashMap<String, String> usersDatabase = new HashMap<String, String>();
	
	LoginPage() {
		
		loginButton.addActionListener(this);
		loginButton.setFocusPainted(false);
		
		registerButton.addActionListener(this);
		
		loginTextField.addFocusListener(this);
		passwordField.addFocusListener(this);
		
		passwordField.setEchoChar((char) 0);
		
		loginTextField.setPreferredSize(new Dimension(250, 40));
		passwordField.setPreferredSize(new Dimension(250, 40));
		
		// Should check if file image exists???
		this.setIconImage(new ImageIcon(iconPath).getImage());
		this.add(loginTextField);
		this.add(passwordField);
		this.add(loginButton);
		this.add(registerButton);
		
		this.setLayout(new GridLayout(4, 1));
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setTitle(title);
		this.pack();
		this.getRootPane().setDefaultButton(loginButton);
		loginButton.requestFocusInWindow();
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		this.setVisible(true);
		
		readDatabase();
	}
	
	private void readDatabase() {
		String line;
		try {
			BufferedReader reader = new BufferedReader(new FileReader(databasePath));
		    while ((line = reader.readLine()) != null)
		    {
		        String[] parts = line.split(";", 2);
		        if (parts.length >= 2)
		        {
		            String fileLogin = parts[0];
		            String filePassword = parts[1];
		            usersDatabase.put(fileLogin, filePassword);
		        } else {
		            System.out.println("ignoring line: " + line);
		        }
		    }
		    reader.close();
		} catch (Exception ex) {
			ex.printStackTrace();
			return;
		}
		
	}
	
	private void registerNewUser(String login, String password) {
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(databasePath, true));
			bw.write(login + ";" + password);
			bw.newLine();
			bw.close();
		} catch(Exception ex) {
			ex.printStackTrace();
			return;
		}
	}
	
	private boolean userExists(String login) {
		return usersDatabase.containsKey(login);
	}
	
	private boolean userExists(String login, String password) {
		return usersDatabase.containsKey(login) && usersDatabase.containsValue(password);
	}
	
	private void login() {
		// Login using the database and launch application
		
		String userLogin = loginTextField.getText();
		String userPassword = new String(passwordField.getPassword());
					
		// This may be redundant because successful login indicates closing the application
		// readDatabase();
				 
		// Check if the values exits in the file to login
		if(userExists(userLogin, userPassword)) {
		// Proceed to the new application window
					    
			// TODO: run application
			this.dispose();
			new SettingsPage(userLogin);
			// this.dispose();
		}
		else {
			// Get message invalid login data
			JOptionPane.showMessageDialog(this, "Invalid login or password", 
						"Invalid data", JOptionPane.WARNING_MESSAGE);
		}
	}
	
	private void register() {
		// Register the user and launch application
		
		String login = loginTextField.getText();
		String password = new String(passwordField.getPassword());
					
		// Should check if the user already exists
		if(userExists(login)) {
			
			JOptionPane.showMessageDialog(this, "User already exists in database", 
								"User exists", JOptionPane.WARNING_MESSAGE);
				return;
			}
		
		if(login.isBlank() || password.isBlank() || login.equals(defaultHintLogin) || password.equals(defaultHintPassword)) {
			JOptionPane.showMessageDialog(this, "Invalid login or password", 
					"Invalid data", JOptionPane.WARNING_MESSAGE);
			return;
		}
					
		registerNewUser(login, password);

		this.dispose();
		new SettingsPage(login);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == registerButton) {
			register();
		}
		else if(e.getSource() == loginButton) {
			login();
		}
	}

	@Override
	public void focusGained(FocusEvent e) {
		if(e.getSource() == loginTextField) {
			if(loginTextField.getText().equals(defaultHintLogin)) {
				loginTextField.setText("");
				// TODO: Change the font from italic to normal
			}
		}
		else if(e.getSource() == passwordField) {
			passwordField.setText("");
			passwordField.setEchoChar('*');
		}
	}

	@Override
	public void focusLost(FocusEvent e) {
		return;
	}
}
