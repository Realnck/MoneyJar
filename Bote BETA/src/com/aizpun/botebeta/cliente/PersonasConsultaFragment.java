package com.aizpun.botebeta.cliente;

import java.util.ArrayList;
import java.util.List;

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
import com.aizpun.botebeta.negocio.Persona;
import com.aizpun.botebeta.negocio.PersonaAdapter;

/**
 * Fragmento consulta de Personas
 * 
 * @author Jorge
 * 
 */
public class PersonasConsultaFragment extends Fragment {
	public final static String ARG_BOTE = "bote";
	public final static String POSICION_PERSONA = "posicion persona";
	public final static String LISTA_PERSONAS = "lista de personas";
	Bote bote = null;
	int mPosicionPersonaActual = -1;
	public List<Persona> mListaPersonas = new ArrayList<Persona>();
	public PersonaAdapter mAdaptadorPersonas = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		if (savedInstanceState != null) {
			bote = savedInstanceState.getParcelable(ARG_BOTE);
			mPosicionPersonaActual = savedInstanceState
					.getInt(POSICION_PERSONA);
			mListaPersonas = savedInstanceState
					.getParcelableArrayList(LISTA_PERSONAS);
		} else {
			Bundle args = getArguments();
			if (args != null)
				bote = (Bote) args.getParcelable(ARG_BOTE);
		}
		if (bote != null)
			bote = Bote.getBote(getActivity(), bote.getId());
		return inflater.inflate(R.layout.personas_consulta, container, false);
	}

	@Override
	public void onStart() {
		super.onStart();
		cargarDatos(bote);

		mAdaptadorPersonas = new PersonaAdapter(getActivity(),
				R.layout.personas_consulta_elemento_lista, mListaPersonas);
		// Crea una adaptador de array para la vista de lista, usando la lista
		// de Personas
		if (mAdaptadorPersonas != null
				&& getView().findViewById(R.id.list_consulta_personas) != null) {
			ListView lista = (ListView) getView().findViewById(
					R.id.list_consulta_personas);
			lista.setAdapter(mAdaptadorPersonas);
			// lista.setOnItemClickListener(mOnItemClickListener);
			registerForContextMenu(lista);
		}
	}

	public void cargarDatos(Bote b) {
		if (b != null) {
			bote = b;
			mListaPersonas = Persona.getAllPersonasBote(getActivity(),
					bote.getId());
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		// Guarda los elementos del bote en caso de que sea necesario recrear el
		// fragmento
		outState.putParcelable(ARG_BOTE, bote);
		outState.putInt(POSICION_PERSONA, mPosicionPersonaActual);
		outState.putParcelableArrayList(LISTA_PERSONAS,
				(ArrayList<Persona>) mListaPersonas);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		if (v.getId() == R.id.list_consulta_personas) {
			AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
			mPosicionPersonaActual = info.position;
			menu.setHeaderTitle(mListaPersonas.get(mPosicionPersonaActual)
					.getNombre());
			super.onCreateContextMenu(menu, v, menuInfo);
			MenuInflater inflater = getActivity().getMenuInflater();
			inflater.inflate(R.menu.personas_consulta_elemento_lista_menu, menu);
		}
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

	public void removePersona() {
		if (mListaPersonas != null && mListaPersonas.size() > 0) {
			if (Persona.DeletePersona(getActivity(),
					mListaPersonas.get(mPosicionPersonaActual))) {
				mListaPersonas.remove(mPosicionPersonaActual);
				CharSequence text = getString(R.string.persona_deleted);
				int duration = Toast.LENGTH_SHORT;
				Toast toast = Toast.makeText(getActivity(), text, duration);
				toast.show();
			}
			mAdaptadorPersonas.notifyDataSetChanged();
		}
	}
}