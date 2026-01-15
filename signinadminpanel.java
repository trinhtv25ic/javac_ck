package doan;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;

public class signinadminpanel extends JPanel {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginBtn;
    private Connection conn;
    private CardLayout card;
    private JPanel content;

    public signinadminpanel(Connection conn, CardLayout card, JPanel content) {
        this.conn = conn;
        this.card = card;
        this.content = content;

        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        JLabel title = new JLabel("Cinema Ticket Management System");
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setBorder(BorderFactory.createLineBorder(Color.BLUE, 2));
        add(title, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel userLabel = new JLabel("Username");
        usernameField = new JTextField(15);

        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(userLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 0;
        formPanel.add(usernameField, gbc);

        JLabel passLabel = new JLabel("Password");
        passwordField = new JPasswordField(15);

        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(passLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 1;
        formPanel.add(passwordField, gbc);

        add(formPanel, BorderLayout.CENTER);

        loginBtn = new JButton("Sign in");
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(loginBtn);
        add(buttonPanel, BorderLayout.SOUTH);

        loginBtn.addActionListener(e -> {
            String user = usernameField.getText();
            String pass = new String(passwordField.getPassword());

            if (user.equals("milimzotri") && pass.equals("141207")) {
                JOptionPane.showMessageDialog(this, "✅ Đăng nhập thành công!");
                card.show(content, "movie"); 
            } else {
                JOptionPane.showMessageDialog(this, "❌ Sai tài khoản hoặc mật khẩu!");
            }
        });
    }
}
