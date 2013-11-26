package com.aizpun.botebeta.datos;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.aizpun.botebeta.negocio.Bote;

/**
 * Origen de datos de la tabla Bote
 * 
 * @author Jorge
 * 
 */
public class BoteDataSource {

	private final String TAG = "BoteDataSource";

	// Database fields
	private SQLiteDatabase database;
	private BDBotes_SQLiteHelper dbHelper;

	// private String[] allColumns = { BDBotes_SQLiteHelper.COLUMN_ID_BOTE,
	// BDBotes_SQLiteHelper.COLUMN_ICONO_BOTE,
	// BDBotes_SQLiteHelper.COLUMN_NOMBRE_BOTE,
	// BDBotes_SQLiteHelper.COLUMN_TOTAL_BOTE,
	// BDBotes_SQLiteHelper.COLUMN_TOPE_BOTE,
	// BDBotes_SQLiteHelper.COLUMN_IMPORTE_TOPE_BOTE,
	// BDBotes_SQLiteHelper.COLUMN_TIPO_BOTE,
	// BDBotes_SQLiteHelper.COLUMN_ADMINISTRADOR_BOTE,
	// BDBotes_SQLiteHelper.COLUMN_FECHA_CREACION_BOTE,
	// BDBotes_SQLiteHelper.COLUMN_FECHA_LIMITE_BOTE };

	public BoteDataSource(Context context) {
		dbHelper = new BDBotes_SQLiteHelper(context);
	}

	/**
	 * Abre la conexión con la base de datos
	 * 
	 * @throws SQLException
	 */
	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	/**
	 * Cierra la conexión con la base de datos
	 */
	public void close() {
		dbHelper.close();
	}

	/**
	 * Elimina la tabla Bote de la base de datos
	 * 
	 * @return Si ha sido eliminada
	 */
	public Boolean deleteTablaBote() {
		try {
			database.execSQL("DROP TABLE IF EXISTS "
					+ BDBotes_SQLiteHelper.TABLE_BOTE);
			return true;
		} catch (Exception ex) {
			Log.e(TAG, "Ha ocurrido un error al eliminar la tabla '"
					+ BDBotes_SQLiteHelper.TABLE_BOTE + "'", ex);
		}
		return false;
	}

	/**
	 * Inserta un bote en la base de datos
	 * 
	 * @param bote
	 *            Bote
	 * @return Si ha sido insertado
	 */
	public Boolean createBote(Bote bote) {
		try {
			ContentValues values = new ContentValues();
			values.put(BDBotes_SQLiteHelper.COLUMN_ICONO_BOTE, bote.getIcono());
			values.put(BDBotes_SQLiteHelper.COLUMN_NOMBRE_BOTE,
					bote.getNombre());
			values.put(BDBotes_SQLiteHelper.COLUMN_TOTAL_BOTE, bote.getTotal());
			values.put(BDBotes_SQLiteHelper.COLUMN_TOPE_BOTE, bote.getTope());
			values.put(BDBotes_SQLiteHelper.COLUMN_IMPORTE_TOPE_BOTE,
					bote.getImporteTope());
			values.put(BDBotes_SQLiteHelper.COLUMN_TIPO_BOTE, bote.getTipo());
			values.put(BDBotes_SQLiteHelper.COLUMN_ADMINISTRADOR_BOTE,
					bote.getAdministrador());
			values.put(BDBotes_SQLiteHelper.COLUMN_FECHA_CREACION_BOTE, bote
					.getFechaCreacion().getTime());
			if (bote.getFechaLimite() != null)
				values.put(BDBotes_SQLiteHelper.COLUMN_FECHA_LIMITE_BOTE, bote
						.getFechaLimite().getTime());
			else
				values.putNull(BDBotes_SQLiteHelper.COLUMN_FECHA_LIMITE_BOTE);
			long insertId = database.insert(BDBotes_SQLiteHelper.TABLE_BOTE,
					null, values);
			bote.setId(insertId);
			return true;
		} catch (Exception ex) {
			Log.e(TAG, "Ha ocurrido un error al crear un bote", ex);
		}
		return false;
	}

