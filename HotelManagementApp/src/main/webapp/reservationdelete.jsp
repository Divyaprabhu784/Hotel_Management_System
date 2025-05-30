<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.model.Reservation" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <title>Cancel Reservation</title>
    <style>
        /* Add your actual CSS here */
        .container {
            max-width: 700px;
            margin: auto;
            padding: 20px;
            font-family: Arial, sans-serif;
        }
        .back-link {
            text-decoration: none;
            color: #007bff;
            margin-bottom: 20px;
            display: inline-block;
        }
        .alert {
            padding: 10px;
            margin-bottom: 15px;
        }
        .alert-error {
            color: #721c24;
            background-color: #f8d7da;
        }
        .alert-success {
            color: #155724;
            background-color: #d4edda;
        }
        .form-group {
            margin-bottom: 15px;
        }
        .btn {
            padding: 8px 15px;
            margin-right: 10px;
            cursor: pointer;
        }
        .btn-danger {
            background-color: #dc3545;
            color: white;
            border: none;
        }
        .btn-secondary {
            background-color: #6c757d;
            color: white;
            border: none;
        }
        .btn-success {
            background-color: #28a745;
            color: white;
            border: none;
        }
    </style>
</head>
<body>
    <div class="container">
        <a href="index.jsp" class="back-link">← Back to Main Menu</a>

        <h2>🗑️ Cancel Reservation</h2>

        <% if (request.getAttribute("error") != null) { %>
            <div class="alert alert-error">
                <%= request.getAttribute("error") %>
            </div>
        <% } %>

        <% if (request.getParameter("success") != null) { %>
            <div class="alert alert-success">
                <%= request.getParameter("success") %>
            </div>
        <% } %>

        <% if (request.getAttribute("reservation") == null) { %>
            <div class="search-section">
                <h3>Find Reservation to Cancel</h3>
                <form action="deleteReservation" method="get" class="search-form">
                    <div class="form-group">
                        <label for="searchId">Reservation ID:</label>
                        <input type="number" id="searchId" name="id" required placeholder="Enter reservation ID to cancel" />
                    </div>
                    <button type="submit" class="btn btn-success">Search</button>
                </form>
            </div>
        <% } %>

        <%
            Reservation reservation = (Reservation) request.getAttribute("reservation");
            if (reservation != null) {
        %>
            <div class="reservation-details">
                <h3>⚠️ Reservation Details</h3>
                <p><strong>Reservation ID:</strong> <%= reservation.getReservationID() %></p>
                <p><strong>Customer Name:</strong> <%= reservation.getCustomerName() %></p>
                <p><strong>Room Number:</strong> <%= reservation.getRoomNumber() %></p>
                <p><strong>Check-in Date:</strong> <%= reservation.getCheckIn() %></p>
                <p><strong>Check-out Date:</strong> <%= reservation.getCheckOut() %></p>
                <p><strong>Total Amount:</strong> ₹<%= reservation.getTotalAmount() %></p>
            </div>

            <div class="confirmation-section">
                <h3>⚠️ Confirm Cancellation</h3>
                <form action="deleteReservation" method="post">
                    <input type="hidden" name="reservationID" value="<%= reservation.getReservationID() %>" />

                    <div class="checkbox-group">
                        <!-- ✅ Checkbox has value="yes", needed for server-side check -->
                        <input type="checkbox" id="confirmDelete" name="confirmDelete" value="yes" required />
                        <label for="confirmDelete">Yes, I want to cancel this reservation</label>
                    </div>

                    <div class="btn-container">
                        <button type="submit" class="btn btn-danger">Cancel Reservation</button>
                        <a href="cancelReservation.jsp" class="btn btn-secondary">Back</a>
                    </div>
                </form>
            </div>
        <% } %>
    </div>
</body>
</html>
