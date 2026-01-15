package doan;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;

public class showtimemanagementpanel extends JPanel {
    private final Connection conn;
    private final showtimedao showtimedao;
    private final moviedao moviedao;
    private final roomdao roomdao;
    private JPanel contentPanel;

    private JTextField showtimeIdField, movieIdField, roomIdField, startTimeField;

    public showtimemanagementpanel(Connection conn) {
        setLayout(new BorderLayout());
        this.conn = conn;
        this.showtimedao = new showtimedao(conn);
        this.moviedao = new moviedao(conn);
        this.roomdao = new roomdao(conn);

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

    // ===== ADD SHOWTIME =====
    private JPanel buildAddContent() {
        JPanel panel = new JPanel(new BorderLayout());

        // Bọc form để căn giữa
        JPanel formWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel form = new JPanel(new GridLayout(4, 2, 15, 15));
        form.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        Dimension labelSize = new Dimension(160, 25);
        Dimension fieldSize = new Dimension(250, 28);

        JLabel lblShowtimeId = new JLabel("Showtime ID"); lblShowtimeId.setPreferredSize(labelSize);
        JLabel lblMovieId = new JLabel("Movie ID"); lblMovieId.setPreferredSize(labelSize);
        JLabel lblRoomId = new JLabel("Room ID"); lblRoomId.setPreferredSize(labelSize);
        JLabel lblStartTime = new JLabel("Start Time (yyyy-MM-ddTHH:mm)"); lblStartTime.setPreferredSize(labelSize);

        showtimeIdField = new JTextField(); showtimeIdField.setEditable(false); showtimeIdField.setPreferredSize(fieldSize);
        movieIdField = new JTextField(); movieIdField.setPreferredSize(fieldSize);
        roomIdField = new JTextField(); roomIdField.setPreferredSize(fieldSize);
        startTimeField = new JTextField(); startTimeField.setPreferredSize(fieldSize);

        form.add(lblShowtimeId); form.add(showtimeIdField);
        form.add(lblMovieId); form.add(movieIdField);
        form.add(lblRoomId); form.add(roomIdField);
        form.add(lblStartTime); form.add(startTimeField);

        formWrapper.add(form);
        panel.add(formWrapper, BorderLayout.CENTER);

        // Nút Add căn giữa
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton addBtnBottom = new JButton("Add");
        btnPanel.add(addBtnBottom);
        panel.add(btnPanel, BorderLayout.SOUTH);

        addBtnBottom.addActionListener(e -> {
            try {
                int movieId = Integer.parseInt(movieIdField.getText());
                int roomId = Integer.parseInt(roomIdField.getText());
                String startTimeStr = startTimeField.getText();

                movie m = moviedao.getmovieByid(movieId);
                room r = roomdao.getroomByid(roomId);

                if (m == null || r == null) {
                    JOptionPane.showMessageDialog(this, "❌ Movie hoặc Room không tồn tại!");
                    return;
                }

                LocalDateTime startTime = LocalDateTime.parse(startTimeStr);
                showtime st = new showtime(0, m, r, startTime, null);
                showtimedao.insertshowtime(st);

                JOptionPane.showMessageDialog(this, "✅ Thêm suất chiếu thành công!");
                movieIdField.setText("");
                roomIdField.setText("");
                startTimeField.setText("");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "❌ Movie ID or Room ID phải là số nguyên!");
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

        // Thanh tìm kiếm theo Showtime ID
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JTextField idField = new JTextField(10);
        JButton searchBtn = new JButton("Tìm");
        searchPanel.add(new JLabel("Showtime ID:"));
        searchPanel.add(idField);
        searchPanel.add(searchBtn);
        panel.add(searchPanel, BorderLayout.NORTH);

        // Form hiển thị thông tin showtime
        JPanel formWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 15, 15));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        Dimension labelSize = new Dimension(160, 25);
        Dimension fieldSize = new Dimension(250, 28);

        JLabel lblMovieId = new JLabel("Movie ID"); lblMovieId.setPreferredSize(labelSize);
        JLabel lblRoomId = new JLabel("Room ID"); lblRoomId.setPreferredSize(labelSize);
        JLabel lblStartTime = new JLabel("Start Time (yyyy-MM-ddTHH:mm)"); lblStartTime.setPreferredSize(labelSize);

        JTextField updateMovieId = new JTextField(); updateMovieId.setEditable(false); updateMovieId.setPreferredSize(fieldSize);
        JTextField updateRoomId = new JTextField(); updateRoomId.setEditable(false); updateRoomId.setPreferredSize(fieldSize);
        JTextField updateStartTime = new JTextField(); updateStartTime.setEditable(false); updateStartTime.setPreferredSize(fieldSize);

        formPanel.add(lblMovieId); formPanel.add(updateMovieId);
        formPanel.add(lblRoomId); formPanel.add(updateRoomId);
        formPanel.add(lblStartTime); formPanel.add(updateStartTime);

        formWrapper.add(formPanel);
        panel.add(formWrapper, BorderLayout.CENTER);

        // Nút Update căn giữa
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton confirmUpdateBtn = new JButton("Update");
        btnPanel.add(confirmUpdateBtn);
        panel.add(btnPanel, BorderLayout.SOUTH);

        // Sự kiện tìm showtime theo ID
        searchBtn.addActionListener(e -> {
            try {
                int id = Integer.parseInt(idField.getText());
                showtime st = showtimedao.getshowtimeByid(id);
                if (st != null) {
                    updateMovieId.setText(String.valueOf(st.getMovie().getMovieid()));
                    updateRoomId.setText(String.valueOf(st.getRoom().getRoomid()));
                    updateStartTime.setText(st.getStartTime().toString());

                    updateMovieId.setEditable(true);
                    updateRoomId.setEditable(true);
                    updateStartTime.setEditable(true);
                } else {
                    updateMovieId.setText("");
                    updateRoomId.setText("");
                    updateStartTime.setText("");
                    updateMovieId.setEditable(false);
                    updateRoomId.setEditable(false);
                    updateStartTime.setEditable(false);
                    JOptionPane.showMessageDialog(this, "❌ Không tìm thấy suất chiếu!");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "❌ Showtime ID phải là số nguyên!");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "❌ Lỗi SQL: " + ex.getMessage());
            }
        });

        // Sự kiện xác nhận Update
        confirmUpdateBtn.addActionListener(e -> {
            try {
                int id = Integer.parseInt(idField.getText());
                int movieId = Integer.parseInt(updateMovieId.getText());
                int roomId = Integer.parseInt(updateRoomId.getText());
                String startTimeStr = updateStartTime.getText();

                movie m = moviedao.getmovieByid(movieId);
                room r = roomdao.getroomByid(roomId);

                if (m == null || r == null) {
                    JOptionPane.showMessageDialog(this, "❌ Movie hoặc Room không tồn tại!");
                    return;
                }

                LocalDateTime startTime = LocalDateTime.parse(startTimeStr);
                showtime st = new showtime(id, m, r, startTime, null);
                showtimedao.updateshowtime(st);

                JOptionPane.showMessageDialog(this, "✅ Cập nhật suất chiếu thành công!");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "❌ ID, Movie ID và Room ID phải là số nguyên!");
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(this, "❌ Định dạng thời gian không hợp lệ! Dùng yyyy-MM-ddTHH:mm");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "❌ Lỗi SQL: " + ex.getMessage());
            }
        });

        return panel;
    }

    private JPanel buildDeleteContent() {
        JPanel panel = new JPanel(new BorderLayout());

        // Thanh tìm kiếm theo Showtime ID
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JTextField idField = new JTextField(10);
        JButton searchBtn = new JButton("Tìm");
        searchPanel.add(new JLabel("Showtime ID:"));
        searchPanel.add(idField);
        searchPanel.add(searchBtn);
        panel.add(searchPanel, BorderLayout.NORTH);

        // Form hiển thị thông tin showtime
        JPanel formWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 15, 15));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        Dimension labelSize = new Dimension(120, 25);
        Dimension fieldSize = new Dimension(250, 28);

        JLabel lblMovieId = new JLabel("Movie ID"); lblMovieId.setPreferredSize(labelSize);
        JLabel lblRoomId = new JLabel("Room ID"); lblRoomId.setPreferredSize(labelSize);
        JLabel lblStartTime = new JLabel("Start Time"); lblStartTime.setPreferredSize(labelSize);

        JTextField delMovieId = new JTextField(); delMovieId.setEditable(false); delMovieId.setPreferredSize(fieldSize);
        JTextField delRoomId = new JTextField(); delRoomId.setEditable(false); delRoomId.setPreferredSize(fieldSize);
        JTextField delStartTime = new JTextField(); delStartTime.setEditable(false); delStartTime.setPreferredSize(fieldSize);

        formPanel.add(lblMovieId); formPanel.add(delMovieId);
        formPanel.add(lblRoomId); formPanel.add(delRoomId);
        formPanel.add(lblStartTime); formPanel.add(delStartTime);

        formWrapper.add(formPanel);
        panel.add(formWrapper, BorderLayout.CENTER);

        // Nút Delete căn giữa
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton confirmDeleteBtn = new JButton("Delete");
        btnPanel.add(confirmDeleteBtn);
        panel.add(btnPanel, BorderLayout.SOUTH);

        // Sự kiện tìm showtime theo ID
        searchBtn.addActionListener(e -> {
            try {
                int id = Integer.parseInt(idField.getText());
                showtime st = showtimedao.getshowtimeByid(id);
                if (st != null) {
                    delMovieId.setText(String.valueOf(st.getMovie().getMovieid()));
                    delRoomId.setText(String.valueOf(st.getRoom().getRoomid()));
                    delStartTime.setText(st.getStartTime().toString());
                } else {
                    delMovieId.setText("");
                    delRoomId.setText("");
                    delStartTime.setText("");
                    JOptionPane.showMessageDialog(this, "❌ Không tìm thấy suất chiếu!");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "❌ Showtime ID phải là số nguyên!");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "❌ Lỗi SQL: " + ex.getMessage());
            }
        });

        // Sự kiện xác nhận Delete
        confirmDeleteBtn.addActionListener(e -> {
            try {
                int id = Integer.parseInt(idField.getText());
                showtimedao.deleteshowtime(id);
                JOptionPane.showMessageDialog(this, "🗑️ Xóa suất chiếu thành công!");
                delMovieId.setText("");
                delRoomId.setText("");
                delStartTime.setText("");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "❌ Showtime ID phải là số nguyên!");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "❌ Lỗi SQL: " + ex.getMessage());
            }
        });

        return panel;
    }

    private JPanel buildFindByIdContent() {
        JPanel panel = new JPanel(new BorderLayout());

        // Thanh tìm kiếm theo Showtime ID
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JTextField idField = new JTextField(10);
        JButton searchBtn = new JButton("Tìm");
        searchPanel.add(new JLabel("Showtime ID:"));
        searchPanel.add(idField);
        searchPanel.add(searchBtn);
        panel.add(searchPanel, BorderLayout.NORTH);

        // Form hiển thị thông tin showtime
        JPanel formWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 15, 15));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        Dimension labelSize = new Dimension(120, 25);
        Dimension fieldSize = new Dimension(250, 28);

        JLabel lblMovieId = new JLabel("Movie ID"); lblMovieId.setPreferredSize(labelSize);
        JLabel lblRoomId = new JLabel("Room ID"); lblRoomId.setPreferredSize(labelSize);
        JLabel lblStartTime = new JLabel("Start Time"); lblStartTime.setPreferredSize(labelSize);

        JTextField fMovieId = new JTextField(); fMovieId.setEditable(false); fMovieId.setPreferredSize(fieldSize);
        JTextField fRoomId = new JTextField(); fRoomId.setEditable(false); fRoomId.setPreferredSize(fieldSize);
        JTextField fStartTime = new JTextField(); fStartTime.setEditable(false); fStartTime.setPreferredSize(fieldSize);

        formPanel.add(lblMovieId); formPanel.add(fMovieId);
        formPanel.add(lblRoomId); formPanel.add(fRoomId);
        formPanel.add(lblStartTime); formPanel.add(fStartTime);

        formWrapper.add(formPanel);
        panel.add(formWrapper, BorderLayout.CENTER);

        // Sự kiện tìm showtime theo ID
        searchBtn.addActionListener(e -> {
            try {
                int id = Integer.parseInt(idField.getText());
                showtime st = showtimedao.getshowtimeByid(id);
                if (st != null) {
                    fMovieId.setText(String.valueOf(st.getMovie().getMovieid()));
                    fRoomId.setText(String.valueOf(st.getRoom().getRoomid()));
                    fStartTime.setText(st.getStartTime().toString());
                } else {
                    fMovieId.setText("");
                    fRoomId.setText("");
                    fStartTime.setText("");
                    JOptionPane.showMessageDialog(this, "❌ Không tìm thấy suất chiếu!");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "❌ Showtime ID phải là số nguyên!");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "❌ Lỗi SQL: " + ex.getMessage());
            }
        });

        return panel;
    }


    private JPanel buildShowAllContent() {
        JPanel panel = new JPanel(new BorderLayout());

        String[] columnNames = {"Showtime ID", "Movie ID", "Room ID", "Start Time"};
        Object[][] data;

        try {
            List<showtime> list = showtimedao.getAllshowtime();
            data = new Object[list.size()][4];
            for (int i = 0; i < list.size(); i++) {
                showtime st = list.get(i);
                data[i][0] = st.getShowtimeid();
                data[i][1] = st.getMovie().getMovieid();
                data[i][2] = st.getRoom().getRoomid();
                data[i][3] = st.getStartTime().toString();
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