	/**
	 * Actualiza un bote en la base de datos
	 * 
	 * @param bote
	 *            Bote
	 * @return Si ha sido actualizado
	 */
	public Boolean updateBote(Bote bote) {
		try {
			String strFilter = "_id=" + bote.getId();
			ContentValues values = new ContentValues();
			values.put(BDBotes_SQLiteHelper.COLUMN_ICONO_BOTE, bote.getIcono());
			values.put(BDBotes_SQLiteHelper.COLUMN_NOMBRE_BOTE,
					bote.getNombre());
			values.put(BDBotes_SQLiteHelper.COLUMN_TOTAL_BOTE, bote.getTotal());
			values.put(BDBotes_SQLiteHelper.COLUMN_TOPE_BOTE, bote.getTope());
			values.put(BDBotes_SQLiteHelper.COLUMN_IMPORTE_TOPE_BOTE,
					bote.getImporteTope());
			values.put(BDBotes_SQLiteHelper.COLUMN_TIPO_BOTE, bote.getTipo());
			values.put(BDBotes_SQLiteHelper.COLUMN_ADMINISTRADOR_BOTE,
					bote.getAdministrador());
			values.put(BDBotes_SQLiteHelper.COLUMN_FECHA_CREACION_BOTE, bote
					.getFechaCreacion().getTime());
			if (bote.getFechaLimite() != null)
				values.put(BDBotes_SQLiteHelper.COLUMN_FECHA_LIMITE_BOTE, bote
						.getFechaLimite().getTime());
			else
				values.putNull(BDBotes_SQLiteHelper.COLUMN_FECHA_LIMITE_BOTE);
			database.update(BDBotes_SQLiteHelper.TABLE_BOTE, values, strFilter,
					null);
			return true;
		} catch (Exception ex) {
			Log.e(TAG, "Ha ocurrido un error al actualizar un bote", ex);
		}
		return false;
	}

	/**
	 * Elimina un bote de la base de datos
	 * 
	 * @param bote
	 *            Bote
	 * @return Si ha sido eliminado
	 */
	public Boolean deleteBote(Bote bote) {
		try {
			long id = bote.getId();
			// Elimina todos los movimientos del bote
			database.delete(
					BDBotes_SQLiteHelper.TABLE_MOVIMIENTO,
					BDBotes_SQLiteHelper.COLUMN_ID_BOTE_MOVIMIENTO + " = " + id,
					null);
			// Elimina todas las personas del bote
			database.delete(BDBotes_SQLiteHelper.TABLE_PERSONA,
					BDBotes_SQLiteHelper.COLUMN_ID_BOTE_PERSONA + " = " + id,
					null);
			// Elimina el bote
			database.delete(BDBotes_SQLiteHelper.TABLE_BOTE,
					BDBotes_SQLiteHelper.COLUMN_ID_BOTE + " = " + id, null);
			return true;
		} catch (Exception ex) {
			Log.e(TAG, "Ha ocurrido un error al eliminar un bote", ex);
		}
		return false;
	}

