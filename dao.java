package doan;
import doan.movie;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

class RevenueDAO {
    private Connection conn;

    public RevenueDAO(Connection conn) {
        this.conn = conn;
    }

    // Doanh thu theo ngày cụ thể
    public double getRevenueByDate(Date date) throws SQLException {
        String sql = "SELECT SUM(t.price) " +
                     "FROM booking b " +
                     "JOIN ticket t ON b.ticketid = t.ticketid " +
                     "JOIN showtime st ON t.showtimeid = st.showtimeid " +
                     "WHERE CAST(b.bookingTime AS DATE) = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, date);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getDouble(1);
            }
        }
        return 0;
    }

    // Doanh thu theo tháng và năm
    public double getRevenueByMonth(int month, int year) throws SQLException {
        String sql = "SELECT SUM(t.price) " +
                     "FROM booking b " +
                     "JOIN ticket t ON b.ticketid = t.ticketid " +
                     "JOIN showtime st ON t.showtimeid = st.showtimeid " +
                     "WHERE MONTH(b.bookingTime) = ? AND YEAR(b.bookingTime) = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, month);
            ps.setInt(2, year);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getDouble(1);
            }
        }
        return 0;
    }

    // Doanh thu theo tên phim
    public double getRevenueByMovie(String title) throws SQLException {
        String sql = "SELECT SUM(t.price) " +
                     "FROM booking b " +
                     "JOIN ticket t ON b.ticketid = t.ticketid " +
                     "JOIN showtime st ON t.showtimeid = st.showtimeid " +
                     "JOIN movie m ON st.movieid = m.movieid " +
                     "WHERE m.title LIKE ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            // thêm % vào tham số để tìm gần đúng
            ps.setString(1, "%" + title + "%");
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getDouble(1);
            }
        }
        return 0;
    }


    // Báo cáo doanh thu theo từng ngày
    public List<Revenue> getRevenueReportByDay() throws SQLException {
        List<Revenue> list = new ArrayList<>();
        String sql = "SELECT CAST(st.starttime AS DATE) AS Ngay, SUM(t.price) AS DoanhThu " +
                     "FROM booking b " +
                     "JOIN ticket t ON b.ticketid = t.ticketid " +
                     "JOIN showtime st ON t.showtimeid = st.showtimeid " +
                     "GROUP BY CAST(st.starttime AS DATE) " +
                     "ORDER BY Ngay";
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            int id = 1;
            while (rs.next()) {
                String period = rs.getDate("Ngay").toString();
                double total = rs.getDouble("DoanhThu");
                list.add(new Revenue(id++, period, total));
            }
        }
        return list;
    }
}



class guestdao {
    private Connection conn;
    public guestdao(Connection conn) {
        this.conn = conn;
    }
    public guest loginGuest(String email, String password) throws SQLException {
        String sql = "SELECT * FROM guest WHERE email=? AND password=? AND status='approved'";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new guest(
                    rs.getInt("guestid"),
                    rs.getString("name"),
                    rs.getString("phone"),
                    rs.getString("email"),
                    rs.getString("password"),
                    rs.getString("status")
                );
            }
        }
        return null; // login fail
    }

    public void insertguest(guest g) throws SQLException {
        String sql = "INSERT INTO guest(name, phone, email,password, status) VALUES (?, ?, ?,?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, g.getName());
            stmt.setString(2, g.getPhone());
            stmt.setString(3, g.getEmail());
            stmt.setString(4,g.getPassword());
            stmt.setString(5, g.getStatus());
            stmt.executeUpdate();
        }
    }

    public void updateguest(guest g) throws SQLException {
        String sql = "UPDATE guest SET name=?, phone=?, email=?,password=?, status=? WHERE guestid=?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, g.getName());
            stmt.setString(2, g.getPhone());
            stmt.setString(3, g.getEmail());
            stmt.setString(4,g.getPassword());
            stmt.setString(5, g.getStatus());
            stmt.setInt(6, g.getGuestid());
            stmt.executeUpdate();
        }
    }

    public void approveguest(int guestid) throws SQLException {
        String sql = "UPDATE guest SET status='approved' WHERE guestid=?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, guestid);
            stmt.executeUpdate();
        }
    }

    public guest getguestByid(int guestid) throws SQLException {
        String sql = "SELECT * FROM guest WHERE guestid=?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, guestid);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new guest(
                    rs.getInt("guestid"),
                    rs.getString("name"),
                    rs.getString("phone"),
                    rs.getString("email"),
                    rs.getString("password"),
                    rs.getString("status")
                );
            }
        }
        return null;
    }

    public List<guest> getAllguest() throws SQLException {
        List<guest> list = new ArrayList<>();
        String sql = "SELECT * FROM guest";
        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                list.add(new guest(
                    rs.getInt("guestid"),
                    rs.getString("name"),
                    rs.getString("phone"),
                    rs.getString("email"),
                    rs.getString("password"),
                    rs.getString("status")
                ));
            }
        }
        return list;
    }
}



