package doan;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;

public class bookingmanagementpanel extends JPanel {
    private final Connection conn;
    private final bookingdao bookingdao;
    private final ticketdao ticketdao;
    private final guestdao guestdao;
    private JPanel contentPanel;

    private JTextField bookingIdField, ticketIdField, guestIdField, bookingTimeField;

    public bookingmanagementpanel(Connection conn) {
        setLayout(new BorderLayout());
        this.conn = conn;
        this.bookingdao = new bookingdao(conn);
        this.ticketdao = new ticketdao(conn);
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
        exitBtn.addActionListener(e -> System.exit(0));
        menuPanel.add(exitBtn);

        return menuPanel;
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

        addBtn.addActionListener(e -> switchContent(buildAddContent()));
        updateBtn.addActionListener(e -> switchContent(buildUpdateContent()));
        deleteBtn.addActionListener(e -> switchContent(buildDeleteContent()));
        findByIdBtn.addActionListener(e -> switchContent(buildFindByIdContent()));
        showAllBtn.addActionListener(e -> switchContent(buildShowAllContent()));

        return topButtons;
    }

    // ===== ADD BOOKING =====
    private JPanel buildAddContent() {
        JPanel panel = new JPanel(new BorderLayout());

        // Bọc form để căn giữa
        JPanel formWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel form = new JPanel(new GridLayout(4, 2, 15, 15));
        form.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        Dimension labelSize = new Dimension(160, 25);
        Dimension fieldSize = new Dimension(250, 28);

        JLabel lblBookingId = new JLabel("Booking ID"); lblBookingId.setPreferredSize(labelSize);
        JLabel lblTicketId = new JLabel("Ticket ID"); lblTicketId.setPreferredSize(labelSize);
        JLabel lblGuestId = new JLabel("Guest ID"); lblGuestId.setPreferredSize(labelSize);
        JLabel lblBookingTime = new JLabel("Booking Time (yyyy-MM-ddTHH:mm)"); lblBookingTime.setPreferredSize(labelSize);

        bookingIdField = new JTextField(); bookingIdField.setEditable(false); bookingIdField.setPreferredSize(fieldSize);
        ticketIdField = new JTextField(); ticketIdField.setPreferredSize(fieldSize);
        guestIdField = new JTextField(); guestIdField.setPreferredSize(fieldSize);
        bookingTimeField = new JTextField(); bookingTimeField.setPreferredSize(fieldSize);

        form.add(lblBookingId); form.add(bookingIdField);
        form.add(lblTicketId); form.add(ticketIdField);
        form.add(lblGuestId); form.add(guestIdField);
        form.add(lblBookingTime); form.add(bookingTimeField);

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
                int ticketId = Integer.parseInt(ticketIdField.getText());
                int guestId = Integer.parseInt(guestIdField.getText());
                String timeStr = bookingTimeField.getText();

                ticket t = ticketdao.getticketByid(ticketId);
                guest g = guestdao.getguestByid(guestId);

                if (t == null || g == null) {
                    JOptionPane.showMessageDialog(this, "❌ Ticket hoặc Guest không tồn tại!");
                    return;
                }

                LocalDateTime bookingTime = LocalDateTime.parse(timeStr);
                booking b = new booking(0, t, g, bookingTime);
                bookingdao.insertbooking(b);

                JOptionPane.showMessageDialog(this, "✅ Thêm booking thành công!");
                ticketIdField.setText("");
                guestIdField.setText("");
                bookingTimeField.setText("");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "❌ Ticket ID và Guest ID phải là số nguyên!");
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(this, "❌ Định dạng thời gian không hợp lệ! Dùng yyyy-MM-ddTHH:mm");
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
        searchPanel.add(new JLabel("Booking ID:"));
        searchPanel.add(idField);
        searchPanel.add(searchBtn);
        panel.add(searchPanel, BorderLayout.NORTH);

