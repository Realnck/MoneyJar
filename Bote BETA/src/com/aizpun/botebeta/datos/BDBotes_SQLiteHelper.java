package com.aizpun.botebeta.datos;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Ayudante de SQLite de la base de datos BDBotes
 * 
 * @author Jorge
 * 
 */
public class BDBotes_SQLiteHelper extends SQLiteOpenHelper {

	// Base de datos
	// ------------------------------------------------------------
	public static final String DATABASE_NAME = "BDBotes.db";
	public static final int DATABASE_VERSION = 1;

	// Tabla Bote
	// ---------------------------------------------------------------
	public static final String TABLE_BOTE = "Bote";
	public static final String COLUMN_ID_BOTE = "_id";
	public static final String COLUMN_ICONO_BOTE = "Icono";
	public static final String COLUMN_NOMBRE_BOTE = "Nombre";
	public static final String COLUMN_TOTAL_BOTE = "Total";
	public static final String COLUMN_TOPE_BOTE = "Tope";
	public static final String COLUMN_IMPORTE_TOPE_BOTE = "ImporteTope";
	public static final String COLUMN_TIPO_BOTE = "Tipo";
	public static final String COLUMN_ADMINISTRADOR_BOTE = "Administrador";
	public static final String COLUMN_FECHA_CREACION_BOTE = "FechaCreacion";
	public static final String COLUMN_FECHA_LIMITE_BOTE = "FechaLimite";

	// Script de creación de la tabla Bote
	private static final String DATABASE_CREATE_TABLE_BOTE = "CREATE TABLE "
			+ TABLE_BOTE + "(" + COLUMN_ID_BOTE
			+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_ICONO_BOTE
			+ " INTEGER NOT NULL, " + COLUMN_NOMBRE_BOTE + " TEXT NOT NULL, "
			+ COLUMN_TOTAL_BOTE + " NUMERIC NOT NULL, " + COLUMN_TOPE_BOTE
			+ " INTEGER NOT NULL, " + COLUMN_IMPORTE_TOPE_BOTE
			+ " NUMERIC NOT NULL, " + COLUMN_TIPO_BOTE + " TEXT NOT NULL, "
			+ COLUMN_ADMINISTRADOR_BOTE + " INTEGER NOT NULL,"
			+ COLUMN_FECHA_CREACION_BOTE + " INTEGER NOT NULL,"
			+ COLUMN_FECHA_LIMITE_BOTE + " INTEGER);";

	// Tabla Persona
	// ------------------------------------------------------------
	public static final String TABLE_PERSONA = "Persona";
	public static final String COLUMN_ID_PERSONA = "_id";
	public static final String COLUMN_ID_CONTACTO_PERSONA = "Contacto_id";
	public static final String COLUMN_ID_BOTE_PERSONA = "Bote_id";
	public static final String COLUMN_ICONO_PERSONA = "Icono";
	public static final String COLUMN_NOMBRE_PERSONA = "Nombre";

	// Script de creación de la Persona
	private static final String DATABASE_CREATE_TABLE_PERSONA = "CREATE TABLE "
			+ TABLE_PERSONA + "(" + COLUMN_ID_PERSONA
			+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ COLUMN_ID_CONTACTO_PERSONA + " TEXT NOT NULL, "
			+ COLUMN_ID_BOTE_PERSONA + " INTEGER NOT NULL, "
			+ COLUMN_ICONO_PERSONA + " INTEGER NOT NULL, "
			+ COLUMN_NOMBRE_PERSONA + " TEXT NOT NULL);";

	// Tabla Movimiento
	// ------------------------------------------------------------
	public static final String TABLE_MOVIMIENTO = "Movimiento";
	public static final String COLUMN_ID_MOVIMIENTO = "_id";
	public static final String COLUMN_ID_BOTE_MOVIMIENTO = "Bote_id";
	public static final String COLUMN_ID_PERSONA_MOVIMIENTO = "Persona_id";
	public static final String COLUMN_ICONO_MOVIMIENTO = "Icono";
	public static final String COLUMN_DESCRIPCION_MOVIMIENTO = "Descripcion";
	public static final String COLUMN_TIPO_MOVIMIENTO = "Tipo";
	public static final String COLUMN_IMPORTE_MOVIMIENTO = "Importe";
	public static final String COLUMN_FECHA_CREACION_MOVIMIENTO = "FechaCreacion";

	// Script de creación de la tabla Movimiento
	private static final String DATABASE_CREATE_TABLE_MOVIMIENTO = "CREATE TABLE "
			+ TABLE_MOVIMIENTO
			+ "("
			+ COLUMN_ID_MOVIMIENTO
			+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ COLUMN_ID_BOTE_MOVIMIENTO
			+ " INTEGER NOT NULL, "
			+ COLUMN_ID_PERSONA_MOVIMIENTO
			+ " INTEGER NOT NULL, "
			+ COLUMN_ICONO_MOVIMIENTO
			+ " INTEGER NOT NULL, "
			+ COLUMN_DESCRIPCION_MOVIMIENTO
			+ " TEXT NOT NULL, "
			+ COLUMN_TIPO_MOVIMIENTO
			+ " TEXT NOT NULL, "
			+ COLUMN_IMPORTE_MOVIMIENTO
			+ " INTEGER NOT NULL, "
			+ COLUMN_FECHA_CREACION_MOVIMIENTO + " INTEGER NOT NULL);";

	public BDBotes_SQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	/**
	 * Crea la base de datos
	 * 
	 * @param database
	 *            Base de datos
	 */
	@Override
	public void onCreate(SQLiteDatabase database) {
		Log.w(BDBotes_SQLiteHelper.class.getName(), "Creando la tabla '"
				+ TABLE_BOTE + "'");
		database.execSQL(DATABASE_CREATE_TABLE_BOTE);
		Log.w(BDBotes_SQLiteHelper.class.getName(), "Creando la tabla '"
				+ TABLE_PERSONA + "'");
		database.execSQL(DATABASE_CREATE_TABLE_PERSONA);
		Log.w(BDBotes_SQLiteHelper.class.getName(), "Creando la tabla '"
				+ TABLE_MOVIMIENTO + "'");
		database.execSQL(DATABASE_CREATE_TABLE_MOVIMIENTO);
	}

	/**
	 * Actualiza la base de datos
	 * 
	 * @param db
	 *            Base de datos
	 * @param oldVersion
	 *            Versión antigua
	 * @param newVersion
	 *            Versión nueva
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// if (oldVersion < 2) {
		// Log.w(DataBaseSQLiteHelper.class.getName(),
		// "Actualizando base de datos de la versión " + oldVersion
		// + " a la versión " + newVersion + ".");
		// final String ALTER_TBL = "ALTER TABLE " + TABLE_BOTE
		// + " ADD COLUMN ejemplo TEXT NOT NULL;";
		// db.execSQL(ALTER_TBL);
		// }
	}

	/**
	 * Elimina la base de datos
	 * 
	 * @param v
	 *            Vista actual
	 */
	public static void EliminarBasedeDatos(Context c) {
		c.deleteDatabase(DATABASE_NAME);
	}
}