class moviedao{
	private Connection conn;
	public moviedao(Connection conn) {
		this.conn=conn;
	}
	public void insertmovie(movie m) throws SQLException {
        String sql = "INSERT INTO movie (title, Author, duration, genre, rating, description) VALUES ( ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, m.getTitle());
            stmt.setString(2, m.getAuthor());
            stmt.setInt(3, m.getDuration());
            stmt.setString(4, m.getGenre());
            stmt.setString(5, m.getRating());
            stmt.setString(6, m.getDescription());
            stmt.executeUpdate();
        }
    }
	 public movie getmovieByid(int id) throws SQLException {
	        String sql = "SELECT * FROM movie WHERE movieid = ?";
	        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
	            stmt.setInt(1, id);
	            ResultSet rs = stmt.executeQuery();
	            if (rs.next()) {
	                return new movie(
	                    rs.getInt("movieid"),
	                    rs.getString("title"),
	                    rs.getString("Author"),
	                    rs.getInt("duration"),
	                    rs.getString("genre"),
	                    rs.getString("rating"),
	                    rs.getString("description")
	                );
	            }
	        }
	        return null;
	  }
	 public List<movie> getAllmovie() throws SQLException {
	        List<movie> movies = new ArrayList<>();
	        String sql = "SELECT * FROM movie";
	        try (Statement stmt = conn.createStatement()) {
	            ResultSet rs = stmt.executeQuery(sql);
	            while (rs.next()) {
	                movies.add(new movie(
	                    rs.getInt("movieid"),
	                    rs.getString("title"),
	                    rs.getString("Author"),
	                    rs.getInt("duration"),
	                    rs.getString("genre"),
	                    rs.getString("rating"),
	                    rs.getString("description")
	                ));
	            }
	        }
	        return movies;
	    }
	 public void deletemovie(int id) throws SQLException {
	        String sql = "delete from movie where movieid = ?";
	        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
	            stmt.setInt(1, id);
	            stmt.executeUpdate();
	        }
	    }
	 public void updatemovie(movie m) throws SQLException {
		    String sql = "update movie set title=?, Author=?, duration=?, genre=?, rating=?, description=? where movieid=?";
		    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
		        stmt.setInt(7, m.getMovieid());
		        stmt.setString(1, m.getTitle());
		        stmt.setString(2, m.getAuthor());
		        stmt.setInt(3, m.getDuration());
		        stmt.setString(4, m.getGenre());
		        stmt.setString(5, m.getRating());
		        stmt.setString(6, m.getDescription());
		        stmt.executeUpdate();
		    }
		}


	 public List<movie> searchmovie(String s) throws SQLException{
		 List<movie> movie = new ArrayList<>();
		 String sql ="select*from movie where title LIKE ? ";
		 try (PreparedStatement stmt =conn.prepareStatement(sql)){
			 stmt.setString(1,"%"+s+"%");
			 ResultSet rs=stmt.executeQuery();
			 while (rs.next()) {
				 movie.add(new movie (
						 rs.getInt("movieid"),
						 rs.getString("title"),
						 rs.getString("Author"),
						 rs.getInt("duration"),
						 rs.getString("genre"),
						 rs.getString("rating"),
						 rs.getString("description")));
			 }
			 return movie;
			 
		 }
		 
		 
	 }
	    public movie getmovieBytitle(String title) throws SQLException {
	        String sql = "select*from movie where title = ?";
	        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
	            stmt.setString(1, title);
	            ResultSet rs = stmt.executeQuery();
	            if (rs.next()) {
	                return new movie(
	                		 rs.getInt("movieid"),
							 rs.getString("title"),
							 rs.getString("Author"),
							 rs.getInt("duration"),
							 rs.getString("genre"),
							 rs.getString("rating"),
							 rs.getString("description")
	                );
	            }
	        }
	        return null;
	    }

	 
}
class roomdao{
	private Connection conn;
	public roomdao(Connection conn) {
		this.conn=conn;
	}
	public void insertroom(room r) throws SQLException {
		String sql="insert into room(capacity) values (?)";
		try(PreparedStatement stmt=conn.prepareStatement(sql)){
			stmt.setInt(1, r.getCapacity());
			stmt.executeUpdate();
		}
	}
	public void updateroom(room r) throws SQLException{
		String sql="update room set capacity =? where roomid=?";
		try(PreparedStatement stmt=conn.prepareStatement(sql)){
			stmt.setInt(2, r.getRoomid());
			stmt.setInt(1, r.getCapacity());
			stmt.executeUpdate();
		}
	}
	public void deleteroom(int roomid) throws SQLException{
		String sql="delete from room where roomid=?";
		try(PreparedStatement stmt=conn.prepareStatement(sql)){
			stmt.setInt(1, roomid);
			stmt.executeUpdate();
		}
	}
	 public List<room> searchroom(int roomid) throws SQLException{
		 List<room> room = new ArrayList<>();
		 String sql ="SELECT*FROM room WHERE roomid = ? ";
		 try (PreparedStatement stmt =conn.prepareStatement(sql)){
			 stmt.setInt(1,roomid);
			 ResultSet rs=stmt.executeQuery();
			 while (rs.next()) {
				 room.add(new room (
						 rs.getInt("roomid"),
						 rs.getInt("capacity")
						 ));
			 }
			 return room;
			 
		 }
		 
		 
	 }
	 public room getroomByid(int roomid) throws SQLException {
	        String sql = "SELECT * FROM room WHERE roomid = ?";
	        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
	            stmt.setInt(1, roomid);
	            ResultSet rs = stmt.executeQuery();
	            if (rs.next()) {
	                return new room(
	                		rs.getInt("roomid"),
		                    rs.getInt("capacity"));
	            }
	        }
	        return null;
	  }
	 public List<room> getAllroom() throws SQLException {
	        List<room> room = new ArrayList<>();
	        String sql = "SELECT * FROM room";
	        try (Statement stmt = conn.createStatement()) {
	            ResultSet rs = stmt.executeQuery(sql);
	            while (rs.next()) {
	                room.add(new room(
	                    rs.getInt("roomid"),
	                    rs.getInt("capacity")
	                ));
	            }
	        }
	        return room;
	    }

}
class seatdao{
	private Connection conn;
	public seatdao(Connection conn) {
		this.conn=conn;
	}
	public void insertseat(seat s) throws SQLException{
		String sql="insert into seat(row,roomid,status) values (?,?,?)";
		try(PreparedStatement stmt=conn.prepareStatement(sql)){
			stmt.setInt(1,s.getRow());
			stmt.setInt(2,s.getRoom().getRoomid());
			stmt.setString(3,s.getStatus());
			stmt.executeUpdate();
		}
	}
	public void updateseat(seat s) throws SQLException{
		String sql="update seat set row=?,roomid=?,status=? where seatid=?";
		try(PreparedStatement stmt=conn.prepareStatement(sql)){
			stmt.setInt(4,s.getSeatid());
			stmt.setInt(1,s.getRow());
			stmt.setInt(2,s.getRoom().getRoomid());
			stmt.setString(3,s.getStatus());
			stmt.executeUpdate();
		}
	}
	public void deleteseat(int seatid) throws SQLException{
		String sql="delete from seat where seatid=?";
		try(PreparedStatement stmt=conn.prepareStatement(sql)){
			stmt.setInt(1,seatid);
			stmt.executeUpdate();
		}
	}
	public List<seat> searchseat(int seatid) throws SQLException{
		String sql="select*from seat where seatid=?";
		List<seat> seat=new ArrayList<>();
		try (PreparedStatement stmt =conn.prepareStatement(sql)){
			 stmt.setInt(1,seatid);
			 ResultSet rs=stmt.executeQuery();
			 while (rs.next()) {
				 seat.add(new seat (
						 rs.getInt("seatid"),
						 rs.getInt("row"),
						 rs.getString("status"),
						 new room(rs.getInt("roomid"), 0)));
			 }
			 return seat;
		}
	}
	public List<seat> getAllseat() throws SQLException {
        List<seat> seat = new ArrayList<>();
        String sql = "SELECT * FROM seat";
        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                seat.add(new seat(
                    rs.getInt("seatid"),
                    rs.getInt("row"),
                    rs.getString("status"),
					new room(rs.getInt("roomid"), 0)
                ));
            }
        }
        return seat;
    }
	public seat getseatByid(int seatid) throws SQLException {
        String sql = "SELECT * FROM seat WHERE seatid = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, seatid);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new seat(
                		rs.getInt("seatid"),
                        rs.getInt("row"),
                        rs.getString("status"),
    					new room(rs.getInt("roomid"), 0)
                );
            }
        }
        return null;
  }	
}
class showtimedao{
	private Connection conn;
	private roomdao rooms;
	private moviedao movies;

