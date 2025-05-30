<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.model.Reservation" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Update Reservation</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }
        
        body {
            font-family: 'Arial', sans-serif;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
            padding: 20px;
        }
        
        .container {
            max-width: 600px;
            margin: 0 auto;
            background: white;
            border-radius: 15px;
            box-shadow: 0 20px 40px rgba(0,0,0,0.1);
            padding: 30px;
        }
        
        h2 {
            color: #333;
            text-align: center;
            margin-bottom: 30px;
            font-size: 2em;
        }
        
        .form-group {
            margin-bottom: 20px;
        }
        
        label {
            display: block;
            margin-bottom: 8px;
            color: #333;
            font-weight: bold;
        }
        
        input[type="text"],
        input[type="date"],
        input[type="number"],
        input[type="hidden"] {
            width: 100%;
            padding: 12px;
            border: 2px solid #ddd;
            border-radius: 8px;
            font-size: 16px;
            transition: border-color 0.3s ease;
        }
        
        input[type="text"]:focus,
        input[type="date"]:focus,
        input[type="number"]:focus {
            outline: none;
            border-color: #667eea;
        }
        
        .btn {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            padding: 12px 30px;
            border: none;
            border-radius: 8px;
            font-size: 16px;
            cursor: pointer;
            transition: transform 0.3s ease;
            margin-right: 10px;
        }
        
        .btn:hover {
            transform: translateY(-2px);
        }
        
        .btn-secondary {
            background: #6c757d;
        }
        
        .btn-container {
            text-align: center;
            margin-top: 30px;
        }
        
        .alert {
            padding: 15px;
            margin-bottom: 20px;
            border-radius: 8px;
            font-weight: bold;
        }
        
        .alert-success {
            background-color: #d4edda;
            color: #155724;
            border: 1px solid #c3e6cb;
        }
        
        .alert-error {
            background-color: #f8d7da;
            color: #721c24;
            border: 1px solid #f5c6cb;
        }
        
        .back-link {
            display: inline-block;
            margin-bottom: 20px;
            color: #667eea;
            text-decoration: none;
            font-weight: bold;
        }
        
        .back-link:hover {
            text-decoration: underline;
        }
        
        .search-section {
            background: #f8f9fa;
            padding: 20px;
            border-radius: 8px;
            margin-bottom: 30px;
        }
        
        .search-form {
            display: flex;
            gap: 10px;
            align-items: end;
        }
        
        .search-form input {
            flex: 1;
        }
        
        .search-form button {
            background: #28a745;
            white-space: nowrap;
        }
    </style>
</head>
<body>
    <div class="container">
        <a href="index.jsp" class="back-link">← Back to Main Menu</a>
        
        <h2>✏️ Update Reservation</h2>
        
        <% if (request.getAttribute("error") != null) { %>
            <div class="alert alert-error">
                <%= request.getAttribute("error") %>
            </div>
        <% } %>
        
        <% if (request.getAttribute("success") != null) { %>
            <div class="alert alert-success">
                <%= request.getAttribute("success") %>
            </div>
        <% } %>
        
        <!-- Search for reservation to update -->
        <% if (request.getAttribute("reservation") == null) { %>
            <div class="search-section">
                <h3>Find Reservation to Update</h3>
                <form action="updateReservation" method="get" class="search-form">
                    <div class="form-group" style="margin-bottom: 0; flex: 1;">
                        <label for="searchId">Reservation ID:</label>
                        <input type="number" id="searchId" name="id" required 
                               placeholder="Enter reservation ID to update">
                    </div>
                    <button type="submit" class="btn" style="background: #28a745;">Search</button>
                </form>
            </div>
        <% } %>
        
        <!-- Update form (shown when reservation is found) -->
        <% 
            Reservation reservation = (Reservation) request.getAttribute("reservation");
            if (reservation != null) { 
        %>
            <form action="updateReservation" method="post">
                <input type="hidden" name="reservationID" value="<%= reservation.getReservationID() %>">
                
                <div class="form-group">
                    <label>Reservation ID:</label>
                    <input type="text" value="<%= reservation.getReservationID() %>" readonly 
                           style="background-color: #e9ecef;">
                </div>
                
                <div class="form-group">
                    <label for="customerName">Customer Name:</label>
                    <input type="text" id="customerName" name="customerName" required 
                           value="<%= reservation.getCustomerName() %>">
                </div>
                
                <div class="form-group">
                    <label for="roomNumber">Room Number:</label>
                    <input type="text" id="roomNumber" name="roomNumber" required 
                           value="<%= reservation.getRoomNumber() %>">
                </div>
                
                <div class="form-group">
                    <label for="checkIn">Check-in Date:</label>
                    <input type="date" id="checkIn" name="checkIn" required 
                           value="<%= reservation.getCheckIn() %>">
                </div>
                
                <div class="form-group">
                    <label for="checkOut">Check-out Date:</label>
                    <input type="date" id="checkOut" name="checkOut" required 
                           value="<%= reservation.getCheckOut() %>">
                </div>
                
                <div class="form-group">
                    <label for="totalAmount">Total Amount ($):</label>
                    <input type="number" id="totalAmount" name="totalAmount" required 
                           min="0" step="0.01" value="<%= reservation.getTotalAmount() %>">
                </div>
                
                <div class="btn-container">
                    <button type="submit" class="btn">Update Reservation</button>
                    <a href="displayReservations" class="btn btn-secondary">View All Reservations</a>
                </div>
            </form>
        <% } %>
    </div>
    
    <script>
        // Update checkout minimum date when checkin changes
        document.getElementById('checkIn')?.addEventListener('change', function() {
            var checkinDate = new Date(this.value);
            checkinDate.setDate(checkinDate.getDate() + 1);
            document.getElementById('checkOut').min = checkinDate.toISOString().split('T')[0];
        });
    </script>
</body>
</html>