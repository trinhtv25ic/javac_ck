package doan;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class roommanagementpanel extends JPanel {
    private final Connection conn;
    private final roomdao roomdao;
    private JPanel contentPanel;



    // field cho form Add
    private JTextField roomidField, capacityField;

    public roommanagementpanel(Connection conn) {
        setLayout(new BorderLayout());
        this.conn = conn;
        this.roomdao = new roomdao(conn);



        // Menu trái
        add(buildMenuPanel(), BorderLayout.WEST);

        // Thanh nút ngang
        JPanel topButtons = buildTopButtons();
        add(topButtons, BorderLayout.NORTH);

        // Nội dung ban đầu: Add Room
        contentPanel = buildAddContent();
        add(contentPanel, BorderLayout.CENTER);
    }


    // tiện ích dùng chung
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


    private void showCard(String name) {
        Container parent = this.getParent();
        while (parent != null && !(parent instanceof JPanel && parent.getLayout() instanceof CardLayout)) {
            parent = parent.getParent();
        }
        if (parent != null) {
            CardLayout cl = (CardLayout) parent.getLayout();
            System.out.println("Đang chuyển sang: " + name);
            cl.show((JPanel) parent, name);
        }
    }


    private JPanel buildTopButtons() {
        JPanel topButtons = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        JButton addBtn = new JButton("Add");
        JButton updateBtn = new JButton("Update");
        JButton deleteBtn = new JButton("Delete");
        JButton findByIdBtn = new JButton("Find by ID");
        JButton showAllBtn = new JButton("Show All");

        topButtons.add(addBtn);
        topButtons.add(updateBtn);
        topButtons.add(deleteBtn);
        topButtons.add(findByIdBtn);
        topButtons.add(showAllBtn);

        // sự kiện đổi nội dung
        addBtn.addActionListener(e -> switchContent(buildAddContent()));
        updateBtn.addActionListener(e -> switchContent(buildUpdateContent()));
        deleteBtn.addActionListener(e -> switchContent(buildDeleteContent()));
        findByIdBtn.addActionListener(e -> switchContent(buildFindByIdContent()));
        showAllBtn.addActionListener(e -> switchContent(buildShowAllContent()));

        return topButtons;
    }

    // ===== ADD ROOM =====
    private JPanel buildAddContent() {
        JPanel panel = new JPanel(new BorderLayout());

        // Bọc form để căn giữa
        JPanel formWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel form = new JPanel(new GridLayout(2, 2, 15, 15));
        form.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        Dimension labelSize = new Dimension(100, 25);
        Dimension fieldSize = new Dimension(250, 28);

        JLabel lblRoomId = new JLabel("Room ID"); lblRoomId.setPreferredSize(labelSize);
        JLabel lblCapacity = new JLabel("Capacity"); lblCapacity.setPreferredSize(labelSize);

        roomidField = new JTextField(); roomidField.setEditable(false); roomidField.setPreferredSize(fieldSize);
        capacityField = new JTextField(); capacityField.setPreferredSize(fieldSize);

        form.add(lblRoomId); form.add(roomidField);
        form.add(lblCapacity); form.add(capacityField);

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
                int capacity = Integer.parseInt(capacityField.getText());
                room r = new room(0, capacity);
                roomdao.insertroom(r);
                JOptionPane.showMessageDialog(this, "✅ Thêm phòng thành công!");
                capacityField.setText("");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "❌ Capacity phải là số nguyên!");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "❌ Lỗi SQL: " + ex.getMessage());
            }
        });

        return panel;
    }


    // ===== UPDATE ROOM =====
    private JPanel buildUpdateContent() {
        JPanel panel = new JPanel(new BorderLayout());

        // Thanh tìm kiếm ID
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JTextField idField = new JTextField(10);
        JButton searchBtn = new JButton("Tìm");
        searchPanel.add(new JLabel("Room ID:"));
        searchPanel.add(idField);
        searchPanel.add(searchBtn);
        panel.add(searchPanel, BorderLayout.NORTH);

        // Form chỉnh sửa
        JPanel formWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel formPanel = new JPanel(new GridLayout(1, 2, 15, 15));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        Dimension labelSize = new Dimension(100, 25);
        Dimension fieldSize = new Dimension(250, 28);

        JLabel lblCapacity = new JLabel("Capacity"); lblCapacity.setPreferredSize(labelSize);

        JTextField updateCapacity = new JTextField(); updateCapacity.setEditable(false); updateCapacity.setPreferredSize(fieldSize);

        formPanel.add(lblCapacity); formPanel.add(updateCapacity);

        formWrapper.add(formPanel);
        panel.add(formWrapper, BorderLayout.CENTER);

        // Nút Update căn giữa
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton confirmUpdateBtn = new JButton("Update");
        btnPanel.add(confirmUpdateBtn);
        panel.add(btnPanel, BorderLayout.SOUTH);

        // Sự kiện tìm kiếm
        searchBtn.addActionListener(e -> {
            try {
                int id = Integer.parseInt(idField.getText());
                room r = roomdao.getroomByid(id);
                if (r != null) {
                    updateCapacity.setText(String.valueOf(r.getCapacity()));
                    updateCapacity.setEditable(true);
                } else {
                    updateCapacity.setText("");
                    updateCapacity.setEditable(false);
                    JOptionPane.showMessageDialog(this, "❌ Không tìm thấy phòng!");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "❌ Room ID phải là số nguyên!");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "❌ Lỗi SQL: " + ex.getMessage());
            }
        });

        // Sự kiện cập nhật
        confirmUpdateBtn.addActionListener(e -> {
            try {
                int id = Integer.parseInt(idField.getText());
                int capacity = Integer.parseInt(updateCapacity.getText());
                room r = new room(id, capacity);
                roomdao.updateroom(r);
                JOptionPane.showMessageDialog(this, "✅ Cập nhật phòng thành công!");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "❌ ID và Capacity phải là số nguyên!");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "❌ Lỗi SQL: " + ex.getMessage());
            }
        });

        return panel;
    }


    // ===== DELETE ROOM =====
    private JPanel buildDeleteContent() {
        JPanel panel = new JPanel(new BorderLayout());

        // Thanh tìm kiếm ID
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JTextField idField = new JTextField(10);
        JButton searchBtn = new JButton("Tìm");
        searchPanel.add(new JLabel("Room ID:"));
        searchPanel.add(idField);
        searchPanel.add(searchBtn);
        panel.add(searchPanel, BorderLayout.NORTH);

        // Form hiển thị thông tin (chỉ đọc)
        JPanel formWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel formPanel = new JPanel(new GridLayout(1, 2, 15, 15));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        Dimension labelSize = new Dimension(100, 25);
        Dimension fieldSize = new Dimension(250, 28);

        JLabel lblCapacity = new JLabel("Capacity"); lblCapacity.setPreferredSize(labelSize);
        JTextField delCapacity = new JTextField(); delCapacity.setEditable(false); delCapacity.setPreferredSize(fieldSize);

        formPanel.add(lblCapacity); formPanel.add(delCapacity);

        formWrapper.add(formPanel);
        panel.add(formWrapper, BorderLayout.CENTER);

        // Nút Delete căn giữa
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton confirmDeleteBtn = new JButton("Delete");
        btnPanel.add(confirmDeleteBtn);
        panel.add(btnPanel, BorderLayout.SOUTH);

        // Sự kiện tìm kiếm
        searchBtn.addActionListener(e -> {
            try {
                int id = Integer.parseInt(idField.getText());
                room r = roomdao.getroomByid(id);
                if (r != null) {
                    delCapacity.setText(String.valueOf(r.getCapacity()));
                } else {
                    delCapacity.setText("");
                    JOptionPane.showMessageDialog(this, "❌ Không tìm thấy phòng!");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "❌ Room ID phải là số nguyên!");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "❌ Lỗi SQL: " + ex.getMessage());
            }
        });

        // Sự kiện Delete
        confirmDeleteBtn.addActionListener(e -> {
            try {
                int id = Integer.parseInt(idField.getText());
                roomdao.deleteroom(id);
                JOptionPane.showMessageDialog(this, "🗑️ Xóa phòng thành công!");
                delCapacity.setText("");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "❌ Room ID phải là số nguyên!");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "❌ Lỗi SQL: " + ex.getMessage());
            }
        });

        return panel;
    }


    // ===== FIND BY ID =====
    private JPanel buildFindByIdContent() {
        JPanel panel = new JPanel(new BorderLayout());

        // Thanh tìm kiếm ID
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JTextField idField = new JTextField(10);
        JButton searchBtn = new JButton("Tìm");
        searchPanel.add(new JLabel("Room ID:"));
        searchPanel.add(idField);
        searchPanel.add(searchBtn);
        panel.add(searchPanel, BorderLayout.NORTH);

        // Form hiển thị thông tin (chỉ đọc)
        JPanel formWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel formPanel = new JPanel(new GridLayout(1, 2, 15, 15));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        Dimension labelSize = new Dimension(100, 25);
        Dimension fieldSize = new Dimension(250, 28);

        JLabel lblCapacity = new JLabel("Capacity"); lblCapacity.setPreferredSize(labelSize);
        JTextField fCapacity = new JTextField(); fCapacity.setEditable(false); fCapacity.setPreferredSize(fieldSize);

        formPanel.add(lblCapacity); formPanel.add(fCapacity);

        formWrapper.add(formPanel);
        panel.add(formWrapper, BorderLayout.CENTER);

        // Sự kiện tìm kiếm
        searchBtn.addActionListener(e -> {
            try {
                int id = Integer.parseInt(idField.getText());
                room r = roomdao.getroomByid(id);
                if (r != null) {
                    fCapacity.setText(String.valueOf(r.getCapacity()));
                } else {
                    fCapacity.setText("");
                    JOptionPane.showMessageDialog(this, "❌ Không tìm thấy phòng!");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "❌ Room ID phải là số nguyên!");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "❌ Lỗi SQL: " + ex.getMessage());
            }
        });

        return panel;
    }


    // ===== SHOW ALL (bảng như SQL) =====
    private JPanel buildShowAllContent() {
        JPanel panel = new JPanel(new BorderLayout());

        String[] columnNames = {"Room ID", "Capacity"};
        Object[][] data;

        try {
            List<room> rooms = roomdao.getAllroom();
            data = new Object[rooms.size()][2];
            for (int i = 0; i < rooms.size(); i++) {
                room r = rooms.get(i);
                data[i][0] = r.getRoomid();
                data[i][1] = r.getCapacity();
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "❌ Lỗi SQL: " + ex.getMessage());
            data = new Object[0][2];
        }

        JTable table = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    // ===== đổi nội dung CENTER, giữ nguyên khung =====
    private void switchContent(JPanel newContent) {
        if (contentPanel != null) {
            remove(contentPanel);
        }
        contentPanel = newContent;
        add(contentPanel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }
}