	public showtimedao(Connection conn){
		this.conn=conn;
        this.movies = new moviedao(conn);
        this.rooms= new roomdao(conn);

	}
	public void insertshowtime(showtime s) throws SQLException {
        String sql = "insert into showtime (movieid, roomid, startTime) VALUES ( ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, s.getMovie().getMovieid());
            stmt.setInt(2, s.getRoom().getRoomid());
            stmt.setTimestamp(3, Timestamp.valueOf(s.getStartTime()));
            stmt.executeUpdate();
        }
    }
	public void updateshowtime(showtime s) throws SQLException {
	    String sql = "UPDATE showtime SET movieid = ?, roomid = ?, startTime = ? WHERE showtimeid = ?";
	    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
	        stmt.setInt(1, s.getMovie().getMovieid());
	        stmt.setInt(2, s.getRoom().getRoomid());
	        stmt.setTimestamp(3, Timestamp.valueOf(s.getStartTime()));
	        stmt.setInt(4, s.getShowtimeid());
	        stmt.executeUpdate();
	    }
	}

	 public void deleteshowtime(int showtimeid) throws SQLException {
	        String sql = "delete from showtime where showtimeid = ?";
	        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
	            stmt.setInt(1, showtimeid);
	            stmt.executeUpdate();
	        }
	    }
	 public showtime findshowtimeById(int showtimeid) throws SQLException {
	        String sql = "select*from showtime where showtimeid = ?";
	        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
	            stmt.setInt(1, showtimeid);
	            ResultSet rs = stmt.executeQuery();
	            if (rs.next()) {
	                movie m = movies.getmovieByid(rs.getInt("movieid"));
	                room r = rooms.getroomByid(rs.getInt("roomid"));
	                LocalDateTime time = rs.getTimestamp("startTime").toLocalDateTime();
	                return new showtime(showtimeid, m, r, time, new ArrayList<>());
	            }
	        }
	        return null;
	    }
	 public showtime getshowtimeByid(int showtimeid) throws SQLException {
		    String sql = "SELECT * FROM showtime WHERE showtimeid = ?";
		    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
		        stmt.setInt(1, showtimeid);
		        ResultSet rs = stmt.executeQuery();
		        if (rs.next()) {
		            movie m = movies.getmovieByid(rs.getInt("movieid"));
		            room r = rooms.getroomByid(rs.getInt("roomid"));
		            LocalDateTime time = rs.getTimestamp("startTime").toLocalDateTime();

		            return new showtime(showtimeid, m, r, time, new ArrayList<>());
		        }
		    }
		    return null;
		}

	 public List<showtime> getAllshowtime() throws SQLException {
	        List<showtime> list = new ArrayList<>();
	        String sql = "select*from showtime";
	        try (Statement stmt = conn.createStatement()) {
	            ResultSet rs = stmt.executeQuery(sql);
	            while (rs.next()) {
	            	 movie m = movies.getmovieByid(rs.getInt("movieid"));
		             room r = rooms.getroomByid(rs.getInt("roomid"));
	                LocalDateTime time = rs.getTimestamp("startTime").toLocalDateTime();
	                list.add(new showtime(rs.getInt("showtimeid"), m, r, time, new ArrayList<>()));
	            }
	        }
	        return list;
	    }
