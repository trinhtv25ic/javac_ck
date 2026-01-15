package doan;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class bookingpanel extends JPanel {
    private Connection conn;
    private CardLayout card;
    private JPanel content;
    private guest currentGuest;
    private bookingdao bookingDao;

    public bookingpanel(Connection conn, CardLayout card, JPanel content,
                        guest currentGuest, bookingdao bookingDao) {
        this.conn = conn;
        this.card = card;
        this.content = content;
        this.currentGuest = currentGuest;
        this.bookingDao = bookingDao;

        setLayout(new BorderLayout());

        // Menu trên cùng
        add(buildMenuBar(), BorderLayout.NORTH);

        // Tiêu đề + nút Return
        JPanel header = new JPanel(new BorderLayout());
        JLabel title = new JLabel("Lịch sử đặt vé của " + currentGuest.getName(), SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        JButton returnBtn = new JButton("Return");
        returnBtn.addActionListener(e -> card.show(content, "guest_movie"));
        header.add(returnBtn, BorderLayout.WEST);
        header.add(title, BorderLayout.CENTER);
        add(header, BorderLayout.SOUTH);

        // Bảng lịch sử booking
        String[] columnNames = {"Booking ID", "Booking Time", "Movie Title", "Seat Row", "Room ID", "Start Time"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(model);
        table.setRowHeight(28);

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        loadBookings(model);
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

        // 👉 Gán sự kiện cho menu
        movieBtn.addActionListener(e -> card.show(content, "guest_movie"));

        showtimeBtn.addActionListener(e -> {
            String panelName = "guest_showtime";
            if (!panelExists(panelName)) {
            	showtimepanel stPanel = new showtimepanel(conn, card, content,currentGuest, bookingDao);
                content.add(stPanel, panelName);
            }
            card.show(content, panelName);
        });

        bookingBtn.addActionListener(e -> {
            String panelName = "guest_booking_" + currentGuest.getGuestid();
            if (!panelExists(panelName)) {
                bookingpanel bPanel = new bookingpanel(conn, card, content, currentGuest, bookingDao);
                content.add(bPanel, panelName);
            }
            card.show(content, panelName);
        });

        searchBtn.addActionListener(e -> {
            String panelName = "guest_search";
            if (!panelExists(panelName)) {
                searchpanel sPanel = new searchpanel(conn, card, content,currentGuest, bookingDao);
                content.add(sPanel, panelName);
            }
            card.show(content, panelName);
        });

        exitBtn.addActionListener(e -> card.show(content, "welcome"));

        return menu;
    }

    // Hàm tiện ích kiểm tra panel đã tồn tại chưa
    private boolean panelExists(String name) {
        for (Component comp : content.getComponents()) {
            if (name.equals(content.getLayout().toString())) {
                return true;
            }
        }
        return false;
    }

    // 👉 Load lịch sử booking của guest
    private void loadBookings(DefaultTableModel model) {
        try (PreparedStatement stmt = conn.prepareStatement(
                "SELECT b.bookingid, b.bookingtime, m.title, s.row, r.roomid, st.starttime " +
                "FROM booking b " +
                "JOIN ticket t ON b.ticketid = t.ticketid " +
                "JOIN seat s ON t.seatid = s.seatid " +
                "JOIN room r ON s.roomid = r.roomid " +
                "JOIN showtime st ON t.showtimeid = st.showtimeid " +
                "JOIN movie m ON st.movieid = m.movieid " +
                "WHERE b.guestid = ? ORDER BY b.bookingtime DESC")) {
            stmt.setInt(1, currentGuest.getGuestid());
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Object[] row = {
                    rs.getInt("bookingid"),
                    rs.getTimestamp("bookingtime"),
                    rs.getString("title"),
                    rs.getInt("row"),
                    rs.getInt("roomid"),
                    rs.getTimestamp("starttime")
                };
                model.addRow(row);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "❌ Lỗi SQL khi load booking: " + ex.getMessage());
        }
    }
}
