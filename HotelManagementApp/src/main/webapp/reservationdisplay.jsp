<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*, com.model.Reservation" %>
<!DOCTYPE html>
<html>
<head>
    <title>All Reservations</title>
    <style>
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background-color: #f4f4f4;
            margin: 0;
            padding: 20px;
        }
        h2 {
            text-align: center;
            color: #333;
        }
        .message {
            text-align: center;
            margin-bottom: 20px;
            padding: 10px;
            border-radius: 5px;
        }
        .success {
            background-color: #d4edda;
            color: #155724;
        }
        .error {
            background-color: #f8d7da;
            color: #721c24;
        }
        table {
            width: 90%;
            margin: 0 auto 30px auto;
            border-collapse: collapse;
            background-color: #fff;
            box-shadow: 0 0 8px rgba(0,0,0,0.1);
        }
        th, td {
            padding: 12px;
            text-align: center;
            border-bottom: 1px solid #ddd;
        }
        th {
            background-color: #007bff;
            color: white;
        }
        tr:hover {
            background-color: #f1f1f1;
        }
        .btn-back {
            display: block;
            width: fit-content;
            margin: 0 auto;
            padding: 10px 20px;
            background-color: #28a745;
            color: white;
            text-decoration: none;
            border-radius: 8px;
            font-weight: bold;
        }
        .btn-back:hover {
            background-color: #218838;
        }
    </style>
</head>
<body>

    <h2>📖 All Reservations</h2>

    <% if (request.getAttribute("success") != null) { %>
        <div class="message success"><%= request.getAttribute("success") %></div>
    <% } %>
    <% if (request.getAttribute("error") != null) { %>
        <div class="message error"><%= request.getAttribute("error") %></div>
    <% } %>

    <%
        List<Reservation> reservations = (List<Reservation>) request.getAttribute("reservations");
        if (reservations != null && !reservations.isEmpty()) {
    %>
        <table>
            <tr>
                <th>Reservation ID</th>
                <th>Customer Name</th>
                <th>Room Number</th>
                <th>Check-In Date</th>
                <th>Check-Out Date</th>
                <th>Total Amount</th>
            </tr>
            <% for (Reservation r : reservations) { %>
                <tr>
                    <td><%= r.getReservationID() %></td>
                    <td><%= r.getCustomerName() %></td>
                    <td><%= r.getRoomNumber() %></td>
                    <td><%= r.getCheckIn() %></td>
                    <td><%= r.getCheckOut() %></td>
                    <td>₹<%= r.getTotalAmount() %></td>
                </tr>
            <% } %>
        </table>
    <% } else { %>
        <p style="text-align:center;">No reservations found.</p>
    <% } %>

    <a href="reservationadd.jsp" class="btn-back">← Back to Add Reservation</a>

</body>
</html>
