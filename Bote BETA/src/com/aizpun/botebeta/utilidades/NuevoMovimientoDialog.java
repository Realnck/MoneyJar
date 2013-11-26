package com.aizpun.botebeta.utilidades;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.aizpun.botebeta.R;
import com.aizpun.botebeta.datos.MovimientoDataSource;
import com.aizpun.botebeta.negocio.Bote;
import com.aizpun.botebeta.negocio.Movimiento;
import com.aizpun.botebeta.negocio.Persona;
import com.aizpun.botebeta.negocio.PersonaSpinnerAdapter;

/**
 * Diálogo que permite añadir un movimiento
 * 
 * @author Jorge
 * 
 */
public class NuevoMovimientoDialog extends Dialog {
	public final static String ARG_BOTE = "bote";
	private final String TAG = "NuevoMovimientoDialog";
	public Bote bote = null;
	public List<Persona> mListaPersonas = new ArrayList<Persona>();
	private Persona[] mPersonas;
	public String tipo = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setTitle(getContext().getString(R.string.titulo_add_movimiento));
		setTitle(tipo);
		setContentView(R.layout.nuevo_movimiento_dialog);
		EditText editText = (EditText) findViewById(R.id.ed_nuevo_movimiento_dialog_importe);
		editText.setOnFocusChangeListener(mOnFocusChangeListener);
		Button btnAceptar = (Button) findViewById(R.id.btn_nuevo_movimiento_dialog_aceptar);
		btnAceptar.setOnClickListener(mOnClick_aceptar_Listener);
		Button btnCancelar = (Button) findViewById(R.id.btn_nuevo_movimiento_dialog_cancelar);
		btnCancelar.setOnClickListener(mOnClick_cancelar_Listener);

		if (bote != null) {
			// Asigna el adaptador de tipos al Spinner
			cargarDatos(bote);
			// Spinner spinner = (Spinner)
			// findViewById(R.id.spin_nuevo_movimiento_dialog_persona);
			// ArrayAdapter<CharSequence> adapter = new
			// ArrayAdapter<CharSequence>(
			// getContext(), R.layout.editor, mPersonas);
			// adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			// spinner.setAdapter(adapter);
			Spinner spinner = (Spinner) findViewById(R.id.spin_nuevo_movimiento_dialog_persona);
			PersonaSpinnerAdapter adapter = new PersonaSpinnerAdapter(
					getContext(), R.layout.personas_consulta_elemento_lista,
					mPersonas);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinner.setAdapter(adapter);
			LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linear_layout_nuevo_movimiento_dialog_persona);
			linearLayout.setOnClickListener(mOnClick_Personas_Listener);
		}
	}

	@Override
	public void show() {
		super.show();
	}

	@Override
	public void dismiss() {
		super.dismiss();
		//
		// Workaround for Spinner's dialog leaking current window during
		// rotation
		// See issue #4936 :
		// http://code.google.com/p/android/issues/detail?id=4936
		//
		LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linear_layout_nuevo_movimiento_dialog_persona);
		Spinner spinner = (Spinner) findViewById(R.id.spin_nuevo_movimiento_dialog_persona);
		linearLayout.removeView(spinner);
	}

	public NuevoMovimientoDialog(Context context, boolean cancelable,
			OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
	}

	public NuevoMovimientoDialog(Context context, int theme) {
		super(context, theme);
	}

	public NuevoMovimientoDialog(Context context) {
		super(context);
	}

	public void cargarDatos(Bote b) {
		if (b != null) {
			bote = b;
			mListaPersonas = Persona.getAllPersonasBote(getContext(),
					bote.getId());
			// mPersonas = new String[mListaPersonas.size()];
			// for (int i = 0; i < mListaPersonas.size(); i++)
			// mPersonas[i] = mListaPersonas.get(i).getNombre();

			mPersonas = new Persona[mListaPersonas.size() + 1];
			mPersonas[0] = new Persona(-1, "-1", bote.getId(), -1, "Ninguna",
					0, bote.getTotal(), -1);
			for (int i = 1; i < mListaPersonas.size() + 1; i++)
				mPersonas[i] = mListaPersonas.get(i - 1);
		}
	}

	private View.OnClickListener mOnClick_Personas_Listener = new View.OnClickListener() {
		public void onClick(View v) {
			showPersonas(v);
		}
	};

	public void showPersonas(View v) {
		Spinner spinner = (Spinner) v
				.findViewById(R.id.spin_nuevo_movimiento_dialog_persona);
		spinner.performClick();
	}

	private View.OnClickListener mOnClick_aceptar_Listener = new View.OnClickListener() {
		public void onClick(View v) {
			aceptar();
		}
	};

	private View.OnClickListener mOnClick_cancelar_Listener = new View.OnClickListener() {
		public void onClick(View v) {
			cancel();
		}
	};

	private View.OnFocusChangeListener mOnFocusChangeListener = new View.OnFocusChangeListener() {
		public void onFocusChange(View v, boolean hasFocus) {
			if (hasFocus) {
				getWindow()
						.setSoftInputMode(
								WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
			}
		}
	};

	/**
	 * Guarda el movimiento
	 */
	public void aceptar() {
		try {
			TextView txtImporte = (TextView) findViewById(R.id.ed_nuevo_movimiento_dialog_importe);
			String importe = txtImporte.getText().toString();
			if (importe != null && !importe.equals("")) {
				// RadioGroup radioGroup = (RadioGroup)
				// findViewById(R.id.radiogroup_nuevo_movimiento_dialog);
				// int radioButtonID = radioGroup.getCheckedRadioButtonId();
				// View radioButton = radioGroup.findViewById(radioButtonID);
				// int idx = radioGroup.indexOfChild(radioButton);
				// String tipo;
				Double imp;
				if (tipo.compareTo("Retirar") == 0
						|| tipo.compareTo("Devolver") == 0) { // if (idx == 1) {
					// tipo = "Retirar";
					imp = 0 - Double.parseDouble(importe);
				} else {
					// tipo = "Ingresar";
					imp = Double.parseDouble(importe);
				}
				TextView txtDescripcion = (TextView) findViewById(R.id.ed_nuevo_movimiento_dialog_concepto);
				String descripcion = txtDescripcion.getText().toString();

				if (descripcion == null || descripcion.equals(""))
					descripcion = getContext().getString(
							R.string.sin_descripcion);
				Spinner spinner = (Spinner) findViewById(R.id.spin_nuevo_movimiento_dialog_persona);
				int posPersona = spinner.getSelectedItemPosition();
				long personaID;
				if (posPersona == 0)
					personaID = -1;
				else
					personaID = mListaPersonas.get(posPersona - 1).getId();
				Movimiento movimiento = new Movimiento(-1, bote.getId(),
						personaID, -1, descripcion, tipo, imp, new Date());
				GuardarMovimiento(movimiento);
			}
		} catch (Exception ex) {
			Log.e(TAG, "Ha ocurrido un error al guardar el movimiento", ex);
		}
		dismiss();
	}

	/**
	 * Guarda un movimiento en la base de datos
	 * 
	 * @param movimiento
	 *            Movimiento
	 */
	private void GuardarMovimiento(Movimiento movimiento) {
		MovimientoDataSource datasource = new MovimientoDataSource(getContext());
		datasource.open();
		if (datasource.createMovimiento(movimiento)) {
			Context context = getContext().getApplicationContext();
			CharSequence text = getContext().getString(
					R.string.movimiento_inserted);
			int duration = Toast.LENGTH_SHORT;
			Toast toast = Toast.makeText(context, text, duration);
			toast.show();
		}
		datasource.close();
	}
}
