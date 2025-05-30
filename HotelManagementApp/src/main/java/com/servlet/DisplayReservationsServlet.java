package com.servlet;

import com.dao.ReservationDAO;
import com.model.Reservation;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/displayReservations")
public class DisplayReservationsServlet extends HttpServlet {
    private ReservationDAO reservationDAO;

    @Override
    public void init() throws ServletException {
        reservationDAO = new ReservationDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            // Fetch all reservations from DAO
            List<Reservation> reservations = reservationDAO.getAllReservations();
            request.setAttribute("reservations", reservations);

            // Forward any success/error messages passed as query parameters
            String success = request.getParameter("success");
            String error = request.getParameter("error");

            if (success != null && !success.isEmpty()) {
                request.setAttribute("success", success);
            }
            if (error != null && !error.isEmpty()) {
                request.setAttribute("error", error);
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Failed to retrieve reservations: " + e.getMessage());
        }

        // Forward to JSP page for display
        request.getRequestDispatcher("/reservationdisplay.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Redirect POST to GET for safe retrieval
        doGet(request, response);
    }
}