	/**
	 * Lista todos los botes
	 * 
	 * @return Botes
	 */
	public List<Bote> getAllBotes() {
		List<Bote> botes = new ArrayList<Bote>();

		// String select = "SELECT " + "B." +
		// BDBotes_SQLiteHelper.COLUMN_ID_BOTE
		// + ", " + "B." + BDBotes_SQLiteHelper.COLUMN_ICONO_BOTE + ", "
		// + "B." + BDBotes_SQLiteHelper.COLUMN_NOMBRE_BOTE + ", "
		// + "SUM(M." + BDBotes_SQLiteHelper.COLUMN_IMPORTE_MOVIMIENTO
		// + ") AS " + BDBotes_SQLiteHelper.COLUMN_TOTAL_BOTE + ", "
		// + "B." + BDBotes_SQLiteHelper.COLUMN_TOPE_BOTE + ", " + "B."
		// + BDBotes_SQLiteHelper.COLUMN_IMPORTE_TOPE_BOTE + ", " + "B."
		// + BDBotes_SQLiteHelper.COLUMN_TIPO_BOTE + ", " + "B."
		// + BDBotes_SQLiteHelper.COLUMN_ADMINISTRADOR_BOTE + ", " + "B."
		// + BDBotes_SQLiteHelper.COLUMN_FECHA_CREACION_BOTE + ", " + "B."
		// + BDBotes_SQLiteHelper.COLUMN_FECHA_LIMITE_BOTE + " FROM "
		// + BDBotes_SQLiteHelper.TABLE_BOTE + " B LEFT JOIN "
		// + BDBotes_SQLiteHelper.TABLE_MOVIMIENTO + " M ON B."
		// + BDBotes_SQLiteHelper.COLUMN_ID_BOTE + " = M."
		// + BDBotes_SQLiteHelper.COLUMN_ID_BOTE_MOVIMIENTO + " GROUP BY "
		// + "B." + BDBotes_SQLiteHelper.COLUMN_ID_BOTE + ", " + "B."
		// + BDBotes_SQLiteHelper.COLUMN_ICONO_BOTE + ", " + "B."
		// + BDBotes_SQLiteHelper.COLUMN_NOMBRE_BOTE + ", " + "B."
		// + BDBotes_SQLiteHelper.COLUMN_TOPE_BOTE + ", " + "B."
		// + BDBotes_SQLiteHelper.COLUMN_IMPORTE_TOPE_BOTE + ", " + "B."
		// + BDBotes_SQLiteHelper.COLUMN_TIPO_BOTE + ", " + "B."
		// + BDBotes_SQLiteHelper.COLUMN_ADMINISTRADOR_BOTE + ", " + "B."
		// + BDBotes_SQLiteHelper.COLUMN_FECHA_CREACION_BOTE + ", " + "B."
		// + BDBotes_SQLiteHelper.COLUMN_FECHA_LIMITE_BOTE
		// + " ORDER BY B."
		// + BDBotes_SQLiteHelper.COLUMN_FECHA_CREACION_BOTE + " ASC";

		String select = "SELECT B._id, B.Icono, B.Nombre, coalesce(M_Total.Total, 0) AS Total, B.Tope, B.ImporteTope, B.Tipo, B.Administrador, B.FechaCreacion, B.FechaLimite, coalesce(M_Ing_Total.Total, 0) AS IngresadoTotal, coalesce(M_Dev.Total, 0) AS Devoluciones, (coalesce(M_Ing_Total.Total, 0) + coalesce(M_Dev.Total, 0)) AS IngresadoActual, coalesce(M_Ret.Total, 0) AS Gastado, (coalesce(M_Ing_Total.Total, 0) + coalesce(M_Dev.Total, 0) + coalesce(M_Ret.Total, 0)) AS Disponible "
				+ "FROM Bote B "
				+ "LEFT JOIN (SELECT Bote_id, SUM(Importe) AS Total "
				+ "           FROM Movimiento "
				+ "           GROUP BY Bote_id) M_Total ON B._id = M_Total.Bote_id "
				+ "LEFT JOIN (SELECT Bote_id, SUM(Importe) AS Total "
				+ "           FROM Movimiento "
				+ "           WHERE Tipo = 'Ingresar' "
				+ "           GROUP BY Bote_id) M_Ing_Total ON B._id = M_Ing_Total.Bote_id "
				+ "LEFT JOIN (SELECT Bote_id, SUM(Importe) AS Total "
				+ "           FROM Movimiento "
				+ "           WHERE Tipo = 'Devolver' "
				+ "           GROUP BY Bote_id) M_Dev ON B._id = M_Dev.Bote_id "
				+ "LEFT JOIN (SELECT Bote_id, SUM(Importe) AS Total "
				+ "           FROM Movimiento "
				+ "           WHERE Tipo = 'Retirar' "
				+ "           GROUP BY Bote_id) M_Ret ON B._id = M_Ret.Bote_id ";
		
		Cursor cursor = database.rawQuery(select, null);

		// Cursor cursor = database.query(BDBotes_SQLiteHelper.TABLE_BOTE,
		// allColumns, null, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Bote bote = cursorToBote(cursor);
			botes.add(bote);
			cursor.moveToNext();
		}
		// Nos aseguramos de cerrar el cursor
		cursor.close();
		return botes;
	}

	/**
	 * Lista un bote
	 * 
	 * @return Bote
	 */
	public Bote getBote(long id) {
		Bote bote = new Bote();

		// String select = "SELECT " + "B." +
		// BDBotes_SQLiteHelper.COLUMN_ID_BOTE
		// + ", " + "B." + BDBotes_SQLiteHelper.COLUMN_ICONO_BOTE + ", "
		// + "B." + BDBotes_SQLiteHelper.COLUMN_NOMBRE_BOTE + ", "
		// + "SUM(M." + BDBotes_SQLiteHelper.COLUMN_IMPORTE_MOVIMIENTO
		// + ") AS " + BDBotes_SQLiteHelper.COLUMN_TOTAL_BOTE + ", "
		// + "B." + BDBotes_SQLiteHelper.COLUMN_TOPE_BOTE + ", " + "B."
		// + BDBotes_SQLiteHelper.COLUMN_IMPORTE_TOPE_BOTE + ", " + "B."
		// + BDBotes_SQLiteHelper.COLUMN_TIPO_BOTE + ", " + "B."
		// + BDBotes_SQLiteHelper.COLUMN_ADMINISTRADOR_BOTE + ", " + "B."
		// + BDBotes_SQLiteHelper.COLUMN_FECHA_CREACION_BOTE + ", " + "B."
		// + BDBotes_SQLiteHelper.COLUMN_FECHA_LIMITE_BOTE + " FROM "
		// + BDBotes_SQLiteHelper.TABLE_BOTE + " B LEFT JOIN "
		// + BDBotes_SQLiteHelper.TABLE_MOVIMIENTO + " M ON B."
		// + BDBotes_SQLiteHelper.COLUMN_ID_BOTE + " = M."
		// + BDBotes_SQLiteHelper.COLUMN_ID_BOTE_MOVIMIENTO + " "
		// + " WHERE B." + BDBotes_SQLiteHelper.COLUMN_ID_BOTE + " = "
		// + id + " GROUP BY " + "B."
		// + BDBotes_SQLiteHelper.COLUMN_ID_BOTE + ", " + "B."
		// + BDBotes_SQLiteHelper.COLUMN_ICONO_BOTE + ", " + "B."
		// + BDBotes_SQLiteHelper.COLUMN_NOMBRE_BOTE + ", " + "B."
		// + BDBotes_SQLiteHelper.COLUMN_TOPE_BOTE + ", " + "B."
		// + BDBotes_SQLiteHelper.COLUMN_IMPORTE_TOPE_BOTE + ", " + "B."
		// + BDBotes_SQLiteHelper.COLUMN_TIPO_BOTE + ", " + "B."
		// + BDBotes_SQLiteHelper.COLUMN_ADMINISTRADOR_BOTE + ", " + "B."
		// + BDBotes_SQLiteHelper.COLUMN_FECHA_CREACION_BOTE + ", " + "B."
		// + BDBotes_SQLiteHelper.COLUMN_FECHA_LIMITE_BOTE;

		String select = "SELECT B._id, B.Icono, B.Nombre, coalesce(M_Total.Total, 0) AS Total, B.Tope, B.ImporteTope, B.Tipo, B.Administrador, B.FechaCreacion, B.FechaLimite, coalesce(M_Ing_Total.Total, 0) AS IngresadoTotal, coalesce(M_Dev.Total, 0) AS Devoluciones, (coalesce(M_Ing_Total.Total, 0) + coalesce(M_Dev.Total, 0)) AS IngresadoActual, coalesce(M_Ret.Total, 0) AS Gastado, (coalesce(M_Ing_Total.Total, 0) + coalesce(M_Dev.Total, 0) + coalesce(M_Ret.Total, 0)) AS Disponible "
				+ "FROM Bote B "
				+ "LEFT JOIN (SELECT Bote_id, SUM(Importe) AS Total "
				+ "           FROM Movimiento "
				+ "           GROUP BY Bote_id) M_Total ON B._id = M_Total.Bote_id "
				+ "LEFT JOIN (SELECT Bote_id, SUM(Importe) AS Total "
				+ "           FROM Movimiento "
				+ "           WHERE Tipo = 'Ingresar' "
				+ "           GROUP BY Bote_id) M_Ing_Total ON B._id = M_Ing_Total.Bote_id "
				+ "LEFT JOIN (SELECT Bote_id, SUM(Importe) AS Total "
				+ "           FROM Movimiento "
				+ "           WHERE Tipo = 'Devolver' "
				+ "           GROUP BY Bote_id) M_Dev ON B._id = M_Dev.Bote_id "
				+ "LEFT JOIN (SELECT Bote_id, SUM(Importe) AS Total "
				+ "           FROM Movimiento "
				+ "           WHERE Tipo = 'Retirar' "
				+ "           GROUP BY Bote_id) M_Ret ON B._id = M_Ret.Bote_id "
				+ "WHERE B._id = " + id;
		Cursor cursor = database.rawQuery(select, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			bote = cursorToBote(cursor);
			cursor.moveToNext();
		}
		// Nos aseguramos de cerrar el cursor
		cursor.close();
		return bote;
	}

	/**
	 * Convierte un cursor en un bote
	 * 
	 * @param cursor
	 *            Cursor
	 * @return Bote
	 */
	private Bote cursorToBote(Cursor cursor) {
		Bote bote = new Bote();
		bote.setId(cursor.getLong(0));
		bote.setIcono(cursor.getInt(1));
		bote.setNombre(cursor.getString(2));
		bote.setTotal(cursor.getDouble(3));
		bote.setTope(cursor.getInt(4) == 1);
		bote.setImporteTope(cursor.getDouble(5));
		bote.setTipo(cursor.getString(6));
		bote.setAdministrador(cursor.getInt(7));
		bote.setFechaCreacion(new Date(cursor.getLong(8)));
		bote.setFechaLimite(new Date(cursor.getLong(9)));
		bote.setIngresadoTotal(cursor.getDouble(10));
		bote.setDevoluciones(cursor.getDouble(11));
		bote.setIngresadoActual(cursor.getDouble(12));
		bote.setGastado(cursor.getDouble(13));
		bote.setDisponible(cursor.getDouble(14));
		return bote;
	}
}
