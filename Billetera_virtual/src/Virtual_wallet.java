package index_billetera;


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
    	try { // conexion de la base de datos mediante JDBC 
    	    Class.forName("com.mysql.cj.jdbc.Driver");
    	    Usuario.cargarDatos(usuarios);
    	} catch (ClassNotFoundException e) {
    	    System.out.println("Error al cargar los controladores JDBC: " + e.getMessage());
    	    return;
    	}

        System.out.println("¡Bienvenido/a a la aplicación de billetera virtual!");
        boolean salir = false;
        Scanner scanner = new Scanner(System.in);

        do { // menu principal para despegar las fuciones basicas de usuarios.
            System.out.println("\n¿Qué te gustaría hacer?");
            System.out.println("1. Iniciar sesión");
            System.out.println("2. Crear un nuevo usuario");
            System.out.println("3. Eliminar usuario");
            System.out.println("4. Salir");
            System.out.print("Selecciona tu elección: ");

            // Validar si se ingresó un número válido
            int opcion;
            try {
                opcion = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Error: Ingresa un número válido.");
                continue; // Volver al inicio del bucle si no se ingresó un número válido
            }

            switch (opcion) {
                case 1:
                   try {
                	   iniciarSesion();
                   }
                   finally{
                	   System.out.println("\n ------ Digite un nombre de usuario y una contraseña correcta ------- \n");
                   }
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
           
        } while (!salir);

        System.out.println("¡Adiós! Gracias por utilizar la billetera virtual.");
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
            System.out.println("\n-------¡Inicio de sesión exitoso! Bienvenido/a, " + usuario.getNombre() + ".-------");
            System.out.println("---------Tu saldo actual es: " + usuario.getSaldo() + "$.---------");
            
            
            
            boolean salir = false;
            Scanner scanner = new Scanner(System.in);

            do {
                System.out.println("\n===================================");
                System.out.println("           MENÚ PRINCIPAL          ");
                System.out.println("===================================\n");

                System.out.println("1. Depositar dinero");
                System.out.println("2. Mostrar cartera");
                System.out.println("3. Enviar dinero");
                System.out.println("4. Salir");
                System.out.print("Seleccione una opción: ");

                // Validar si se ingresó un número válido
                int opcion;
                try {
                    opcion = Integer.parseInt(scanner.nextLine());
                } catch (NumberFormatException e) {
                    System.out.println("Error: Ingresa un número válido.");
                    continue; // Volver al inicio del bucle si no se ingresó un número válido
                }

                switch (opcion) {
                    case 1:
                        depositarDinero(usuario);
                        break;

                    case 2:
                        mostrarCartera(usuario);
                        break;

                    case 3:
                        enviarDinero(usuario);
                        break;

                    case 4:
                        salir = true;
                        break;

                    default:
                        System.out.println("Opción inválida. Por favor, seleccione nuevamente.");
                        break;
                }
                
                
            } while (!salir);
            
            System.out.println("¡Gracias por usar Virtual Wallet! ¡Hasta luego!");}
        	
        	
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
        System.out.print("\nDepositar ahora o mas tarde. \n1 --- ahora\n2 --- despues\n");
        
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

            Usuario nuevoUsuario = new Usuario(nombreUsuario, contraseña, saldo, correo);//Instancia nueva para crear un nuevo usuario
            nuevoUsuario.guardarDatos();//guardar los datos en la base de datos 
            usuarios.put(nombreUsuario, nuevoUsuario);

            System.out.println("¡Usuario creado exitosamente! |||Ahora puedes iniciar sesión con tus credenciales.|||");
            // 
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
