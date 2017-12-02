/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import data.DBConnection;
import data.DonacionDAO;
import data.DonadorDAO;
import data.ReceptorDAO;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import models.Donacion;
import models.Donador;
import models.Receptor;


/**
 *
 * @author juans
 */
@WebServlet(name = "RegistroDonacionServlet", urlPatterns = {"/registroDonacion"})
public class RegistroDonacionServlet extends HttpServlet {

    public static String message = "";
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
   

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //Parse birthdate before creating object
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        java.util.Date parsed = null;
        try {
            parsed = format.parse(request.getParameter("fecha_donacion"));
        } catch (ParseException ex) {
            Logger.getLogger(RegistroReceptorServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        java.sql.Date fechaDonacion = new java.sql.Date(parsed.getTime());
        
         //Make DB connection
        DBConnection dbConn = new DBConnection();
        Connection conn = dbConn.getConnection();
        
        ReceptorDAO receptorDao = new ReceptorDAO(conn);
        DonadorDAO donadorDao = new DonadorDAO(conn);
        ArrayList<Receptor> receptores = new ArrayList();
        ArrayList<Donador> donadores = new ArrayList();
        
        receptores = receptorDao.selectAll();
        donadores = donadorDao.selectAll();
        
        
        Receptor receptor = receptores.get(Integer.parseInt(request.getParameter("receptor"))-1);
        Donador donador = donadores.get(Integer.parseInt(request.getParameter("donador"))-1);
        //Create receptor object
        Donacion donacion = new Donacion(receptor,
                                      donador,
                                      fechaDonacion,
                                      request.getParameter("ubicacion"),
                                      request.getParameter("comentarios"));
                                      
        
        
        

        DonacionDAO donacionDao = new DonacionDAO(conn);
        donacionDao.insert(donacion);
        
        // set User object in request object and set URL
        String resp = "true";
        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(message);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
