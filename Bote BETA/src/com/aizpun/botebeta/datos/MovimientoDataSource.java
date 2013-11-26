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

import com.aizpun.botebeta.negocio.Movimiento;

/**
 * Origen de datos de la tabla Movimiento
 * 
 * @author Jorge
 * 
 */
public class MovimientoDataSource {

	private final String TAG = "MovimientoDataSource";

	// Database fields
	private SQLiteDatabase database;
	private BDBotes_SQLiteHelper dbHelper;
	private String[] allColumns = { BDBotes_SQLiteHelper.COLUMN_ID_MOVIMIENTO,
			BDBotes_SQLiteHelper.COLUMN_ID_BOTE_MOVIMIENTO,
			BDBotes_SQLiteHelper.COLUMN_ID_PERSONA_MOVIMIENTO,
			BDBotes_SQLiteHelper.COLUMN_ICONO_MOVIMIENTO,
			BDBotes_SQLiteHelper.COLUMN_DESCRIPCION_MOVIMIENTO,
			BDBotes_SQLiteHelper.COLUMN_TIPO_MOVIMIENTO,
			BDBotes_SQLiteHelper.COLUMN_IMPORTE_MOVIMIENTO,
			BDBotes_SQLiteHelper.COLUMN_FECHA_CREACION_MOVIMIENTO };

