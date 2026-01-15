package doan;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class ticketmanagementpanel extends JPanel {
    private final Connection conn;
    private final ticketdao ticketdao;
    private final showtimedao showtimedao;
    private final seatdao seatdao;
    private JPanel contentPanel;

    private JTextField ticketIdField, showtimeIdField, seatIdField, priceField;

    public ticketmanagementpanel(Connection conn) {
        setLayout(new BorderLayout());
        this.conn = conn;
        this.ticketdao = new ticketdao(conn);
        this.showtimedao = new showtimedao(conn);
        this.seatdao = new seatdao(conn);

        add(buildMenuPanel(), BorderLayout.WEST);
        add(buildTopButtons(), BorderLayout.NORTH);
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
        exitBtn.addActionListener(e -> showCard("welcome"));
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

    // ===== ADD TICKET =====
    private JPanel buildAddContent() {
        JPanel panel = new JPanel(new BorderLayout());

        // Bọc form để căn giữa
        JPanel formWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel form = new JPanel(new GridLayout(4, 2, 15, 15));
        form.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        Dimension labelSize = new Dimension(120, 25);
        Dimension fieldSize = new Dimension(250, 28);

        JLabel lblTicketId = new JLabel("Ticket ID"); lblTicketId.setPreferredSize(labelSize);
        JLabel lblShowtimeId = new JLabel("Showtime ID"); lblShowtimeId.setPreferredSize(labelSize);
        JLabel lblSeatId = new JLabel("Seat ID"); lblSeatId.setPreferredSize(labelSize);
        JLabel lblPrice = new JLabel("Price"); lblPrice.setPreferredSize(labelSize);

        ticketIdField = new JTextField(); ticketIdField.setEditable(false); ticketIdField.setPreferredSize(fieldSize);
        showtimeIdField = new JTextField(); showtimeIdField.setPreferredSize(fieldSize);
        seatIdField = new JTextField(); seatIdField.setPreferredSize(fieldSize);
        priceField = new JTextField(); priceField.setPreferredSize(fieldSize);

        form.add(lblTicketId); form.add(ticketIdField);
        form.add(lblShowtimeId); form.add(showtimeIdField);
        form.add(lblSeatId); form.add(seatIdField);
        form.add(lblPrice); form.add(priceField);

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
                int showtimeId = Integer.parseInt(showtimeIdField.getText());
                int seatId = Integer.parseInt(seatIdField.getText());
                double price = Double.parseDouble(priceField.getText());

                showtime st = showtimedao.getshowtimeByid(showtimeId);
                seat s = seatdao.getseatByid(seatId);

                if (st == null || s == null) {
                    JOptionPane.showMessageDialog(this, "❌ Showtime hoặc Seat không tồn tại!");
                    return;
                }

                ticket t = new ticket(0, st, s, price);
                ticketdao.insertticket(t);

                JOptionPane.showMessageDialog(this, "✅ Thêm vé thành công!");
                showtimeIdField.setText("");
                seatIdField.setText("");
                priceField.setText("");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "❌ Showtime ID, Seat ID và Price phải hợp lệ!");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "❌ Lỗi SQL: " + ex.getMessage());
            }
        });

        return panel;
    }


    // ===== UPDATE / DELETE / FIND / SHOW ALL =====
    private JPanel buildUpdateContent() {
        JPanel panel = new JPanel(new BorderLayout());

        // Thanh tìm kiếm theo Ticket ID
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JTextField idField = new JTextField(10);
        JButton searchBtn = new JButton("Tìm");
        searchPanel.add(new JLabel("Ticket ID:"));
        searchPanel.add(idField);
        searchPanel.add(searchBtn);
        panel.add(searchPanel, BorderLayout.NORTH);

        // Form chỉnh sửa
        JPanel formWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 15, 15));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        Dimension labelSize = new Dimension(120, 25);
        Dimension fieldSize = new Dimension(250, 28);

        JLabel lblShowtimeId = new JLabel("Showtime ID"); lblShowtimeId.setPreferredSize(labelSize);
        JLabel lblSeatId = new JLabel("Seat ID"); lblSeatId.setPreferredSize(labelSize);
        JLabel lblPrice = new JLabel("Price"); lblPrice.setPreferredSize(labelSize);

        JTextField updateShowtimeId = new JTextField(); updateShowtimeId.setEditable(false); updateShowtimeId.setPreferredSize(fieldSize);
        JTextField updateSeatId = new JTextField(); updateSeatId.setEditable(false); updateSeatId.setPreferredSize(fieldSize);
        JTextField updatePrice = new JTextField(); updatePrice.setEditable(false); updatePrice.setPreferredSize(fieldSize);

        formPanel.add(lblShowtimeId); formPanel.add(updateShowtimeId);
        formPanel.add(lblSeatId); formPanel.add(updateSeatId);
        formPanel.add(lblPrice); formPanel.add(updatePrice);

        formWrapper.add(formPanel);
        panel.add(formWrapper, BorderLayout.CENTER);

        // Nút Update căn giữa
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton confirmUpdateBtn = new JButton("Update");
        btnPanel.add(confirmUpdateBtn);
        panel.add(btnPanel, BorderLayout.SOUTH);

        // Sự kiện tìm ticket theo ID
        searchBtn.addActionListener(e -> {
            try {
                int id = Integer.parseInt(idField.getText());
                ticket t = ticketdao.getticketByid(id);
                if (t != null) {
                    updateShowtimeId.setText(String.valueOf(t.getShowtime().getShowtimeid()));
                    updateSeatId.setText(String.valueOf(t.getSeat().getSeatid()));
                    updatePrice.setText(String.valueOf(t.getPrice()));

                    updateShowtimeId.setEditable(true);
                    updateSeatId.setEditable(true);
                    updatePrice.setEditable(true);
                } else {
                    updateShowtimeId.setText("");
                    updateSeatId.setText("");
                    updatePrice.setText("");
                    JOptionPane.showMessageDialog(this, "❌ Không tìm thấy vé!");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "❌ Lỗi: " + ex.getMessage());
            }
        });

        // Sự kiện xác nhận Update
        confirmUpdateBtn.addActionListener(e -> {
            try {
                int id = Integer.parseInt(idField.getText());
                int showtimeId = Integer.parseInt(updateShowtimeId.getText());
                int seatId = Integer.parseInt(updateSeatId.getText());
                double price = Double.parseDouble(updatePrice.getText());

                showtime st = showtimedao.getshowtimeByid(showtimeId);
                seat s = seatdao.getseatByid(seatId);

                if (st == null || s == null) {
                    JOptionPane.showMessageDialog(this, "❌ Showtime hoặc Seat không tồn tại!");
                    return;
                }

                ticket t = new ticket(id, st, s, price);
                ticketdao.updateticket(t);

                JOptionPane.showMessageDialog(this, "✅ Cập nhật vé thành công!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "❌ Lỗi: " + ex.getMessage());
            }
        });

        return panel;
    }

    private JPanel buildDeleteContent() {
        JPanel panel = new JPanel(new BorderLayout());

        // Thanh tìm kiếm theo Ticket ID
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JTextField idField = new JTextField(10);
        JButton searchBtn = new JButton("Tìm");
        searchPanel.add(new JLabel("Ticket ID:"));
        searchPanel.add(idField);
        searchPanel.add(searchBtn);
        panel.add(searchPanel, BorderLayout.NORTH);

        // Form hiển thị thông tin ticket
        JPanel formWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 15, 15));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        Dimension labelSize = new Dimension(120, 25);
        Dimension fieldSize = new Dimension(250, 28);

        JLabel lblShowtimeId = new JLabel("Showtime ID"); lblShowtimeId.setPreferredSize(labelSize);
        JLabel lblSeatId = new JLabel("Seat ID"); lblSeatId.setPreferredSize(labelSize);
        JLabel lblPrice = new JLabel("Price"); lblPrice.setPreferredSize(labelSize);

        JTextField delShowtimeId = new JTextField(); delShowtimeId.setEditable(false); delShowtimeId.setPreferredSize(fieldSize);
        JTextField delSeatId = new JTextField(); delSeatId.setEditable(false); delSeatId.setPreferredSize(fieldSize);
        JTextField delPrice = new JTextField(); delPrice.setEditable(false); delPrice.setPreferredSize(fieldSize);

        formPanel.add(lblShowtimeId); formPanel.add(delShowtimeId);
        formPanel.add(lblSeatId); formPanel.add(delSeatId);
        formPanel.add(lblPrice); formPanel.add(delPrice);

        formWrapper.add(formPanel);
        panel.add(formWrapper, BorderLayout.CENTER);

        // Nút Delete căn giữa
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton confirmDeleteBtn = new JButton("Delete");
        btnPanel.add(confirmDeleteBtn);
        panel.add(btnPanel, BorderLayout.SOUTH);

        // Sự kiện tìm ticket theo ID
        searchBtn.addActionListener(e -> {
            try {
                int id = Integer.parseInt(idField.getText());
                ticket t = ticketdao.getticketByid(id);
                if (t != null) {
                    delShowtimeId.setText(String.valueOf(t.getShowtime().getShowtimeid()));
                    delSeatId.setText(String.valueOf(t.getSeat().getSeatid()));
                    delPrice.setText(String.valueOf(t.getPrice()));
                } else {
                    delShowtimeId.setText("");
                    delSeatId.setText("");
                    delPrice.setText("");
                    JOptionPane.showMessageDialog(this, "❌ Không tìm thấy vé!");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "❌ Lỗi: " + ex.getMessage());
            }
        });

        // Sự kiện xác nhận Delete
        confirmDeleteBtn.addActionListener(e -> {
            try {
                int id = Integer.parseInt(idField.getText());
                ticketdao.deleteticket(id);
                JOptionPane.showMessageDialog(this, "🗑️ Xóa vé thành công!");
                delShowtimeId.setText("");
                delSeatId.setText("");
                delPrice.setText("");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "❌ Lỗi: " + ex.getMessage());
            }
        });

        return panel;
    }

    private JPanel buildFindByIdContent() {
        JPanel panel = new JPanel(new BorderLayout());

        // Thanh tìm kiếm theo Ticket ID
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JTextField idField = new JTextField(10);
        JButton searchBtn = new JButton("Tìm");
        searchPanel.add(new JLabel("Ticket ID:"));
        searchPanel.add(idField);
        searchPanel.add(searchBtn);
        panel.add(searchPanel, BorderLayout.NORTH);

        // Form hiển thị thông tin ticket
        JPanel formWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 15, 15));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        Dimension labelSize = new Dimension(120, 25);
        Dimension fieldSize = new Dimension(250, 28);

        JLabel lblShowtimeId = new JLabel("Showtime ID"); lblShowtimeId.setPreferredSize(labelSize);
        JLabel lblSeatId = new JLabel("Seat ID"); lblSeatId.setPreferredSize(labelSize);
        JLabel lblPrice = new JLabel("Price"); lblPrice.setPreferredSize(labelSize);

        JTextField fShowtimeId = new JTextField(); fShowtimeId.setEditable(false); fShowtimeId.setPreferredSize(fieldSize);
        JTextField fSeatId = new JTextField(); fSeatId.setEditable(false); fSeatId.setPreferredSize(fieldSize);
        JTextField fPrice = new JTextField(); fPrice.setEditable(false); fPrice.setPreferredSize(fieldSize);

        formPanel.add(lblShowtimeId); formPanel.add(fShowtimeId);
        formPanel.add(lblSeatId); formPanel.add(fSeatId);
        formPanel.add(lblPrice); formPanel.add(fPrice);

        formWrapper.add(formPanel);
        panel.add(formWrapper, BorderLayout.CENTER);

        // Sự kiện tìm ticket theo ID
        searchBtn.addActionListener(e -> {
            try {
                int id = Integer.parseInt(idField.getText());
                ticket t = ticketdao.getticketByid(id);
                if (t != null) {
                    fShowtimeId.setText(String.valueOf(t.getShowtime().getShowtimeid()));
                    fSeatId.setText(String.valueOf(t.getSeat().getSeatid()));
                    fPrice.setText(String.valueOf(t.getPrice()));
                } else {
                    fShowtimeId.setText("");
                    fSeatId.setText("");
                    fPrice.setText("");
                    JOptionPane.showMessageDialog(this, "❌ Không tìm thấy vé!");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "❌ Lỗi: " + ex.getMessage());
            }
        });

        return panel;
    }

    private JPanel buildShowAllContent() {
        JPanel panel = new JPanel(new BorderLayout());

        String[] columnNames = {"Ticket ID", "Showtime ID", "Seat ID", "Price"};
        Object[][] data;

        try {
            List<ticket> list = ticketdao.getAllticket();
            data = new Object[list.size()][4];
            for (int i = 0; i < list.size(); i++) {
                ticket t = list.get(i);
                data[i][0] = t.getTicketid();
                data[i][1] = t.getShowtime().getShowtimeid();
                data[i][2] = t.getSeat().getSeatid();
                data[i][3] = t.getPrice();
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
