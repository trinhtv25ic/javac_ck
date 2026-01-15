package doan;

import java.sql.*;
import java.util.List;
class RevenueService {
    private RevenueDAO revenueDAO;

    public RevenueService(Connection conn) {
        this.revenueDAO = new RevenueDAO(conn);
    }

    public double getRevenueByDate(java.sql.Date date) throws SQLException {
        return revenueDAO.getRevenueByDate(date);
    }

    public double getRevenueByMonth(int month, int year) throws SQLException {
        return revenueDAO.getRevenueByMonth(month, year);
    }

    public double getRevenueByMovie(String title) throws SQLException {
        return revenueDAO.getRevenueByMovie(title);
    }
}

class Account {
    private String username;
    private String password;
    private boolean approved;

    public Account(String username, String password) {
        this.username = username;
        this.password = password;
        this.approved = false;
    }

    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public boolean isApproved() { return approved; }
    public void approve() { this.approved = true; }
}

class movieservice {
    private moviedao moviedao;

    public movieservice(Connection conn) {
        this.moviedao = new moviedao(conn);
    }
    public void addmovie(movie m) throws SQLException {
        movie existing = moviedao.getmovieBytitle(m.getTitle());
        if (existing != null) {
            throw new SQLException("movie with this title already exists!");
        }
        moviedao.insertmovie(m);
    }
    public void updatemovie(movie m) throws SQLException {
        moviedao.updatemovie(m);
    }
    public void deletemovie(int movieId) throws SQLException {
        moviedao.deletemovie(movieId);
    }
    public movie getmovieByid(int movieId) throws SQLException {
        return moviedao.getmovieByid(movieId);
    }
    public movie getmovieBytitle(String title) throws SQLException {
        return moviedao.getmovieBytitle(title);
    }
    public List<movie> getAllmovie() throws SQLException {
        return moviedao.getAllmovie();
    }
}
class roomservice {
    private roomdao roomdao;

    public roomservice(Connection conn) {
        this.roomdao = new roomdao(conn);
    }

    public void addroom(room r) throws SQLException {
        roomdao.insertroom(r);
    }

    public void updateroom(room r) throws SQLException {
        roomdao.updateroom(r);
    }

    public void deleteroom(int roomid) throws SQLException {
        roomdao.deleteroom(roomid);
    }

    public room getroomByid(int roomid) throws SQLException {
        return roomdao.getroomByid(roomid);
    }

    public List<room> getAllroom() throws SQLException {
        return roomdao.getAllroom();
    }
}
class seatservice {
    private seatdao seatdao;

    public seatservice(Connection conn) {
        this.seatdao = new seatdao(conn);
    }

    public void addseat(seat s) throws SQLException {
        seatdao.insertseat(s);
    }

    public void updateseat(seat s) throws SQLException {
        seatdao.updateseat(s);
    }

    public void deleteseat(int seatid) throws SQLException {
        seatdao.deleteseat(seatid);
    }

    public seat getseatByid(int seatid) throws SQLException {
        return seatdao.getseatByid(seatid);
    }

    public List<seat> getAllseat() throws SQLException {
        return seatdao.getAllseat();
    }
}
class showtimeservice {
    private showtimedao showtimedao;

    public showtimeservice(Connection conn) {
        this.showtimedao = new showtimedao(conn);
    }

    public void addshowtime(showtime s) throws SQLException {
        showtimedao.insertshowtime(s);
    }

    public void updateshowtime(showtime s) throws SQLException {
        showtimedao.updateshowtime(s);
    }

    public void deleteshowtime(int showtimeid) throws SQLException {
        showtimedao.deleteshowtime(showtimeid);
    }

    public showtime getshowtimeByid(int showtimeid) throws SQLException {
        return showtimedao.getshowtimeByid(showtimeid);
    }

    public List<showtime> getAllshowtime() throws SQLException {
        return showtimedao.getAllshowtime();
    }
}
class ticketservice {
    private ticketdao ticketdao;

    public ticketservice(Connection conn) {
        this.ticketdao = new ticketdao(conn);
    }

    public void addticket(ticket t) throws SQLException {
        ticketdao.insertticket(t);
    }

    public void updateticket(ticket t) throws SQLException {
        ticketdao.updateticket(t);
    }

    public void deleteticket(int ticketid) throws SQLException {
        ticketdao.deleteticket(ticketid);
    }

    public ticket getticketByid(int ticketid) throws SQLException {
        return ticketdao.getticketByid(ticketid);
    }

    public List<ticket> getAllticket() throws SQLException {
        return ticketdao.getAllticket();
    }
}
class bookingservice {
    private bookingdao bookingdao;

    public bookingservice(Connection conn) {
        this.bookingdao = new bookingdao(conn);
    }

    public void addbooking(booking b) throws SQLException {
        bookingdao.insertbooking(b);
    }

    public void updatebooking(booking b) throws SQLException {
        bookingdao.updatebooking(b);
    }

    public void deletebooking(int bookingid) throws SQLException {
        bookingdao.deletebooking(bookingid);
    }

    public booking getbookingByid(int bookingid) throws SQLException {
        return bookingdao.getbookingByid(bookingid);
    }

    public List<booking> getAllbooking() throws SQLException {
        return bookingdao.getAllbooking();
    }
}


public class service {

	public static void main(String[] args) {
		String url = "jdbc:sqlserver://localhost\\SQLEXPRESS:1433;databaseName=quanli;encrypt=true;trustServerCertificate=true";
        String username = "sa";
        String password = "123456789";

        try (Connection conn = DriverManager.getConnection(url, username, password)) {
            System.out.println("Kết nối thành công!");

            // tạo movieservice
            movieservice ms = new movieservice(conn);

            // 1. thêm phim mới
            movie m = new movie(1, "Avatar", "James Cameron", 180, "Sci-Fi", "PG-13", "Epic movie");
            ms.addmovie(m);
            System.out.println("Đã thêm phim: " + m.getTitle());

            // 2. lấy phim theo id
            movie found = ms.getmovieByid(1);
            if (found != null) {
                System.out.println("Tìm thấy phim: " + found.getTitle() + " - " + found.getAuthor());
            }

            // 3. cập nhật phim
            found.setDuration(190);
            found.setDescription("Epic movie (extended)");
            ms.updatemovie(found);
            System.out.println("Đã cập nhật phim: " + found.getTitle());

            // 4. lấy tất cả phim
            List<movie> list = ms.getAllmovie();
            System.out.println("Danh sách phim:");
            for (movie mv : list) {
                System.out.println(mv.getMovieid() + " - " + mv.getTitle());
            }

            // 5. xóa phim
            ms.deletemovie(1);
            System.out.println("Đã xóa phim có id = 1");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}


	


