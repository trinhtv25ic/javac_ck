package doan;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class seatpanel extends JPanel {
    private Connection conn;
    private CardLayout card;
    private JPanel content;
    private int showtimeId;
    private int roomId;
    private String startTime;
    private guest currentGuest;
    private bookingdao bookingDao;

    public seatpanel(Connection conn, CardLayout card, JPanel content,
                     int showtimeId, int roomId, String startTime,
                     guest currentGuest, bookingdao bookingDao) {
        this.conn = conn;
        this.card = card;
        this.content = content;
        this.showtimeId = showtimeId;
        this.roomId = roomId;
        this.startTime = startTime;
        this.currentGuest = currentGuest;
        this.bookingDao = bookingDao;

        setLayout(new BorderLayout());

        // Menu trên cùng
        add(buildMenuBar(), BorderLayout.NORTH);

        // Tiêu đề + nút Return
        JPanel header = new JPanel(new BorderLayout());
        JLabel title = new JLabel("Danh sách ghế - Showtime " + showtimeId +
                " | Room " + roomId + " | Start: " + startTime, SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 18));
        JButton returnBtn = new JButton("Return");
        returnBtn.addActionListener(e -> card.show(content, "guest_showtime_" + showtimeId));
        header.add(returnBtn, BorderLayout.WEST);
        header.add(title, BorderLayout.CENTER);
        add(header, BorderLayout.SOUTH);

        // Bảng ghế
        String[] columnNames = {"Seat ID", "Row", "Room ID", "Status", "Ticket"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4; // chỉ cột Ticket có nút
            }
        };

        JTable table = new JTable(model);
        table.setRowHeight(28);

        table.getColumn("Ticket").setCellRenderer(new ButtonRenderer("Ticket"));
        table.getColumn("Ticket").setCellEditor(
            new TicketButtonEditor(new JCheckBox(), conn, card, content, table,
                                   showtimeId, currentGuest, bookingDao)
        );

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        loadSeats(model);
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
        showtimeBtn.addActionListener(e -> card.show(content, "guest_showtime_" + showtimeId));
        bookingBtn.addActionListener(e -> card.show(content, "guest_booking"));
        searchBtn.addActionListener(e -> card.show(content, "guest_search"));
        exitBtn.addActionListener(e -> card.show(content, "welcome"));

        return menu;
    }

    // 👉 Sửa câu SQL: lọc theo roomId và dùng đúng cột
    private void loadSeats(DefaultTableModel model) {
        try (PreparedStatement stmt = conn.prepareStatement(
                "SELECT seatid, row, roomid, status FROM seat WHERE roomid = ?")) {
            stmt.setInt(1, roomId);   // dùng roomId để lọc
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Object[] row = {
                    rs.getInt("seatid"),
                    rs.getInt("row"),
                    rs.getInt("roomid"),
                    rs.getString("status"),
                    "Ticket"
                };
                model.addRow(row);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "❌ Lỗi SQL khi load ghế: " + ex.getMessage());
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

    // Editor cho nút Ticket
    static class TicketButtonEditor extends DefaultCellEditor {
        private JButton button;
        private boolean clicked;
        private JTable table;
        private Connection conn;
        private CardLayout card;
        private JPanel content;
        private int showtimeId;
        private guest currentGuest;
        private bookingdao bookingDao;

        public TicketButtonEditor(JCheckBox checkBox, Connection conn, CardLayout card, JPanel content,
                                  JTable table, int showtimeId, guest currentGuest, bookingdao bookingDao) {
            super(checkBox);
            this.conn = conn;
            this.card = card;
            this.content = content;
            this.table = table;
            this.showtimeId = showtimeId;
            this.currentGuest = currentGuest;
            this.bookingDao = bookingDao;

            button = new JButton("Ticket");
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
                int row = table.getSelectedRow();
                int seatId = (int) table.getValueAt(row, 0);
                int seatRow = (int) table.getValueAt(row, 1);
                int roomId = (int) table.getValueAt(row, 2);
                String status = table.getValueAt(row, 3).toString();

                // 👉 Nếu ghế đã được đặt thì không cho đặt
                if (status.equalsIgnoreCase("false")) {
                    JOptionPane.showMessageDialog(button, "❌ Ghế này đã được đặt. Vui lòng chọn ghế khác!");
                    return "Ticket";
                }

                // 👉 Nếu còn chỗ thì chuyển sang ticketpanel
                ticketpanel ticketPanel = new ticketpanel(conn, card, content,
                        showtimeId, currentGuest, bookingDao);

                content.add(ticketPanel, "guest_ticket_" + showtimeId + "_" + seatId);
                card.show(content, "guest_ticket_" + showtimeId + "_" + seatId);
            }
            clicked = false;
            return "Ticket";
        }

        @Override
        public boolean stopCellEditing() {
            clicked = false;
            return super.stopCellEditing();
        }
    }
}
