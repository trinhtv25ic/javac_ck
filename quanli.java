package doan;
import java.time.LocalDateTime;
import java.util.List;
class Revenue {
    private int id;
    private String period;
    private double total;

    public Revenue(int id, String period, double total) {
        this.id = id;
        this.period = period;
        this.total = total;
    }

    public int getId() { return id; }
    public String getPeriod() { return period; }
    public double getTotal() { return total; }

    public void setPeriod(String period) { this.period = period; }
    public void setTotal(double total) { this.total = total; }
}

class room{
	private int roomid;
	private int capacity;
	public int getRoomid() {
		return roomid;
	}
	public void setRoomid(int roomid) {
		this.roomid = roomid;
	}
	public int getCapacity() {
		return capacity;
	}
	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}
	public room(int roomid, int capacity) {
	    this.roomid = roomid;
	    this.capacity = capacity;
	}
	public String toString() {
	    return "Room ID: " + roomid + ", Capacity: " + capacity;
	}	
}
class seat{
	private int seatid;
	private int row;
	private String status;
	private room room;
	public room getRoom() {
		return room;
	}
	public void setRoom(room room) {
		this.room = room;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public int getSeatid() {
		return seatid;
	}
	public void setSeatid(int seatid) {
		this.seatid = seatid;
	}
	public int getRow() {
		return row;
	}
	public void setRow(int row) {
		this.row = row;
	}
	public seat(int seatid, int row,String status,room room) {
		this.seatid=seatid;
		this.row=row;
		this.status=status;
		this.room=room;
	}
	public String toString() {
	    return "Seat ID: " + seatid + ", row: " + row+" , Status: "+status;
	}
}
class movie{
	private int movieid;
    private String title;
    private String Author;
    private int duration;      
    private String genre;      
    private String rating;     
    private String description;
	public int getMovieid() {
		return movieid;
	}
	public void setMovieid(int movieid) {
		this.movieid = movieid;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getAuthor() {
		return Author;
	}
	public void setAuthor(String author) {
		Author = author;
	}
	public int getDuration() {
		return duration;
	}
	public void setDuration(int duration) {
		this.duration = duration;
	}
	public String getGenre() {
		return genre;
	}
	public void setGenre(String genre) {
		this.genre = genre;
	}
	public String getRating() {
		return rating;
	}
	public void setRating(String rating) {
		this.rating = rating;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public movie(int movieid,String title,String Author,int duration,String genre,String rating,String description) {
		this.movieid=movieid;
		this.title=title;
		this.Author=Author;
		this.duration=duration;
		this.genre=genre;
		this.rating=rating;
		this.description=description;
	}
	 public String toString() {
	        return "Movie ID: " + movieid +
	               ", Title: " + title +
	               ", Duration: " + duration + " minutes" +
	               ", Genre: " + genre +
	               ", Rating: " + rating +
	               ", Description: " + description;
	 }
}
class showtime{
	private int showtimeid;
    private movie movie;            
    private room room;         
    private LocalDateTime startTime;  
    private List<seat> seats;
    public showtime(int showtimeid) {
    	this.showtimeid=showtimeid;
    }
	public int getShowtimeid() {
		return showtimeid;
	}
	public void setShowtimeid(int showtimeid) {
		this.showtimeid = showtimeid;
	}
	public movie getMovie() {
		return movie;
	}
	public void setMovie(movie movie) {
		this.movie = movie;
	}
	public room getRoom() {
		return room;
	}
	public void setRoom(room room) {
		this.room = room;
	}
	public LocalDateTime getStartTime() {
		return startTime;
	}
	public void setStartTime(LocalDateTime startTime) {
		this.startTime = startTime;
	}
	public List<seat> getSeats() {
		return seats;
	}
	public void setSeats(List<seat> seats) {
		this.seats = seats;
	}
	public showtime(int showtimeid, movie movie, room room, LocalDateTime startTime, List<seat> seats) {
        this.showtimeid = showtimeid;
        this.movie = movie;
        this.room = room;
        this.startTime = startTime;
        this.seats = seats;
    }
	public String toString() {
        return "Showtime ID: " + showtimeid +
               ", Movie: " + movie.getTitle() +
               ", Room: " + room.getRoomid() +
               ", Start Time: " + startTime;
    }
}
class ticket{
	private int ticketid;
	private showtime showtime;
	private seat seat;
	private double price;
	public int getTicketid() {
		return ticketid;
	}
	public ticket(int ticketid) {
        this.ticketid = ticketid;
    }

	public void setTicketid(int ticketid) {
		this.ticketid = ticketid;
	}
	public showtime getShowtime() {
		return showtime;
	}
	public void setShowtime(showtime showtime) {
		this.showtime = showtime;
	}
	public seat getSeat() {
		return seat;
	}
	public void setSeat(seat seat) {
		this.seat = seat;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public ticket(int ticketid,showtime showtime ,seat seat,double price) {
		this.ticketid=ticketid;
		this.showtime=showtime;
		this.seat=seat;
		this.price=price;
	}
	public String ToString(){
		return "Ticket ID: "+ticketid+
				" ,Showtime: "+showtime+
				" ,seat: "+seat+
				" ,price: "+price;
		
	}
	
}
class booking {
    private int bookingid;
    private ticket ticket;     
    private guest guest;
    private LocalDateTime bookingTime;

    public booking(int bookingid, ticket t, guest g, LocalDateTime bookingTime) {
        this.bookingid = bookingid;
        this.ticket = new ticket(t.getTicketid(), null, null, 0);
        this.guest = new guest(g.getGuestid(), null, null, null, null,null);
        this.bookingTime = bookingTime;
    }
    public booking() {}

    // Getter & Setter
    public int getBookingid() {
        return bookingid;
    }
    public void setBookingid(int bookingid) {
        this.bookingid = bookingid;
    }

    public ticket getTicket() {
        return ticket;
    }
    public void setTicket(ticket ticket) {
        this.ticket = ticket;
    }

    public guest getGuest() {
        return guest;
    }
    public void setGuest(guest guest) {
        this.guest = guest;
    }

    public LocalDateTime getBookingTime() {
        return bookingTime;
    }
    public void setBookingTime(LocalDateTime bookingTime) {
        this.bookingTime = bookingTime;
    }

    @Override
    public String toString() {
        return "Booking ID: " + bookingid +
               ", Ticket ID: " + (ticket != null ? ticket.getTicketid() : "null") +
               ", Guest ID: " + (guest != null ? guest.getGuestid() : "null") +
               ", Booking Time: " + bookingTime;
    }

}
class guest {
    private int guestid;
    private String name;
    private String phone;
    private String email;
    private String password;
    private String status; // pending / approved

    public guest(int guestid, String name, String phone, String email,String password, String status) {
        this.guestid = guestid;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.password=password;
        this.status = status;
    }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public int getGuestid() {
        return guestid;
    }
    public void setGuestid(int guestid) {
        this.guestid = guestid;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
}



public class quanli {

	public static void main(String[] args) {
		
	}
	
}

