<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Select Report Criteria</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f8f9fa;
            display: flex;
            align-items: center;
            justify-content: center;
            height: 100vh;
            margin: 0;
        }
        .container {
            background-color: white;
            padding: 30px;
            border-radius: 10px;
            box-shadow: 0 4px 8px rgba(0,0,0,0.1);
            width: 400px;
        }
        h2 {
            text-align: center;
            margin-bottom: 20px;
            color: #333;
        }
        label {
            font-weight: bold;
        }
        input[type="radio"] {
            margin-top: 10px;
        }
        input[type="date"], button {
            width: 100%;
            padding: 8px;
            margin-top: 5px;
            margin-bottom: 15px;
            border: 1px solid #ccc;
            border-radius: 4px;
        }
        button {
            background-color: #007bff;
            color: white;
            font-weight: bold;
            border: none;
            cursor: pointer;
        }
        button:hover {
            background-color: #0056b3;
        }
        a {
            display: block;
            text-align: center;
            margin-top: 10px;
            color: #007bff;
            text-decoration: none;
        }
        a:hover {
            text-decoration: underline;
        }
        p {
            text-align: center;
        }
    </style>
</head>
<body>
    <div class="container">
        <h2>📋 Generate Report</h2>

        <% if (request.getAttribute("error") != null) { %>
            <p style="color:red;"><%= request.getAttribute("error") %></p>
        <% } %>

        <form action="generateReport" method="post">
            <label>Select Report Type:</label><br>
            <input type="radio" name="reportType" value="dateRange" required> Reservations by Date Range<br>
            <input type="radio" name="reportType" value="roomFrequency" required> Most Frequently Booked Rooms<br>
            <input type="radio" name="reportType" value="revenue" required> Revenue Report<br>

            <br>
            <label>Start Date (yyyy-mm-dd):</label><br>
            <input type="date" name="startDate"><br>

            <label>End Date (yyyy-mm-dd):</label><br>
            <input type="date" name="endDate"><br>

            <br>
            <button type="submit">Generate Report</button>
        </form>

        <a href="reports.jsp">← Back to Reports</a>
    </div>
</body>
</html>
