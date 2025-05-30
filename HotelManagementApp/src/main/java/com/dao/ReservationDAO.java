package com.dao;

import com.model.Reservation;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReservationDAO {
    private final String jdbcURL = "jdbc:mysql://localhost:3306/hotel_reservation_db";
    private final String jdbcUsername = "root";
    private final String jdbcPassword = ""; // Replace with your MySQL password

    private static final String INSERT_SQL = "INSERT INTO Reservations (CustomerName, RoomNumber, CheckIn, CheckOut, TotalAmount) VALUES (?, ?, ?, ?, ?)";
    private static final String SELECT_CONFLICT_SQL = "SELECT * FROM Reservations WHERE RoomNumber = ? AND (CheckIn < ? AND CheckOut > ?)";
    private static final String SELECT_BY_ID_SQL = "SELECT * FROM Reservations WHERE ReservationID = ?";
    private static final String DELETE_SQL = "DELETE FROM Reservations WHERE ReservationID = ?";

    public ReservationDAO() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // Load MySQL driver
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
    }

    // Add a new reservation
    public boolean addReservation(Reservation reservation) {
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(INSERT_SQL)) {
            ps.setString(1, reservation.getCustomerName());
            ps.setString(2, reservation.getRoomNumber());
            ps.setDate(3, reservation.getCheckIn());
            ps.setDate(4, reservation.getCheckOut());
            ps.setBigDecimal(5, reservation.getTotalAmount());

            int rowsInserted = ps.executeUpdate();
            return rowsInserted > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Check room availability
    public boolean isRoomAvailable(String roomNumber, Date checkIn, Date checkOut) {
        String sql = "SELECT * FROM Reservations WHERE RoomNumber = ? AND NOT (CheckOut <= ? OR CheckIn >= ?)";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, roomNumber);
            ps.setDate(2, checkIn);  // existing reservation must end before new check-in
            ps.setDate(3, checkOut); // existing reservation must start after new check-out
            ResultSet rs = ps.executeQuery();
            return !rs.next(); // If no conflicts, room is available
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    // Get a reservation by ID
    public Reservation getReservationById(int id) {
        Reservation reservation = null;
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(SELECT_BY_ID_SQL)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                reservation = mapResultSetToReservation(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reservation;
    }

    // Delete a reservation by ID
    public boolean deleteReservation(int reservationID) {
        boolean rowDeleted = false;
        String sql = "DELETE FROM Reservations WHERE ReservationID = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, reservationID);
            int affectedRows = stmt.executeUpdate();
            rowDeleted = (affectedRows > 0);

            System.out.println("Deleted rows: " + affectedRows); // Debug print

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rowDeleted;
    }



    // Get all reservations
    public List<Reservation> getAllReservations() {
        List<Reservation> reservations = new ArrayList<>();
        String SELECT_ALL_SQL = "SELECT * FROM Reservations";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_ALL_SQL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                reservations.add(mapResultSetToReservation(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reservations;
    }

    // Get reservations by date range
    public List<Reservation> getReservationsByDateRange(Date startDate, Date endDate) {
        List<Reservation> reservations = new ArrayList<>();
        String sql = "SELECT * FROM Reservations WHERE CheckIn >= ? AND CheckOut <= ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, startDate);
            ps.setDate(2, endDate);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                reservations.add(mapResultSetToReservation(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reservations;
    }

    // Get rooms by booking frequency
    public List<Object[]> getMostFrequentlyBookedRooms() {
        List<Object[]> roomStats = new ArrayList<>();
        String sql = "SELECT RoomNumber, COUNT(*) AS Frequency FROM Reservations GROUP BY RoomNumber ORDER BY Frequency DESC";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                roomStats.add(new Object[]{rs.getString("RoomNumber"), rs.getInt("Frequency")});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return roomStats;
    }

    // Calculate total revenue
    public BigDecimal getTotalRevenue(Date startDate, Date endDate) {
        BigDecimal totalRevenue = BigDecimal.ZERO;
        String sql = "SELECT SUM(TotalAmount) AS TotalRevenue FROM Reservations WHERE CheckIn >= ? AND CheckOut <= ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, startDate);
            ps.setDate(2, endDate);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                totalRevenue = rs.getBigDecimal("TotalRevenue");
                if (totalRevenue == null) {
                    totalRevenue = BigDecimal.ZERO;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return totalRevenue;
    }

    // Update reservation
    public boolean updateReservation(Reservation reservation) {
        String sql = "UPDATE Reservations SET CustomerName = ?, RoomNumber = ?, CheckIn = ?, CheckOut = ?, TotalAmount = ? WHERE ReservationID = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, reservation.getCustomerName());
            ps.setString(2, reservation.getRoomNumber());
            ps.setDate(3, reservation.getCheckIn());
            ps.setDate(4, reservation.getCheckOut());
            ps.setBigDecimal(5, reservation.getTotalAmount());
            ps.setInt(6, reservation.getReservationID());

            int rowsUpdated = ps.executeUpdate();
            return rowsUpdated > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Utility method to map ResultSet to Reservation object
    private Reservation mapResultSetToReservation(ResultSet rs) throws SQLException {
        Reservation reservation = new Reservation();
        reservation.setReservationID(rs.getInt("ReservationID"));
        reservation.setCustomerName(rs.getString("CustomerName"));
        reservation.setRoomNumber(rs.getString("RoomNumber"));
        reservation.setCheckIn(rs.getDate("CheckIn"));
        reservation.setCheckOut(rs.getDate("CheckOut"));
        reservation.setTotalAmount(rs.getBigDecimal("TotalAmount"));
        return reservation;
    }
}
