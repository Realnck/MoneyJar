package com.aizpun.botebeta.negocio;

import java.util.List;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.aizpun.botebeta.datos.PersonaDataSource;

/**
 * Movimiento
 * 
 * @author Jorge
 * 
 */
public class Persona implements Parcelable {
	private long id; // ID
	private String idContacto; // ID del contacto en el teléfono
	private long boteID; // ID del bote
	private int icono; // Icono
	private String nombre; // Nombre
	private double total; // Indica el total
	private double totalBote; // Indica el total del bote
	private int numPersonas; // Indica el número de personas
	public static Bote bote;

	public static final Parcelable.Creator<Persona> CREATOR = new Parcelable.Creator<Persona>() {
		public Persona createFromParcel(Parcel in) {
			return new Persona(in);
		}

		public Persona[] newArray(int size) {
			return new Persona[size];
		}
	};

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getIdContacto() {
		return idContacto;
	}

	public void setIdContacto(String idContacto) {
		this.idContacto = idContacto;
	}

	public long getBoteID() {
		return boteID;
	}

	public void setBoteID(long boteID) {
		this.boteID = boteID;
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

	public double getTotalBote() {
		return totalBote;
	}

	public void setTotalBote(double totalBote) {
		this.totalBote = totalBote;
	}

	public int getNumPersonas() {
		return numPersonas;
	}

	public void setNumPersonas(int numPersonas) {
		this.numPersonas = numPersonas;
	}

	/**
	 * Construye un Movimiento
	 */
	public Persona() {
		super();
	}

	/**
	 * Construye una Persona a partir de unos valores
	 * 
	 * @param id
	 *            ID
	 * @param idContacto
	 *            ID del contacto en el teléfono
	 * @param boteID
	 *            ID del bote
	 * @param icono
	 *            Icono
	 * @param nombre
	 *            Nombre de de la persona
	 * @param total
	 *            Total
	 * @param totalBote
	 *            Total del bote
	 * @param numPersonas
	 *            Número de personas
	 */
	public Persona(long id, String idContacto, long boteID, int icono,
			String nombre, double total, double totalBote, int numPersonas) {
		super();
		this.id = id;
		this.idContacto = idContacto;
		this.boteID = boteID;
		this.icono = icono;
		this.nombre = nombre;
		this.total = total;
		this.totalBote = totalBote;
		this.numPersonas = numPersonas;
	}

	/**
	 * Construye una Persona a partir de unos valores
	 * 
	 * @param source
	 *            valores
	 */
	public Persona(Parcel source) {
		// Reconstruct from the Parcel
		this.id = source.readLong();
		this.idContacto = source.readString();
		this.boteID = source.readLong();
		this.icono = source.readInt();
		this.nombre = source.readString();
		this.total = source.readDouble();
		this.totalBote = source.readDouble();
		this.numPersonas = source.readInt();
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
		dest.writeString(idContacto);
		dest.writeLong(boteID);
		dest.writeInt(icono);
		dest.writeString(nombre);
		dest.writeDouble(total);
		dest.writeDouble(totalBote);
		dest.writeInt(numPersonas);
	}

	/**
	 * Se necesitará durante el proceso de un-marshaling de los datos
	 * almacenados en el Parcel
	 */
	public class MyCreator implements Parcelable.Creator<Persona> {
		public Persona createFromParcel(Parcel source) {
			return new Persona(source);
		}

		public Persona[] newArray(int size) {
			return new Persona[size];
		}
	}

	/**
	 * Lista todos las Personas
	 * 
	 * @param context
	 *            Contexto
	 * @param idBote
	 *            ID del bote
	 * @return Lista de personas
	 */
	public static List<Persona> getAllPersonasBote(Context context, long idBote) {
		List<Persona> mListaPersonas = null;
		PersonaDataSource datasource = new PersonaDataSource(context);
		datasource.open();
		mListaPersonas = datasource.getAllPersonasBote(idBote);
		datasource.close();
		return mListaPersonas;
	}

	/**
	 * Elimina una persona
	 * 
	 * @param context
	 *            Contexto
	 * @param persona
	 *            Persona
	 * @return Si ha sido eliminada
	 */
	public static Boolean DeletePersona(Context context, Persona persona) {
		Boolean eliminada = false;
		PersonaDataSource datasource = new PersonaDataSource(context);
		datasource.open();
		if (datasource.deletePersona(persona))
			eliminada = true;
		datasource.close();
		return eliminada;
	}
}
