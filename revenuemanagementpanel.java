package doan;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.ArrayList;


public class revenuemanagementpanel extends JPanel {
    private final Connection conn;
    private final RevenueDAO revenuedao;
    private JPanel contentPanel;

    public revenuemanagementpanel(Connection conn) {
        setLayout(new BorderLayout());
        this.conn = conn;
        this.revenuedao = new RevenueDAO(conn);

        add(buildMenuPanel(), BorderLayout.WEST);
        add(buildTabButtons(), BorderLayout.NORTH);
        contentPanel = buildByDateContent();
        add(contentPanel, BorderLayout.CENTER);
    }

    // ===== MENU TRÁI =====
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


    // ===== TAB NÚT NGANG =====
    private JPanel buildTabButtons() {
        JPanel tabPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        JButton byDateBtn = new JButton("By Date");
        JButton byMonthBtn = new JButton("By Month");
        JButton reportByDayBtn = new JButton("Report By Day");
        JButton byMovieBtn = new JButton("By Movie Title");

        tabPanel.add(byDateBtn);
        tabPanel.add(byMonthBtn);
        tabPanel.add(reportByDayBtn);
        tabPanel.add(byMovieBtn);

        byDateBtn.addActionListener(e -> switchContent(buildByDateContent()));
        byMonthBtn.addActionListener(e -> switchContent(buildByMonthContent()));
        reportByDayBtn.addActionListener(e -> switchContent(buildReportByDayContent()));
        byMovieBtn.addActionListener(e -> switchContent(buildByMovieContent()));

        return tabPanel;
    }

    // ===== BY DATE =====
    private JPanel buildByDateContent() {
        JPanel panel = new JPanel(new BorderLayout());

        JPanel queryPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        JTextField dateField = new JTextField(10);
        JButton findBtn = new JButton("Find");
        JTextField sumField = new JTextField(10);
        sumField.setEditable(false);

        queryPanel.add(new JLabel("Date (yyyy-MM-dd):"));
        queryPanel.add(dateField);
        queryPanel.add(findBtn);
        queryPanel.add(new JLabel("Sum:"));
        queryPanel.add(sumField);

        panel.add(queryPanel, BorderLayout.NORTH);

        findBtn.addActionListener(e -> {
            try {
                LocalDate localDate = LocalDate.parse(dateField.getText());
                java.sql.Date sqlDate = java.sql.Date.valueOf(localDate);
                double total = revenuedao.getRevenueByDate(sqlDate);
                sumField.setText(String.valueOf(total));
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(this, "❌ Định dạng ngày không hợp lệ! Dùng yyyy-MM-dd");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "❌ Lỗi SQL: " + ex.getMessage());
            }
        });


        return panel;
    }

    // ===== BY MONTH =====
    private JPanel buildByMonthContent() {
    	JPanel panel = new JPanel(new BorderLayout());

        JPanel queryPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        JTextField monthField = new JTextField(5);
        JTextField yearField = new JTextField(5);
        JButton findBtn = new JButton("Find");
        JTextField sumField = new JTextField(10);
        sumField.setEditable(false);

        queryPanel.add(new JLabel("Month (1-12):"));
        queryPanel.add(monthField);
        queryPanel.add(new JLabel("Year:"));
        queryPanel.add(yearField);
        queryPanel.add(findBtn);
        queryPanel.add(new JLabel("Sum:"));
        queryPanel.add(sumField);

        panel.add(queryPanel, BorderLayout.NORTH);

        findBtn.addActionListener(e -> {
            try {
                int month = Integer.parseInt(monthField.getText());
                int year = Integer.parseInt(yearField.getText());

                if (month < 1 || month > 12) {
                    JOptionPane.showMessageDialog(this, "❌ Tháng phải từ 1 đến 12!");
                    return;
                }

                double total = revenuedao.getRevenueByMonth(month, year);
                sumField.setText(String.valueOf(total));
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "❌ Năm và tháng phải là số nguyên!");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "❌ Lỗi SQL: " + ex.getMessage());
            }
        });

        return panel;

    }

    // ===== REPORT BY DAY =====
    private JPanel buildReportByDayContent() {
        JPanel panel = new JPanel(new BorderLayout());

        JButton findBtn = new JButton("Find");
        panel.add(findBtn, BorderLayout.NORTH);

        String[] columnNames = {"ID", "Date", "Total"};
        Object[][] emptyData = new Object[0][3];
        JTable table = new JTable(emptyData, columnNames);
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        findBtn.addActionListener(e -> {
            try {
                List<Revenue> list = revenuedao.getRevenueReportByDay();
                Object[][] data = new Object[list.size()][3];
                for (int i = 0; i < list.size(); i++) {
                    Revenue r = list.get(i);
                    data[i][0] = r.getId();
                    data[i][1] = r.getPeriod();
                    data[i][2] = r.getTotal();
                }
                table.setModel(new DefaultTableModel(data, columnNames));
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "❌ Lỗi SQL: " + ex.getMessage());
            }
        });


        return panel;

    }

    // ===== BY MOVIE TITLE =====
    private JPanel buildByMovieContent() {
    	 JPanel panel = new JPanel(new BorderLayout());

    	    JPanel queryPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
    	    JTextField titleField = new JTextField(15);
    	    JButton findBtn = new JButton("Find");
    	    JTextField sumField = new JTextField(10);
    	    sumField.setEditable(false);

    	    queryPanel.add(new JLabel("Movie Title:"));
    	    queryPanel.add(titleField);
    	    queryPanel.add(findBtn);
    	    queryPanel.add(new JLabel("Sum:"));
    	    queryPanel.add(sumField);

    	    panel.add(queryPanel, BorderLayout.NORTH);

    	    findBtn.addActionListener(e -> {
    	        String title = titleField.getText().trim();
    	        if (title.isEmpty()) {
    	            JOptionPane.showMessageDialog(this, "❌ Vui lòng nhập tên phim!");
    	            return;
    	        }

    	        try {
    	            double total = revenuedao.getRevenueByMovie(title);
    	            sumField.setText(String.valueOf(total));
    	        } catch (SQLException ex) {
    	            JOptionPane.showMessageDialog(this, "❌ Lỗi SQL: " + ex.getMessage());
    	        }
    	    });

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
