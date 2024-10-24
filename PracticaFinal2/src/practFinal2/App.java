package practFinal2;
import java.sql.*;

public class App {
    
    public static int i = 1;

    public static void main(String[] args) {
        Connection conexion  = null;
        Statement sentencia=null;
        ResultSet resultadoTodo=null;
        ResultSet resultadoPasajeros=null;
        ResultSet resultadoQueryCOD_VUELO=null;
        PreparedStatement queryCOD_VUELO=null;
        PreparedStatement insertVUELO =null;
        PreparedStatement cambiarFumadores = null;

        try {
           Class.forName("com.mysql.cj.jdbc.Driver");
                     
           // Conectamos con la base de datos
           conexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/vuelosypasajeros","root","Dam2425*");
            
           // Selecciono toda la información de la base de datos
           sentencia = conexion.createStatement(); 
           String sqlTodo = "SELECT * FROM vuelos, pasajeros";
           resultadoTodo = sentencia.executeQuery(sqlTodo);
           
           // Mostrar y pedir información de la base de datos en general.
           // Muestra las 3 primeras columnas de 2 filas de la tabla vuelos       
           System.out.println("Mostrar y pedir información de la base de datos en general.");

           while (resultadoTodo.next() && i < 3) {                
		        String COD_VUELO = resultadoTodo.getString(1);// 1
	            String HORA = resultadoTodo.getString(2); // 2
		        String DESTINO = resultadoTodo.getString(3); // 3
	            System.out.printf("COD_VUELO: %s, HORA: %s, DESTINO: %s .", COD_VUELO, HORA, DESTINO);
                System.out.println();
                i++;   
	        }
                            
           // Mostrar la información de la tabla pasajeros. 
           i = 1; // Reset i
           
           String sqlPasajeros = "SELECT * FROM pasajeros";
           resultadoPasajeros = sentencia.executeQuery(sqlPasajeros);
           
           System.out.println("Mostrar la información de la tabla pasajeros.");
           
           while (resultadoPasajeros.next()) {            
		        String NUM = resultadoPasajeros.getString(1);// 1
	            String COD_VUELO = resultadoPasajeros.getString(2); // 2
		        String TIPO_PLAZA = resultadoPasajeros.getString(3); // 3
                String FUMADOR = resultadoPasajeros.getString(4); // 3
                System.out.printf("NUM: %s, COD_VUELO: %s, TIPO_PLAZA: %s, FUMADOR: %s . ", NUM, COD_VUELO, TIPO_PLAZA, FUMADOR);
                System.out.println();

                i++;     
	        }
           
           // Ver la información de los pasajeros de un vuelo, pasando el código de vuelo como parámetro.
           queryCOD_VUELO = conexion.prepareStatement("SELECT * FROM pasajeros WHERE COD_VUELO = ?");
           queryCOD_VUELO.setString(1,"FR-DC-4667"); // Query FR-DC-4667
           resultadoQueryCOD_VUELO = queryCOD_VUELO.executeQuery();
           System.out.println("Ver la información de los pasajeros de un vuelo, pasando el código de vuelo como parámetro.");
           resultadoQueryCOD_VUELO.next();
           System.out.printf("NUM: %s, COD_VUELO: %s, TIPO_PLAZA: %s, FUMADOR: %s . ", resultadoQueryCOD_VUELO.getString(1), resultadoQueryCOD_VUELO.getString(2), resultadoQueryCOD_VUELO.getString(3), resultadoQueryCOD_VUELO.getString(4));
           
           // Insertar un vuelo cuyos valores se pasan como parámetros.
           System.out.println("Insertar un vuelo cuyos valores se pasan como parámetros.");
           insertVUELO = conexion.prepareStatement("INSERT INTO vuelos VALUES (?,?,?,?,?,?,?,?)");
           
           insertVUELO.setString(1,"FR-PR-5000"); // COD_VUELO
           insertVUELO.setString(2,"21/05/99 15:30"); // HORA
           insertVUELO.setString(3,"Madrid"); // DESTINO
           insertVUELO.setString(4,"Paris"); // PROCEDENCIA
           insertVUELO.setInt(5,30); // PLAZAS FUMADOR
           insertVUELO.setInt(6,120); // PLAZAS NO FUMADOR
           insertVUELO.setInt(7,90); // PLAZAS TURISTA
           insertVUELO.setInt(8,60); // PLAZAS PRIMERA
            
           insertVUELO.executeUpdate();
           
           // Borrar el vuelo que se metió anteriormente en el que se pasa por parámetro su número de vuelo.
           System.out.println("Borrar el vuelo que se metió anteriormente en el que se pasa por parámetro su número de vuelo.");
           PreparedStatement borrarVUELO = conexion.prepareStatement("DELETE FROM vuelos WHERE COD_VUELO = ?");
           borrarVUELO.setString(1,"FR-PR-5000"); // COD_VUELO
           borrarVUELO.executeUpdate();
           
           // Modificar los vuelos de fumadores a no fumadores.
           System.out.println("Modificar los vuelos de fumadores a no fumadores.");
           cambiarFumadores = conexion.prepareStatement("UPDATE vuelos SET vuelos.PLAZAS_FUMADOR = vuelos.PLAZAS_NO_FUMADOR");
           cambiarFumadores.executeUpdate();
               
        } catch (SQLException se) {
            System.out.println("SQL Excepcion " + se);
	    } catch (ClassNotFoundException exc) {
               System.out.println("No clase " + exc);  
        } finally {
            // Cerrar la conexión y los recursos
            try {
                if (resultadoQueryCOD_VUELO != null) resultadoQueryCOD_VUELO.close();
                if (resultadoPasajeros != null) resultadoPasajeros.close();
                if (resultadoTodo != null) resultadoTodo.close();
                if (cambiarFumadores != null) cambiarFumadores.close();
                if (insertVUELO != null) insertVUELO.close();
                if (queryCOD_VUELO != null) queryCOD_VUELO.close();
                if (sentencia != null) sentencia.close();
                if (conexion != null) conexion.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
             
    }
    
}
