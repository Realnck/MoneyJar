package com.aizpun.botebeta.cliente;

import java.util.List;
import java.util.Vector;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Contacts;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.aizpun.botebeta.R;
import com.aizpun.botebeta.datos.PersonaDataSource;
import com.aizpun.botebeta.negocio.Bote;
import com.aizpun.botebeta.negocio.Movimiento;
import com.aizpun.botebeta.negocio.Persona;
import com.aizpun.botebeta.negocio.TabPagerAdapter;
import com.aizpun.botebeta.utilidades.Fragmento;
import com.aizpun.botebeta.utilidades.NuevaPersonaDialog;
import com.aizpun.botebeta.utilidades.NuevoMovimientoDialog;
import com.viewpagerindicator.TitlePageIndicator;

/**
 * La clase <code>BoteConsultaTabsViewPagerFragmentActivity</code> implementa la
 * FragmentActivity que mantiene un TabHost usando un ViewPager
 * 
 * @author Jorge
 *         http://thepseudocoder.wordpress.com/2011/10/13/android-tabs-viewpager
 *         -swipe-able-tabs-ftw/
 * 
 */
public class BoteConsultaTabsViewPagerFragmentActivity extends FragmentActivity
		implements ViewPager.OnPageChangeListener,
		BoteConsultaFragment.OnNuevoMovimientoCreatedListener,
		MovimientosConsultaFragment.OnMovimientoActualizadoListener {
	public final static String ARG_BOTE = "bote";
	public final static String CURRENT_PAGE = "página actual";
	public final static String LISTA_PERSONAS = "lista de personas";
	private static final int PICK_CONTACT_REQUEST = 1;
	private static final int CONTACT_PICKER_RESULT = 1001;
	private ViewPager mViewPager;
	private TabPagerAdapter mPagerAdapter;
	private List<Fragmento> fragments;
	private Bote bote = null;
	private int currentPage = 0;

	/**
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.FragmentActivity#onCreate(android.os.Bundle)
	 */
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bote_consulta_tabs_viewpager);
		// Inicializa el ViewPager
		if (savedInstanceState != null) {
			currentPage = savedInstanceState.getInt(CURRENT_PAGE);
			bote = savedInstanceState.getParcelable(ARG_BOTE);
		} else {
			Intent intent = getIntent();
			if (intent != null)
				bote = (Bote) intent.getParcelableExtra(ARG_BOTE);
		}
		if (bote != null) {
			bote = Bote.getBote(this, bote.getId());
			Persona.bote = bote;
		} else
			Persona.bote = null;
		this.intialiseViewPager();
		// Set the pager with an adapter
		ViewPager pager = (ViewPager) findViewById(R.id.bote_consulta_viewpager);
		pager.setAdapter(mPagerAdapter);

		// Bind the title indicator to the adapter
		TitlePageIndicator titleIndicator = (TitlePageIndicator) findViewById(R.id.bote_consulta_titles);
		titleIndicator.setViewPager(pager);
		pager.setCurrentItem(currentPage);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.FragmentActivity#onSaveInstanceState(android.os.Bundle)
	 */
	protected void onSaveInstanceState(Bundle outState) {
		// Guarda la pestaña seleccionada
		outState.putInt(CURRENT_PAGE, currentPage);
		outState.putParcelable(ARG_BOTE, bote);
		super.onSaveInstanceState(outState);
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		if (bote.getTipo().equals("Colectivo")) {
			inflater.inflate(R.menu.bote_consulta_colectivo, menu);
			return true;
		} else if (bote.getTipo().equals("Individual")) {
			inflater.inflate(R.menu.bote_consulta_individual, menu);
			return true;
		}
		return false;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.menu_bote_consulta_persona_nueva:
			addPersonaNueva();
			return true;
		case R.id.menu_bote_consulta_persona_desde_contacto:
			// pickContact();
			doLaunchContactPicker();
			return true;
		case R.id.menu_bote_consulta_movimiento_ingresar:
			addMovimientoNuevo("Ingresar");
			return true;
		case R.id.menu_bote_consulta_movimiento_retirar:
			addMovimientoNuevo("Retirar");
			return true;
		case R.id.menu_bote_consulta_movimiento_devolver:
			addMovimientoNuevo("Devolver");
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// Make sure the request was successful
		if (resultCode == RESULT_OK) {
			// Nueva
			if (requestCode == CONTACT_PICKER_RESULT) {
				Uri result = data.getData();
				// get the contact id from the Uri
				String id = result.getLastPathSegment();
				// query for everything email
				// Cursor cursor = getContentResolver().query(Email.CONTENT_URI,
				// null, Email.CONTACT_ID + "=?", new String[] { id },
				// null);
				Cursor cursor = getContentResolver().query(
						Contacts.CONTENT_URI, null, Contacts._ID + "=?",
						new String[] { id }, null);
				cursor.moveToFirst();

				String name = cursor
						.getString(cursor
								.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
				// String idContacto = cursor.getString(cursor
				// .getColumnIndex(ContactsContract.Data.CONTACT_ID));
				String idContacto = cursor.getString(cursor
						.getColumnIndex(Contacts._ID));
				cursor.close();
				Persona persona = new Persona(-1, idContacto, bote.getId(), -1,
						name, 0, bote.getTotal(), -1);
				GuardarPersona(persona);
				PersonasConsultaFragment fPersonas = (PersonasConsultaFragment) fragments
						.get(1).getFragmento();
				if (fPersonas != null && fPersonas.mListaPersonas != null
						&& fPersonas.mAdaptadorPersonas != null) {
					fPersonas.mListaPersonas.add(persona);
					fPersonas.mAdaptadorPersonas.notifyDataSetChanged();
				}
				mViewPager.setCurrentItem(1);
			}
			// Check which request we're responding to
			// Antigua
			if (requestCode == PICK_CONTACT_REQUEST) {
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
				PersonasConsultaFragment fPersonas = (PersonasConsultaFragment) fragments
						.get(1).getFragmento();
				if (fPersonas != null && fPersonas.mListaPersonas != null
						&& fPersonas.mAdaptadorPersonas != null) {
					fPersonas.mListaPersonas.add(persona);
					fPersonas.mAdaptadorPersonas.notifyDataSetChanged();
				}
				mViewPager.setCurrentItem(1);
			}
		}
	}

	/**
	 * Inicializa el ViewPager
	 */
	private void intialiseViewPager() {

		fragments = new Vector<Fragmento>();
		// Información
		Fragment fInfoAux = Fragment.instantiate(this,
				BoteConsultaFragment.class.getName());
		Bundle argsInfo = new Bundle();
		argsInfo.putParcelable(ARG_BOTE, bote);
		fInfoAux.setArguments(argsInfo);
		Fragmento fInfo = new Fragmento("Información", fInfoAux);
		fragments.add(fInfo);

		// Personas
		if (bote.getTipo().equals("Colectivo")) {
			Fragment fPersonasAux = Fragment.instantiate(this,
					PersonasConsultaFragment.class.getName());
			Bundle argsPersonas = new Bundle();
			argsPersonas.putParcelable(ARG_BOTE, bote);
			fPersonasAux.setArguments(argsPersonas);
			Fragmento fPersonas = new Fragmento("Personas", fPersonasAux);
			fragments.add(fPersonas);
		}

		// Movimientos
		Fragment fMovimientosAux = Fragment.instantiate(this,
				MovimientosConsultaFragment.class.getName());
		Bundle argsMovimientos = new Bundle();
		argsMovimientos.putParcelable(ARG_BOTE, bote);
		fMovimientosAux.setArguments(argsMovimientos);
		Fragmento fMovimientos = new Fragmento("Movimientos", fMovimientosAux);
		fragments.add(fMovimientos);

		this.mPagerAdapter = new TabPagerAdapter(
				super.getSupportFragmentManager(), fragments);
		//
		this.mViewPager = (ViewPager) super
				.findViewById(R.id.bote_consulta_viewpager);
		this.mViewPager.setAdapter(this.mPagerAdapter);
		this.mViewPager.setOnPageChangeListener(this);
	}

	public void addMovimientoNuevo(String tipo) {
		NuevoMovimientoDialog nuevoMovimientoDialog = new NuevoMovimientoDialog(
				this);
		nuevoMovimientoDialog.bote = bote;
		nuevoMovimientoDialog.tipo = tipo;
		nuevoMovimientoDialog.setOnDismissListener(onDismissListenerMovimiento);
		nuevoMovimientoDialog.show();
	}

	public void addPersonaNueva() {
		NuevaPersonaDialog nuevaPersonaDialog = new NuevaPersonaDialog(this);
		nuevaPersonaDialog.idBote = bote.getId();
		nuevaPersonaDialog.setOnDismissListener(onDismissListenerPersona);
		nuevaPersonaDialog.show();
	}

	@SuppressWarnings("unused")
	private void pickContact() {
		Uri uri = Uri.parse("content://contacts");
		Intent pickContactIntent = new Intent(Intent.ACTION_PICK, uri);
		pickContactIntent.setType(Phone.CONTENT_TYPE);
		startActivityForResult(pickContactIntent, PICK_CONTACT_REQUEST);
	}

	public void doLaunchContactPicker() {
		Intent contactPickerIntent = new Intent(Intent.ACTION_PICK,
				Contacts.CONTENT_URI);
		startActivityForResult(contactPickerIntent, CONTACT_PICKER_RESULT);
	}

	private DialogInterface.OnDismissListener onDismissListenerPersona = new DialogInterface.OnDismissListener() {

		public void onDismiss(DialogInterface dialog) {
			onNuevaPersonaDialogDismiss(dialog);
		}
	};

	public void onNuevaPersonaDialogDismiss(DialogInterface arg0) {
		PersonasConsultaFragment fPersonas = (PersonasConsultaFragment) fragments
				.get(1).getFragmento();
		if (fPersonas != null && fPersonas.mAdaptadorPersonas != null) {
			fPersonas.mAdaptadorPersonas.clear();
			List<Persona> lPersona = Persona.getAllPersonasBote(this,
					bote.getId());
			if (lPersona != null && lPersona.size() > 0)
				for (int i = 0; i < lPersona.size(); i++)
					fPersonas.mAdaptadorPersonas.add(lPersona.get(i));
			if (!((NuevaPersonaDialog) arg0).cancelado)
				mViewPager.setCurrentItem(1);
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.support.v4.view.ViewPager.OnPageChangeListener#onPageScrolled
	 * (int, float, int)
	 */
	public void onPageScrolled(int position, float positionOffset,
			int positionOffsetPixels) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.support.v4.view.ViewPager.OnPageChangeListener#onPageSelected
	 * (int)
	 */
	public void onPageSelected(int position) {
		currentPage = position;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.view.ViewPager.OnPageChangeListener#
	 * onPageScrollStateChanged(int)
	 */
	// @Override
	public void onPageScrollStateChanged(int state) {
	}

	private DialogInterface.OnDismissListener onDismissListenerMovimiento = new DialogInterface.OnDismissListener() {

		public void onDismiss(DialogInterface dialog) {
			onNuevoMovimientoCreatedListener();
		}
	};

	public void onNuevoMovimientoCreatedListener() {
		RefrescarDatos();
	}

	public void onMovimientoActualizadoListener() {
		RefrescarDatos();
	}

	public void RefrescarDatos() {
		int i = 0;
		// Carga el bote
		BoteConsultaFragment fBote = (BoteConsultaFragment) fragments.get(i)
				.getFragmento();
		if (fBote != null) {
			Bote boteActual = Bote.getBote(this, bote.getId());
			fBote.cargarDatos(boteActual);
			Persona.bote = boteActual;
		}
		i++;
		if (bote.getTipo().equals("Colectivo")) {
			// Carga las personas
			PersonasConsultaFragment fPersonas = (PersonasConsultaFragment) fragments
					.get(i).getFragmento();
			if (fPersonas != null && fPersonas.mAdaptadorPersonas != null) {
				fPersonas.mAdaptadorPersonas.clear();
				List<Persona> lPersona = Persona.getAllPersonasBote(this,
						bote.getId());
				if (lPersona != null && lPersona.size() > 0)
					for (int j = 0; j < lPersona.size(); j++)
						fPersonas.mAdaptadorPersonas.add(lPersona.get(j));
			}
			i++;
		}
		// Carga los movimientos
		MovimientosConsultaFragment fMovimientos = (MovimientosConsultaFragment) fragments
				.get(i).getFragmento();
		if (fMovimientos != null && fMovimientos.mAdaptadorMovimientos != null) {
			fMovimientos.mAdaptadorMovimientos.clear();
			List<Movimiento> lMovimiento = Movimiento.getAllMovimientosBote(
					this, bote.getId());
			if (lMovimiento != null && lMovimiento.size() > 0)
				for (int j = 0; j < lMovimiento.size(); j++)
					fMovimientos.mAdaptadorMovimientos.add(lMovimiento.get(j));
		}
	}
}
