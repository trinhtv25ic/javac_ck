package doan;
import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class approveaccountpanel extends JPanel {
    private final Connection conn;
    private final guestdao guestdao;
    private JPanel contentPanel;

    private JTextField guestIdField, nameField, phoneField, emailField, passwordField, statusField;

    public approveaccountpanel(Connection conn) {
        setLayout(new BorderLayout());
        this.conn = conn;
        this.guestdao = new guestdao(conn);

        add(buildMenuPanel(), BorderLayout.WEST);
        add(buildTopButtons(), BorderLayout.NORTH);
        contentPanel = buildAddContent();
        add(contentPanel, BorderLayout.CENTER);
    }

    private JPanel buildMenuPanel() {
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setPreferredSize(new Dimension(180, 0));
        menuPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10)); // padding

        Dimension buttonSize = new Dimension(160, 35);

        // Movie
        JButton movieBtn = new JButton("Movie Management");
        movieBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        movieBtn.setMaximumSize(buttonSize);
        movieBtn.setPreferredSize(buttonSize);
        movieBtn.addActionListener(e -> showCard("movie"));
        menuPanel.add(movieBtn);
        menuPanel.add(Box.createVerticalStrut(10));

        // Room
        JButton roomBtn = new JButton("Room Management");
        roomBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        roomBtn.setMaximumSize(buttonSize);
        roomBtn.setPreferredSize(buttonSize);
        roomBtn.addActionListener(e -> showCard("roommanagement"));
        menuPanel.add(roomBtn);
        menuPanel.add(Box.createVerticalStrut(10));

        // Seat
        JButton seatBtn = new JButton("Seat Management");
        seatBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        seatBtn.setMaximumSize(buttonSize);
        seatBtn.setPreferredSize(buttonSize);
        seatBtn.addActionListener(e -> showCard("seatmanagement"));
        menuPanel.add(seatBtn);
        menuPanel.add(Box.createVerticalStrut(10));

        // Ticket
        JButton ticketBtn = new JButton("Ticket Management");
        ticketBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        ticketBtn.setMaximumSize(buttonSize);
        ticketBtn.setPreferredSize(buttonSize);
        ticketBtn.addActionListener(e -> showCard("ticketmanagement"));
        menuPanel.add(ticketBtn);
        menuPanel.add(Box.createVerticalStrut(10));

        // Showtime
        JButton showtimeBtn = new JButton("Showtime Management");
        showtimeBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        showtimeBtn.setMaximumSize(buttonSize);
        showtimeBtn.setPreferredSize(buttonSize);
        showtimeBtn.addActionListener(e -> showCard("showtimemanagement"));
        menuPanel.add(showtimeBtn);
        menuPanel.add(Box.createVerticalStrut(10));

        // Booking
        JButton bookingBtn = new JButton("Booking Management");
        bookingBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        bookingBtn.setMaximumSize(buttonSize);
        bookingBtn.setPreferredSize(buttonSize);
        bookingBtn.addActionListener(e -> showCard("bookingmanagement"));
        menuPanel.add(bookingBtn);
        menuPanel.add(Box.createVerticalStrut(10));

        // Revenue
        JButton revenueBtn = new JButton("Revenue Management");
        revenueBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        revenueBtn.setMaximumSize(buttonSize);
        revenueBtn.setPreferredSize(buttonSize);
        revenueBtn.addActionListener(e -> showCard("revenuemanagement"));
        menuPanel.add(revenueBtn);
        menuPanel.add(Box.createVerticalStrut(10));

        // Approve
        JButton approveBtn = new JButton("Approve Account");
        approveBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        approveBtn.setMaximumSize(buttonSize);
        approveBtn.setPreferredSize(buttonSize);
        approveBtn.addActionListener(e -> showCard("approveaccount"));
        menuPanel.add(approveBtn);
        menuPanel.add(Box.createVerticalStrut(10));

        // Exit (màu đỏ)
        JButton exitBtn = new JButton("Exit");
        exitBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        exitBtn.setMaximumSize(buttonSize);
        exitBtn.setPreferredSize(buttonSize);
        exitBtn.setForeground(Color.RED); // chữ đỏ
        exitBtn.addActionListener(e -> showCard("welcome"));
        menuPanel.add(exitBtn);

        return menuPanel;
    }


    private JPanel buildTopButtons() {
        JPanel topButtons = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        JButton addBtn = new JButton("Add");
        JButton updateBtn = new JButton("Update");
        JButton deleteBtn = new JButton("Delete");
        JButton findByIdBtn = new JButton("Find by ID");
        JButton approveBtn = new JButton("Status");
        JButton showAllBtn = new JButton("Show All");

        topButtons.add(addBtn);
        topButtons.add(updateBtn);
        topButtons.add(deleteBtn);
        topButtons.add(findByIdBtn);
        topButtons.add(approveBtn);
        topButtons.add(showAllBtn);

        addBtn.addActionListener(e -> switchContent(buildAddContent()));
        updateBtn.addActionListener(e -> switchContent(buildUpdateContent()));
        deleteBtn.addActionListener(e -> switchContent(buildDeleteContent()));
        findByIdBtn.addActionListener(e -> switchContent(buildFindByIdContent()));
        approveBtn.addActionListener(e -> switchContent(buildApproveContent()));
        showAllBtn.addActionListener(e -> switchContent(buildShowAllContent()));

        return topButtons;
    }

    private JPanel buildAddContent() {
        JPanel panel = new JPanel(new BorderLayout());

        // Bọc form để căn giữa
        JPanel formWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel form = new JPanel(new GridLayout(6, 2, 15, 15));
        form.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        Dimension labelSize = new Dimension(100, 25);
        Dimension fieldSize = new Dimension(250, 28);

        JLabel lblGuestId = new JLabel("Guest ID"); lblGuestId.setPreferredSize(labelSize);
        JLabel lblName = new JLabel("Name"); lblName.setPreferredSize(labelSize);
        JLabel lblPhone = new JLabel("Phone"); lblPhone.setPreferredSize(labelSize);
        JLabel lblEmail = new JLabel("Email"); lblEmail.setPreferredSize(labelSize);
        JLabel lblPassword = new JLabel("Password"); lblPassword.setPreferredSize(labelSize);
        JLabel lblStatus = new JLabel("Status"); lblStatus.setPreferredSize(labelSize);

        guestIdField = new JTextField(); guestIdField.setEditable(false); guestIdField.setPreferredSize(fieldSize);
        nameField = new JTextField(); nameField.setPreferredSize(fieldSize);
        phoneField = new JTextField(); phoneField.setPreferredSize(fieldSize);
        emailField = new JTextField(); emailField.setPreferredSize(fieldSize);
        passwordField = new JTextField(); passwordField.setPreferredSize(fieldSize);
        statusField = new JTextField(); statusField.setPreferredSize(fieldSize);

        form.add(lblGuestId); form.add(guestIdField);
        form.add(lblName); form.add(nameField);
        form.add(lblPhone); form.add(phoneField);
        form.add(lblEmail); form.add(emailField);
        form.add(lblPassword); form.add(passwordField);
        form.add(lblStatus); form.add(statusField);

        formWrapper.add(form);
        panel.add(formWrapper, BorderLayout.CENTER);

        // Nút Add căn giữa
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton addBtnBottom = new JButton("Add");
        btnPanel.add(addBtnBottom);
        panel.add(btnPanel, BorderLayout.SOUTH);

        // Sự kiện Add
        addBtnBottom.addActionListener(e -> {
            try {
                guest g = new guest(0,
                    nameField.getText(),
                    phoneField.getText(),
                    emailField.getText(),
                    passwordField.getText(),
                    statusField.getText()
                );
                guestdao.insertguest(g);
                JOptionPane.showMessageDialog(this, "✅ Thêm tài khoản thành công!");
                nameField.setText("");
                phoneField.setText("");
                emailField.setText("");
                passwordField.setText("");
                statusField.setText("");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "❌ Lỗi SQL: " + ex.getMessage());
            }
        });

        return panel;
    }


    private JPanel buildUpdateContent() {
        JPanel panel = new JPanel(new BorderLayout());

        // Thanh tìm kiếm ID
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JTextField idField = new JTextField(10);
        JButton searchBtn = new JButton("Tìm");
        searchPanel.add(new JLabel("Guest ID:"));
        searchPanel.add(idField);
        searchPanel.add(searchBtn);
        panel.add(searchPanel, BorderLayout.NORTH);

        // Form chỉnh sửa
        JPanel formWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER)); // căn giữa toàn bộ form
        JPanel formPanel = new JPanel(new GridLayout(5, 2, 15, 15));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        Dimension labelSize = new Dimension(100, 25);
        Dimension fieldSize = new Dimension(250, 28);

        JLabel lblName = new JLabel("Name"); lblName.setPreferredSize(labelSize);
        JLabel lblPhone = new JLabel("Phone"); lblPhone.setPreferredSize(labelSize);
        JLabel lblEmail = new JLabel("Email"); lblEmail.setPreferredSize(labelSize);
        JLabel lblPassword = new JLabel("Password"); lblPassword.setPreferredSize(labelSize);
        JLabel lblStatus = new JLabel("Status"); lblStatus.setPreferredSize(labelSize);

        JTextField nameField = new JTextField(); nameField.setPreferredSize(fieldSize);
        JTextField phoneField = new JTextField(); phoneField.setPreferredSize(fieldSize);
        JTextField emailField = new JTextField(); emailField.setPreferredSize(fieldSize);
        JTextField passwordField = new JTextField(); passwordField.setPreferredSize(fieldSize);
        JTextField statusField = new JTextField(); statusField.setPreferredSize(fieldSize);

        formPanel.add(lblName); formPanel.add(nameField);
        formPanel.add(lblPhone); formPanel.add(phoneField);
        formPanel.add(lblEmail); formPanel.add(emailField);
        formPanel.add(lblPassword); formPanel.add(passwordField);
        formPanel.add(lblStatus); formPanel.add(statusField);

        formWrapper.add(formPanel);
        panel.add(formWrapper, BorderLayout.CENTER);

        // Nút Update căn giữa
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton confirmUpdateBtn = new JButton("Update");
        btnPanel.add(confirmUpdateBtn);
        panel.add(btnPanel, BorderLayout.SOUTH);

        // Sự kiện tìm kiếm theo ID
        searchBtn.addActionListener(e -> {
            try {
                int id = Integer.parseInt(idField.getText());
                guest g = guestdao.getguestByid(id);
                if (g != null) {
                    nameField.setText(g.getName());
                    phoneField.setText(g.getPhone());
                    emailField.setText(g.getEmail());
                    passwordField.setText(g.getPassword());
                    statusField.setText(g.getStatus());
                } else {
                    JOptionPane.showMessageDialog(this, "❌ Không tìm thấy tài khoản!");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "❌ Lỗi: " + ex.getMessage());
            }
        });

        // Sự kiện cập nhật
        confirmUpdateBtn.addActionListener(e -> {
            try {
                int id = Integer.parseInt(idField.getText());
                guest g = new guest(id,
                    nameField.getText(),
                    phoneField.getText(),
                    emailField.getText(),
                    passwordField.getText(),
                    statusField.getText()
                );
                guestdao.updateguest(g);
                JOptionPane.showMessageDialog(this, "✅ Cập nhật tài khoản thành công!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "❌ Lỗi: " + ex.getMessage());
            }
        });

        return panel;
    }


    private JPanel buildDeleteContent() {
        JPanel panel = new JPanel(new BorderLayout());

        // Thanh tìm kiếm ID
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JTextField idField = new JTextField(10);
        JButton deleteBtn = new JButton("Delete");
        searchPanel.add(new JLabel("Guest ID:"));
        searchPanel.add(idField);
        searchPanel.add(deleteBtn);
        panel.add(searchPanel, BorderLayout.NORTH);

        // Nút Delete căn giữa
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnPanel.add(deleteBtn);
        panel.add(btnPanel, BorderLayout.SOUTH);

        // Sự kiện Delete
        deleteBtn.addActionListener(e -> {
            try {
                int id = Integer.parseInt(idField.getText());
                guest g = guestdao.getguestByid(id);
                if (g != null) {
                    int confirm = JOptionPane.showConfirmDialog(this,
                        "Xác nhận xóa tài khoản: " + g.getName(),
                        "Xác nhận", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        String sql = "DELETE FROM guest WHERE guestid=?";
                        try (PreparedStatement ps = conn.prepareStatement(sql)) {
                            ps.setInt(1, id);
                            ps.executeUpdate();
                            JOptionPane.showMessageDialog(this, "🗑️ Xóa tài khoản thành công!");
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "❌ Không tìm thấy tài khoản!");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "❌ Lỗi: " + ex.getMessage());
            }
        });

        return panel;
    }


    private JPanel buildFindByIdContent() {
        JPanel panel = new JPanel(new BorderLayout());

        // Thanh tìm kiếm ID
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JTextField idField = new JTextField(10);
        JButton searchBtn = new JButton("Tìm");
        searchPanel.add(new JLabel("Guest ID:"));
        searchPanel.add(idField);
        searchPanel.add(searchBtn);
        panel.add(searchPanel, BorderLayout.NORTH);

        // Form hiển thị thông tin (chỉ đọc)
        JPanel formWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel formPanel = new JPanel(new GridLayout(5, 2, 15, 15));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        Dimension labelSize = new Dimension(100, 25);
        Dimension fieldSize = new Dimension(250, 28);

        JLabel lblName = new JLabel("Name"); lblName.setPreferredSize(labelSize);
        JLabel lblPhone = new JLabel("Phone"); lblPhone.setPreferredSize(labelSize);
        JLabel lblEmail = new JLabel("Email"); lblEmail.setPreferredSize(labelSize);
        JLabel lblPassword = new JLabel("Password"); lblPassword.setPreferredSize(labelSize);
        JLabel lblStatus = new JLabel("Status"); lblStatus.setPreferredSize(labelSize);

        JTextField nameField = new JTextField(); nameField.setEditable(false); nameField.setPreferredSize(fieldSize);
        JTextField phoneField = new JTextField(); phoneField.setEditable(false); phoneField.setPreferredSize(fieldSize);
        JTextField emailField = new JTextField(); emailField.setEditable(false); emailField.setPreferredSize(fieldSize);
        JTextField passwordField = new JTextField(); passwordField.setEditable(false); passwordField.setPreferredSize(fieldSize);
        JTextField statusField = new JTextField(); statusField.setEditable(false); statusField.setPreferredSize(fieldSize);

        formPanel.add(lblName); formPanel.add(nameField);
        formPanel.add(lblPhone); formPanel.add(phoneField);
        formPanel.add(lblEmail); formPanel.add(emailField);
        formPanel.add(lblPassword); formPanel.add(passwordField);
        formPanel.add(lblStatus); formPanel.add(statusField);

        formWrapper.add(formPanel);
        panel.add(formWrapper, BorderLayout.CENTER);

        // Sự kiện tìm kiếm
        searchBtn.addActionListener(e -> {
            try {
                int id = Integer.parseInt(idField.getText());
                guest g = guestdao.getguestByid(id);
                if (g != null) {
                    nameField.setText(g.getName());
                    phoneField.setText(g.getPhone());
                    emailField.setText(g.getEmail());
                    passwordField.setText(g.getPassword());
                    statusField.setText(g.getStatus());
                } else {
                    JOptionPane.showMessageDialog(this, "❌ Không tìm thấy tài khoản!");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "❌ Lỗi: " + ex.getMessage());
            }
        });

        return panel;
    }


    private JPanel buildApproveContent() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JTextField idField = new JTextField(10);
        JButton approveBtn = new JButton("Approve");

        panel.add(new JLabel("Guest ID:"));
        panel.add(idField);
        panel.add(approveBtn);

        approveBtn.addActionListener(e -> {
            try {
                int id = Integer.parseInt(idField.getText());
                guestdao.approveguest(id);
                JOptionPane.showMessageDialog(this, "✅ Đã duyệt tài khoản!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "❌ Lỗi: " + ex.getMessage());
            }
        });

        return panel;
    }


    private JPanel buildShowAllContent() {
        JPanel panel = new JPanel(new BorderLayout());

        String[] columnNames = {"Guest ID", "Name", "Phone", "Email", "Password", "Status"};
        Object[][] data;

        try {
            List<guest> list = guestdao.getAllguest();
            data = new Object[list.size()][6];
            for (int i = 0; i < list.size(); i++) {
                guest g = list.get(i);
                data[i][0] = g.getGuestid();
                data[i][1] = g.getName();
                data[i][2] = g.getPhone();
                data[i][3] = g.getEmail();
                data[i][4] = g.getPassword();
                data[i][5] = g.getStatus();
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "❌ Lỗi SQL: " + ex.getMessage());
            data = new Object[0][6];
        }

        JTable table = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private void switchContent(JPanel newContent) {
        if (contentPanel != null) {
            remove(contentPanel);
        }
        contentPanel = newContent;
        add(contentPanel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    private void showCard(String name) {
        Container parent = this.getParent();
        while (parent != null && !(parent instanceof JPanel && parent.getLayout() instanceof CardLayout)) {
            parent = parent.getParent();
        }
        if (parent != null) {
            CardLayout cl = (CardLayout) parent.getLayout();
            cl.show((JPanel) parent, name);
        }
    }
}

