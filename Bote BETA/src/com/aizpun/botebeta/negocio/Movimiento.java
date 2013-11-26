package com.aizpun.botebeta.negocio;

import java.util.Date;
import java.util.List;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.aizpun.botebeta.datos.MovimientoDataSource;

/**
 * Movimiento
 * 
 * @author Jorge
 * 
 */
public class Movimiento implements Parcelable {
	private long id; // ID
	private long boteID; // ID del bote
	private long personaID; // ID de la persona
	private int icono; // Icono
	private String descripcion; // Descripción
	private String tipo; // Tipo
	private double importe; // Indica el importe
	private Date fechaCreacion; // Fecha de creación

	public static final Parcelable.Creator<Movimiento> CREATOR = new Parcelable.Creator<Movimiento>() {
		public Movimiento createFromParcel(Parcel in) {
			return new Movimiento(in);
		}

		public Movimiento[] newArray(int size) {
			return new Movimiento[size];
		}
	};

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getBoteID() {
		return boteID;
	}

	public void setBoteID(long boteID) {
		this.boteID = boteID;
	}

	public long getPersonaID() {
		return personaID;
	}

	public void setPersonaID(long personaID) {
		this.personaID = personaID;
	}

	public int getIcono() {
		return icono;
	}

	public void setIcono(int icono) {
		this.icono = icono;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public double getImporte() {
		return importe;
	}

	public void setImporte(double importe) {
		this.importe = importe;
	}

	public Date getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	/**
	 * Construye un Movimiento
	 */
	public Movimiento() {
		super();
	}

	/**
	 * Construye un Movimiento a partir de unos valores
	 * 
	 * @param id
	 *            ID
	 * @param boteID
	 *            ID del bote
	 * @param personaID
	 *            ID de la persona
	 * @param icono
	 *            Icono
	 * @param descripcion
	 *            Descripción del movimiento
	 * @param tipo
	 *            Tipo
	 * @param importe
	 *            Importe
	 * @param fechaCreacion
	 *            Fecha de creación
	 */
	public Movimiento(long id, long boteID, long personaID, int icono,
			String descripcion, String tipo, double importe, Date fechaCreacion) {
		super();
		this.id = id;
		this.boteID = boteID;
		this.personaID = personaID;
		this.icono = icono;
		this.descripcion = descripcion;
		this.tipo = tipo;
		this.importe = importe;
		this.fechaCreacion = fechaCreacion;
	}

	/**
	 * Construye un Movimiento a partir de unos valores
	 * 
	 * @param source
	 *            valores
	 */
	public Movimiento(Parcel source) {
		// Reconstruct from the Parcel
		this.id = source.readLong();
		this.boteID = source.readLong();
		this.personaID = source.readLong();
		this.icono = source.readInt();
		this.descripcion = source.readString();
		this.tipo = source.readString();
		this.importe = source.readDouble();
		long fC = source.readLong();
		if (fC == -1)
			fechaCreacion = null;
		else
			this.fechaCreacion = new Date(fC);
	}

	/**
	 * Describe el contenido
	 */
	public int describeContents() {
		return this.hashCode();
	}

	/**
	 * Escribe el objeto Parcel
	 * 
	 * @param dest
	 *            destino
	 * @param flags
	 *            variables
	 */
	public void writeToParcel(Parcel dest, int flags) {
		// Log.v(TAG, "writeToParcel..."+ flags);
		dest.writeLong(id);
		dest.writeLong(boteID);
		dest.writeLong(personaID);
		dest.writeInt(icono);
		dest.writeString(descripcion);
		dest.writeString(tipo);
		dest.writeDouble(importe);
		if (fechaCreacion == null)
			dest.writeLong(-1);
		else
			dest.writeLong(fechaCreacion.getTime());
	}

	/**
	 * Se necesitará durante el proceso de un-marshaling de los datos
	 * almacenados en el Parcel
	 */
	public class MyCreator implements Parcelable.Creator<Movimiento> {
		public Movimiento createFromParcel(Parcel source) {
			return new Movimiento(source);
		}

		public Movimiento[] newArray(int size) {
			return new Movimiento[size];
		}
	}

	/**
	 * Lista todos los movimientos
	 * 
	 * @param context
	 *            Contexto
	 * @return Lista de movimientos
	 */
	public static List<Movimiento> getAllMovimientos(Context context) {
		List<Movimiento> mListaMovimientos = null;
		MovimientoDataSource datasource = new MovimientoDataSource(context);
		datasource.open();
		mListaMovimientos = datasource.getAllMovimientos();
		datasource.close();
		return mListaMovimientos;
	}

	/**
	 * Lista todos los Movimiento
	 * 
	 * @param context
	 *            Contexto
	 * @param idBote
	 *            ID del bote
	 * @return Lista de Movimientos
	 */
	public static List<Movimiento> getAllMovimientosBote(Context context,
			long idBote) {
		List<Movimiento> mListaMovimientos = null;
		MovimientoDataSource datasource = new MovimientoDataSource(context);
		datasource.open();
		mListaMovimientos = datasource.getAllMovimientosBote(idBote);
		datasource.close();
		return mListaMovimientos;
	}

	/**
	 * Elimina un movimiento
	 * 
	 * @param context
	 *            Contexto
	 * @param movimiento
	 *            Movimiento
	 * @return Si ha sido eliminado
	 */
	public static Boolean DeleteMovimiento(Context context,
			Movimiento movimiento) {
		Boolean eliminado = false;
		MovimientoDataSource datasource = new MovimientoDataSource(context);
		datasource.open();
		if (datasource.deleteMovimiento(movimiento))
			eliminado = true;
		datasource.close();
		return eliminado;
	}
}
