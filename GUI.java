package doan;

import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.*;

public class GUI extends JFrame {
    private CardLayout card;
    private JPanel content;
    private Connection conn;

    public GUI(String title, Connection conn) {
        super(title);
        this.conn = conn;
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        card = new CardLayout();
        content = new JPanel(card);

        // Welcome
        welcomepanel welcome = new welcomepanel();
        content.add(welcome, "welcome");

        // Signin admin
        signinadminpanel adminPanel = new signinadminpanel(conn, card, content);
        content.add(adminPanel, "signinadmin");

       
     // Guest panels
        signinguestpanel signInGuestPanel = new signinguestpanel(conn, card, content);
        content.add(signInGuestPanel, "signinguest");

        signupguestpanel signUpGuestPanel = new signupguestpanel(conn, card, content);
        content.add(signUpGuestPanel, "signupguest");

        // Management panels (add sẵn)
     // trong GUI constructor
        moviemanagementpanel moviePanel = new moviemanagementpanel(conn);
        content.add(moviePanel, "movie");
        roommanagementpanel roomPanel=new roommanagementpanel(conn);
        content.add(roomPanel,"roommanagement");

        seatmanagementpanel seatPanel = new seatmanagementpanel(conn);
        content.add(seatPanel, "seatmanagement");

       ticketmanagementpanel ticketPanel = new ticketmanagementpanel(conn);
       content.add(ticketPanel, "ticketmanagement");

       showtimemanagementpanel showtimePanel = new showtimemanagementpanel(conn);
       content.add(showtimePanel, "showtimemanagement");

       bookingmanagementpanel bookingPanel = new bookingmanagementpanel(conn);
       content.add(bookingPanel, "bookingmanagement");

       revenuemanagementpanel revenuePanel = new revenuemanagementpanel(conn);
       content.add(revenuePanel, "revenuemanagement");

       approveaccountpanel approvePanel = new approveaccountpanel(conn);
       content.add(approvePanel, "approveaccount");

        // Welcome buttons
        welcome.signinadmin.addActionListener(e -> card.show(content, "signinadmin"));
        welcome.signinguest.addActionListener(e -> card.show(content, "signinguest"));
        welcome.signupguest.addActionListener(e -> card.show(content, "signupguest"));

        add(content);
        card.show(content, "welcome");

        setVisible(true);
    }

    public static void main(String[] args) {
        Connection conn = DBConnection.getConnection();

        if (conn != null) {
            System.out.println("✅ Kết nối SQL Server thành công!");
            new GUI("Cinema Ticket Management System", conn);
        } else {
            System.out.println("❌ Không kết nối được SQL Server");
        }
    }
}