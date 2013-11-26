package com.aizpun.botebeta.cliente;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.aizpun.botebeta.R;
import com.aizpun.botebeta.negocio.Bote;
import com.aizpun.botebeta.negocio.BoteAdapter;

/**
 * Fragmento consulta de botes
 * 
 * @author Jorge
 * 
 */
public class BotesConsultaActivity extends Activity {

	public final static String ARG_POSICION_ACTUAL = "posición actual";
	public final static String ARG_LISTA_BOTES = "lista botes";
	private static final int VIEW_BOTE_REQUEST = 1;
	private static final int ADD_BOTE_REQUEST = 2;
	// OnCabeceraBoteSeleccionadaListener mCallback;
	int mPosicionActual = -1;
	List<Bote> mListaBotes = new ArrayList<Bote>();
	BoteAdapter mAdaptadorBotes = null;

	// // La actividad contenedora tiene que implementar esta interfaz para que
	// el
	// // fragmento pueda entregar mensajes
	// public interface OnCabeceraBoteSeleccionadaListener {
	// /**
	// * Lo llama BotesConsultaFragment cuando un elemento de la lista es
	// * seleccionado
	// */
	// public void onBoteSelected(Bote bote);
	// }

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.botes_consulta);

		// Necesitamos usar un disposición de elemento de lista (list item
		// layout) diferente para dispositivos anteriores a Honeycomb
		// = Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ?
		// android.R.layout.simple_list_item_activated_1 :
		// android.R.layout.simple_list_item_1;

		if (savedInstanceState != null) {
			mPosicionActual = savedInstanceState.getInt(ARG_POSICION_ACTUAL);
			mListaBotes = savedInstanceState
					.getParcelableArrayList(ARG_LISTA_BOTES);
		} else {
			// // Elimina la base de datos
			// BDBotes_SQLiteHelper.EliminarBasedeDatos(this);

			mListaBotes = Bote.getAllBotes(this);
			if (mListaBotes != null)
				mPosicionActual = mListaBotes.size() - 1;
			else
				mPosicionActual = -1;
		}

		mAdaptadorBotes = new BoteAdapter(this,
				R.layout.botes_consulta_elemento_lista, mListaBotes);
		// Crea una adaptador de array para la vista de lista, usando la lista
		// de botes
		if (mAdaptadorBotes != null
				&& findViewById(R.id.list_consulta_botes) != null) {
			ListView lista = (ListView) findViewById(R.id.list_consulta_botes);
			lista.setAdapter(mAdaptadorBotes);
			lista.setOnItemClickListener(mOnItemClickListener);
			registerForContextMenu(lista);
		}

		// ((Button) vista.findViewById(R.id.boton_add_bote))
		// .setOnClickListener(mOnClick_addBote_Listener);
		// ((Button) vista.findViewById(R.id.boton_remove_bote))
		// .setOnClickListener(mOnClick_removeBote_Listener);
	}

	// @Override
	// public void onStart() {
	// super.onStart();
	//
	// // Cuando estamos en el layout de dos paneles, se selecciona el elemento
	// // de la listview (Esto se hace durante el onStart porque en este
	// // momento la
	// // listview está disponible.)
	// if (getFragmentManager().findFragmentById(R.id.bote_consulta_fragment) !=
	// null) {
	// View vista = (View) getFragmentManager().findFragmentById(
	// R.id.botes_consulta_fragment).getView();
	// if (vista != null && vista.findViewById(R.id.list_consulta_botes) !=
	// null) {
	// ListView lista = (ListView) vista
	// .findViewById(R.id.list_consulta_botes);
	// lista.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
	// if (mPosicionActual != -1)
	// lista.setItemChecked(mPosicionActual, true);
	// }
	// }
	// }

	// @Override
	// public void onAttach(Activity actividad) {
	// super.onAttach(actividad);
	//
	// // Esto asegura que la actividad contenedora implementa
	// // el interfaz callback. Si no, lanza una excepción.
	// try {
	// mCallback = (OnCabeceraBoteSeleccionadaListener) actividad;
	// } catch (ClassCastException e) {
	// throw new ClassCastException(
	// actividad.toString()
	// + " tiene que implementar OnCabeceraBoteSeleccionadaListener");
	// }
	// }

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		// Guarda los elementos del bote en caso de que sea necesario recrear el
		// fragmento
		outState.putInt(ARG_POSICION_ACTUAL, mPosicionActual);
		outState.putParcelableArrayList(ARG_LISTA_BOTES,
				(ArrayList<Bote>) mListaBotes);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.botes_consulta, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.menu_botes_consulta_add:
			addBote();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
		mPosicionActual = info.position;
		menu.setHeaderTitle(mListaBotes.get(mPosicionActual).getNombre());
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.botes_consulta_elemento_lista_menu, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_botes_consulta_elemento_lista_remove_bote:
			removeBote();
			return true;
		case R.id.menu_botes_consulta_elemento_lista_edit_bote:
			editBote();
			return true;
		default:
			return super.onContextItemSelected(item);
		}
	}

	// private OnClickListener mOnClick_addBote_Listener = new OnClickListener()
	// {
	//
	// public void onClick(View v) {
	// addBote(v);
	// }
	// };

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if ((requestCode == ADD_BOTE_REQUEST && resultCode == Activity.RESULT_OK)
				|| requestCode == VIEW_BOTE_REQUEST) {
			mListaBotes.clear();
			mListaBotes.addAll(Bote.getAllBotes(this));
			if (mListaBotes != null)
				mPosicionActual = mListaBotes.size() - 1;
			else
				mPosicionActual = -1;
			mAdaptadorBotes.notifyDataSetChanged();
		}
	}

	public void addBote() {
		if (mListaBotes != null) {

			// Bote bote = new Bote(1, 0, "Bote " + (mListaBotes.size() + 1),
			// 200,
			// true, "normal", 1, new Date(), null);
			// Bote bote = null;
			Intent intent = new Intent(this, BoteMantenimientoActivity.class);
			startActivityForResult(intent, ADD_BOTE_REQUEST);
			/*
			 * if (bote != null) { BoteDataSource datasource = new
			 * BoteDataSource(v.getContext()); datasource.open(); if
			 * (datasource.createBote(bote)) { mListaBotes.add(bote); Context
			 * context = v.getContext().getApplicationContext(); CharSequence
			 * text = getString(R.string.bote_inserted); int duration =
			 * Toast.LENGTH_SHORT; Toast toast = Toast.makeText(context, text,
			 * duration); toast.show(); } datasource.close(); // ListView lista
			 * = (ListView) findViewById(R.id.lista_botes); //
			 * ArrayAdapter<String> adaptador = new //
			 * ArrayAdapter<String>(this, // R.layout.lista, mBotes); //
			 * lista.setAdapter(adaptador); // lista.setTextFilterEnabled(true);
			 * 
			 * // ListView lista = (ListView) v.findViewById(R.id.lista_botes);
			 * // if (lista != null) // lista.setAdapter(mAdaptadorBotes);
			 * mAdaptadorBotes.notifyDataSetChanged(); }
			 */
		}
	}

	public void editBote() {
		if (mListaBotes != null) {

			// Bote bote = new Bote(1, 0, "Bote " + (mListaBotes.size() + 1),
			// 200,
			// true, "normal", 1, new Date(), null);
			// Bote bote = null;
			Intent intent = new Intent(this, BoteMantenimientoActivity.class);
			intent.putExtra(BoteMantenimientoActivity.MODO_EDICION, true);
			intent.putExtra(BoteMantenimientoActivity.BOTE,
					mListaBotes.get(mPosicionActual));
			startActivityForResult(intent, ADD_BOTE_REQUEST);
			/*
			 * if (bote != null) { BoteDataSource datasource = new
			 * BoteDataSource(v.getContext()); datasource.open(); if
			 * (datasource.createBote(bote)) { mListaBotes.add(bote); Context
			 * context = v.getContext().getApplicationContext(); CharSequence
			 * text = getString(R.string.bote_inserted); int duration =
			 * Toast.LENGTH_SHORT; Toast toast = Toast.makeText(context, text,
			 * duration); toast.show(); } datasource.close(); // ListView lista
			 * = (ListView) findViewById(R.id.lista_botes); //
			 * ArrayAdapter<String> adaptador = new //
			 * ArrayAdapter<String>(this, // R.layout.lista, mBotes); //
			 * lista.setAdapter(adaptador); // lista.setTextFilterEnabled(true);
			 * 
			 * // ListView lista = (ListView) v.findViewById(R.id.lista_botes);
			 * // if (lista != null) // lista.setAdapter(mAdaptadorBotes);
			 * mAdaptadorBotes.notifyDataSetChanged(); }
			 */
		}
	}

	// private OnClickListener mOnClick_removeBote_Listener = new
	// OnClickListener() {
	//
	// public void onClick(View v) {
	// removeBote(v);
	// }
	// };

	public void removeBote() {
		if (mListaBotes != null && mListaBotes.size() > 0) {
			if (Bote.DeleteBote(this, mListaBotes.get(mPosicionActual))) {
				mListaBotes.remove(mPosicionActual);
				CharSequence text = getString(R.string.bote_deleted);
				int duration = Toast.LENGTH_SHORT;
				Toast toast = Toast.makeText(this, text, duration);
				toast.show();
			}
			// ListView lista = (ListView) findViewById(R.id.lista_botes);
			// ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this,
			// R.layout.lista, mBotes);
			// lista.setAdapter(adaptador);
			// lista.setTextFilterEnabled(true);

			// ListView lista = (ListView) v.findViewById(R.id.lista_botes);
			// if (lista != null)
			// lista.setAdapter(mAdaptadorBotes);
			mAdaptadorBotes.notifyDataSetChanged();
		}
	}

	private OnItemClickListener mOnItemClickListener = new OnItemClickListener() {
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			listaClick(arg1, arg2, arg3);
		}
	};

	public void listaClick(View v, int arg2, long arg3) {
		ListView lista = (ListView) v.getParent();
		if (lista != null) {
			mPosicionActual = arg2;
			/*
			 * Intent intent = new Intent(this, BoteConsultaActivity.class);
			 * intent.putExtra(BoteConsultaActivity.ARG_BOTE,
			 * mListaBotes.get(mPosicionActual)); startActivityForResult(intent,
			 * VIEW_BOTE_REQUEST); lista.setItemChecked(mPosicionActual, true);
			 */

			// Prueba
			Intent intent = new Intent(this,
					BoteConsultaTabsViewPagerFragmentActivity.class);
			intent.putExtra(BoteConsultaActivity.ARG_BOTE,
					mListaBotes.get(mPosicionActual));
			startActivityForResult(intent, VIEW_BOTE_REQUEST);
		}
	}
}