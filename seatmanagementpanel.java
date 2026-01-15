package doan;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class seatmanagementpanel extends JPanel {
    private final Connection conn;
    private final seatdao seatdao;
    private final roomdao roomdao;   // cần thêm để lấy room object
    private JPanel contentPanel;

    // field cho form Add
    private JTextField seatIdField, rowField, statusField, roomIdField;

    public seatmanagementpanel(Connection conn) {
        setLayout(new BorderLayout());
        this.conn = conn;
        this.seatdao = new seatdao(conn);
        this.roomdao = new roomdao(conn);

        // Menu trái
        add(buildMenuPanel(), BorderLayout.WEST);

        // Thanh nút ngang
        JPanel topButtons = buildTopButtons();
        add(topButtons, BorderLayout.NORTH);

        // Nội dung ban đầu: Add Seat
        contentPanel = buildAddContent();
        add(contentPanel, BorderLayout.CENTER);
    }

    // ===== MENU DỌC =====
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
        exitBtn.addActionListener(e -> System.exit(0));
        menuPanel.add(exitBtn);

        return menuPanel;
    }


    // ===== NÚT NGANG =====
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

        addBtn.addActionListener(e -> switchContent(buildAddContent()));
        updateBtn.addActionListener(e -> switchContent(buildUpdateContent()));
        deleteBtn.addActionListener(e -> switchContent(buildDeleteContent()));
        findByIdBtn.addActionListener(e -> switchContent(buildFindByIdContent()));
        showAllBtn.addActionListener(e -> switchContent(buildShowAllContent()));

        return topButtons;
    }

    // ===== ADD SEAT =====
    private JPanel buildAddContent() {
        JPanel panel = new JPanel(new BorderLayout());

        // Bọc form để căn giữa
        JPanel formWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel form = new JPanel(new GridLayout(4, 2, 15, 15));
        form.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        Dimension labelSize = new Dimension(100, 25);
        Dimension fieldSize = new Dimension(250, 28);

        JLabel lblSeatId = new JLabel("Seat ID"); lblSeatId.setPreferredSize(labelSize);
        JLabel lblRow = new JLabel("Row"); lblRow.setPreferredSize(labelSize);
        JLabel lblStatus = new JLabel("Status"); lblStatus.setPreferredSize(labelSize);
        JLabel lblRoomId = new JLabel("Room ID"); lblRoomId.setPreferredSize(labelSize);

        seatIdField = new JTextField(); seatIdField.setEditable(false); seatIdField.setPreferredSize(fieldSize);
        rowField = new JTextField(); rowField.setPreferredSize(fieldSize);
        statusField = new JTextField(); statusField.setPreferredSize(fieldSize);
        roomIdField = new JTextField(); roomIdField.setPreferredSize(fieldSize);

        form.add(lblSeatId); form.add(seatIdField);
        form.add(lblRow); form.add(rowField);
        form.add(lblStatus); form.add(statusField);
        form.add(lblRoomId); form.add(roomIdField);

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
                int row = Integer.parseInt(rowField.getText());
                String status = statusField.getText();
                int roomId = Integer.parseInt(roomIdField.getText());

                room r = roomdao.getroomByid(roomId);
                if (r == null) {
                    JOptionPane.showMessageDialog(this, "❌ Không tìm thấy phòng với ID: " + roomId);
                    return;
                }

                seat s = new seat(0, row, status, r);
                seatdao.insertseat(s);

                JOptionPane.showMessageDialog(this, "✅ Thêm ghế thành công!");
                rowField.setText("");
                statusField.setText("");
                roomIdField.setText("");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "❌ Row và Room ID phải là số nguyên!");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "❌ Lỗi SQL: " + ex.getMessage());
            }
        });

        return panel;
    }


    private JPanel buildUpdateContent() {
        JPanel panel = new JPanel(new BorderLayout());

        // Thanh tìm kiếm theo Seat ID
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JTextField idField = new JTextField(10);
        JButton searchBtn = new JButton("Tìm");
        searchPanel.add(new JLabel("Seat ID:"));
        searchPanel.add(idField);
        searchPanel.add(searchBtn);
        panel.add(searchPanel, BorderLayout.NORTH);

        // Form hiển thị thông tin seat
        JPanel formWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 15, 15));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        Dimension labelSize = new Dimension(100, 25);
        Dimension fieldSize = new Dimension(250, 28);

        JLabel lblRow = new JLabel("Row"); lblRow.setPreferredSize(labelSize);
        JLabel lblStatus = new JLabel("Status"); lblStatus.setPreferredSize(labelSize);
        JLabel lblRoomId = new JLabel("Room ID"); lblRoomId.setPreferredSize(labelSize);

        JTextField updateRow = new JTextField(); updateRow.setEditable(false); updateRow.setPreferredSize(fieldSize);
        JTextField updateStatus = new JTextField(); updateStatus.setEditable(false); updateStatus.setPreferredSize(fieldSize);
        JTextField updateRoomId = new JTextField(); updateRoomId.setEditable(false); updateRoomId.setPreferredSize(fieldSize);

        formPanel.add(lblRow); formPanel.add(updateRow);
        formPanel.add(lblStatus); formPanel.add(updateStatus);
        formPanel.add(lblRoomId); formPanel.add(updateRoomId);

        formWrapper.add(formPanel);
        panel.add(formWrapper, BorderLayout.CENTER);

        // Nút Update căn giữa
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton confirmUpdateBtn = new JButton("Update");
        btnPanel.add(confirmUpdateBtn);
        panel.add(btnPanel, BorderLayout.SOUTH);

        // Sự kiện tìm seat theo ID
        searchBtn.addActionListener(e -> {
            try {
                int id = Integer.parseInt(idField.getText());
                seat s = seatdao.getseatByid(id);
                if (s != null) {
                    updateRow.setText(String.valueOf(s.getRow()));
                    updateStatus.setText(s.getStatus());
                    updateRoomId.setText(String.valueOf(s.getRoom().getRoomid()));

                    updateRow.setEditable(true);
                    updateStatus.setEditable(true);
                    updateRoomId.setEditable(true);
                } else {
                    updateRow.setText("");
                    updateStatus.setText("");
                    updateRoomId.setText("");
                    updateRow.setEditable(false);
                    updateStatus.setEditable(false);
                    updateRoomId.setEditable(false);
                    JOptionPane.showMessageDialog(this, "❌ Không tìm thấy ghế!");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "❌ Seat ID phải là số nguyên!");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "❌ Lỗi SQL: " + ex.getMessage());
            }
        });

        // Sự kiện xác nhận Update
        confirmUpdateBtn.addActionListener(e -> {
            try {
                int id = Integer.parseInt(idField.getText());
                int row = Integer.parseInt(updateRow.getText());
                String status = updateStatus.getText();
                int roomId = Integer.parseInt(updateRoomId.getText());

                room r = roomdao.getroomByid(roomId);
                if (r == null) {
                    JOptionPane.showMessageDialog(this, "❌ Không tìm thấy phòng với ID: " + roomId);
                    return;
                }

                seat s = new seat(id, row, status, r);
                seatdao.updateseat(s);

                JOptionPane.showMessageDialog(this, "✅ Cập nhật ghế thành công!");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "❌ ID, Row và Room ID phải là số nguyên!");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "❌ Lỗi SQL: " + ex.getMessage());
            }
        });

        return panel;
    }

    private JPanel buildDeleteContent() {
        JPanel panel = new JPanel(new BorderLayout());

        // Thanh tìm kiếm theo Seat ID
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JTextField idField = new JTextField(10);
        JButton searchBtn = new JButton("Tìm");
        searchPanel.add(new JLabel("Seat ID:"));
        searchPanel.add(idField);
        searchPanel.add(searchBtn);
        panel.add(searchPanel, BorderLayout.NORTH);

        // Form hiển thị thông tin seat
        JPanel formWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 15, 15));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        Dimension labelSize = new Dimension(100, 25);
        Dimension fieldSize = new Dimension(250, 28);

        JLabel lblRow = new JLabel("Row"); lblRow.setPreferredSize(labelSize);
        JLabel lblStatus = new JLabel("Status"); lblStatus.setPreferredSize(labelSize);
        JLabel lblRoomId = new JLabel("Room ID"); lblRoomId.setPreferredSize(labelSize);

        JTextField delRow = new JTextField(); delRow.setEditable(false); delRow.setPreferredSize(fieldSize);
        JTextField delStatus = new JTextField(); delStatus.setEditable(false); delStatus.setPreferredSize(fieldSize);
        JTextField delRoomId = new JTextField(); delRoomId.setEditable(false); delRoomId.setPreferredSize(fieldSize);

        formPanel.add(lblRow); formPanel.add(delRow);
        formPanel.add(lblStatus); formPanel.add(delStatus);
        formPanel.add(lblRoomId); formPanel.add(delRoomId);

        formWrapper.add(formPanel);
        panel.add(formWrapper, BorderLayout.CENTER);

        // Nút Delete căn giữa
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton confirmDeleteBtn = new JButton("Delete");
        btnPanel.add(confirmDeleteBtn);
        panel.add(btnPanel, BorderLayout.SOUTH);

        // Sự kiện tìm seat theo ID
        searchBtn.addActionListener(e -> {
            try {
                int id = Integer.parseInt(idField.getText());
                seat s = seatdao.getseatByid(id);
                if (s != null) {
                    delRow.setText(String.valueOf(s.getRow()));
                    delStatus.setText(s.getStatus());
                    delRoomId.setText(String.valueOf(s.getRoom().getRoomid()));
                } else {
                    delRow.setText("");
                    delStatus.setText("");
                    delRoomId.setText("");
                    JOptionPane.showMessageDialog(this, "❌ Không tìm thấy ghế!");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "❌ Seat ID phải là số nguyên!");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "❌ Lỗi SQL: " + ex.getMessage());
            }
        });

        // Sự kiện xác nhận Delete
        confirmDeleteBtn.addActionListener(e -> {
            try {
                int id = Integer.parseInt(idField.getText());
                seatdao.deleteseat(id);
                JOptionPane.showMessageDialog(this, "🗑️ Xóa ghế thành công!");
                delRow.setText("");
                delStatus.setText("");
                delRoomId.setText("");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "❌ Seat ID phải là số nguyên!");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "❌ Lỗi SQL: " + ex.getMessage());
            }
        });

        return panel;
    }

    private JPanel buildFindByIdContent() {
        JPanel panel = new JPanel(new BorderLayout());

        // Thanh tìm kiếm theo Seat ID
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JTextField idField = new JTextField(10);
        JButton searchBtn = new JButton("Tìm");
        searchPanel.add(new JLabel("Seat ID:"));
        searchPanel.add(idField);
        searchPanel.add(searchBtn);
        panel.add(searchPanel, BorderLayout.NORTH);

        // Form hiển thị thông tin seat
        JPanel formWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 15, 15));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        Dimension labelSize = new Dimension(100, 25);
        Dimension fieldSize = new Dimension(250, 28);

        JLabel lblRow = new JLabel("Row"); lblRow.setPreferredSize(labelSize);
        JLabel lblStatus = new JLabel("Status"); lblStatus.setPreferredSize(labelSize);
        JLabel lblRoomId = new JLabel("Room ID"); lblRoomId.setPreferredSize(labelSize);

        JTextField fRow = new JTextField(); fRow.setEditable(false); fRow.setPreferredSize(fieldSize);
        JTextField fStatus = new JTextField(); fStatus.setEditable(false); fStatus.setPreferredSize(fieldSize);
        JTextField fRoomId = new JTextField(); fRoomId.setEditable(false); fRoomId.setPreferredSize(fieldSize);

        formPanel.add(lblRow); formPanel.add(fRow);
        formPanel.add(lblStatus); formPanel.add(fStatus);
        formPanel.add(lblRoomId); formPanel.add(fRoomId);

        formWrapper.add(formPanel);
        panel.add(formWrapper, BorderLayout.CENTER);

        // Sự kiện tìm seat theo ID
        searchBtn.addActionListener(e -> {
            try {
                int id = Integer.parseInt(idField.getText());
                seat s = seatdao.getseatByid(id);
                if (s != null) {
                    fRow.setText(String.valueOf(s.getRow()));
                    fStatus.setText(s.getStatus());
                    fRoomId.setText(String.valueOf(s.getRoom().getRoomid()));
                } else {
                    fRow.setText("");
                    fStatus.setText("");
                    fRoomId.setText("");
                    JOptionPane.showMessageDialog(this, "❌ Không tìm thấy ghế!");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "❌ Seat ID phải là số nguyên!");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "❌ Lỗi SQL: " + ex.getMessage());
            }
        });

        return panel;
    }

    private JPanel buildShowAllContent() { 
    	JPanel panel = new JPanel(new BorderLayout());

        String[] columnNames = {"Seat ID", "Row", "Status", "Room ID"};
        Object[][] data;

        try {
            List<seat> seats = seatdao.getAllseat();
            data = new Object[seats.size()][4];
            for (int i = 0; i < seats.size(); i++) {
                seat s = seats.get(i);
                data[i][0] = s.getSeatid();
                data[i][1] = s.getRow();
                data[i][2] = s.getStatus();
                data[i][3] = s.getRoom().getRoomid(); 
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "❌ Lỗi SQL: " + ex.getMessage());
            data = new Object[0][4];
        }

        JTable table = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;

    }

    // ===== đổi nội dung CENTER =====
    private void switchContent(JPanel newContent) {
        if (contentPanel != null) {
            remove(contentPanel);
        }
        contentPanel = newContent;
        add(contentPanel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    // ===== tiện ích chuyển panel =====
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
