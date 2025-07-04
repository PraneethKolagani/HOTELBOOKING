import java.sql.*;
import java.util.*;

public class hotel_booking {
    static final String DB_URL = "jdbc:mysql://localhost:3306/hotel_booking";
    static final String USER = "root";
    static final String PASS = "";
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
            while (true) {
                System.out.println("\n1. Register\n2. List Cities\n3. List Hotels in City\n4. View Available Rooms\n5. Book Room\n6. View My Bookings\n7. Cancel Booking\n8. Exit");
                System.out.print("Choose option: ");
                int choice = sc.nextInt();
                switch (choice) {
                    case 1 -> registerCustomer(conn);
                    case 2 -> listCities(conn);
                    case 3 -> listHotelsInCity(conn);
                    case 4 -> viewAvailableRooms(conn);
                    case 5 -> bookRoom(conn);
                    case 6 -> viewBookings(conn);
                    case 7 -> cancelBooking(conn);
                    case 8 -> System.exit(0);
                    default -> System.out.println("Invalid choice");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    static void registerCustomer(Connection conn) throws SQLException {
        sc.nextLine();
        System.out.print("Enter name: ");
        String name = sc.nextLine();
        System.out.print("Enter email: ");
        String email = sc.nextLine();
        System.out.print("Enter phone: ");
        String phone = sc.nextLine();
        System.out.print("Enter password: ");
        String password = sc.nextLine();

        PreparedStatement ps = conn.prepareStatement("INSERT INTO customers (name, email, phone, password) VALUES (?, ?, ?, ?)");
        ps.setString(1, name);
        ps.setString(2, email);
        ps.setString(3, phone);
        ps.setString(4, password);
        ps.executeUpdate();
        System.out.println("Registration successful.");
    }

    static void listCities(Connection conn) throws SQLException {
        ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM cities");
        System.out.println("Available Cities:");
        while (rs.next()) {
            System.out.println(rs.getInt("city_id") + ". " + rs.getString("city_name"));
        }
    }

    static void listHotelsInCity(Connection conn) throws SQLException {
        System.out.print("Enter city name: ");
        sc.nextLine();
        String city = sc.nextLine();
        PreparedStatement ps = conn.prepareStatement("SELECT hotel_id, hotel_name, address FROM hotels WHERE city_id = (SELECT city_id FROM cities WHERE city_name = ?)");
        ps.setString(1, city);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            System.out.println("Hotel ID: " + rs.getInt("hotel_id") + ", Name: " + rs.getString("hotel_name") + ", Address: " + rs.getString("address"));
        }
    }

    static void viewAvailableRooms(Connection conn) throws SQLException {
        System.out.print("Enter hotel ID: ");
        int hotelId = sc.nextInt();
        PreparedStatement ps = conn.prepareStatement("SELECT * FROM rooms WHERE hotel_id = ? AND is_available = TRUE");
        ps.setInt(1, hotelId);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            System.out.println("Room ID: " + rs.getInt("room_id") + ", Type: " + rs.getString("room_type") + ", Price: " + rs.getDouble("price"));
        }
    }

    static void bookRoom(Connection conn) throws SQLException {
        System.out.print("Enter customer ID: ");
        int customerId = sc.nextInt();
        System.out.print("Enter room ID: ");
        int roomId = sc.nextInt();
        sc.nextLine();
        System.out.print("Check-in Date (YYYY-MM-DD): ");
        String checkIn = sc.nextLine();
        System.out.print("Check-out Date (YYYY-MM-DD): ");
        String checkOut = sc.nextLine();

        PreparedStatement ps = conn.prepareStatement("INSERT INTO bookings (customer_id, room_id, check_in_date, check_out_date) VALUES (?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
        ps.setInt(1, customerId);
        ps.setInt(2, roomId);
        ps.setDate(3, java.sql.Date.valueOf(checkIn));
        ps.setDate(4, java.sql.Date.valueOf(checkOut));

        ps.executeUpdate();

        ResultSet keys = ps.getGeneratedKeys();
        keys.next();
        int bookingId = keys.getInt(1);

        PreparedStatement updateRoom = conn.prepareStatement("UPDATE rooms SET is_available = FALSE WHERE room_id = ?");
        updateRoom.setInt(1, roomId);
        updateRoom.executeUpdate();

        System.out.print("Enter amount to pay: ");
        double amount = sc.nextDouble();
        PreparedStatement payment = conn.prepareStatement("INSERT INTO payments (booking_id, amount) VALUES (?, ?)");
        payment.setInt(1, bookingId);
        payment.setDouble(2, amount);
        payment.executeUpdate();

        System.out.println("Room booked and payment successful. Booking ID: " + bookingId);
    }

    static void viewBookings(Connection conn) throws SQLException {
        System.out.print("Enter customer ID: ");
        int customerId = sc.nextInt();
        String query = "SELECT b.booking_id, h.hotel_name, r.room_type, b.check_in_date, b.check_out_date, b.status " +
                       "FROM bookings b JOIN rooms r ON b.room_id = r.room_id " +
                       "JOIN hotels h ON r.hotel_id = h.hotel_id WHERE b.customer_id = ?";
        PreparedStatement ps = conn.prepareStatement(query);
        ps.setInt(1, customerId);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            System.out.println("Booking ID: " + rs.getInt("booking_id") + ", Hotel: " + rs.getString("hotel_name") +
                ", Room: " + rs.getString("room_type") + ", From: " + rs.getDate("check_in_date") +
                ", To: " + rs.getDate("check_out_date") + ", Status: " + rs.getString("status"));
        }
    }

    static void cancelBooking(Connection conn) throws SQLException {
        System.out.print("Enter booking ID to cancel: ");
        int bookingId = sc.nextInt();
        PreparedStatement ps = conn.prepareStatement("UPDATE bookings SET status = 'Cancelled' WHERE booking_id = ?");
        ps.setInt(1, bookingId);
        ps.executeUpdate();

        PreparedStatement getRoom = conn.prepareStatement("SELECT room_id FROM bookings WHERE booking_id = ?");
        getRoom.setInt(1, bookingId);
        ResultSet rs = getRoom.executeQuery();
        if (rs.next()) {
            int roomId = rs.getInt("room_id");
            PreparedStatement updateRoom = conn.prepareStatement("UPDATE rooms SET is_available = TRUE WHERE room_id = ?");
            updateRoom.setInt(1, roomId);
            updateRoom.executeUpdate();
        }

        System.out.println("Booking cancelled and room is now available.");
    }
}
