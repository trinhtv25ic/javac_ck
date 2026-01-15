package doan;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.SQLException;

public class signinguestpanel extends JPanel {
    private guestdao dao;

    public signinguestpanel(Connection conn, CardLayout card, JPanel content) {
        dao = new guestdao(conn);
        setLayout(new BorderLayout());

        // Tiêu đề
        JLabel title = new JLabel("Guest Login", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        add(title, BorderLayout.NORTH);

        // Form nhập Email + Password
        JPanel formWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel form = new JPanel(new GridLayout(2, 2, 15, 15));
        form.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        Dimension labelSize = new Dimension(100, 25);
        Dimension fieldSize = new Dimension(250, 28);

        JLabel lblEmail = new JLabel("Email"); lblEmail.setPreferredSize(labelSize);
        JLabel lblPassword = new JLabel("Password"); lblPassword.setPreferredSize(labelSize);

        JTextField emailField = new JTextField(); emailField.setPreferredSize(fieldSize);
        JPasswordField passwordField = new JPasswordField(); passwordField.setPreferredSize(fieldSize);

        form.add(lblEmail); form.add(emailField);
        form.add(lblPassword); form.add(passwordField);

        formWrapper.add(form);
        add(formWrapper, BorderLayout.CENTER);

        // Nút Login và Return
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton loginBtn = new JButton("Login");
        JButton returnBtn = new JButton("Return");
        btnPanel.add(returnBtn);
        btnPanel.add(loginBtn);
        add(btnPanel, BorderLayout.SOUTH);

        // Sự kiện Login
        loginBtn.addActionListener(e -> {
            try {
                String email = emailField.getText().trim();
                String password = new String(passwordField.getPassword()).trim();

                if (email.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "❌ Vui lòng nhập đầy đủ Email và Password!");
                    return;
                }

                guest g = dao.loginGuest(email, password);
                if (g != null) {
                    JOptionPane.showMessageDialog(this, "✅ Đăng nhập thành công! Chào " + g.getName());

                    // 👉 Khởi tạo bookingdao
                    bookingdao bookingDao = new bookingdao(conn);

                    // 👉 Tạo panel danh sách phim cho guest với constructor mới
                    movielistpanel guestMoviePanel = new movielistpanel(conn, card, content, g, bookingDao);
                    content.add(guestMoviePanel, "guest_movie");

                    // 👉 Nhảy sang panel movie của guest
                    card.show(content, "guest_movie");
                } else {
                    JOptionPane.showMessageDialog(this, "❌ Sai thông tin hoặc tài khoản chưa được duyệt!");
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "❌ Lỗi SQL: " + ex.getMessage());
            }
        });

        // Sự kiện Return
        returnBtn.addActionListener(e -> card.show(content, "welcome"));
    }
}
