package com.aizpun.botebeta.negocio;

import java.util.Date;
import java.util.List;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.aizpun.botebeta.datos.BoteDataSource;

/**
 * Bote
 * 
 * @author Jorge
 * 
 */
public class Bote implements Parcelable {
	private long id; // ID
	private int icono; // Icono
	private String nombre; // Nombre
	private double total; // Dinero total disponible
	private Boolean tope; // Indica si tiene tope
	private double importeTope; // Indica el importe tope
	private String tipo; // Tipo
	private long administrador; // Administrador
	private Date fechaCreacion; // Fecha de creación
	private Date fechaLimite; // Fecha límite
	private double ingresadoTotal; // Ingresado total
	private double devoluciones; // Devoluciones
	private double ingresadoActual; // Ingresado actual
	private double gastado; // Gastado
	private double disponible; // Disponible

	public static final Parcelable.Creator<Bote> CREATOR = new Parcelable.Creator<Bote>() {
		public Bote createFromParcel(Parcel in) {
			return new Bote(in);
		}

		public Bote[] newArray(int size) {
			return new Bote[size];
		}
	};

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getIcono() {
		return icono;
	}

	public void setIcono(int icono) {
		this.icono = icono;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	public Boolean getTope() {
		return tope;
	}

	public void setTope(Boolean tope) {
		this.tope = tope;
	}

	public double getImporteTope() {
		return importeTope;
	}

	public void setImporteTope(double importeTope) {
		this.importeTope = importeTope;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public long getAdministrador() {
		return administrador;
	}

	public void setAdministrador(long administrador) {
		this.administrador = administrador;
	}

	public Date getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public Date getFechaLimite() {
		return fechaLimite;
	}

	public void setFechaLimite(Date fechaLimite) {
		this.fechaLimite = fechaLimite;
	}

	public double getIngresadoTotal() {
		return ingresadoTotal;
	}

	public void setIngresadoTotal(double ingresadoTotal) {
		this.ingresadoTotal = ingresadoTotal;
	}

	public double getDevoluciones() {
		return devoluciones;
	}

	public void setDevoluciones(double devoluciones) {
		this.devoluciones = devoluciones;
	}

	public double getIngresadoActual() {
		return ingresadoActual;
	}

	public void setIngresadoActual(double ingresadoActual) {
		this.ingresadoActual = ingresadoActual;
	}

	public double getGastado() {
		return gastado;
	}

	public void setGastado(double gastado) {
		this.gastado = gastado;
	}

	public double getDisponible() {
		return disponible;
	}

	public void setDisponible(double disponible) {
		this.disponible = disponible;
	}

	/**
	 * Construye un Bote
	 */
	public Bote() {
		super();
	}

	/**
	 * Construye un Bote a partir de unos valores
	 * 
	 * @param id
	 *            ID
	 * @param icono
	 *            Icono
	 * @param nombre
	 *            Nombre
	 * @param total
	 *            Dinero total disponible
	 * @param tope
	 *            Indica si tiene tope
	 * @param importeTope
	 *            Indica el importe tope
	 * @param tipo
	 *            Tipo
	 * @param administrador
	 *            Administrador del bote
	 * @param fechaCreacion
	 *            Fecha de creación
	 * @param fechaLimite
	 *            fecha límite
	 * @param ingresadoTotal
	 *            Ingresado total
	 * @param devoluciones
	 *            Devoluciones
	 * @param ingresadoActual
	 *            Ingresado actual
	 * @param gastado
	 *            Gastado
	 * @param disponible
	 *            Disponible
	 */
	public Bote(long id, int icono, String nombre, double total, Boolean tope,
			double importeTope, String tipo, long administrador,
			Date fechaCreacion, Date fechaLimite, double ingresadoTotal,
			double devoluciones, double ingresadoActual, double gastado,
			double disponible) {
		super();
		this.id = id;
		this.icono = icono;
		this.nombre = nombre;
		this.total = total;
		this.tope = tope;
		this.importeTope = importeTope;
		this.tipo = tipo;
		this.administrador = administrador;
		this.fechaCreacion = fechaCreacion;
		this.fechaLimite = fechaLimite;
		this.ingresadoTotal = ingresadoTotal;
		this.devoluciones = devoluciones;
		this.ingresadoActual = ingresadoActual;
		this.gastado = gastado;
		this.disponible = disponible;
	}

	/**
	 * Construye un Bote a partir de unos valores
	 * 
	 * @param source
	 *            valores
	 */
	public Bote(Parcel source) {
		// Reconstruct from the Parcel
		id = source.readLong();
		icono = source.readInt();
		nombre = source.readString();
		total = source.readDouble();
		tope = source.readByte() == 1;
		importeTope = source.readDouble();
		tipo = source.readString();
		administrador = source.readLong();
		long fC = source.readLong();
		if (fC == -1)
			fechaCreacion = null;
		else
			fechaCreacion = new Date(fC);
		long fL = source.readLong();
		if (fL == -1)
			fechaLimite = null;
		else
			fechaLimite = new Date(fL);
		ingresadoTotal = source.readDouble();
		devoluciones = source.readDouble();
		ingresadoActual = source.readDouble();
		gastado = source.readDouble();
		disponible = source.readDouble();
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
		dest.writeInt(icono);
		dest.writeString(nombre);
		dest.writeDouble(total);
		dest.writeByte((byte) (tope ? 1 : 0));
		dest.writeDouble(importeTope);
		dest.writeString(tipo);
		dest.writeLong(administrador);
		if (fechaCreacion == null)
			dest.writeLong(-1);
		else
			dest.writeLong(fechaCreacion.getTime());
		if (fechaLimite == null)
			dest.writeLong(-1);
		else
			dest.writeLong(fechaLimite.getTime());
		dest.writeDouble(ingresadoTotal);
		dest.writeDouble(devoluciones);
		dest.writeDouble(ingresadoActual);
		dest.writeDouble(gastado);
		dest.writeDouble(disponible);
	}

	/**
	 * Se necesitará durante el proceso de un-marshaling de los datos
	 * almacenados en el Parcel
	 */
	public class MyCreator implements Parcelable.Creator<Bote> {
		public Bote createFromParcel(Parcel source) {
			return new Bote(source);
		}

		public Bote[] newArray(int size) {
			return new Bote[size];
		}
	}

	/**
	 * Lista todos los botes
	 * 
	 * @param context
	 *            Contexto
	 * @return Lista de botes
	 */
	public static List<Bote> getAllBotes(Context context) {
		List<Bote> mListaBotes = null;
		BoteDataSource datasource = new BoteDataSource(context);
		datasource.open();
		mListaBotes = datasource.getAllBotes();
		datasource.close();
		return mListaBotes;
	}

	/**
	 * Lista un bote
	 * 
	 * @param context
	 *            Contexto
	 * @return Bote
	 */
	public static Bote getBote(Context context, long id) {
		Bote mBote = null;
		BoteDataSource datasource = new BoteDataSource(context);
		datasource.open();
		mBote = datasource.getBote(id);
		datasource.close();
		return mBote;
	}

	/**
	 * Elimina un bote
	 * 
	 * @param context
	 *            Contexto
	 * @param bote
	 *            Bote
	 * @return Si ha sido eliminado
	 */
	public static Boolean DeleteBote(Context context, Bote bote) {
		Boolean eliminado = false;
		BoteDataSource datasource = new BoteDataSource(context);
		datasource.open();
		if (datasource.deleteBote(bote))
			eliminado = true;
		datasource.close();
		return eliminado;
	}
}