        // Form chỉnh sửa
        JPanel formWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 15, 15));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        Dimension labelSize = new Dimension(160, 25);
        Dimension fieldSize = new Dimension(250, 28);

        JLabel lblTicketId = new JLabel("Ticket ID"); lblTicketId.setPreferredSize(labelSize);
        JLabel lblGuestId = new JLabel("Guest ID"); lblGuestId.setPreferredSize(labelSize);
        JLabel lblBookingTime = new JLabel("Booking Time (yyyy-MM-ddTHH:mm)"); lblBookingTime.setPreferredSize(labelSize);

        JTextField updateTicketId = new JTextField(); updateTicketId.setEditable(false); updateTicketId.setPreferredSize(fieldSize);
        JTextField updateGuestId = new JTextField(); updateGuestId.setEditable(false); updateGuestId.setPreferredSize(fieldSize);
        JTextField updateBookingTime = new JTextField(); updateBookingTime.setEditable(false); updateBookingTime.setPreferredSize(fieldSize);

        formPanel.add(lblTicketId); formPanel.add(updateTicketId);
        formPanel.add(lblGuestId); formPanel.add(updateGuestId);
        formPanel.add(lblBookingTime); formPanel.add(updateBookingTime);

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
                booking b = bookingdao.getbookingByid(id);
                if (b != null) {
                    updateTicketId.setText(String.valueOf(b.getTicket().getTicketid()));
                    updateGuestId.setText(String.valueOf(b.getGuest().getGuestid()));
                    updateBookingTime.setText(b.getBookingTime().toString());

                    updateTicketId.setEditable(true);
                    updateGuestId.setEditable(true);
                    updateBookingTime.setEditable(true);
                } else {
                    JOptionPane.showMessageDialog(this, "❌ Không tìm thấy booking!");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "❌ Lỗi: " + ex.getMessage());
            }
        });

        // Sự kiện cập nhật
        confirmUpdateBtn.addActionListener(e -> {
            try {
                int id = Integer.parseInt(idField.getText());
                int ticketId = Integer.parseInt(updateTicketId.getText());
                int guestId = Integer.parseInt(updateGuestId.getText());
                LocalDateTime time = LocalDateTime.parse(updateBookingTime.getText());

                ticket t = ticketdao.getticketByid(ticketId);
                guest g = guestdao.getguestByid(guestId);

                if (t == null || g == null) {
                    JOptionPane.showMessageDialog(this, "❌ Ticket hoặc Guest không tồn tại!");
                    return;
                }

                booking b = new booking(id, t, g, time);
                bookingdao.updatebooking(b);

                JOptionPane.showMessageDialog(this, "✅ Cập nhật booking thành công!");
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
        JButton searchBtn = new JButton("Tìm");
        searchPanel.add(new JLabel("Booking ID:"));
        searchPanel.add(idField);
        searchPanel.add(searchBtn);
        panel.add(searchPanel, BorderLayout.NORTH);

        // Form hiển thị thông tin (chỉ đọc)
        JPanel formWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 15, 15));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        Dimension labelSize = new Dimension(120, 25);
        Dimension fieldSize = new Dimension(250, 28);

        JLabel lblTicketId = new JLabel("Ticket ID"); lblTicketId.setPreferredSize(labelSize);
        JLabel lblGuestId = new JLabel("Guest ID"); lblGuestId.setPreferredSize(labelSize);
        JLabel lblBookingTime = new JLabel("Booking Time"); lblBookingTime.setPreferredSize(labelSize);

        JTextField delTicketId = new JTextField(); delTicketId.setEditable(false); delTicketId.setPreferredSize(fieldSize);
        JTextField delGuestId = new JTextField(); delGuestId.setEditable(false); delGuestId.setPreferredSize(fieldSize);
        JTextField delBookingTime = new JTextField(); delBookingTime.setEditable(false); delBookingTime.setPreferredSize(fieldSize);

        formPanel.add(lblTicketId); formPanel.add(delTicketId);
        formPanel.add(lblGuestId); formPanel.add(delGuestId);
        formPanel.add(lblBookingTime); formPanel.add(delBookingTime);

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
                booking b = bookingdao.getbookingByid(id);
                if (b != null) {
                    delTicketId.setText(String.valueOf(b.getTicket().getTicketid()));
                    delGuestId.setText(String.valueOf(b.getGuest().getGuestid()));
                    delBookingTime.setText(b.getBookingTime().toString());
                } else {
                    JOptionPane.showMessageDialog(this, "❌ Không tìm thấy booking!");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "❌ Lỗi: " + ex.getMessage());
            }
        });

        // Sự kiện Delete
        confirmDeleteBtn.addActionListener(e -> {
            try {
                int id = Integer.parseInt(idField.getText());
                bookingdao.deletebooking(id);
                JOptionPane.showMessageDialog(this, "🗑️ Xóa booking thành công!");
                delTicketId.setText("");
                delGuestId.setText("");
                delBookingTime.setText("");
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
        searchPanel.add(new JLabel("Booking ID:"));
        searchPanel.add(idField);
        searchPanel.add(searchBtn);
        panel.add(searchPanel, BorderLayout.NORTH);

        // Form hiển thị thông tin (chỉ đọc)
        JPanel formWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 15, 15));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        Dimension labelSize = new Dimension(120, 25);
        Dimension fieldSize = new Dimension(250, 28);

        JLabel lblTicketId = new JLabel("Ticket ID"); lblTicketId.setPreferredSize(labelSize);
        JLabel lblGuestId = new JLabel("Guest ID"); lblGuestId.setPreferredSize(labelSize);
        JLabel lblBookingTime = new JLabel("Booking Time"); lblBookingTime.setPreferredSize(labelSize);

        JTextField fTicketId = new JTextField(); fTicketId.setEditable(false); fTicketId.setPreferredSize(fieldSize);
        JTextField fGuestId = new JTextField(); fGuestId.setEditable(false); fGuestId.setPreferredSize(fieldSize);
        JTextField fBookingTime = new JTextField(); fBookingTime.setEditable(false); fBookingTime.setPreferredSize(fieldSize);

        formPanel.add(lblTicketId); formPanel.add(fTicketId);
        formPanel.add(lblGuestId); formPanel.add(fGuestId);
        formPanel.add(lblBookingTime); formPanel.add(fBookingTime);

        formWrapper.add(formPanel);
        panel.add(formWrapper, BorderLayout.CENTER);

        // Sự kiện tìm kiếm
        searchBtn.addActionListener(e -> {
            try {
                int id = Integer.parseInt(idField.getText());
                booking b = bookingdao.getbookingByid(id);
                if (b != null) {
                    fTicketId.setText(String.valueOf(b.getTicket().getTicketid()));
                    fGuestId.setText(String.valueOf(b.getGuest().getGuestid()));
                    fBookingTime.setText(b.getBookingTime().toString());
                } else {
                    JOptionPane.showMessageDialog(this, "❌ Không tìm thấy booking!");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "❌ Lỗi: " + ex.getMessage());
            }
        });

        return panel;
    }

    // ===== SHOW ALL BOOKING =====
    private JPanel buildShowAllContent() {
        JPanel panel = new JPanel(new BorderLayout());

        String[] columnNames = {"Booking ID", "Ticket ID", "Guest ID", "Booking Time"};
        Object[][] data;

        try {
            List<booking> list = bookingdao.getAllbooking();
            data = new Object[list.size()][4];
            for (int i = 0; i < list.size(); i++) {
                booking b = list.get(i);
                data[i][0] = b.getBookingid();
                data[i][1] = b.getTicket().getTicketid();
                data[i][2] = b.getGuest().getGuestid();
                data[i][3] = b.getBookingTime().toString();
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
