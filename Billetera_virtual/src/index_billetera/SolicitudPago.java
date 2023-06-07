package index_billetera;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import java.util.Date;

class SolicitudPago {
    private double cantidad;
    private String motivo;
    private Date horaEnvio;
    private String remitente;
    private Connection connection;

    public SolicitudPago(double cantidad, String motivo, Date horaEnvio, String remitente, Connection connection) {
        this.cantidad = cantidad;
        this.motivo = motivo;
        this.horaEnvio = horaEnvio;
        this.remitente = remitente;
        this.connection = connection;
    }

    public double getCantidad() {
        return cantidad;
    }

    public String getMotivo() {
        return motivo;
    }

    public Date getHoraEnvio() {
        return horaEnvio;
    }

    public String getRemitente() {
        return remitente;
    }

    public void enviarSolicitud() {
        String sql = "INSERT INTO solicitudes_pago (cantidad, motivo, hora_envio, remitente) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setDouble(1, cantidad);
            statement.setString(2, motivo);
            statement.setDate(3, new java.sql.Date(horaEnvio.getTime()));
            statement.setString(4, remitente);
            statement.executeUpdate();
            System.out.println("Solicitud de pago enviada con éxito.");
        } catch (SQLException e) {
            System.out.println("Error al enviar la solicitud de pago: " + e.getMessage());
        }
    }
}
