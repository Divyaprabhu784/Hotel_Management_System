<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Hotel Management System</title>
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
            display: flex;
            align-items: center;
            justify-content: center;
        }
        
        .container {
            background: white;
            border-radius: 15px;
            box-shadow: 0 20px 40px rgba(0,0,0,0.1);
            padding: 40px;
            text-align: center;
            max-width: 800px;
            width: 90%;
        }
        
        h1 {
            color: #333;
            margin-bottom: 30px;
            font-size: 2.5em;
        }
        
        .menu-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
            gap: 20px;
            margin-top: 30px;
        }
        
        .menu-item {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            padding: 25px;
            border-radius: 10px;
            text-decoration: none;
            transition: transform 0.3s ease, box-shadow 0.3s ease;
            border: none;
            cursor: pointer;
        }
        
        .menu-item:hover {
            transform: translateY(-5px);
            box-shadow: 0 10px 25px rgba(0,0,0,0.2);
            text-decoration: none;
            color: white;
        }
        
        .menu-item h3 {
            margin-bottom: 10px;
            font-size: 1.3em;
        }
        
        .menu-item p {
            font-size: 0.9em;
            opacity: 0.9;
        }
        
        .welcome-text {
            color: #666;
            font-size: 1.1em;
            margin-bottom: 20px;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>🏨 Hotel Management System</h1>
        <p class="welcome-text">Welcome to our comprehensive hotel reservation management system</p>
        
        <div class="menu-grid">
            <a href="addReservation" class="menu-item">
                <h3>📝 Add Reservation</h3>
                <p>Create new hotel reservations</p>
            </a>
            
            <a href="displayReservations" class="menu-item">
                <h3>📋 View Reservations</h3>
                <p>Display all current reservations</p>
            </a>
            
            <a href="reportCriteria" class="menu-item">
                <h3>📊 Generate Reports</h3>
                <p>View detailed reports and analytics</p>
            </a>
            
            <a href="updateReservation" class="menu-item">
                <h3>✏️ Update Reservation</h3>
                <p>Modify existing reservations</p>
            </a>
            
            <a href="deleteReservation" class="menu-item">
                <h3>🗑️ Cancel Reservation</h3>
                <p>Remove reservations from system</p>
            </a>
        </div>
    </div>
</body>
</html>