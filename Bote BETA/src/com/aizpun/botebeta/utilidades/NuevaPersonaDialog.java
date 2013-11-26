package com.aizpun.botebeta.utilidades;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.aizpun.botebeta.R;
import com.aizpun.botebeta.datos.PersonaDataSource;
import com.aizpun.botebeta.negocio.Persona;

/**
 * Diálogo que permite añadir una persona
 * 
 * @author Jorge
 * 
 */
public class NuevaPersonaDialog extends Dialog {
	private final String TAG = "NuevaPersonaDialog";
	public long idBote;
	public Boolean cancelado = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(getContext().getString(R.string.nueva_persona));
		setContentView(R.layout.nueva_persona_dialog);
		EditText editText = (EditText) findViewById(R.id.ed_nueva_persona_dialog_nombre);
		editText.setOnFocusChangeListener(mOnFocusChangeListener);
		Button btnAceptar = (Button) findViewById(R.id.btn_nueva_persona_dialog_aceptar);
		btnAceptar.setOnClickListener(mOnClick_aceptar_Listener);
		Button btnCancelar = (Button) findViewById(R.id.btn_nueva_persona_dialog_cancelar);
		btnCancelar.setOnClickListener(mOnClick_cancelar_Listener);
	}

	@Override
	public void show() {
		super.show();
	}

	public NuevaPersonaDialog(Context context, boolean cancelable,
			OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
	}

	public NuevaPersonaDialog(Context context, int theme) {
		super(context, theme);
	}

	public NuevaPersonaDialog(Context context) {
		super(context);
	}

	private View.OnClickListener mOnClick_aceptar_Listener = new View.OnClickListener() {
		public void onClick(View v) {
			aceptar();
		}
	};

	private View.OnClickListener mOnClick_cancelar_Listener = new View.OnClickListener() {
		public void onClick(View v) {
			cancelado = true;
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
	 * Guarda la persona
	 */
	public void aceptar() {
		try {
			TextView textView = (TextView) findViewById(R.id.ed_nueva_persona_dialog_nombre);
			String nombre = textView.getText().toString();
			if (nombre == null || nombre.equals(""))
				nombre = getContext().getString(R.string.sin_nombre);
			Persona persona = new Persona(-1, "-1", idBote, -1, nombre, 0, 0,
					-1);
			GuardarPersona(persona);
		} catch (Exception ex) {
			Log.e(TAG, "Ha ocurrido un error al guardar la persona", ex);
		}
		dismiss();
	}

	/**
	 * Guarda una persona en la base de datos
	 * 
	 * @param persona
	 *            Persona
	 */
	private void GuardarPersona(Persona persona) {
		PersonaDataSource datasource = new PersonaDataSource(getContext());
		datasource.open();
		if (datasource.createPersona(persona)) {
			Context context = getContext().getApplicationContext();
			CharSequence text = getContext().getString(
					R.string.persona_inserted);
			int duration = Toast.LENGTH_SHORT;
			Toast toast = Toast.makeText(context, text, duration);
			toast.show();
		}
		datasource.close();
	}
}
