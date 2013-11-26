package com.aizpun.botebeta.cliente;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.aizpun.botebeta.R;
import com.aizpun.botebeta.datos.PersonaDataSource;
import com.aizpun.botebeta.negocio.Bote;
import com.aizpun.botebeta.negocio.Persona;
import com.aizpun.botebeta.negocio.PersonaAdapter;
import com.aizpun.botebeta.utilidades.NuevaPersonaDialog;
import com.aizpun.botebeta.utilidades.NuevoMovimientoDialog;
import com.aizpun.botebeta.utilidades.Utilidades;

/**
 * Fragmento consulta de bote
 * 
 * @author Jorge
 * 
 */
public class BoteConsultaActivity extends Activity {
	public final static String ARG_BOTE = "bote";
	public final static String POSICION = "posicion";
	public final static String LISTA_PERSONAS = "lista de personas";
	private static final int PICK_CONTACT_REQUEST = 1;
	Bote bote = null;
	int mPosicionActual = -1;
	List<Persona> mListaPersonas = new ArrayList<Persona>();
	PersonaAdapter mAdaptadorPersonas = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bote_consulta);

		if (savedInstanceState != null) {
			bote = savedInstanceState.getParcelable(ARG_BOTE);
			mPosicionActual = savedInstanceState.getInt(POSICION);
			mListaPersonas = savedInstanceState
					.getParcelableArrayList(LISTA_PERSONAS);
		} else {
			Intent intent = getIntent();
			if (intent != null)
				bote = (Bote) intent.getParcelableExtra(ARG_BOTE);
		}
		cargarDatos(bote);
		/*
		 * mAdaptadorPersonas = new PersonaAdapter(this,
		 * R.layout.personas_consulta_elemento_lista, mListaPersonas); // Crea
		 * una adaptador de array para la vista de lista, usando la lista // de
		 * Personas if (mAdaptadorPersonas != null &&
		 * findViewById(R.id.list_consulta_bote_consulta_personas) != null) {
		 * ListView lista = (ListView)
		 * findViewById(R.id.list_consulta_bote_consulta_personas);
		 * lista.setAdapter(mAdaptadorPersonas); //
		 * lista.setOnItemClickListener(mOnItemClickListener);
		 * registerForContextMenu(lista); }
		 */
	}

	// @Override
	// public void onStart() {
	// super.onStart();
	// cargarDatos(bote);
	// }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		if (bote.getTipo().equals("Colectivo")) {
			inflater.inflate(R.menu.bote_consulta_colectivo, menu);
			return true;
		} else if (bote.getTipo().equals("Individual")) {
			inflater.inflate(R.menu.bote_consulta_individual, menu);
			return true;
		}
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.menu_bote_consulta_persona_nueva:
			addPersonaNueva();
			return true;
		case R.id.menu_bote_consulta_persona_desde_contacto:
			pickContact();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		/*
		 * if (v.getId() == R.id.list_consulta_bote_consulta_personas) {
		 * AdapterView.AdapterContextMenuInfo info =
		 * (AdapterView.AdapterContextMenuInfo) menuInfo; mPosicionActual =
		 * info.position;
		 * menu.setHeaderTitle(mListaPersonas.get(mPosicionActual).getNombre());
		 * super.onCreateContextMenu(menu, v, menuInfo); MenuInflater inflater =
		 * getMenuInflater();
		 * inflater.inflate(R.menu.personas_consulta_elemento_lista_menu, menu);
		 * }
		 */
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_personas_consulta_elemento_lista_remove_persona:
			removePersona();
			return true;
			// case R.id.menu_personas_consulta_elemento_lista_edit_persona:
			// // editPersona();
			// return true;
		default:
			return super.onContextItemSelected(item);
		}
	}

	public void cargarDatos(Bote b) {
		bote = b;
		DateFormat datef = SimpleDateFormat.getDateInstance(
				SimpleDateFormat.MEDIUM, Locale.getDefault());
		// // Título
		// setTitle(getString(R.string.title_activity_bote_edit));
		// Nombre
		TextView textViewNombre = (TextView) findViewById(R.id.txt_consulta_bote_nombre);
		textViewNombre.setText(bote.getNombre());
		// Total
		// LinearLayout linearLayoutTotal = (LinearLayout) v
		// .findViewById(R.id.linear_Layout_consulta_bote);
		// linearLayoutTotal.setOnClickListener(mOnClickListener);
		// TextView textViewTotal = (TextView) v
		// .findViewById(R.id.lbl_consulta_bote_total);
		// textViewTotal.setText(Utilidades.formatearDouble(bote.getTotal()));
		TextView textViewDisponibleTitulo = (TextView) findViewById(R.id.txt_consulta_bote_disponible_titulo);
		textViewDisponibleTitulo.setText(getString(R.string.total) + ": ");
		// Button buttonTotal = (Button)
		// findViewById(R.id.btn_consulta_bote_total);
		// buttonTotal.setText(Utilidades.formatearMoneda(bote.getTotal()));
		// buttonTotal.setOnClickListener(mOnClickListener);
		TextView textViewDisponible = (TextView) findViewById(R.id.txt_consulta_bote_disponible);
		textViewDisponible.setText(Utilidades.formatearMoneda(bote.getTotal()));
		// Tope
		TextView textViewTope = (TextView) findViewById(R.id.txt_consulta_bote_importe_tope);
		if (bote.getTope()) {
			textViewTope.setHint(R.string.toca_para_editar);
			if (bote.getImporteTope() != -1) {
				textViewTope.setText(Utilidades.formatearDouble(bote
						.getImporteTope()));
			} else {
				textViewTope.setText("");
			}
		} else {
			textViewTope.setHint(R.string.tope_editor_sin_tope);
			textViewTope.setText("");
		}
		// Tipo
		TextView textViewTipo = (TextView) findViewById(R.id.txt_consulta_bote_tipo);
		textViewTipo.setText(bote.getTipo());
		// Fecha creación
		TextView textViewFechaCreacion = (TextView) findViewById(R.id.txt_consulta_bote_fecha_creacion);
		textViewFechaCreacion.setText(datef.format(bote.getFechaCreacion()
				.getTime()));
		// Fecha límite
		TextView textViewFechaLimite = (TextView) findViewById(R.id.txt_consulta_bote_fecha_limite);
		if (bote.getFechaLimite() != null
				&& bote.getFechaLimite().getTime() != 0) {
			textViewFechaLimite.setHint(R.string.toca_para_seleccionar);
			textViewFechaLimite.setText(datef.format(bote.getFechaLimite()
					.getTime()));
		} else {
			textViewFechaLimite
					.setHint(R.string.fecha_limite_editor_sin_limite);
			textViewFechaLimite.setText("");
		}

		// // Administrador
		// TextView textViewAdministrador = (TextView)
		// findViewById(R.id.txt_consulta_bote_admnistrador);
		// textViewAdministrador.setText("");

		mListaPersonas = Persona.getAllPersonasBote(this, bote.getId());
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		// Guarda los elementos del bote en caso de que sea necesario recrear el
		// fragmento
		outState.putParcelable(ARG_BOTE, bote);
		outState.putInt(POSICION, mPosicionActual);
		outState.putParcelableArrayList(LISTA_PERSONAS,
				(ArrayList<Persona>) mListaPersonas);
	}

	// private View.OnClickListener mOnClickListener = new
	// View.OnClickListener() {
	// public void onClick(View v) {
	// addMovimiento(v);
	// }
	// };

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// Check which request we're responding to
		if (requestCode == PICK_CONTACT_REQUEST) {
			// Make sure the request was successful
			if (resultCode == RESULT_OK) {
				// Get the URI that points to the selected contact
				Uri contactUri = data.getData();
				String[] proyection = new String[] {
						ContactsContract.Data.CONTACT_ID,
						ContactsContract.Contacts.DISPLAY_NAME };

				// Perform the query on the contact to get the NUMBER column
				// We don't need a selection or sort order (there's only one
				// result for the given URI)
				// CAUTION: The query() method should be called from a separate
				// thread to avoid blocking
				// your app's UI thread. (For simplicity of the sample, this
				// code doesn't do that.)
				// Consider using CursorLoader to perform the query.
				Cursor cursor = getContentResolver().query(contactUri,
						proyection, null, null, null);
				cursor.moveToFirst();

				String name = cursor
						.getString(cursor
								.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
				String idContacto = cursor.getString(cursor
						.getColumnIndex(ContactsContract.Data.CONTACT_ID));
				cursor.close();
				Persona persona = new Persona(-1, idContacto, bote.getId(), -1,
						name, 0, bote.getTotal(), -1);
				GuardarPersona(persona);
				mListaPersonas.add(persona);
				mAdaptadorPersonas.notifyDataSetChanged();
			}
		}
	}

	private void GuardarPersona(Persona persona) {
		PersonaDataSource datasource = new PersonaDataSource(this);
		datasource.open();
		if (datasource.createPersona(persona)) {
			Context context = getApplicationContext();
			CharSequence text = getString(R.string.persona_inserted);
			int duration = Toast.LENGTH_SHORT;
			Toast toast = Toast.makeText(context, text, duration);
			toast.show();
		}
		datasource.close();
	}

	public void addMovimiento(View v) {
		NuevoMovimientoDialog nuevoMovimientoDialog = new NuevoMovimientoDialog(
				this);
		nuevoMovimientoDialog.show();
	}

	public void addPersonaNueva() {
		NuevaPersonaDialog nuevaPersonaDialog = new NuevaPersonaDialog(this);
		nuevaPersonaDialog.idBote = bote.getId();
		nuevaPersonaDialog.setOnDismissListener(onDismissListener);
		nuevaPersonaDialog.show();
	}

	private void pickContact() {
		Uri uri = Uri.parse("content://contacts");
		Intent pickContactIntent = new Intent(Intent.ACTION_PICK, uri);
		pickContactIntent.setType(Phone.CONTENT_TYPE); // Show user only
														// contacts w/ phone
														// numbers
		startActivityForResult(pickContactIntent, PICK_CONTACT_REQUEST);
	}

	public void removePersona() {
		if (mListaPersonas != null && mListaPersonas.size() > 0) {
			if (Persona
					.DeletePersona(this, mListaPersonas.get(mPosicionActual))) {
				mListaPersonas.remove(mPosicionActual);
				CharSequence text = getString(R.string.persona_deleted);
				int duration = Toast.LENGTH_SHORT;
				Toast toast = Toast.makeText(this, text, duration);
				toast.show();
			}
			mAdaptadorPersonas.notifyDataSetChanged();
		}
	}

	private DialogInterface.OnDismissListener onDismissListener = new DialogInterface.OnDismissListener() {

		public void onDismiss(DialogInterface dialog) {
			onNuevaPersonaDialogDismiss(dialog);
		}
	};

	public void onNuevaPersonaDialogDismiss(DialogInterface arg0) {
		mAdaptadorPersonas.clear();
		mAdaptadorPersonas
				.addAll(Persona.getAllPersonasBote(this, bote.getId()));
	}

	// public void onDismiss(DialogInterface dialog) {
	// mListaPersonas = Persona.getAllPersonasBote(this, bote.getId());
	// mAdaptadorPersonas.clear();
	// mAdaptadorPersonas.addAll(mListaPersonas);
	// }
}