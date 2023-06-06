package es.teis.test.repasoud2;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

public class Main {

	public static void main(String[] args) {
		matricularAlumno("12345678A");
	}

	private static void matricularAlumno(String dni) {
		String nombre = "", apellidos = "", direccion = "";
		Date fechaNac = null;
		Alumno alumno = null;

		Connection conexion = null;
		DataSource ds = DBCPDataSourceFactory.getDataSource();

		try {

			conexion = ds.getConnection();

			PreparedStatement sentenciaSelect = conexion.prepareStatement(" SELECT * FROM alumnos where dni = ?");

			PreparedStatement sentenciaInsert = conexion.prepareStatement(
					"INSERT INTO alumnosvigo(dni, nombre, apellidos, direccion, fechaNac) values (?, ?, ?, ?, ?)");
			PreparedStatement sentenciaDelete = conexion.prepareStatement("delete from alumnos where dni =?");

			conexion.setAutoCommit(false);

			sentenciaSelect.setString(1, dni);

			ResultSet result = sentenciaSelect.executeQuery();

			while (result.next()) {
				nombre = result.getString(2);
				apellidos = result.getString(3);
				direccion = result.getString(4);
				fechaNac = result.getDate(5);

				alumno = new Alumno(dni, nombre, apellidos, direccion, fechaNac);

				System.out.println(result.getString(1) + "   " + result.getString(2) + " " + result.getString(3) + "  "
						+ result.getString(4) + result.getDate(5));
			}

			sentenciaInsert.setString(1, dni);
			sentenciaInsert.setString(2, nombre);
			sentenciaInsert.setString(3, apellidos);
			sentenciaInsert.setString(4, direccion);
			sentenciaInsert.setDate(5, fechaNac);

			sentenciaInsert.executeUpdate();

			sentenciaDelete.setString(1, dni);
			sentenciaDelete.executeUpdate();

			conexion.commit();

		} catch (SQLException ex) {
			ex.printStackTrace();
			try {
				System.out.println("Transaction failed.");
				conexion.rollback();
			} catch (SQLException se) {
				se.printStackTrace();
			}
		} finally {
			if (conexion != null) {
				try {
					conexion.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}

	private static void testConnection() {
		DataSource ds = DBCPDataSourceFactory.getDataSource();

		try (Connection conexion = ds.getConnection();
				Statement sentencia = conexion.createStatement();
				ResultSet result = sentencia.executeQuery("SELECT * FROM alumnos");) {

			int columnas = result.getMetaData().getColumnCount();
			for (int i = 1; i <= columnas; i++) {
				System.out.print(result.getMetaData().getColumnName(i) + "   ");
			}

			System.out.println("");
			System.out.println(
					"--------------------------------------------------------------------------------------------------------------------");
			while (result.next()) {
				System.out.println(result.getString(1) + "   " + result.getString(2) + " " + result.getString(3) + "  "
						+ result.getString(4) + result.getDate(5));
			}

		} catch (SQLException ex) {
			ex.printStackTrace();
			System.err.println("Ha ocurrido una excepción: " + ex.getMessage());

		}
	}
}
