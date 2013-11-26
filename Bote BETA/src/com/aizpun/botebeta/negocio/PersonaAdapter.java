package com.aizpun.botebeta.negocio;

import java.util.List;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.aizpun.botebeta.R;
import com.aizpun.botebeta.utilidades.Utilidades;

/**
 * Adaptador de la tabla Persona
 * 
 * @author Jorge
 * 
 */
public class PersonaAdapter extends ArrayAdapter<Persona> {

	Context context;
	int layoutResourceId;
	List<Persona> data = null;

	public PersonaAdapter(Context context, int layoutResourceId,
			List<Persona> data) {
		super(context, layoutResourceId, data);
		this.layoutResourceId = layoutResourceId;
		this.context = context;
		this.data = data;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		PersonaHolder holder = null;

		if (row == null) {
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			row = inflater.inflate(layoutResourceId, parent, false);
			holder = new PersonaHolder();
			holder.imgIcono = (ImageView) row
					.findViewById(R.id.img_elemento_lista_persona_icono);
			holder.txtNombre = (TextView) row
					.findViewById(R.id.txt_elemento_lista_persona_nombre);
			holder.txtTotal = (TextView) row
					.findViewById(R.id.txt_elemento_lista_persona_total);
			holder.txtTextoDebe = (TextView) row
					.findViewById(R.id.txt_elemento_lista_persona_texto_debe);
			holder.txtDebe = (TextView) row
					.findViewById(R.id.txt_elemento_lista_persona_debe);
			row.setTag(holder);
		} else {
			holder = (PersonaHolder) row.getTag();
		}

		Persona persona = data.get(position);
		Uri u = getPhotoUri(persona.getIdContacto());
		if (u != null) {
			holder.imgIcono.setImageURI(u);
			if (holder.imgIcono.getDrawable() == null)
				holder.imgIcono.setImageResource(R.drawable.ic_contact_picture);
		} else
			holder.imgIcono.setImageResource(R.drawable.ic_contact_picture);
		holder.txtNombre.setText(persona.getNombre());
		holder.txtTotal.setText(Utilidades.formatearMoneda(persona.getTotal()));
		if (Persona.bote != null && persona.getNumPersonas() > 0) {
			double totalIngresadoActual = Persona.bote.getIngresadoActual();
			double totalPersona = persona.getTotal();
			double numPersonas = persona.getNumPersonas();
			double parte = totalIngresadoActual / numPersonas;
			if (totalPersona > parte) {
				holder.txtTextoDebe.setText("Le deben: ");
				holder.txtDebe.setText(Utilidades.formatearMoneda(totalPersona
						- parte));
				holder.txtDebe.setTextColor(((Activity) context).getResources()
						.getColor(R.color.verde));
			} else if (totalPersona < parte) {
				holder.txtTextoDebe.setText("Debe: ");
				holder.txtDebe.setText(Utilidades.formatearMoneda(parte
						- totalPersona));
				holder.txtDebe.setTextColor(((Activity) context).getResources()
						.getColor(R.color.rojo));
			}
		}
		return row;
	}

	static class PersonaHolder {
		ImageView imgIcono;
		TextView txtNombre;
		TextView txtTotal;
		TextView txtTextoDebe;
		TextView txtDebe;
	}

	/**
	 * Devuelve la URI correspondiente a la foto de un contacto
	 * 
	 * @param id
	 *            ID del contacto
	 * @return La URI de la foto
	 */
	public Uri getPhotoUri(String id) {
		try {
			Cursor cur = context
					.getContentResolver()
					.query(ContactsContract.Data.CONTENT_URI,
							null,
							ContactsContract.Data.CONTACT_ID
									+ "="
									+ id
									+ " AND "
									+ ContactsContract.Data.MIMETYPE
									+ "='"
									+ ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE
									+ "'", null, null);
			if (cur != null) {
				if (!cur.moveToFirst()) {
					return null; // No hay foto
				}
			} else {
				return null; // No hay cursor
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		Uri person = ContentUris.withAppendedId(
				ContactsContract.Contacts.CONTENT_URI, Long.parseLong(id));
		return Uri.withAppendedPath(person,
				ContactsContract.Contacts.Photo.CONTENT_DIRECTORY);
	}

}