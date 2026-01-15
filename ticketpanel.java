package doan;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.time.LocalDateTime;

public class ticketpanel extends JPanel {
    private Connection conn;
    private CardLayout card;
    private JPanel content;
    private int showtimeId;
    private guest currentGuest;   // guest đang đăng nhập
    private bookingdao bookingDao; // DAO để insert booking

    public ticketpanel(Connection conn, CardLayout card, JPanel content, int showtimeId,
                       guest currentGuest, bookingdao bookingDao) {
        this.conn = conn;
        this.card = card;
        this.content = content;
        this.showtimeId = showtimeId;
        this.currentGuest = currentGuest;
        this.bookingDao = bookingDao;

        setLayout(new BorderLayout());

        // Menu trên cùng
        add(buildMenuBar(), BorderLayout.NORTH);

        // Tiêu đề + nút Return
        JPanel header = new JPanel(new BorderLayout());
        JLabel title = new JLabel("Danh sách vé - Showtime " + showtimeId, SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 18));
        JButton returnBtn = new JButton("Return");
        returnBtn.addActionListener(e -> card.show(content, "guest_seat_" + showtimeId));
        header.add(returnBtn, BorderLayout.WEST);
        header.add(title, BorderLayout.CENTER);
        add(header, BorderLayout.SOUTH);

        // Bảng vé
        String[] columnNames = {"Showtime ID", "Ticket ID", "Price", "Status", "Booking"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4; // chỉ cột Booking có nút
            }
        };

        JTable table = new JTable(model);
        table.setRowHeight(28);

        table.getColumn("Booking").setCellRenderer(new ButtonRenderer("Booking"));
        table.getColumn("Booking").setCellEditor(
            new BookingButtonEditor(new JCheckBox(), conn, card, content, table,
                                    currentGuest, bookingDao, showtimeId)
        );

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        loadTickets(model);
    }

    private JPanel buildMenuBar() {
        JPanel menu = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        JButton movieBtn = new JButton("Movie");
        JButton showtimeBtn = new JButton("Showtime");
        JButton bookingBtn = new JButton("Booking");
        JButton searchBtn = new JButton("Search");
        JButton exitBtn = new JButton("Exit");

        menu.add(movieBtn);
        menu.add(showtimeBtn);
        menu.add(bookingBtn);
        menu.add(searchBtn);
        menu.add(exitBtn);

        movieBtn.addActionListener(e -> card.show(content, "guest_movie"));
        showtimeBtn.addActionListener(e -> card.show(content, "guest_showtime"));
        bookingBtn.addActionListener(e -> card.show(content, "guest_booking"));
        searchBtn.addActionListener(e -> card.show(content, "guest_search"));
        exitBtn.addActionListener(e -> card.show(content, "welcome"));

        return menu;
    }

    // 👉 Lấy danh sách vé theo showtimeId, kèm status của seat
    private void loadTickets(DefaultTableModel model) {
        try (PreparedStatement stmt = conn.prepareStatement(
                "SELECT t.ticketid, t.price, s.status " +
                "FROM ticket t JOIN seat s ON t.seatid = s.seatid " +
                "WHERE t.showtimeid = ?")) {
            stmt.setInt(1, showtimeId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Object[] row = {
                    showtimeId,
                    rs.getInt("ticketid"),
                    rs.getDouble("price"),
                    rs.getString("status"),
                    "Booking"
                };
                model.addRow(row);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "❌ Lỗi SQL khi load vé: " + ex.getMessage());
        }
    }

    // Renderer cho nút
    static class ButtonRenderer extends JButton implements javax.swing.table.TableCellRenderer {
        public ButtonRenderer(String text) { setText(text); }
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus,
                                                       int row, int column) {
            return this;
        }
    }

    // Editor cho nút Booking
    static class BookingButtonEditor extends DefaultCellEditor {
        private JButton button;
        private boolean clicked;
        private JTable table;
        private Connection conn;
        private CardLayout card;
        private JPanel content;
        private guest currentGuest;
        private bookingdao bookingDao;
        private int showtimeId;

        public BookingButtonEditor(JCheckBox checkBox, Connection conn, CardLayout card, JPanel content,
                                   JTable table, guest currentGuest, bookingdao bookingDao, int showtimeId) {
            super(checkBox);
            this.conn = conn;
            this.card = card;
            this.content = content;
            this.table = table;
            this.currentGuest = currentGuest;
            this.bookingDao = bookingDao;
            this.showtimeId = showtimeId;

            button = new JButton("Booking");
            button.addActionListener(e -> fireEditingStopped());
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            clicked = true;
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            if (clicked) {
                try {
                    int row = table.getSelectedRow();
                    int ticketId = (int) table.getValueAt(row, 1);
                    double price = (double) table.getValueAt(row, 2);
                    String status = table.getValueAt(row, 3).toString();

                    // 👉 Nếu ghế đã được đặt thì không cho đặt
                    if (status.equalsIgnoreCase("false")) {
                        JOptionPane.showMessageDialog(button, "❌ Ghế này đã được đặt. Vui lòng chọn ghế khác!");
                        return "Booking";
                    }

                    // Nếu còn chỗ thì tạo booking
                    ticket t = new ticket(ticketId);
                    booking b = new booking();
                    b.setTicket(t);
                    b.setGuest(currentGuest);
                    b.setBookingTime(LocalDateTime.now());

                    bookingDao.insertbooking(b);

                    JOptionPane.showMessageDialog(button,
                            "✅ Đặt vé thành công! Mã booking: " + b.getBookingid());

                    // 👉 Sau khi đặt, cập nhật status ghế thành false
                    try (PreparedStatement updateStmt = conn.prepareStatement(
                            "UPDATE seat SET status = 'false' WHERE seatid = " +
                            "(SELECT seatid FROM ticket WHERE ticketid = ?)")) {
                        updateStmt.setInt(1, ticketId);
                        updateStmt.executeUpdate();
                    }

                    // Refresh lại bảng
                    ((DefaultTableModel) table.getModel()).setRowCount(0);
                    ((ticketpanel) content.getComponent(content.getComponentCount() - 1)).loadTickets((DefaultTableModel) table.getModel());

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(button, "❌ Lỗi khi đặt vé: " + ex.getMessage());
                    ex.printStackTrace();
                }
            }
            clicked = false;
            return "Booking";
        }

        @Override
        public boolean stopCellEditing() {
            clicked = false;
            return super.stopCellEditing();
        }
    }
}
