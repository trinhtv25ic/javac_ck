package doan;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.sql.*;

public class movielistpanel extends JPanel {
    private Connection conn;
    private CardLayout card;
    private JPanel content;
    private guest currentGuest;       // guest đang đăng nhập
    private bookingdao bookingDao;    // DAO booking

    public movielistpanel(Connection conn, CardLayout card, JPanel content,
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
        JLabel title = new JLabel("Danh sách phim (Guest)", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 22));
        JButton returnBtn = new JButton("Return");
        returnBtn.addActionListener(e -> card.show(content, "guest_movie"));
        header.add(returnBtn, BorderLayout.WEST);
        header.add(title, BorderLayout.CENTER);
        add(header, BorderLayout.SOUTH);

        // Bảng phim
        String[] columnNames = {"Movie ID", "Title", "Author", "Duration", "Genre", "Rating", "Description", "Action"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 7; // chỉ cột Action có nút
            }
        };

        JTable table = new JTable(model);
        table.setRowHeight(28);

        // Ẩn cột Movie ID
        TableColumn idColumn = table.getColumnModel().getColumn(0);
        idColumn.setMinWidth(0);
        idColumn.setMaxWidth(0);
        idColumn.setPreferredWidth(0);

        table.getColumn("Action").setCellRenderer(new ButtonRenderer("Showtime"));
        table.getColumn("Action").setCellEditor(
            new ShowtimeButtonEditor(new JCheckBox(), conn, card, content, table, currentGuest, bookingDao)
        );

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        loadMovies(model);
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
        	showtimepanel stPanel = new showtimepanel(conn, card, content,currentGuest, bookingDao);
            content.add(stPanel, "guest_showtime");
            card.show(content, "guest_showtime");
        });

        bookingBtn.addActionListener(e -> {
        	bookingpanel bPanel = new bookingpanel(conn, card, content, currentGuest, bookingDao);
            content.add(bPanel, "guest_booking_" + currentGuest.getGuestid());
            card.show(content, "guest_booking_" + currentGuest.getGuestid());
        });

        searchBtn.addActionListener(e -> {
        	searchpanel sPanel=new searchpanel(conn, card,content,currentGuest, bookingDao);
        	content.add(sPanel,"guest_search");
        	card.show(content, "guest_search");
        });
        exitBtn.addActionListener(e -> card.show(content, "welcome"));

        return menu;
    }

    private void loadMovies(DefaultTableModel model) {
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT movieid, title, author, duration, genre, rating, description FROM movie")) {

            while (rs.next()) {
                Object[] row = {
                    rs.getInt("movieid"),
                    rs.getString("title"),
                    rs.getString("author"),
                    rs.getInt("duration"),
                    rs.getString("genre"),
                    rs.getString("rating"),
                    rs.getString("description"),
                    "Showtime"
                };
                model.addRow(row);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "❌ Lỗi SQL khi load phim: " + ex.getMessage());
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

    // Editor cho nút Showtime trong bảng phim
    static class ShowtimeButtonEditor extends DefaultCellEditor {
        private JButton button;
        private boolean clicked;
        private JTable table;
        private Connection conn;
        private CardLayout card;
        private JPanel content;
        private guest currentGuest;
        private bookingdao bookingDao;

        public ShowtimeButtonEditor(JCheckBox checkBox, Connection conn, CardLayout card, JPanel content,
                                    JTable table, guest currentGuest, bookingdao bookingDao) {
            super(checkBox);
            this.conn = conn;
            this.card = card;
            this.content = content;
            this.table = table;
            this.currentGuest = currentGuest;
            this.bookingDao = bookingDao;

            button = new JButton("Showtime");
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
                if (row >= 0) {
                    int movieId = (int) table.getValueAt(row, 0);
                    String title = table.getValueAt(row, 1).toString();

                    // 👉 Chuyển sang panel suất chiếu của phim này
                    showtimeforguestpanel stPanel = new showtimeforguestpanel(
                        conn, card, content, movieId, title, currentGuest, bookingDao
                    );
                    content.add(stPanel, "guest_showtime_" + movieId);
                    card.show(content, "guest_showtime_" + movieId);
                }
            }
            clicked = false;
            return "Showtime";
        }

        @Override
        public boolean stopCellEditing() {
            clicked = false;
            return super.stopCellEditing();
        }
    }
}
