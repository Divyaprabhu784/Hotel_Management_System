package com.servlet;

import com.dao.ReservationDAO;
import com.model.Reservation;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;

@WebServlet("/addReservation")
public class AddReservationServlet extends HttpServlet {
    private ReservationDAO reservationDAO;

    @Override
    public void init() throws ServletException {
        reservationDAO = new ReservationDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/reservationadd.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String customerName = request.getParameter("customerName");
            String roomNumber = request.getParameter("roomNumber");
            String checkInStr = request.getParameter("checkIn");
            String checkOutStr = request.getParameter("checkOut");
            String totalAmountStr = request.getParameter("totalAmount");

            // Debug print
            System.out.println("Customer: " + customerName);
            System.out.println("Room: " + roomNumber);
            System.out.println("CheckIn: " + checkInStr);
            System.out.println("CheckOut: " + checkOutStr);
            System.out.println("Amount: " + totalAmountStr);

            if (customerName == null || customerName.trim().isEmpty() ||
            	    roomNumber == null || roomNumber.trim().isEmpty() ||
            	    checkInStr == null || checkInStr.trim().isEmpty() ||
            	    checkOutStr == null || checkOutStr.trim().isEmpty() ||
            	    totalAmountStr == null || totalAmountStr.trim().isEmpty()) {
            	    request.setAttribute("error", "All fields are required");
            	    request.getRequestDispatcher("/reservationadd.jsp").forward(request, response);
            	    return;
            	}


            Date checkIn = Date.valueOf(checkInStr);
            Date checkOut = Date.valueOf(checkOutStr);

            if (checkIn.after(checkOut) || checkIn.before(Date.valueOf(LocalDate.now()))) {
                request.setAttribute("error", "Invalid date range");
                request.getRequestDispatcher("/reservationadd.jsp").forward(request, response);
                return;
            }

            BigDecimal totalAmount;
            try {
                totalAmount = new BigDecimal(totalAmountStr);
            } catch (NumberFormatException e) {
                request.setAttribute("error", "Invalid amount format");
                request.getRequestDispatcher("/reservationadd.jsp").forward(request, response);
                return;
            }

            // Optional: comment this for now if causing issue
            /*
            boolean isAvailable = reservationDAO.isRoomAvailable(roomNumber, checkIn, checkOut);
            if (!isAvailable) {
                request.setAttribute("error", "Room is not available for the selected dates");
                request.getRequestDispatcher("/reservationadd.jsp").forward(request, response);
                return;
            }
            */

            Reservation reservation = new Reservation(customerName, roomNumber, checkIn, checkOut, totalAmount);
            boolean success = reservationDAO.addReservation(reservation);

            if (success) {
                response.sendRedirect("displayReservations");
            } else {
                request.setAttribute("error", "Failed to add reservation");
                request.getRequestDispatcher("/reservationadd.jsp").forward(request, response);
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "An error occurred: " + e.getMessage());
            request.getRequestDispatcher("/reservationadd.jsp").forward(request, response);
        }
    }
}
