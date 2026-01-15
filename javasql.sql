CREATE TABLE room (
    roomid INT PRIMARY KEY,
    capacity INT NOT NULL
);

CREATE TABLE seat (
    seatid INT PRIMARY KEY,
    row INT NOT NULL,
    roomid INT,
    FOREIGN KEY (roomid) REFERENCES room(roomid)
);
alter table seat
add status VARCHAR(20);
CREATE TABLE movie (
    movieid INT PRIMARY KEY,
    title NVARCHAR(100),
    Author Varchar(100),
    duration INT,
    genre NVARCHAR(50),
    rating NVARCHAR(10),
    description NVARCHAR(255)
);
CREATE TABLE showtime (
    showtimeid INT PRIMARY KEY,
    movieid INT,
    roomid INT,
    startTime DATETIME,
    FOREIGN KEY (movieid) REFERENCES movie(movieid),
    FOREIGN KEY (roomid) REFERENCES room(roomid)
);
CREATE TABLE ticket (
    ticketid INT PRIMARY KEY,
    showtimeid INT NOT NULL,
    seatid INT NOT NULL,
    price decimal (10,2) NOT NULL,
    FOREIGN KEY (showtimeid) REFERENCES showtime(showtimeid),
    FOREIGN KEY (seatid) REFERENCES seat(seatid)
);

CREATE TABLE booking (
    bookingid INT PRIMARY KEY,        
    ticketid INT NOT NULL,             
    customerName NVARCHAR(100) NOT NULL,
    customerPhone NVARCHAR(20),       
    bookingTime DATETIME NOT NULL,     
    FOREIGN KEY (ticketId) REFERENCES ticket(ticketId)
);
DROP TABLE booking;
DROP TABLE ticket;
DROP TABLE showtime;
DROP TABLE seat;
DROP TABLE room;
CREATE TABLE room (
    roomid INT IDENTITY(1,1) PRIMARY KEY,
    capacity INT NOT NULL
);

CREATE TABLE seat (
    seatid INT IDENTITY(1,1) PRIMARY KEY,
    row INT NOT NULL,
    roomid INT,
    status VARCHAR(20),
    FOREIGN KEY (roomid) REFERENCES room(roomid)
);
CREATE TABLE showtime (
    showtimeid INT IDENTITY(1,1) PRIMARY KEY,
    movieid INT,
    roomid INT,
    startTime DATETIME,
    FOREIGN KEY (movieid) REFERENCES movie(movieid),
    FOREIGN KEY (roomid) REFERENCES room(roomid)
);

CREATE TABLE ticket (
    ticketid INT IDENTITY(1,1) PRIMARY KEY,
    showtimeid INT NOT NULL,
    seatid INT NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (showtimeid) REFERENCES showtime(showtimeid),
    FOREIGN KEY (seatid) REFERENCES seat(seatid)
);

CREATE TABLE booking (
    bookingid INT IDENTITY(1,1) PRIMARY KEY,
    ticketid INT NOT NULL,
    customerName NVARCHAR(100) NOT NULL,
    customerPhone NVARCHAR(20),
    bookingTime DATETIME NOT NULL,
    FOREIGN KEY (ticketid) REFERENCES ticket(ticketid)
);