public List<showtime> getshowtimeBymovieid(int movieid) throws SQLException {
    List<showtime> list = new ArrayList<>();
    String sql = "select*from showtime where movieid = ?";
    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setInt(1, movieid);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            movie m = movies.getmovieByid(rs.getInt("movieid"));
            room r = rooms.getroomByid(rs.getInt("roomid"));
            LocalDateTime time = rs.getTimestamp("startTime").toLocalDateTime();
            list.add(new showtime(
                rs.getInt("showtimeid"),
                m,
                r,
                time,
                new ArrayList<>()
            ));
        }
    }
    return list;
}
}

class ticketdao {
    private Connection conn;
    private showtimedao showtimeDAO;
    private seatdao seatDAO;

    public ticketdao(Connection conn) {
        this.conn = conn;
        this.showtimeDAO = new showtimedao(conn);
        this.seatDAO = new seatdao(conn);
    }
    public void insertticket(ticket t) throws SQLException {
        String sql = "insert into ticket (showtimeid, seatid, price) values (?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, t.getShowtime().getShowtimeid());
            stmt.setInt(2, t.getSeat().getSeatid());
            stmt.setDouble(3, t.getPrice());
            stmt.executeUpdate();
        }
    }
    public void updateticket(ticket t) throws SQLException {
        String sql = "UPDATE ticket SET showtimeid = ?, seatid = ?, price = ? WHERE ticketid = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, t.getShowtime().getShowtimeid());
            stmt.setInt(2, t.getSeat().getSeatid());         
            stmt.setDouble(3, t.getPrice());                 
            stmt.setInt(4, t.getTicketid());              
            stmt.executeUpdate();
        }
    }
    public List<ticket> getAllticket() throws SQLException {
        List<ticket> list = new ArrayList<>();
        String sql = "select*from ticket";
        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
            	showtime s = showtimeDAO.getshowtimeByid(rs.getInt("showtimeid"));
                seat seatObj = seatDAO.getseatByid(rs.getInt("seatid"));
                double price = rs.getDouble("price");
                list.add(new ticket(rs.getInt("ticketid"), s, seatObj, price));
            }
        }
        return list;
    }
    public void deleteticket(int ticketid) throws SQLException {
        String sql = "delete from ticket where ticketid = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, ticketid);
            stmt.executeUpdate();
        }
    }
    public ticket getticketByid(int ticketid) throws SQLException {
        String sql = "select*from ticket where ticketid = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, ticketid);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                showtime s = showtimeDAO.getshowtimeByid(rs.getInt("showtimeid"));
                seat seatObj = seatDAO.getseatByid(rs.getInt("seatid"));
                double price = rs.getDouble("price");
                return new ticket(ticketid, s, seatObj, price);
            }
        }
        return null;
    }

}
class bookingdao {
    private Connection conn;
    private ticketdao ticketDAO;
    private guestdao guestDAO;

