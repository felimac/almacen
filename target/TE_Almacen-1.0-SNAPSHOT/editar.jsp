<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="com.emergentes.modelo.Productos"%>
<%
    Productos prod = (Productos) request.getAttribute("prod");
%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>        
        <h1>
            <c:if test="${prod.id == 0}">Nuevo Producto</c:if>
            <c:if test="${prod.id != 0}">Editar Producto</c:if>
            </h1>
            <form action="MainController" method="post">
                <table>
                    <input type="hidden" name="id" value="${prod.id}">
                <tr>
                    <td>Producto</td>
                    <td><input type="text" name="producto" value="${prod.producto}"></td>
                </tr>

                <tr>
                    <td>Precio</td>
                    <td><input type="text" name="precio" value="${prod.precio}"></td>
                </tr>
                <tr>
                    <td>Cantidad</td>
                    <td><input type="text" name="cantidad" value="${prod.cantidad}"></td>
                </tr>
                <tr>
                    <td></td>
                    <td><input type="submit"  value="Enviar"></td>
                </tr>
            </table>
        </form>
    </body>
</html>
