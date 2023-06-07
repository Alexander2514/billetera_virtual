package index_billetera;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Transaccion {
    private double cantidad;
    private String motivo;
    private Date horaEnvio;
    private String nombreUsuarioRemitente;
    private String nombreUsuarioDestinatario;

    public Transaccion(double cantidad, String motivo, Date horaEnvio, String nombreUsuarioRemitente, String nombreUsuarioDestinatario) {
        this.cantidad = cantidad;
        this.motivo = motivo;
        this.horaEnvio = horaEnvio;
        this.nombreUsuarioRemitente = nombreUsuarioRemitente;
        this.nombreUsuarioDestinatario = nombreUsuarioDestinatario;
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

    public String getNombreUsuarioRemitente() {
        return nombreUsuarioRemitente;
    }

    public String getNombreUsuarioDestinatario() {
        return nombreUsuarioDestinatario;
    }

    public void guardarDatos() {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/virtual_wallet", "root", "123456")) {
            String query = "INSERT INTO transacciones (cantidad, motivo, hora_envio, nombre_usuario_remitente, nombre_usuario_destinatario) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setDouble(1, cantidad);
            statement.setString(2, motivo);
            statement.setTimestamp(3, new java.sql.Timestamp(horaEnvio.getTime()));
            statement.setString(4, nombreUsuarioRemitente);
            statement.setString(5, nombreUsuarioDestinatario);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    

        


    public static List<Transaccion> cargarDatos(String nombreUsuario) {
        List<Transaccion> transacciones = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/virtual_wallet", "root", "123456")) {
            String query = "SELECT cantidad, motivo, hora_envio, nombre_usuario_remitente, nombre_usuario_destinatario FROM transacciones WHERE nombre_usuario_remitente = ? OR nombre_usuario_destinatario = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, nombreUsuario);
            statement.setString(2, nombreUsuario);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                double cantidad = resultSet.getDouble("cantidad");
                String motivo = resultSet.getString("motivo");
                Date horaEnvio = resultSet.getTimestamp("hora_envio");
                String nombreRemitente = resultSet.getString("nombre_usuario_remitente");
                String nombreDestinatario = resultSet.getString("nombre_usuario_destinatario");
                Transaccion transaccion = new Transaccion(cantidad, motivo, horaEnvio, nombreRemitente, nombreDestinatario);
                transacciones.add(transaccion);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        
        
        
        return transacciones;
    }}

