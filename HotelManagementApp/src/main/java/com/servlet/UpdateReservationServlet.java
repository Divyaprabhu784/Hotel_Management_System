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

@WebServlet("/updateReservation")
public class UpdateReservationServlet extends HttpServlet {
    private ReservationDAO reservationDAO;

    @Override
    public void init() throws ServletException {
        reservationDAO = new ReservationDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idStr = request.getParameter("id");

        if (idStr != null && !idStr.trim().isEmpty()) {
            try {
                int reservationID = Integer.parseInt(idStr);
                Reservation reservation = reservationDAO.getReservationById(reservationID);

                if (reservation != null) {
                    request.setAttribute("reservation", reservation);
                } else {
                    request.setAttribute("error", "Reservation not found.");
                }
            } catch (NumberFormatException e) {
                request.setAttribute("error", "Invalid reservation ID format.");
            }
        } else {
            request.setAttribute("error", "Reservation ID is required.");
        }

        request.getRequestDispatcher("/reservationupdate.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            String reservationIDStr = request.getParameter("reservationID");
            String customerName = request.getParameter("customerName");
            String roomNumber = request.getParameter("roomNumber");
            String checkInStr = request.getParameter("checkIn");
            String checkOutStr = request.getParameter("checkOut");
            String totalAmountStr = request.getParameter("totalAmount");

            // Input validation
            if (isNullOrEmpty(reservationIDStr) || isNullOrEmpty(customerName) ||
                isNullOrEmpty(roomNumber) || isNullOrEmpty(checkInStr) ||
                isNullOrEmpty(checkOutStr) || isNullOrEmpty(totalAmountStr)) {

                request.setAttribute("error", "All fields are required.");
                request.getRequestDispatcher("/reservationupdate.jsp").forward(request, response);
                return;
            }

            int reservationID = Integer.parseInt(reservationIDStr);
            Date checkIn = Date.valueOf(checkInStr);
            Date checkOut = Date.valueOf(checkOutStr);
            BigDecimal totalAmount = new BigDecimal(totalAmountStr);

            if (checkIn.after(checkOut)) {
                request.setAttribute("error", "Check-out date must be after check-in date.");
                request.getRequestDispatcher("/reservationupdate.jsp").forward(request, response);
                return;
            }

            // Construct reservation object
            Reservation reservation = new Reservation(reservationID, customerName, roomNumber, checkIn, checkOut, totalAmount);

            boolean success = reservationDAO.updateReservation(reservation);

            if (success) {
                response.sendRedirect("displayReservations");
            } else {
                request.setAttribute("error", "Failed to update reservation.");
                request.setAttribute("reservation", reservation);
                request.getRequestDispatcher("/reservationupdate.jsp").forward(request, response);
            }

        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid number format: " + e.getMessage());
            request.getRequestDispatcher("/reservationupdate.jsp").forward(request, response);
        } catch (IllegalArgumentException e) {
            request.setAttribute("error", "Invalid date format. Please use YYYY-MM-DD.");
            request.getRequestDispatcher("/reservationupdate.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "An error occurred: " + e.getMessage());
            request.getRequestDispatcher("/reservationupdate.jsp").forward(request, response);
        }
    }

    private boolean isNullOrEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }
}
