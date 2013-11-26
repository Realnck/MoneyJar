package com.aizpun.botebeta.cliente;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.aizpun.botebeta.R;
import com.aizpun.botebeta.negocio.Bote;
import com.aizpun.botebeta.negocio.Movimiento;
import com.aizpun.botebeta.negocio.MovimientoAdapter;

/**
 * Fragmento consulta de Movimientos
 * 
 * @author Jorge
 * 
 */
public class MovimientosConsultaFragment extends Fragment {
	public final static String ARG_BOTE = "bote";
	public final static String POSICION_MOVIMIENTO = "posicion movimiento";
	public final static String LISTA_MOVIMIENTOS = "lista de movimientos";
	Bote bote = null;
	int mPosicionMovimientoActual = -1;
	public List<Movimiento> mListaMovimientos = new ArrayList<Movimiento>();
	public MovimientoAdapter mAdaptadorMovimientos = null;
	OnMovimientoActualizadoListener mCallback;

	// The container Activity must implement this interface so the frag can
	// deliver messages
	public interface OnMovimientoActualizadoListener {
		public void onMovimientoActualizadoListener();
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		// This makes sure that the container activity has implemented
		// the callback interface. If not, it throws an exception.
		try {
			mCallback = (OnMovimientoActualizadoListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " debe implementar OnMovimientoActualizadoListener");
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		if (savedInstanceState != null) {
			bote = savedInstanceState.getParcelable(ARG_BOTE);
			mPosicionMovimientoActual = savedInstanceState
					.getInt(POSICION_MOVIMIENTO);
			mListaMovimientos = savedInstanceState
					.getParcelableArrayList(LISTA_MOVIMIENTOS);
		} else {
			Bundle args = getArguments();
			if (args != null)
				bote = (Bote) args.getParcelable(ARG_BOTE);
		}
		if (bote != null)
			bote = Bote.getBote(getActivity(), bote.getId());
		return inflater
				.inflate(R.layout.movimientos_consulta, container, false);
	}

	@Override
	public void onStart() {
		super.onStart();
		cargarDatos(bote);

		mAdaptadorMovimientos = new MovimientoAdapter(getActivity(),
				R.layout.movimientos_consulta_elemento_lista, mListaMovimientos);
		// Crea una adaptador de array para la vista de lista, usando la lista
		// de Movimientos
		if (mAdaptadorMovimientos != null
				&& getView().findViewById(R.id.list_consulta_movimientos) != null) {
			ListView lista = (ListView) getView().findViewById(
					R.id.list_consulta_movimientos);
			lista.setAdapter(mAdaptadorMovimientos);
			// lista.setOnItemClickListener(mOnItemClickListener);
			registerForContextMenu(lista);
		}
	}

	public void cargarDatos(Bote b) {
		if (b != null) {
			bote = b;
			mListaMovimientos = Movimiento.getAllMovimientosBote(getActivity(),
					bote.getId());
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		// Guarda los elementos del bote en caso de que sea necesario recrear el
		// fragmento
		outState.putParcelable(ARG_BOTE, bote);
		outState.putInt(POSICION_MOVIMIENTO, mPosicionMovimientoActual);
		outState.putParcelableArrayList(LISTA_MOVIMIENTOS,
				(ArrayList<Movimiento>) mListaMovimientos);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		if (v.getId() == R.id.list_consulta_movimientos) {
			AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
			mPosicionMovimientoActual = info.position;
			menu.setHeaderTitle(mListaMovimientos
					.get(mPosicionMovimientoActual).getDescripcion());
			super.onCreateContextMenu(menu, v, menuInfo);
			MenuInflater inflater = getActivity().getMenuInflater();
			inflater.inflate(R.menu.movimientos_consulta_elemento_lista_menu,
					menu);
		}
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_movimientos_consulta_elemento_lista_remove_movimiento:
			removeMovimiento();
			return true;
			// case
			// R.id.menu_movimientos_consulta_elemento_lista_edit_movimiento:
			// // editMovimiento();
			// return true;
		default:
			return super.onContextItemSelected(item);
		}
	}

	public void removeMovimiento() {
		if (mListaMovimientos != null && mListaMovimientos.size() > 0) {
			if (Movimiento.DeleteMovimiento(getActivity(),
					mListaMovimientos.get(mPosicionMovimientoActual))) {
				mListaMovimientos.remove(mPosicionMovimientoActual);
				CharSequence text = getString(R.string.movimiento_deleted);
				int duration = Toast.LENGTH_SHORT;
				Toast toast = Toast.makeText(getActivity(), text, duration);
				toast.show();
			}
			mAdaptadorMovimientos.notifyDataSetChanged();
		}
		mCallback.onMovimientoActualizadoListener();
	}
}