package doan;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class searchpanel extends JPanel {
    private Connection conn;
    private CardLayout card;
    private JPanel content;
    private guest currentGuest;     
    private bookingdao bookingDao;

    public searchpanel(Connection conn, CardLayout card, JPanel content,
                       guest currentGuest, bookingdao bookingDao) {
        this.conn = conn;
        this.card = card;
        this.content = content;
        this.currentGuest = currentGuest;
        this.bookingDao = bookingDao;

        setLayout(new BorderLayout());

        // Menu phía trên
        add(buildMenuBar(), BorderLayout.NORTH);

        // Tiêu đề + nút Return
        JPanel header = new JPanel(new BorderLayout());
        JLabel title = new JLabel("Tìm kiếm phim theo Title", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        JButton returnBtn = new JButton("Return");
        returnBtn.addActionListener(e -> card.show(content, "guest_movie"));
        header.add(returnBtn, BorderLayout.WEST);
        header.add(title, BorderLayout.CENTER);
        add(header, BorderLayout.SOUTH);

        // Ô nhập title + nút Search
        JPanel searchBox = new JPanel(new FlowLayout());
        JTextField titleField = new JTextField(20);
        JButton searchBtn = new JButton("Search");
        searchBox.add(new JLabel("Nhập Title:"));
        searchBox.add(titleField);
        searchBox.add(searchBtn);
        add(searchBox, BorderLayout.NORTH);

        // Bảng kết quả
        String[] columnNames = {"Movie ID", "Title", "Author", "Duration", "Genre", "Rating", "Description"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(model);
        table.setRowHeight(28);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Sự kiện nút Search
        searchBtn.addActionListener(e -> {
            String keyword = titleField.getText().trim();
            if (!keyword.isEmpty()) {
                model.setRowCount(0); // clear bảng
                loadMovieByTitle(model, keyword);
            } else {
                JOptionPane.showMessageDialog(this, "❌ Vui lòng nhập Title để tìm kiếm!");
            }
        });
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
                searchpanel sp = new searchpanel(conn, card, content, currentGuest, bookingDao);
                content.add(sp, panelName);
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

    // 👉 Load movie theo title
    private void loadMovieByTitle(DefaultTableModel model, String keyword) {
        try (PreparedStatement stmt = conn.prepareStatement(
                "SELECT movieid, title, author, duration, genre, rating, description " +
                "FROM movie WHERE title LIKE ?")) {
            stmt.setString(1, "%" + keyword + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Object[] row = {
                    rs.getInt("movieid"),
                    rs.getString("title"),
                    rs.getString("author"),
                    rs.getInt("duration"),
                    rs.getString("genre"),
                    rs.getString("rating"),
                    rs.getString("description")
                };
                model.addRow(row);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "❌ Lỗi SQL khi tìm kiếm: " + ex.getMessage());
        }
    }
}
