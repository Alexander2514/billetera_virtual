package index_billetera;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GestorSolicitudes {
    private List<SolicitudPago> solicitudes;
    private Connection connection;

    public GestorSolicitudes( String nombreUsuario, String contraseña) {
        this.solicitudes = new ArrayList<>();
        this.connection = getConnection( nombreUsuario, contraseña);
    }

    public Connection getConnection( String usuario, String contraseña) {
        Connection connection = null;
        try {
            // Construye la URL de conexión utilizando el nombre de la base de datos proporcionado
            String url = "jdbc:mysql://localhost:3306/gestor_solicitudes";
            usuario= "root";
            contraseña="123456";

            // Establece la conexión a la base de datos utilizando los detalles proporcionados
            connection = DriverManager.getConnection(url, usuario, contraseña);
        } catch (SQLException e) {
            System.out.println("Error al obtener la conexión: " + e.getMessage());
        }
        return connection;
    }


    public void agregarSolicitud(double cantidad, String motivo, Date horaEnvio, String remitente) {
        SolicitudPago solicitud = new SolicitudPago(cantidad, motivo, new java.sql.Date(horaEnvio.getTime()), remitente, connection);
        solicitud.enviarSolicitud();
    }

    public List<SolicitudPago> getSolicitudes() {
        List<SolicitudPago> solicitudes = new ArrayList<>();
        String sql = "SELECT * FROM solicitudes_pago";
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                double cantidad = resultSet.getDouble("cantidad");
                String motivo = resultSet.getString("motivo");
                java.sql.Date horaEnvio = resultSet.getDate("hora_envio");
                String remitente = resultSet.getString("remitente");

                SolicitudPago solicitud = new SolicitudPago(cantidad, motivo, horaEnvio, remitente, connection);
                solicitudes.add(solicitud);
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener las solicitudes de pago: " + e.getMessage());
        }
        return solicitudes;
    }}

