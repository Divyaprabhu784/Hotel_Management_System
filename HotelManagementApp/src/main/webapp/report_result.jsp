<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*, com.model.Reservation, java.math.BigDecimal" %>
<!DOCTYPE html>
<html>
<head>
    <title>Report Result</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f4f7fa;
            margin: 40px;
            color: #333;
        }

        h2, h3 {
            color: #2c3e50;
        }

        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
            background-color: white;
            box-shadow: 0 0 10px rgba(0,0,0,0.1);
        }

        table, th, td {
            border: 1px solid #ccc;
        }

        th {
            background-color: #2980b9;
            color: white;
            padding: 12px;
        }

        td {
            padding: 10px;
            text-align: center;
        }

        a {
            display: inline-block;
            margin-top: 30px;
            text-decoration: none;
            color: #2980b9;
            font-weight: bold;
        }

        a:hover {
            text-decoration: underline;
        }

        .total-revenue {
            font-size: 18px;
            font-weight: bold;
            color: #27ae60;
            margin-top: 20px;
        }
    </style>
</head>
<body>
    <h2><%= request.getAttribute("reportType") %></h2>
    <h3><%= request.getAttribute("reportTitle") %></h3>

    <%
        List<Reservation> reservations = (List<Reservation>) request.getAttribute("reservations");
        if (reservations != null) {
    %>
        <table>
            <tr>
                <th>ID</th>
                <th>Customer</th>
                <th>Room</th>
                <th>Check-In</th>
                <th>Check-Out</th>
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
    <% } %>

    <% 
        BigDecimal totalRevenue = (BigDecimal) request.getAttribute("totalRevenue");
        if (totalRevenue != null) {
    %>
        <div class="total-revenue">Total Revenue: ₹<%= totalRevenue %></div>
    <% } %>

    <% 
        List<Object[]> roomStats = (List<Object[]>) request.getAttribute("roomStats");
        if (roomStats != null) {
    %>
        <table>
            <tr>
                <th>Room Number</th>
                <th>Times Booked</th>
            </tr>
            <% for (Object[] row : roomStats) { %>
                <tr>
                    <td><%= row[0] %></td>
                    <td><%= row[1] %></td>
                </tr>
            <% } %>
        </table>
    <% } %>

    <a href="report_form.jsp">← Generate Another Report</a>
</body>
</html>
