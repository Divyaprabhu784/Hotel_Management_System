package com.servlet;

import com.dao.ReservationDAO;
import com.model.Reservation;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/deleteReservation")
public class DeleteReservationServlet extends HttpServlet {
    private ReservationDAO reservationDAO;

    @Override
    public void init() throws ServletException {
        reservationDAO = new ReservationDAO();
    }

    // Show the delete confirmation page with reservation details
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
                request.setAttribute("error", "Invalid reservation ID.");
            }
        } else {
            request.setAttribute("error", "Reservation ID is missing.");
        }

        request.getRequestDispatcher("/reservationdelete.jsp").forward(request, response);
    }

    // Process the deletion after confirmation
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String reservationIDStr = request.getParameter("reservationID");
        String confirmDelete = request.getParameter("confirmDelete");

        try {
            if (reservationIDStr == null || reservationIDStr.trim().isEmpty()) {
                request.setAttribute("error", "Reservation ID is required.");
                request.getRequestDispatcher("/reservationdelete.jsp").forward(request, response);
                return;
            }

            int reservationID = Integer.parseInt(reservationIDStr);

            // Check if checkbox value is "yes"
            if (confirmDelete == null || !confirmDelete.equalsIgnoreCase("yes")) {
                request.setAttribute("error", "Please confirm deletion by checking the box.");
                Reservation reservation = reservationDAO.getReservationById(reservationID);
                request.setAttribute("reservation", reservation);
                request.getRequestDispatcher("/reservationdelete.jsp").forward(request, response);
                return;
            }

            boolean success = reservationDAO.deleteReservation(reservationID);

            if (success) {
                // Redirect to reservations list page with success message
                response.sendRedirect("displayReservations?success=Reservation+deleted+successfully");
            } else {
                request.setAttribute("error", "Failed to delete reservation.");
                Reservation reservation = reservationDAO.getReservationById(reservationID);
                request.setAttribute("reservation", reservation);
                request.getRequestDispatcher("/reservationdelete.jsp").forward(request, response);
            }

        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid reservation ID format.");
            request.getRequestDispatcher("/reservationdelete.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "An error occurred: " + e.getMessage());
            request.getRequestDispatcher("/reservationdelete.jsp").forward(request, response);
        }
    }
}
