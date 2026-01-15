package doan;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.SQLException;

public class signupguestpanel extends JPanel {
    public signupguestpanel(Connection conn, CardLayout card, JPanel content) {
        setLayout(new BorderLayout());        // Tiêu đề
        JLabel title = new JLabel("Sign up", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        add(title, BorderLayout.NORTH);

        // Form nhập thông tin
        JPanel formWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel form = new JPanel(new GridLayout(4, 2, 15, 15));
        form.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        Dimension labelSize = new Dimension(100, 25);
        Dimension fieldSize = new Dimension(250, 28);

        JLabel lblName = new JLabel("Name"); lblName.setPreferredSize(labelSize);
        JLabel lblPhone = new JLabel("Phone"); lblPhone.setPreferredSize(labelSize);
        JLabel lblEmail = new JLabel("Email"); lblEmail.setPreferredSize(labelSize);
        JLabel lblPassword = new JLabel("Password"); lblPassword.setPreferredSize(labelSize);

        JTextField nameField = new JTextField(); nameField.setPreferredSize(fieldSize);
        JTextField phoneField = new JTextField(); phoneField.setPreferredSize(fieldSize);
        JTextField emailField = new JTextField(); emailField.setPreferredSize(fieldSize);
        JTextField passwordField = new JTextField(); passwordField.setPreferredSize(fieldSize);

        form.add(lblName); form.add(nameField);
        form.add(lblPhone); form.add(phoneField);
        form.add(lblEmail); form.add(emailField);
        form.add(lblPassword); form.add(passwordField);

        formWrapper.add(form);
        add(formWrapper, BorderLayout.CENTER);

        // Nút Save và Return
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton saveBtn = new JButton("Save");
        JButton returnBtn = new JButton("Return");
        btnPanel.add(returnBtn);
        btnPanel.add(saveBtn);
        add(btnPanel, BorderLayout.SOUTH);

        // Sự kiện Save
        saveBtn.addActionListener(e -> {
            try {
                String name = nameField.getText().trim();
                String phone = phoneField.getText().trim();
                String email = emailField.getText().trim();
                String password = passwordField.getText().trim();

                if (name.isEmpty() || phone.isEmpty() || email.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "❌ Vui lòng nhập đầy đủ thông tin!");
                    return;
                }

                guest g = new guest(0, name, phone, email, password, "pending");
                guestdao dao = new guestdao(conn);
                dao.insertguest(g);
                JOptionPane.showMessageDialog(this, "✅ Đăng ký thành công! Vui lòng chờ admin duyệt tài khoản.");
                nameField.setText("");
                phoneField.setText("");
                emailField.setText("");
                passwordField.setText("");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "❌ Lỗi SQL: " + ex.getMessage());
            }
        });

        // Sự kiện Return
        returnBtn.addActionListener(e -> card.show(content, "welcome"));
    }
}
