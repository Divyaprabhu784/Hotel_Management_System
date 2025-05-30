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
import java.util.List;

@WebServlet("/generateReport")
public class ReportServlet extends HttpServlet {
    private ReservationDAO reservationDAO;

    @Override
    public void init() throws ServletException {
        reservationDAO = new ReservationDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Forward to the report selection form
        request.getRequestDispatcher("/report_form.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            String reportType = request.getParameter("reportType");

            if (reportType == null || reportType.trim().isEmpty()) {
                request.setAttribute("error", "Please select a report type.");
                request.getRequestDispatcher("/report_form.jsp").forward(request, response);
                return;
            }

            switch (reportType) {
                case "dateRange":
                    generateDateRangeReport(request, response);
                    break;
                case "roomFrequency":
                    generateRoomFrequencyReport(request, response);
                    break;
                case "revenue":
                    generateRevenueReport(request, response);
                    break;
                default:
                    request.setAttribute("error", "Invalid report type selected.");
                    request.getRequestDispatcher("/report_form.jsp").forward(request, response);
                    break;
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "An error occurred while generating the report: " + e.getMessage());
            request.getRequestDispatcher("/report_form.jsp").forward(request, response);
        }
    }

    private void generateDateRangeReport(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String startDateStr = request.getParameter("startDate");
        String endDateStr = request.getParameter("endDate");

        if (isInvalidDateInput(startDateStr, endDateStr, request, response)) return;

        Date startDate = Date.valueOf(startDateStr);
        Date endDate = Date.valueOf(endDateStr);

        List<Reservation> reservations = reservationDAO.getReservationsByDateRange(startDate, endDate);

        request.setAttribute("reportType", "Date Range Report");
        request.setAttribute("reportTitle", "Reservations from " + startDate + " to " + endDate);
        request.setAttribute("reservations", reservations);
        request.getRequestDispatcher("/report_result.jsp").forward(request, response);
    }

    private void generateRoomFrequencyReport(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<Object[]> roomStats = reservationDAO.getMostFrequentlyBookedRooms();

        request.setAttribute("reportType", "Room Frequency Report");
        request.setAttribute("reportTitle", "Most Frequently Booked Rooms");
        request.setAttribute("roomStats", roomStats);
        request.getRequestDispatcher("/report_result.jsp").forward(request, response);
    }

    private void generateRevenueReport(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String startDateStr = request.getParameter("startDate");
        String endDateStr = request.getParameter("endDate");

        if (isInvalidDateInput(startDateStr, endDateStr, request, response)) return;

        Date startDate = Date.valueOf(startDateStr);
        Date endDate = Date.valueOf(endDateStr);

        BigDecimal totalRevenue = reservationDAO.getTotalRevenue(startDate, endDate);
        List<Reservation> reservations = reservationDAO.getReservationsByDateRange(startDate, endDate);

        request.setAttribute("reportType", "Revenue Report");
        request.setAttribute("reportTitle", "Revenue from " + startDate + " to " + endDate);
        request.setAttribute("totalRevenue", totalRevenue);
        request.setAttribute("reservations", reservations);
        request.getRequestDispatcher("/report_result.jsp").forward(request, response);
    }

    // Utility method to validate dates
    private boolean isInvalidDateInput(String startDateStr, String endDateStr,
                                       HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (startDateStr == null || startDateStr.trim().isEmpty() ||
            endDateStr == null || endDateStr.trim().isEmpty()) {
            request.setAttribute("error", "Start date and end date are required.");
            request.getRequestDispatcher("/report_form.jsp").forward(request, response);
            return true;
        }

        try {
            Date startDate = Date.valueOf(startDateStr);
            Date endDate = Date.valueOf(endDateStr);
            if (startDate.after(endDate)) {
                request.setAttribute("error", "Start date must be before or equal to end date.");
                request.getRequestDispatcher("/report_form.jsp").forward(request, response);
                return true;
            }
        } catch (IllegalArgumentException e) {
            request.setAttribute("error", "Invalid date format. Please use YYYY-MM-DD.");
            request.getRequestDispatcher("/report_form.jsp").forward(request, response);
            return true;
        }

        return false;
    }
}
