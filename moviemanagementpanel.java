package doan;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class moviemanagementpanel extends JPanel {
    private final Connection conn;
    private final moviedao moviedao;

    private JPanel contentPanel;

    private JTextField movieidField, titleField, authorField, durationField, genreField, ratingField, descriptionField;

    public moviemanagementpanel(Connection conn) {
        setLayout(new BorderLayout());
        this.conn = conn;
        this.moviedao = new moviedao(conn);

        add(buildMenuPanel(), BorderLayout.WEST);

        JPanel topButtons = buildTopButtons();
        add(topButtons, BorderLayout.NORTH);

        contentPanel = buildMainContent();
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
        exitBtn.addActionListener(e -> showCard("welcome"));
        menuPanel.add(exitBtn);

        return menuPanel;
    }


    private void showCard(String name) {
        Container parent = this.getParent();
        while (parent != null && !(parent instanceof JPanel && parent.getLayout() instanceof CardLayout)) {
            parent = parent.getParent();
        }
        if (parent != null) {
            CardLayout cl = (CardLayout) parent.getLayout();
            System.out.println("Đang chuyển sang: " + name);
            cl.show((JPanel) parent, name);
        }
    }


    private JPanel buildTopButtons() {
        JPanel topButtons = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        JButton addBtn = new JButton("Add");
        JButton updateBtn = new JButton("Update");
        JButton deleteBtn = new JButton("Delete");
        JButton findByIdBtn = new JButton("Find by ID");
        JButton findByTitleBtn = new JButton("Find by Title");
        JButton showAllBtn = new JButton("Show All");

        topButtons.add(addBtn);
        topButtons.add(updateBtn);
        topButtons.add(deleteBtn);
        topButtons.add(findByIdBtn);
        topButtons.add(findByTitleBtn);
        topButtons.add(showAllBtn);

        // Sự kiện: chỉ thay đổi phần CENTER
        addBtn.addActionListener(e -> switchContent(buildMainContent()));
        updateBtn.addActionListener(e -> switchContent(buildUpdateContent()));

        // Các sự kiện dùng form chính (CENTER đang là main content)
        deleteBtn.addActionListener(e -> switchContent(buildDeleteContent()));

        findByIdBtn.addActionListener(e -> switchContent(buildFindByIdContent()));
        findByTitleBtn.addActionListener(e -> switchContent(buildFindByTitleContent()));


        showAllBtn.addActionListener(e -> switchContent(buildShowAllContent()));

        return topButtons;
    }

    // Nội dung chính (Add form + nút Add dưới)
    private JPanel buildMainContent() {
        JPanel panel = new JPanel(new BorderLayout());

        // Bọc form để căn giữa
        JPanel formWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel form = new JPanel(new GridLayout(7, 2, 15, 15));
        form.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50)); // cách lề trái/phải

        Dimension labelSize = new Dimension(100, 25);
        Dimension fieldSize = new Dimension(250, 28);

        JLabel lblMovieId = new JLabel("Movie ID"); lblMovieId.setPreferredSize(labelSize);
        JLabel lblTitle = new JLabel("Title"); lblTitle.setPreferredSize(labelSize);
        JLabel lblAuthor = new JLabel("Author"); lblAuthor.setPreferredSize(labelSize);
        JLabel lblDuration = new JLabel("Duration"); lblDuration.setPreferredSize(labelSize);
        JLabel lblGenre = new JLabel("Genre"); lblGenre.setPreferredSize(labelSize);
        JLabel lblRating = new JLabel("Rating"); lblRating.setPreferredSize(labelSize);
        JLabel lblDescription = new JLabel("Description"); lblDescription.setPreferredSize(labelSize);

        movieidField = new JTextField(); movieidField.setEditable(false); movieidField.setPreferredSize(fieldSize);
        titleField = new JTextField(); titleField.setPreferredSize(fieldSize);
        authorField = new JTextField(); authorField.setPreferredSize(fieldSize);
        durationField = new JTextField(); durationField.setPreferredSize(fieldSize);
        genreField = new JTextField(); genreField.setPreferredSize(fieldSize);
        ratingField = new JTextField(); ratingField.setPreferredSize(fieldSize);
        descriptionField = new JTextField(); descriptionField.setPreferredSize(fieldSize);

        form.add(lblMovieId); form.add(movieidField);
        form.add(lblTitle); form.add(titleField);
        form.add(lblAuthor); form.add(authorField);
        form.add(lblDuration); form.add(durationField);
        form.add(lblGenre); form.add(genreField);
        form.add(lblRating); form.add(ratingField);
        form.add(lblDescription); form.add(descriptionField);

        formWrapper.add(form);
        panel.add(formWrapper, BorderLayout.CENTER);

        // Nút Add căn giữa
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton addBtnBottom = new JButton("Add");
        btnPanel.add(addBtnBottom);
        panel.add(btnPanel, BorderLayout.SOUTH);

        // Sự kiện Add (dùng form chính)
        addBtnBottom.addActionListener(e -> {
            try {
                int duration = Integer.parseInt(durationField.getText());
                movie m = new movie(
                    0,
                    titleField.getText(),
                    authorField.getText(),
                    duration,
                    genreField.getText(),
                    ratingField.getText(),
                    descriptionField.getText()
                );
                moviedao.insertmovie(m);
                JOptionPane.showMessageDialog(this, "✅ Thêm phim thành công!");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "❌ Duration phải là số nguyên!");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "❌ Lỗi SQL: " + ex.getMessage());
            }
        });

        return panel;
    }


    // Nội dung Update theo ID
    private JPanel buildUpdateContent() {
        JPanel panel = new JPanel(new BorderLayout());

        // ===== Thanh tìm kiếm ID =====
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JTextField idField = new JTextField(10);
        JButton searchBtn = new JButton("Tìm");
        searchPanel.add(new JLabel("Movie ID:"));
        searchPanel.add(idField);
        searchPanel.add(searchBtn);
        panel.add(searchPanel, BorderLayout.NORTH);

        // ===== Form chỉnh sửa =====
        JPanel formWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER)); // căn giữa toàn bộ form
        JPanel formPanel = new JPanel(new GridLayout(6, 2, 15, 15));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // padding

        Dimension labelSize = new Dimension(100, 25);
        Dimension fieldSize = new Dimension(250, 28);

        JLabel lblTitle = new JLabel("Title"); lblTitle.setPreferredSize(labelSize);
        JLabel lblAuthor = new JLabel("Author"); lblAuthor.setPreferredSize(labelSize);
        JLabel lblDuration = new JLabel("Duration"); lblDuration.setPreferredSize(labelSize);
        JLabel lblGenre = new JLabel("Genre"); lblGenre.setPreferredSize(labelSize);
        JLabel lblRating = new JLabel("Rating"); lblRating.setPreferredSize(labelSize);
        JLabel lblDescription = new JLabel("Description"); lblDescription.setPreferredSize(labelSize);

        JTextField updateTitle = new JTextField(); updateTitle.setPreferredSize(fieldSize);
        JTextField updateAuthor = new JTextField(); updateAuthor.setPreferredSize(fieldSize);
        JTextField updateDuration = new JTextField(); updateDuration.setPreferredSize(fieldSize);
        JTextField updateGenre = new JTextField(); updateGenre.setPreferredSize(fieldSize);
        JTextField updateRating = new JTextField(); updateRating.setPreferredSize(fieldSize);
        JTextField updateDescription = new JTextField(); updateDescription.setPreferredSize(fieldSize);

        formPanel.add(lblTitle); formPanel.add(updateTitle);
        formPanel.add(lblAuthor); formPanel.add(updateAuthor);
        formPanel.add(lblDuration); formPanel.add(updateDuration);
        formPanel.add(lblGenre); formPanel.add(updateGenre);
        formPanel.add(lblRating); formPanel.add(updateRating);
        formPanel.add(lblDescription); formPanel.add(updateDescription);

        formWrapper.add(formPanel);
        panel.add(formWrapper, BorderLayout.CENTER);

        // ===== Nút Update =====
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton confirmUpdateBtn = new JButton("Update");
        btnPanel.add(confirmUpdateBtn);
        panel.add(btnPanel, BorderLayout.SOUTH);

        // ===== Sự kiện tìm kiếm theo ID =====
        searchBtn.addActionListener(e -> {
            try {
                int id = Integer.parseInt(idField.getText());
                movie m = moviedao.getmovieByid(id);
                if (m != null) {
                    updateTitle.setText(m.getTitle());
                    updateAuthor.setText(m.getAuthor());
                    updateDuration.setText(String.valueOf(m.getDuration()));
                    updateGenre.setText(m.getGenre());
                    updateRating.setText(m.getRating());
                    updateDescription.setText(m.getDescription());
                } else {
                    JOptionPane.showMessageDialog(this, "❌ Không tìm thấy phim!");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "❌ Movie ID phải là số nguyên!");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "❌ Lỗi SQL: " + ex.getMessage());
            }
        });

        // ===== Sự kiện cập nhật =====
        confirmUpdateBtn.addActionListener(e -> {
            try {
                int id = Integer.parseInt(idField.getText());
                int duration = Integer.parseInt(updateDuration.getText());
                movie m = new movie(
                    id,
                    updateTitle.getText(),
                    updateAuthor.getText(),
                    duration,
                    updateGenre.getText(),
                    updateRating.getText(),
                    updateDescription.getText()
                );
                moviedao.updatemovie(m);
                JOptionPane.showMessageDialog(this, "✅ Cập nhật phim thành công!");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "❌ ID và Duration phải là số nguyên!");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "❌ Lỗi SQL: " + ex.getMessage());
            }
        });

        return panel;
    }


    private JPanel buildDeleteContent() {
        JPanel panel = new JPanel(new BorderLayout());

        // ===== Thanh tìm kiếm ID =====
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JTextField idField = new JTextField(10);
        JButton searchBtn = new JButton("Tìm");
        searchPanel.add(new JLabel("Movie ID:"));
        searchPanel.add(idField);
        searchPanel.add(searchBtn);
        panel.add(searchPanel, BorderLayout.NORTH);

        // ===== Form hiển thị thông tin (chỉ đọc) =====
        JPanel formWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER)); // căn giữa toàn bộ form
        JPanel formPanel = new JPanel(new GridLayout(6, 2, 15, 15));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        Dimension labelSize = new Dimension(100, 25);
        Dimension fieldSize = new Dimension(250, 28);

        JLabel lblTitle = new JLabel("Title"); lblTitle.setPreferredSize(labelSize);
        JLabel lblAuthor = new JLabel("Author"); lblAuthor.setPreferredSize(labelSize);
        JLabel lblDuration = new JLabel("Duration"); lblDuration.setPreferredSize(labelSize);
        JLabel lblGenre = new JLabel("Genre"); lblGenre.setPreferredSize(labelSize);
        JLabel lblRating = new JLabel("Rating"); lblRating.setPreferredSize(labelSize);
        JLabel lblDescription = new JLabel("Description"); lblDescription.setPreferredSize(labelSize);

        JTextField delTitle = new JTextField(); delTitle.setEditable(false); delTitle.setPreferredSize(fieldSize);
        JTextField delAuthor = new JTextField(); delAuthor.setEditable(false); delAuthor.setPreferredSize(fieldSize);
        JTextField delDuration = new JTextField(); delDuration.setEditable(false); delDuration.setPreferredSize(fieldSize);
        JTextField delGenre = new JTextField(); delGenre.setEditable(false); delGenre.setPreferredSize(fieldSize);
        JTextField delRating = new JTextField(); delRating.setEditable(false); delRating.setPreferredSize(fieldSize);
        JTextField delDescription = new JTextField(); delDescription.setEditable(false); delDescription.setPreferredSize(fieldSize);

        formPanel.add(lblTitle); formPanel.add(delTitle);
        formPanel.add(lblAuthor); formPanel.add(delAuthor);
        formPanel.add(lblDuration); formPanel.add(delDuration);
        formPanel.add(lblGenre); formPanel.add(delGenre);
        formPanel.add(lblRating); formPanel.add(delRating);
        formPanel.add(lblDescription); formPanel.add(delDescription);

        formWrapper.add(formPanel);
        panel.add(formWrapper, BorderLayout.CENTER);

        // ===== Nút Delete =====
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton confirmDeleteBtn = new JButton("Delete");
        btnPanel.add(confirmDeleteBtn);
        panel.add(btnPanel, BorderLayout.SOUTH);

        // ===== Sự kiện tìm kiếm =====
        searchBtn.addActionListener(e -> {
            try {
                int id = Integer.parseInt(idField.getText());
                movie m = moviedao.getmovieByid(id);
                if (m != null) {
                    delTitle.setText(m.getTitle());
                    delAuthor.setText(m.getAuthor());
                    delDuration.setText(String.valueOf(m.getDuration()));
                    delGenre.setText(m.getGenre());
                    delRating.setText(m.getRating());
                    delDescription.setText(m.getDescription());
                } else {
                    JOptionPane.showMessageDialog(this, "❌ Không tìm thấy phim!");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "❌ Movie ID phải là số nguyên!");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "❌ Lỗi SQL: " + ex.getMessage());
            }
        });

        // ===== Sự kiện Delete =====
        confirmDeleteBtn.addActionListener(e -> {
            try {
                int id = Integer.parseInt(idField.getText());
                moviedao.deletemovie(id);
                JOptionPane.showMessageDialog(this, "🗑️ Xóa phim thành công!");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "❌ Movie ID phải là số nguyên!");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "❌ Lỗi SQL: " + ex.getMessage());
            }
        });

        return panel;
    }

 // ===== FORM FIND BY ID =====
    private JPanel buildFindByIdContent() {
        JPanel panel = new JPanel(new BorderLayout());

        // Thanh tìm kiếm ID
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JTextField idField = new JTextField(10);
        JButton searchBtn = new JButton("Tìm");
        searchPanel.add(new JLabel("Movie ID:"));
        searchPanel.add(idField);
        searchPanel.add(searchBtn);
        panel.add(searchPanel, BorderLayout.NORTH);

        // Form hiển thị thông tin (chỉ đọc)
        JPanel formWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel formPanel = new JPanel(new GridLayout(6, 2, 15, 15));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        Dimension labelSize = new Dimension(100, 25);
        Dimension fieldSize = new Dimension(250, 28);

        JLabel lblTitle = new JLabel("Title"); lblTitle.setPreferredSize(labelSize);
        JLabel lblAuthor = new JLabel("Author"); lblAuthor.setPreferredSize(labelSize);
        JLabel lblDuration = new JLabel("Duration"); lblDuration.setPreferredSize(labelSize);
        JLabel lblGenre = new JLabel("Genre"); lblGenre.setPreferredSize(labelSize);
        JLabel lblRating = new JLabel("Rating"); lblRating.setPreferredSize(labelSize);
        JLabel lblDescription = new JLabel("Description"); lblDescription.setPreferredSize(labelSize);

        JTextField fTitle = new JTextField(); fTitle.setEditable(false); fTitle.setPreferredSize(fieldSize);
        JTextField fAuthor = new JTextField(); fAuthor.setEditable(false); fAuthor.setPreferredSize(fieldSize);
        JTextField fDuration = new JTextField(); fDuration.setEditable(false); fDuration.setPreferredSize(fieldSize);
        JTextField fGenre = new JTextField(); fGenre.setEditable(false); fGenre.setPreferredSize(fieldSize);
        JTextField fRating = new JTextField(); fRating.setEditable(false); fRating.setPreferredSize(fieldSize);
        JTextField fDescription = new JTextField(); fDescription.setEditable(false); fDescription.setPreferredSize(fieldSize);

        formPanel.add(lblTitle); formPanel.add(fTitle);
        formPanel.add(lblAuthor); formPanel.add(fAuthor);
        formPanel.add(lblDuration); formPanel.add(fDuration);
        formPanel.add(lblGenre); formPanel.add(fGenre);
        formPanel.add(lblRating); formPanel.add(fRating);
        formPanel.add(lblDescription); formPanel.add(fDescription);

        formWrapper.add(formPanel);
        panel.add(formWrapper, BorderLayout.CENTER);

        // Sự kiện tìm kiếm
        searchBtn.addActionListener(e -> {
            try {
                int id = Integer.parseInt(idField.getText());
                movie m = moviedao.getmovieByid(id);
                if (m != null) {
                    fTitle.setText(m.getTitle());
                    fAuthor.setText(m.getAuthor());
                    fDuration.setText(String.valueOf(m.getDuration()));
                    fGenre.setText(m.getGenre());
                    fRating.setText(m.getRating());
                    fDescription.setText(m.getDescription());
                } else {
                    JOptionPane.showMessageDialog(this, "❌ Không tìm thấy phim!");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "❌ Movie ID phải là số nguyên!");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "❌ Lỗi SQL: " + ex.getMessage());
            }
        });

        return panel;
    }


    private JPanel buildFindByTitleContent() {
        JPanel panel = new JPanel(new BorderLayout());

        // Thanh tìm kiếm Title
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JTextField titleFieldSearch = new JTextField(15);
        JButton searchBtn = new JButton("Tìm");
        searchPanel.add(new JLabel("Movie Title:"));
        searchPanel.add(titleFieldSearch);
        searchPanel.add(searchBtn);
        panel.add(searchPanel, BorderLayout.NORTH);

        // Form hiển thị thông tin (chỉ đọc)
        JPanel formWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel formPanel = new JPanel(new GridLayout(6, 2, 15, 15));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        Dimension labelSize = new Dimension(100, 25);
        Dimension fieldSize = new Dimension(250, 28);

        JLabel lblId = new JLabel("Movie ID"); lblId.setPreferredSize(labelSize);
        JLabel lblAuthor = new JLabel("Author"); lblAuthor.setPreferredSize(labelSize);
        JLabel lblDuration = new JLabel("Duration"); lblDuration.setPreferredSize(labelSize);
        JLabel lblGenre = new JLabel("Genre"); lblGenre.setPreferredSize(labelSize);
        JLabel lblRating = new JLabel("Rating"); lblRating.setPreferredSize(labelSize);
        JLabel lblDescription = new JLabel("Description"); lblDescription.setPreferredSize(labelSize);

        JTextField fId = new JTextField(); fId.setEditable(false); fId.setPreferredSize(fieldSize);
        JTextField fAuthor = new JTextField(); fAuthor.setEditable(false); fAuthor.setPreferredSize(fieldSize);
        JTextField fDuration = new JTextField(); fDuration.setEditable(false); fDuration.setPreferredSize(fieldSize);
        JTextField fGenre = new JTextField(); fGenre.setEditable(false); fGenre.setPreferredSize(fieldSize);
        JTextField fRating = new JTextField(); fRating.setEditable(false); fRating.setPreferredSize(fieldSize);
        JTextField fDescription = new JTextField(); fDescription.setEditable(false); fDescription.setPreferredSize(fieldSize);

        formPanel.add(lblId); formPanel.add(fId);
        formPanel.add(lblAuthor); formPanel.add(fAuthor);
        formPanel.add(lblDuration); formPanel.add(fDuration);
        formPanel.add(lblGenre); formPanel.add(fGenre);
        formPanel.add(lblRating); formPanel.add(fRating);
        formPanel.add(lblDescription); formPanel.add(fDescription);

        formWrapper.add(formPanel);
        panel.add(formWrapper, BorderLayout.CENTER);

        // Sự kiện tìm kiếm theo Title
        searchBtn.addActionListener(e -> {
            try {
                String title = titleFieldSearch.getText();
                movie m = moviedao.getmovieBytitle(title);
                if (m != null) {
                    fId.setText(String.valueOf(m.getMovieid()));
                    fAuthor.setText(m.getAuthor());
                    fDuration.setText(String.valueOf(m.getDuration()));
                    fGenre.setText(m.getGenre());
                    fRating.setText(m.getRating());
                    fDescription.setText(m.getDescription());
                } else {
                    JOptionPane.showMessageDialog(this, "❌ Không tìm thấy phim!");
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "❌ Lỗi SQL: " + ex.getMessage());
            }
        });

        return panel;
    }

    private JPanel buildShowAllContent() {
        JPanel panel = new JPanel(new BorderLayout());

        // Tên cột giống SQL
        String[] columnNames = {"Movie ID", "Title", "Author", "Duration", "Genre", "Rating", "Description"};

        // Lấy dữ liệu từ DB
        Object[][] data;
        try {
            java.util.List<movie> movies = moviedao.getAllmovie();
            data = new Object[movies.size()][7];
            for (int i = 0; i < movies.size(); i++) {
                movie m = movies.get(i);
                data[i][0] = m.getMovieid();
                data[i][1] = m.getTitle();
                data[i][2] = m.getAuthor();
                data[i][3] = m.getDuration();
                data[i][4] = m.getGenre();
                data[i][5] = m.getRating();
                data[i][6] = m.getDescription();
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "❌ Lỗi SQL: " + ex.getMessage());
            data = new Object[0][7];
        }

        // Tạo bảng
        JTable table = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(table);

        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    // Thay đổi nội dung ở CENTER, giữ nguyên khung
    private void switchContent(JPanel newContent) {
        if (contentPanel != null) {
            remove(contentPanel);
        }
        contentPanel = newContent;
        add(contentPanel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }
}
