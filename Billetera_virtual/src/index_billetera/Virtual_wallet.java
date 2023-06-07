package index_billetera;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;

//import index_billetera.Transaccion;

//import index_billetera.Usuario;

//import static index_billetera.Usuario.cargarDatos;
//import java.util.Date;


import java.util.*;
import java.util.Date;



public class Virtual_wallet {
    
    private static Map<String, Usuario> usuarios = new HashMap<>();
    private static Scanner scanner = new Scanner(System.in);
    
    public static void main(String[] args) {
    	try {
    	    Class.forName("com.mysql.cj.jdbc.Driver");
    	    Usuario.cargarDatos(usuarios);
    	} catch (ClassNotFoundException e) {
    	    System.out.println("Error al cargar los controladores JDBC: " + e.getMessage());
    	    return;
    	}

        System.out.println("¡Bienvenido/a a la aplicación de billetera virtual!");
        boolean salir = false;
        while (!salir) { //Bloque de eleccion de accion para ejecutar si desea alguna de estas acciones.
            System.out.println("\n¿Qué te gustaría hacer?");
            System.out.println("1. Iniciar sesión");
            System.out.println("2. Crear un nuevo usuario");
            System.out.println("3. Eliminar usuario");
            System.out.println("4. Salir");
            System.out.print("Selecciona tu eleccion: ");
            int opcion = scanner.nextInt();
            scanner.nextLine();//Separador

            switch (opcion) {
                case 1:
                    iniciarSesion();
                    break;
                case 2:
                    crearUsuario();
                    break;
                case 3:
                    eliminarUsuario();
                    break;
                case 4:
                    salir = true;
                    break;
                default:
                    System.out.println("Opción inválida. Por favor, intenta nuevamente.");
            }
        }
        System.out.println("Adios! gracias por utilizar la billetera virual");
    }
    
    
	private static void iniciarSesion() {
        System.out.print("Digita tu nombre de usuario: ");
        String nombreUsuario = scanner.nextLine();
        System.out.print("Digite su contraseña: ");
        String Contraseña = scanner.nextLine();

        //Usuario usuario = Usuario.cargarDatos(nombreUsuario);
        Usuario usuario = usuarios.get(nombreUsuario);
        
        if (usuario != null && usuario.getContraseña().equals(Contraseña)) {
            usuarios.put(nombreUsuario, usuario);
            System.out.println("-------¡Inicio de sesión exitoso! Bienvenido/a, " + usuario.getNombre() + ".-------");
            System.out.println("---------Tu saldo actual es: " + usuario.getSaldo() + "$.---------");
            
            
            boolean salir = false;
            
            
            while (!salir) {
                System.out.println("\n===================================");
                System.out.println("           MENÚ PRINCIPAL          ");
                System.out.println("===================================\n");
               
                System.out.println("1. Depositar dinero");           
                System.out.println("2. Mostrar cartera");
                System.out.println("3. Enviar dinero");
                System.out.println("4. Salir");
                System.out.println("Seleccione una opción:");
                int opcion = scanner.nextInt();
                scanner.nextLine();

				switch (opcion) {
                    
                    case 1:
                        depositarDinero(usuario);
                        break;
                    
                    case 2:
                    	
                        mostrarCartera(usuario);
                        break;
                    case 3:
                    	enviarDinero(usuario);
                    case 4:
                        salir = true;
                        break;
                    default:
                        System.out.println("Opción inválida. Por favor, seleccione nuevamente.");
                        break;
                }
            }

            System.out.println("¡Gracias por usar Virtual Wallet! ¡Hasta luego!");
        }
        else {
            System.out.println("Inicio de sesión fallido. Nombre de usuario o contraseña incorrectos.");
        }
    
            }
         

    private static void crearUsuario() {
    	
        System.out.println("Por favor, ingrese un nombre de usuario:");
        String nombreUsuario = scanner.nextLine();

        if (usuarios.containsKey(nombreUsuario)) {
            System.out.println("Error: El nombre de usuario ya está en uso.");
            return;
        }

        System.out.print("Digite una contraseña:");
        String contraseña = scanner.nextLine();
        System.out.print("Digite su correo electronico:");
        String correo = scanner.nextLine();
        System.out.print("Depositar ahora o mas tarde. \n1 --- ahora \n 2 --- despues");
        
        int eleccion =scanner.nextInt();
        while (eleccion != 1 && eleccion != 2) {
            System.out.print("Opción inválida. Por favor, seleccione 1 (ahora) o 2 (después): ");
            eleccion = scanner.nextInt();
        }

        
        
        //meterlo en un while por si la eleccion no es cualquiera de estos que muestre un ms por pantalla de volvee a intentar
        if(eleccion==1) {
        	System.out.print("Digite la cantidad a depositar");
        	double saldo = scanner.nextDouble();
            scanner.nextLine();

            Usuario nuevoUsuario = new Usuario(nombreUsuario, contraseña, saldo, correo);
            nuevoUsuario.guardarDatos();

            usuarios.put(nombreUsuario, nuevoUsuario);

            System.out.println("¡Usuario creado exitosamente! |||Ahora puedes iniciar sesión con tus credenciales.|||");
        } else if(eleccion==2) {
        	double saldo=0;
        	Usuario nuevoUsuario = new Usuario(nombreUsuario, contraseña, saldo, correo);
            nuevoUsuario.guardarDatos();

            usuarios.put(nombreUsuario, nuevoUsuario);

            System.out.println("¡Usuario creado exitosamente! |||Ahora puedes iniciar sesión con tus credenciales.|||\nrecuerda que puedes depositar en el apartado pago");
        }
        
        
    }

