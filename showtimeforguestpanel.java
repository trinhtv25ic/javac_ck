package doan;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class showtimeforguestpanel extends JPanel {
    private Connection conn;
    private CardLayout card;
    private JPanel content;
    private int movieId;
    private String movieTitle;
    private guest currentGuest;       // thêm guest
    private bookingdao bookingDao;    // thêm bookingdao

    public showtimeforguestpanel(Connection conn, CardLayout card, JPanel content,
                                 int movieId, String movieTitle,
                                 guest currentGuest, bookingdao bookingDao) {
        this.conn = conn;
        this.card = card;
        this.content = content;
        this.movieId = movieId;
        this.movieTitle = movieTitle;
        this.currentGuest = currentGuest;
        this.bookingDao = bookingDao;

        setLayout(new BorderLayout());

        // Menu trên cùng
        add(buildMenuBar(), BorderLayout.NORTH);

        // Tiêu đề + nút Return
        JPanel header = new JPanel(new BorderLayout());
        JLabel title = new JLabel("Suất chiếu của phim: " + movieTitle, SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        JButton returnBtn = new JButton("Return");
        returnBtn.addActionListener(e -> card.show(content, "guest_movie"));
        header.add(returnBtn, BorderLayout.WEST);
        header.add(title, BorderLayout.CENTER);
        add(header, BorderLayout.SOUTH);

        // Bảng suất chiếu
        String[] columnNames = {"Showtime ID", "Room ID", "Start Time", "Seat"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 3; // chỉ cột Seat có nút
            }
        };

        JTable table = new JTable(model);
        table.setRowHeight(28);

        table.getColumn("Seat").setCellRenderer(new ButtonRenderer("Seat"));
        table.getColumn("Seat").setCellEditor(
            new SeatButtonEditor(new JCheckBox(), conn, card, content, table, currentGuest, bookingDao)
        );

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        loadShowtimes(model);
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

    private void loadShowtimes(DefaultTableModel model) {
        try (PreparedStatement stmt = conn.prepareStatement(
                "SELECT showtimeid, roomid, starttime FROM showtime WHERE movieid = ?")) {
            stmt.setInt(1, movieId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Object[] row = {
                    rs.getInt("showtimeid"),
                    rs.getInt("roomid"),
                    rs.getString("starttime"),
                    "Seat" // nút bấm
                };
                model.addRow(row);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "❌ Lỗi SQL khi load suất chiếu: " + ex.getMessage());
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

    // Editor cho nút Seat
    static class SeatButtonEditor extends DefaultCellEditor {
        private JButton button;
        private boolean clicked;
        private JTable table;
        private Connection conn;
        private CardLayout card;
        private JPanel content;
        private guest currentGuest;
        private bookingdao bookingDao;

        public SeatButtonEditor(JCheckBox checkBox, Connection conn, CardLayout card, JPanel content,
                                JTable table, guest currentGuest, bookingdao bookingDao) {
            super(checkBox);
            this.conn = conn;
            this.card = card;
            this.content = content;
            this.table = table;
            this.currentGuest = currentGuest;
            this.bookingDao = bookingDao;

            button = new JButton("Seat");
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
                int showtimeId = (int) table.getValueAt(row, 0);
                int roomId = (int) table.getValueAt(row, 1);
                String startTime = table.getValueAt(row, 2).toString();

                // 👉 Chuyển sang panel seat để hiển thị danh sách ghế
                seatpanel seatPanel = new seatpanel(conn, card, content,
                        showtimeId, roomId, startTime, currentGuest, bookingDao);

                content.add(seatPanel, "guest_seat_" + showtimeId);
                card.show(content, "guest_seat_" + showtimeId);
            }
            clicked = false;
            return "Seat";
        }

        @Override
        public boolean stopCellEditing() {
            clicked = false;
            return super.stopCellEditing();
        }
    }
}