    public bookingdao(Connection conn) {
        this.conn = conn;
        this.ticketDAO = new ticketdao(conn);
        this.guestDAO = new guestdao(conn);
    }

    public void insertbooking(booking b) throws SQLException {
        String sql = "INSERT INTO booking (ticketid, guestid, bookingTime) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, b.getTicket().getTicketid());
            stmt.setInt(2, b.getGuest().getGuestid());
            stmt.setTimestamp(3, Timestamp.valueOf(b.getBookingTime()));
            stmt.executeUpdate();
        }
    }

    public void updatebooking(booking b) throws SQLException {
        String sql = "UPDATE booking SET ticketid = ?, guestid = ?, bookingTime = ? WHERE bookingid = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, b.getTicket().getTicketid());
            stmt.setInt(2, b.getGuest().getGuestid());
            stmt.setTimestamp(3, Timestamp.valueOf(b.getBookingTime()));
            stmt.setInt(4, b.getBookingid());
            stmt.executeUpdate();
        }
    }
    public booking getbookingByid(int bookingid) throws SQLException {
        String sql = "SELECT * FROM booking WHERE bookingid = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, bookingid);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                ticket t = ticketDAO.getticketByid(rs.getInt("ticketid"));
                guest g = guestDAO.getguestByid(rs.getInt("guestid"));
                LocalDateTime time = rs.getTimestamp("bookingTime").toLocalDateTime();
                return new booking(bookingid, t, g, time);
            }
        }
        return null;
    }
    public List<booking> getAllbooking() throws SQLException {
        List<booking> list = new ArrayList<>();
        String sql = "SELECT * FROM booking";
        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                ticket t = ticketDAO.getticketByid(rs.getInt("ticketid"));
                guest g = guestDAO.getguestByid(rs.getInt("guestid"));
                LocalDateTime time = rs.getTimestamp("bookingTime").toLocalDateTime();
                list.add(new booking(rs.getInt("bookingid"), t, g, time));
            }
        }
        return list;
    }
    public void deletebooking(int bookingid) throws SQLException {
        String sql = "DELETE FROM booking WHERE bookingid = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, bookingid);
            stmt.executeUpdate();
        }
    }
}

public class dao {

	public static void main(String[] args) throws SQLException {
		String url = "jdbc:sqlserver://localhost\\SQLEXPRESS:1433;databaseName=quanli;encrypt=true;trustServerCertificate=true";
		String username ="sa";
		String password ="123456789";
		try (Connection conn = DriverManager.getConnection(url, username, password)) {
            System.out.println("✅ Kết nối SQL Server thành công!");
            moviedao moviedao = new moviedao(conn);
            List<movie> r= moviedao.searchmovie("Kaisen");
            if(r.isEmpty()) {
            	System.out.println("Không tìm thấy");
            }
            else {
            	System.out.println("Đã tìm thấy "+ r.size()+" phim");
            	for(movie e : r) {
            		System.out.println(
            				"Movieid: "+e.getMovieid()+
            				"\nTitle: "+e.getTitle()+
            				"\nAuthor: "+e.getAuthor()+
            				"\nDuration: "+e.getDuration()+
            				"\nRating "+e.getRating()+
            				"\nDesciption: "+e.getDescription());
            	}
            }
	}
		catch (SQLException e) {
            System.out.println("❌ Lỗi kết nối hoặc thao tác SQL:");
            e.printStackTrace();
        }

	}

}
