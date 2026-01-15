package doan;

import javax.swing.*;
import java.awt.*;
public class welcomepanel extends JPanel {
    public JButton signinadmin;
    public JButton signinguest;
    public JButton signupguest;

    public welcomepanel() {
        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createLineBorder(Color.BLUE, 3)); 

        JLabel welcomelabel = new JLabel("WELCOME !");
        welcomelabel.setFont(new Font("Arial", Font.BOLD, 32));
        welcomelabel.setForeground(Color.RED);
        welcomelabel.setHorizontalAlignment(SwingConstants.CENTER);
        welcomelabel.setBorder(BorderFactory.createLineBorder(Color.darkGray, 2));
        add(welcomelabel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel adminPanel = new JPanel();
        adminPanel.setLayout(new BoxLayout(adminPanel, BoxLayout.Y_AXIS));
        adminPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        JLabel adminlabel = new JLabel("Administrator");
        adminlabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        signinadmin = new JButton("Sign in");
        signinadmin.setAlignmentX(Component.CENTER_ALIGNMENT);
        adminPanel.add(Box.createVerticalStrut(20));
        adminPanel.add(adminlabel);
        adminPanel.add(Box.createVerticalStrut(10));
        adminPanel.add(signinadmin);

        JPanel guestPanel = new JPanel();
        guestPanel.setLayout(new BoxLayout(guestPanel, BoxLayout.Y_AXIS));
        guestPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        JLabel guestlabel = new JLabel("Guest");
        guestlabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        signupguest = new JButton("Sign up");
        signupguest.setAlignmentX(Component.CENTER_ALIGNMENT);
        signinguest = new JButton("Sign in");
        signinguest.setAlignmentX(Component.CENTER_ALIGNMENT);
        guestPanel.add(Box.createVerticalStrut(20));
        guestPanel.add(guestlabel);
        guestPanel.add(Box.createVerticalStrut(10));
        guestPanel.add(signupguest);
        guestPanel.add(Box.createVerticalStrut(10));
        guestPanel.add(signinguest);

        centerPanel.add(adminPanel);
        centerPanel.add(guestPanel);

        add(centerPanel, BorderLayout.CENTER);
    }



	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
