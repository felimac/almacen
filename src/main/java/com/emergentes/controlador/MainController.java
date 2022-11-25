package com.emergentes.controlador;

import com.emergentes.modelo.Productos;
import com.emergentes.utiles.ConexionDB;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "MainController", urlPatterns = {"/MainController"})
public class MainController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String op;
            op = (request.getParameter("op") != null) ? request.getParameter("op") : "list";
            ArrayList<com.emergentes.modelo.Productos> lista = new ArrayList<com.emergentes.modelo.Productos>();
            ConexionDB canal = new ConexionDB();
            Connection conn = canal.conectar();
            PreparedStatement ps;
            ResultSet rs;
            if (op.equals("list")) {

                //Para listar los datos
                String sql = "select * from productos";
                //consulta de seleccion y almacenamiento en una coleccion
                ps = conn.prepareStatement(sql);
                rs = ps.executeQuery();

                while (rs.next()) {
                    com.emergentes.modelo.Productos pro = new com.emergentes.modelo.Productos();
                    pro.setId(rs.getInt("id"));
                    pro.setProducto(rs.getString("producto"));
                    pro.setPrecio(rs.getFloat("precio"));
                    pro.setCantidad(rs.getInt("cantidad"));
                    lista.add(pro);
                }
                request.setAttribute("lista", lista);
                //enviar al index para mostrar la informacion
                request.getRequestDispatcher("index.jsp").forward(request, response);

            }
            if (op.equals("nuevo")) {
                //instanciar un objeto de la clase producto
                com.emergentes.modelo.Productos pr = new com.emergentes.modelo.Productos();
                //el objeto se pone como atributo de request
                request.setAttribute("prod", pr);
                //redireccionar a editar
                request.getRequestDispatcher("editar.jsp").forward(request, response);

            }
            if (op.equals("modificar")) {
                int id = Integer.parseInt(request.getParameter("id"));
                String sql = "select * from productos where id = ?";

                ps = conn.prepareStatement(sql);
                ps.setInt(1, id);
                rs = ps.executeQuery();
                com.emergentes.modelo.Productos pr = new com.emergentes.modelo.Productos();
                while (rs.next()) {

                    pr.setId(rs.getInt("id"));
                    pr.setProducto(rs.getString("producto"));
                    pr.setPrecio(rs.getFloat("precio"));
                    pr.setCantidad(rs.getInt("cantidad"));
                }
                request.setAttribute("prod", pr);
                request.getRequestDispatcher("editar.jsp").forward(request, response);
            }
            if (op.equals("eliminar")) {
                //obtener el id
                int id = Integer.parseInt(request.getParameter("id"));
                //realizar la eliminacion en la base de datos
                String sql = "delete from productos where id = ?";
                ps = conn.prepareStatement(sql);
                ps.setInt(1, id);
                ps.executeUpdate();
                //redireccionar a maincontroller
                response.sendRedirect("MainController");
            }
        } catch (SQLException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        String producto = request.getParameter("producto");
        float precio = Float.parseFloat(request.getParameter("precio"));
        int cantidad = Integer.parseInt(request.getParameter("cantidad"));

        com.emergentes.modelo.Productos prod = new com.emergentes.modelo.Productos();
        prod.setProducto(producto);
        prod.setId(id);
        prod.setPrecio(precio);
        prod.setCantidad(cantidad);

        ConexionDB canal = new ConexionDB();
        Connection conn = canal.conectar();
        PreparedStatement ps;

        if (id == 0) {

            //nuevo registro
            String sql = "insert into productos (producto, precio, cantidad) values (?,?,?)";
            try {
                ps = conn.prepareStatement(sql);
                ps.setString(1, prod.getProducto());
                ps.setFloat(2, prod.getPrecio());
                ps.setInt(3, prod.getCantidad());
                ps.executeUpdate();

            } catch (SQLException ex) {
                System.out.println("error de sql" + ex.getMessage());
            } finally {
                canal.desconectar();
            }
            response.sendRedirect("MainController");

        } else {

            //edicion de registro
            String sql = "update productos set producto = ?, precio = ?, cantidad = ? where id = ?";
            try {
                ps = conn.prepareStatement(sql);

                ps.setString(1, prod.getProducto());
                ps.setFloat(2, prod.getPrecio());
                ps.setInt(3, prod.getCantidad());
                ps.setInt(4, prod.getId());
                ps.executeUpdate();

            } catch (SQLException ex) {
                System.out.println("error en sql " + ex.getMessage());
            } finally {
                canal.desconectar();
            }
            response.sendRedirect("MainController");
        }
    }
}
