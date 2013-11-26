package com.aizpun.botebeta.negocio;

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

public class PersonaSpinnerAdapter extends ArrayAdapter<Persona> {
	Context context;
	int layoutResourceId;
	Persona[] data = null;

	public PersonaSpinnerAdapter(Context context, int layoutResourceId,
			Persona[] data) {
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
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(layoutResourceId, parent, false);
			row.setDuplicateParentStateEnabled(true);
			holder = new PersonaHolder();
			holder.imgIcono = (ImageView) row
					.findViewById(R.id.img_elemento_lista_persona_icono);
			holder.txtNombre = (TextView) row
					.findViewById(R.id.txt_elemento_lista_persona_nombre);
			TextView txtTextoDebe = (TextView) row
					.findViewById(R.id.txt_elemento_lista_persona_texto_debe);
			TextView txtDebe = (TextView) row
					.findViewById(R.id.txt_elemento_lista_persona_debe);
			txtTextoDebe.setVisibility(View.GONE);
			txtDebe.setVisibility(View.GONE);
			row.setTag(holder);
		} else {
			holder = (PersonaHolder) row.getTag();
		}

		Persona persona = data[position];
		if (persona != null) {
			Uri u = getPhotoUri(persona.getIdContacto());
			if (u != null) {
				holder.imgIcono.setImageURI(u);
				if (holder.imgIcono.getDrawable() == null)
					holder.imgIcono
							.setImageResource(R.drawable.ic_contact_picture);
			} else
				holder.imgIcono.setImageResource(R.drawable.ic_contact_picture);
			holder.txtNombre.setText(persona.getNombre());
		}
		return row;
	}

	static class PersonaHolder {
		ImageView imgIcono;
		TextView txtNombre;
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

	// And here is when the "chooser" is popped up
	// Normally is the same view, but you can customize it if you want
	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		PersonaHolder holder = null;

		if (row == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(layoutResourceId, parent, false);
			holder = new PersonaHolder();
			holder.imgIcono = (ImageView) row
					.findViewById(R.id.img_elemento_lista_persona_icono);
			holder.txtNombre = (TextView) row
					.findViewById(R.id.txt_elemento_lista_persona_nombre);
			TextView txtTextoDebe = (TextView) row
					.findViewById(R.id.txt_elemento_lista_persona_texto_debe);
			TextView txtDebe = (TextView) row
					.findViewById(R.id.txt_elemento_lista_persona_debe);
			txtTextoDebe.setVisibility(View.GONE);
			txtDebe.setVisibility(View.GONE);
			row.setTag(holder);
		} else {
			holder = (PersonaHolder) row.getTag();
		}

		Persona persona = data[position];
		if (persona != null) {
			Uri u = getPhotoUri(persona.getIdContacto());
			if (u != null) {
				holder.imgIcono.setImageURI(u);
				if (holder.imgIcono.getDrawable() == null)
					holder.imgIcono
							.setImageResource(R.drawable.ic_contact_picture);
			} else
				holder.imgIcono.setImageResource(R.drawable.ic_contact_picture);
			holder.txtNombre.setText(persona.getNombre());
		}
		return row;
	}
}
