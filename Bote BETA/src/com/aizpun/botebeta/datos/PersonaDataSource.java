package com.aizpun.botebeta.datos;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.aizpun.botebeta.negocio.Persona;

/**
 * Origen de datos de la tabla Persona
 * 
 * @author Jorge
 * 
 */
public class PersonaDataSource {

	private final String TAG = "PersonaDataSource";

	// Database fields
	private SQLiteDatabase database;
	private BDBotes_SQLiteHelper dbHelper;

	// private String[] allColumns = { BDBotes_SQLiteHelper.COLUMN_ID_PERSONA,
	// BDBotes_SQLiteHelper.COLUMN_ID_CONTACTO_PERSONA,
	// BDBotes_SQLiteHelper.COLUMN_ID_BOTE_PERSONA,
	// BDBotes_SQLiteHelper.COLUMN_ICONO_PERSONA,
	// BDBotes_SQLiteHelper.COLUMN_NOMBRE_PERSONA };

	public PersonaDataSource(Context context) {
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
	 * Elimina la tabla Persona de la base de datos
	 * 
	 * @return Si ha sido eliminada
	 */
	public Boolean deleteTablaPersona() {
		try {
			database.execSQL("DROP TABLE IF EXISTS "
					+ BDBotes_SQLiteHelper.TABLE_PERSONA);
			return true;
		} catch (Exception ex) {
			Log.e(TAG, "Ha ocurrido un error al eliminar la tabla '"
					+ BDBotes_SQLiteHelper.TABLE_PERSONA + "'", ex);
		}
		return false;
	}

	/**
	 * Inserta una persona en la base de datos
	 * 
	 * @param persona
	 *            Persona
	 * @return Si ha sido insertada
	 */
	public Boolean createPersona(Persona persona) {
		try {
			ContentValues values = new ContentValues();
			values.put(BDBotes_SQLiteHelper.COLUMN_ID_CONTACTO_PERSONA,
					persona.getIdContacto());
			values.put(BDBotes_SQLiteHelper.COLUMN_ID_BOTE_PERSONA,
					persona.getBoteID());
			values.put(BDBotes_SQLiteHelper.COLUMN_ICONO_PERSONA,
					persona.getIcono());
			values.put(BDBotes_SQLiteHelper.COLUMN_NOMBRE_PERSONA,
					persona.getNombre());
			long insertId = database.insert(BDBotes_SQLiteHelper.TABLE_PERSONA,
					null, values);
			persona.setId(insertId);
			return true;
		} catch (Exception ex) {
			Log.e(TAG, "Ha ocurrido un error al crear una persona", ex);
		}
		return false;
	}

	/**
	 * Actualiza una persona en la base de datos
	 * 
	 * @param persona
	 *            Persona
	 * @return Si ha sido actualizada
	 */
	public Boolean updatePersona(Persona persona) {
		try {
			String strFilter = "_id=" + persona.getId();
			ContentValues values = new ContentValues();
			values.put(BDBotes_SQLiteHelper.COLUMN_ID_CONTACTO_PERSONA,
					persona.getIdContacto());
			values.put(BDBotes_SQLiteHelper.COLUMN_ID_BOTE_PERSONA,
					persona.getBoteID());
			values.put(BDBotes_SQLiteHelper.COLUMN_ICONO_PERSONA,
					persona.getIcono());
			values.put(BDBotes_SQLiteHelper.COLUMN_NOMBRE_PERSONA,
					persona.getNombre());
			database.update(BDBotes_SQLiteHelper.TABLE_PERSONA, values,
					strFilter, null);
			return true;
		} catch (Exception ex) {
			Log.e(TAG, "Ha ocurrido un error al actualizar una persona", ex);
		}
		return false;
	}

	/**
	 * Elimina una persona de la base de datos
	 * 
	 * @param persona
	 *            Persona
	 * @return Si ha sido eliminada
	 */
	public Boolean deletePersona(Persona persona) {
		try {
			long id = persona.getId();
			// System.out.println("Persona eliminada con el id: " + id);
			database.delete(BDBotes_SQLiteHelper.TABLE_PERSONA,
					BDBotes_SQLiteHelper.COLUMN_ID_PERSONA + " = " + id, null);
			return true;
		} catch (Exception ex) {
			Log.e(TAG, "Ha ocurrido un error al eliminar una persona", ex);
		}
		return false;
	}

	/**
	 * Lista todas las personas de un bote
	 * 
	 * @param idBote
	 *            ID del bote
	 * @return Personas
	 */
	public List<Persona> getAllPersonasBote(long idBote) {
		List<Persona> personas = new ArrayList<Persona>();

		String select = "SELECT " + "P."
				+ BDBotes_SQLiteHelper.COLUMN_ID_PERSONA + ", " + "P."
				+ BDBotes_SQLiteHelper.COLUMN_ID_CONTACTO_PERSONA + ", " + "P."
				+ BDBotes_SQLiteHelper.COLUMN_ID_BOTE_PERSONA + ", " + "P."
				+ BDBotes_SQLiteHelper.COLUMN_ICONO_PERSONA + ", " + "P."
				+ BDBotes_SQLiteHelper.COLUMN_NOMBRE_PERSONA + ", " + "SUM(M."
				+ BDBotes_SQLiteHelper.COLUMN_IMPORTE_MOVIMIENTO + ") AS Total"
				+ " FROM " + BDBotes_SQLiteHelper.TABLE_PERSONA
				+ " P LEFT JOIN " + BDBotes_SQLiteHelper.TABLE_MOVIMIENTO
				+ " M ON P." + BDBotes_SQLiteHelper.COLUMN_ID_PERSONA + " = M."
				+ BDBotes_SQLiteHelper.COLUMN_ID_PERSONA_MOVIMIENTO
				+ " AND (M." + BDBotes_SQLiteHelper.COLUMN_TIPO_MOVIMIENTO
				+ " = 'Ingresar' OR M."
				+ BDBotes_SQLiteHelper.COLUMN_TIPO_MOVIMIENTO
				+ " = 'Devolver')" + " WHERE P."
				+ BDBotes_SQLiteHelper.COLUMN_ID_BOTE_PERSONA + "=" + idBote
				+ " GROUP BY " + "P." + BDBotes_SQLiteHelper.COLUMN_ID_PERSONA
				+ ", " + "P." + BDBotes_SQLiteHelper.COLUMN_ID_CONTACTO_PERSONA
				+ ", " + "P." + BDBotes_SQLiteHelper.COLUMN_ID_BOTE_PERSONA
				+ ", " + "P." + BDBotes_SQLiteHelper.COLUMN_ICONO_PERSONA
				+ ", " + "P." + BDBotes_SQLiteHelper.COLUMN_NOMBRE_PERSONA
				+ " ORDER BY P." + BDBotes_SQLiteHelper.COLUMN_NOMBRE_PERSONA
				+ " ASC";

		Cursor cursor = database.rawQuery(select, null);

		// String where = BDBotes_SQLiteHelper.COLUMN_ID_BOTE_PERSONA + "="
		// + String.valueOf(idBote);
		//
		// Cursor cursor = database.query(BDBotes_SQLiteHelper.TABLE_PERSONA,
		// allColumns, where, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Persona persona = cursorToPersona(cursor);
			personas.add(persona);
			cursor.moveToNext();
		}
		// Nos aseguramos de cerrar el cursor
		cursor.close();
		int numPersonas = personas.size();
		if (numPersonas > 0)
			for (int i = 0; i < numPersonas; i++) {
				Persona p = personas.get(i);
				p.setNumPersonas(numPersonas);
			}
		return personas;
	}

	/**
	 * Convierte un cursor en una persona
	 * 
	 * @param cursor
	 *            Cursor
	 * @return Persona
	 */
	private Persona cursorToPersona(Cursor cursor) {
		Persona persona = new Persona();
		persona.setId(cursor.getLong(0));
		persona.setIdContacto(cursor.getString(1));
		persona.setBoteID(cursor.getLong(2));
		persona.setIcono(cursor.getInt(3));
		persona.setNombre(cursor.getString(4));
		persona.setTotal(cursor.getDouble(5));
		return persona;
	}
}