	public MovimientoDataSource(Context context) {
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
	 * Elimina la tabla Movimient de la base de datos
	 * 
	 * @return Si ha sido eliminada
	 */
	public Boolean deleteTablaMovimiento() {
		try {
			database.execSQL("DROP TABLE IF EXISTS "
					+ BDBotes_SQLiteHelper.TABLE_MOVIMIENTO);
			return true;
		} catch (Exception ex) {
			Log.e(TAG, "Ha ocurrido un error al eliminar la tabla '"
					+ BDBotes_SQLiteHelper.TABLE_MOVIMIENTO + "'", ex);
		}
		return false;
	}

	/**
	 * Inserta un movimiento en la base de datos
	 * 
	 * @param movimiento
	 *            Movimiento
	 * @return Si ha sido insertado
	 */
	public Boolean createMovimiento(Movimiento movimiento) {
		try {
			ContentValues values = new ContentValues();
			values.put(BDBotes_SQLiteHelper.COLUMN_ID_BOTE_MOVIMIENTO,
					movimiento.getBoteID());
			values.put(BDBotes_SQLiteHelper.COLUMN_ID_PERSONA_MOVIMIENTO,
					movimiento.getPersonaID());
			values.put(BDBotes_SQLiteHelper.COLUMN_ICONO_MOVIMIENTO,
					movimiento.getIcono());
			values.put(BDBotes_SQLiteHelper.COLUMN_DESCRIPCION_MOVIMIENTO,
					movimiento.getDescripcion());
			values.put(BDBotes_SQLiteHelper.COLUMN_TIPO_MOVIMIENTO,
					movimiento.getTipo());
			values.put(BDBotes_SQLiteHelper.COLUMN_IMPORTE_MOVIMIENTO,
					movimiento.getImporte());
			values.put(BDBotes_SQLiteHelper.COLUMN_FECHA_CREACION_MOVIMIENTO,
					movimiento.getFechaCreacion().getTime());
			long insertId = database.insert(
					BDBotes_SQLiteHelper.TABLE_MOVIMIENTO, null, values);
			movimiento.setId(insertId);
			return true;
		} catch (Exception ex) {
			Log.e(TAG, "Ha ocurrido un error al crear un movimiento", ex);
		}
		return false;
	}

	/**
	 * Actualiza un movimiento en la base de datos
	 * 
	 * @param movimiento
	 *            Movimiento
	 * @return Si ha sido actualizado
	 */
	public Boolean updateMovimiento(Movimiento movimiento) {
		try {
			String strFilter = "_id=" + movimiento.getId();
			ContentValues values = new ContentValues();
			values.put(BDBotes_SQLiteHelper.COLUMN_ID_BOTE_MOVIMIENTO,
					movimiento.getBoteID());
			values.put(BDBotes_SQLiteHelper.COLUMN_ID_PERSONA_MOVIMIENTO,
					movimiento.getPersonaID());
			values.put(BDBotes_SQLiteHelper.COLUMN_ICONO_MOVIMIENTO,
					movimiento.getIcono());
			values.put(BDBotes_SQLiteHelper.COLUMN_DESCRIPCION_MOVIMIENTO,
					movimiento.getDescripcion());
			values.put(BDBotes_SQLiteHelper.COLUMN_TIPO_MOVIMIENTO,
					movimiento.getTipo());
			values.put(BDBotes_SQLiteHelper.COLUMN_IMPORTE_MOVIMIENTO,
					movimiento.getImporte());
			values.put(BDBotes_SQLiteHelper.COLUMN_FECHA_CREACION_MOVIMIENTO,
					movimiento.getFechaCreacion().getTime());
			database.update(BDBotes_SQLiteHelper.TABLE_MOVIMIENTO, values,
					strFilter, null);
			return true;
		} catch (Exception ex) {
			Log.e(TAG, "Ha ocurrido un error al actualizar un movimiento", ex);
		}
		return false;
	}

	/**
	 * Elimina un movimiento de la base de datos
	 * 
	 * @param movimiento
	 *            Movimiento
	 * @return Si ha sido eliminado
	 */
	public Boolean deleteMovimiento(Movimiento movimiento) {
		try {
			long id = movimiento.getId();
			// System.out.println("Movimiento eliminado con el id: " + id);
			database.delete(BDBotes_SQLiteHelper.TABLE_MOVIMIENTO,
					BDBotes_SQLiteHelper.COLUMN_ID_MOVIMIENTO + " = " + id,
					null);
			return true;
		} catch (Exception ex) {
			Log.e(TAG, "Ha ocurrido un error al eliminar un movimiento", ex);
		}
		return false;
	}

	/**
	 * Lista todos los movimientos
	 * 
	 * @return Movimientos
	 */
	public List<Movimiento> getAllMovimientos() {
		List<Movimiento> movimientos = new ArrayList<Movimiento>();

		Cursor cursor = database.query(BDBotes_SQLiteHelper.TABLE_MOVIMIENTO,
				allColumns, null, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Movimiento movimiento = cursorToMovimiento(cursor);
			movimientos.add(movimiento);
			cursor.moveToNext();
		}
		// Nos aseguramos de cerrar el cursor
		cursor.close();
		return movimientos;
	}

	/**
	 * Lista todos los movimientos de un bote
	 * 
	 * @param idBote
	 *            ID del bote
	 * @return Movimientos
	 */
	public List<Movimiento> getAllMovimientosBote(long idBote) {
		List<Movimiento> movimientos = new ArrayList<Movimiento>();

		String where = BDBotes_SQLiteHelper.COLUMN_ID_BOTE_MOVIMIENTO + "="
				+ String.valueOf(idBote);

		Cursor cursor = database.query(BDBotes_SQLiteHelper.TABLE_MOVIMIENTO,
				allColumns, where, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Movimiento movimiento = cursorToMovimiento(cursor);
			movimientos.add(movimiento);
			cursor.moveToNext();
		}
		// Nos aseguramos de cerrar el cursor
		cursor.close();
		return movimientos;
	}

	/**
	 * Convierte un cursor en un movimiento
	 * 
	 * @param cursor
	 *            Cursor
	 * @return Movimiento
	 */
	private Movimiento cursorToMovimiento(Cursor cursor) {
		Movimiento movimiento = new Movimiento();
		movimiento.setId(cursor.getLong(0));
		movimiento.setBoteID(cursor.getLong(1));
		movimiento.setPersonaID(cursor.getLong(2));
		movimiento.setIcono(cursor.getInt(3));
		movimiento.setDescripcion(cursor.getString(4));
		movimiento.setTipo(cursor.getString(5));
		movimiento.setImporte(cursor.getDouble(6));
		movimiento.setFechaCreacion(new Date(cursor.getLong(7)));
		return movimiento;
	}
}
