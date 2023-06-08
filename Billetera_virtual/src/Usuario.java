package index_billetera;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public class Usuario {

 //Bloque clase usuario

    
    private String nombreUsuario;
    private String contraseña;
    private String correo;
    private double saldo;

    public Usuario(String nombreUsuario, String contraseña, double saldo, String correo) {
        this.nombreUsuario = nombreUsuario;
        this.contraseña = contraseña;
        this.correo = correo;
        this.saldo = saldo;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public String getContraseña() {
        return contraseña;
    }

    public String getNombre() {
        return correo;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }
    
    public void guardarDatos() {// Leer y agregar datos introducidos por pantalla
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/virtual_wallet", "root", "123456")) {
            String query = "INSERT INTO usuarios (nombre_usuario, contrasenia, correo, saldo) VALUES (?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, nombreUsuario);
            statement.setString(2, contraseña);
            statement.setString(3, correo);
            statement.setDouble(4, saldo);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void actualizarSaldo() {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/virtual_wallet", "root", "123456")) {
            String query = "UPDATE usuarios SET saldo = ? WHERE nombre_usuario = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setDouble(1, saldo);
            statement.setString(2, nombreUsuario);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static void cargarDatos(Map<String, Usuario> usuarios) {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/virtual_wallet", "root", "123456")) {
            String query = "SELECT nombre_usuario, contrasenia, correo, saldo FROM usuarios";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String nombreUsuario = resultSet.getString("nombre_usuario");
                String contraseña = resultSet.getString("contrasenia");
                String correo = resultSet.getString("correo");
                double saldo = resultSet.getDouble("saldo");
                Usuario usuario = new Usuario(nombreUsuario, contraseña, saldo, correo);
                usuarios.put(nombreUsuario, usuario);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

