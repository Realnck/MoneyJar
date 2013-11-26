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

import com.aizpun.botebeta.R;

/**
 * Diálogo que permite introducir un número decimal
 * 
 * @author Jorge
 * 
 */
public class DecimalDialog extends Dialog {
	private final String TAG = "DecimalDialog";
	public TextView textView = null;
	public Double valor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(getContext().getString(R.string.introduce_valor));
		setContentView(R.layout.decimal_dialog);
		EditText editText = (EditText) findViewById(R.id.ed_decimal_dialog);
		editText.setOnFocusChangeListener(mOnFocusChangeListener);
		actualizarValor();
		Button btnAceptar = (Button) findViewById(R.id.btn_decimal_dialog_aceptar);
		btnAceptar.setOnClickListener(mOnClick_aceptar_Listener);
		Button btnCancelar = (Button) findViewById(R.id.btn_decimal_dialog_cancelar);
		btnCancelar.setOnClickListener(mOnClick_cancelar_Listener);
	}

	@Override
	public void show() {
		super.show();
		actualizarValor();
		EditText editText = (EditText) findViewById(R.id.ed_decimal_dialog);
		editText.selectAll();
	}

	public DecimalDialog(Context context, boolean cancelable,
			OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
	}

	public DecimalDialog(Context context, int theme) {
		super(context, theme);
	}

	public DecimalDialog(Context context) {
		super(context);
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
	 * Guarda el valor introducido
	 */
	public void aceptar() {
		try {
			if (textView != null) {
				EditText editor = (EditText) findViewById(R.id.ed_decimal_dialog);
				String s = editor.getText().toString();
				if (s != null && !s.equals("")) {
					valor = Double.parseDouble(s);
					textView.setText(Utilidades.formatearDouble(valor));
				} else {
					valor = null;
					textView.setText("");
				}
			}
		} catch (Exception ex) {
			Log.e(TAG, "Ha ocurrido un error al guardar el valor", ex);
		}
		dismiss();
	}

	/**
	 * Actualiza el valor del editor
	 */
	public void actualizarValor() {
		try {
			EditText editor = (EditText) findViewById(R.id.ed_decimal_dialog);
			if (valor != null)
				editor.setText(String.valueOf(valor));
			else
				editor.setText(String.valueOf(""));
		} catch (Exception ex) {
			Log.e(TAG, "Ha ocurrido un error al actualizar el valor", ex);
		}
	}
}
