package es.teis.test.repasoud2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

public class Main {

	public static void main(String[] args) {
		DataSource ds =  DBCPDataSourceFactory.getDataSource();
		
		  try (
	                 Connection conexion = ds.getConnection();  
	        		Statement sentencia = conexion.createStatement();  
	        		ResultSet result = sentencia.executeQuery("SELECT * FROM alumnos");) {

	            int columnas = result.getMetaData().getColumnCount();
	            for (int i = 1; i <= columnas; i++) {
	                System.out.print(result.getMetaData()
	                		.getColumnName(i) + "   ");
	            }

	            System.out.println("");
	            System.out.println("--------------------------------------------------------------------------------------------------------------------");
	            while (result.next()) {
	                System.out.println(result.getString(1) + "   " + result.getString(2) + " " + result.getString(3) + "  " +  result.getString(4)
	                + result.getDate(5));
	            }

	        } catch (SQLException ex) {
	            ex.printStackTrace();
	            System.err.println("Ha ocurrido una excepción: " + ex.getMessage());
	          
	        }
	}
}