    private static void eliminarUsuario() {
        System.out.println("Ingrese el nombre de usuario del usuario que deseas eliminar:");
        String nombreUsuario = scanner.nextLine();

        if (usuarios.containsKey(nombreUsuario)) {
            usuarios.remove(nombreUsuario);
            System.out.println("Usuario eliminado exitosamente.");
        } else {
            System.out.println("El usuario especificado no existe. Verifica el nombre de usuario e intenta nuevamente.");
        }
    }

    private static void enviarDinero(Usuario remitente) {
        System.out.print("Digite el nombre de usuario de la billetera a la que le quiere mandar dinero: ");
        String nombreUsuario = scanner.nextLine();

        if (usuarios.containsKey(nombreUsuario) && !nombreUsuario.equals(remitente.getNombreUsuario())) {
            Usuario destinatario = usuarios.get(nombreUsuario);

            System.out.println("Por favor, ingresa la cantidad a enviar:");
            double cantidad = scanner.nextDouble();
            scanner.nextLine();

            if (remitente.getSaldo() >= cantidad) {
                remitente.setSaldo(remitente.getSaldo() - cantidad);
                destinatario.setSaldo(destinatario.getSaldo() + cantidad);

                Transaccion transaccion = new Transaccion(cantidad, "Pago", new Date(), remitente.getNombreUsuario(), nombreUsuario);
                transaccion.guardarDatos();

                // Actualizar los saldos en la base de datos
                remitente.actualizarSaldo();
                destinatario.actualizarSaldo();

                System.out.println("¡Transferencia exitosa!");
            } else {
                System.out.println("No tienes suficiente saldo para realizar la transferencia.");
            }
        } else {
            System.out.println("El usuario especificado no existe o no puedes enviar dinero a ti mismo. Verifica el nombre de usuario e intenta nuevamente.");
        }
    }



   

    private static void depositarDinero(Usuario usuario) {
        System.out.println("Ingrese la cantidad a depositar:");
        double cantidad = scanner.nextDouble();
        scanner.nextLine();

        if (cantidad > 0) {
            usuario.setSaldo(usuario.getSaldo() + cantidad);
            Transaccion transaccion = new Transaccion(cantidad, "Depósito", new Date(), usuario.getNombreUsuario(), usuario.getNombreUsuario());
            transaccion.guardarDatos();
            usuario.actualizarSaldo();
            System.out.println("¡Depósito exitoso!");
        } else {
            System.out.println("La cantidad debe ser mayor a cero.");
        }
    }

    private static void mostrarCartera(Usuario usuario) {
        System.out.println("===== Estado de Cuenta =====");
        System.out.println("Usuario: " + usuario.getNombreUsuario());
        System.out.println("Saldo actual: " + usuario.getSaldo());
        System.out.println();
        
        List<Transaccion> transacciones = Transaccion.cargarDatos(usuario.getNombreUsuario());
        
        if (transacciones.isEmpty()) {
            System.out.println("No se encontraron transacciones.");
            return;
        }
        
        System.out.println("===== Movimientos Recientes =====");
        
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        
        for (Transaccion transaccion : transacciones) {
            String tipoTransaccion;
            String nombreOtroUsuario;
            
            if (transaccion.getNombreUsuarioDestinatario().equals(usuario.getNombreUsuario())) {
                tipoTransaccion = "Recibido";
                nombreOtroUsuario = transaccion.getNombreUsuarioRemitente();
            } else {
                tipoTransaccion = "Enviado";
                nombreOtroUsuario = transaccion.getNombreUsuarioDestinatario();
            }
            
            String fechaTransaccion = dateFormat.format(transaccion.getHoraEnvio());
            
            System.out.println("Fecha: " + fechaTransaccion);
            System.out.println("Tipo: " + tipoTransaccion);
            System.out.println("Otro Usuario: " + nombreOtroUsuario);
            System.out.println("Cantidad: " + transaccion.getCantidad());
            System.out.println("Motivo: " + transaccion.getMotivo());
            System.out.println("----------------------------------");
        }
    }


}
