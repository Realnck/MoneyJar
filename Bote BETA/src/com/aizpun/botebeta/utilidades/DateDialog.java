package com.aizpun.botebeta.utilidades;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.aizpun.botebeta.R;

/**
 * Diálogo que permite seleccionar una fecha
 * 
 * @author Jorge
 * 
 */
public class DateDialog extends Dialog {
	private final String TAG = "DateDialog";
	public TextView textView = null;
	public Calendar fecha = Calendar.getInstance();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(getContext().getString(R.string.definir_fecha));
		setContentView(R.layout.date_dialog);
		Button btnAceptar = (Button) findViewById(R.id.btn_date_dialog_definir);
		btnAceptar.setOnClickListener(mOnClick_definir_Listener);
		Button btnCancelar = (Button) findViewById(R.id.btn_date_dialog_cancelar);
		btnCancelar.setOnClickListener(mOnClick_cancelar_Listener);
	}

	@Override
	public void show() {
		super.show();
		actualizarValor();
	}

	public DateDialog(Context context, boolean cancelable,
			OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
	}

	public DateDialog(Context context, int theme) {
		super(context, theme);
	}

	public DateDialog(Context context) {
		super(context);
	}

	private View.OnClickListener mOnClick_definir_Listener = new View.OnClickListener() {
		public void onClick(View v) {
			definir();
		}
	};

	private View.OnClickListener mOnClick_cancelar_Listener = new View.OnClickListener() {
		public void onClick(View v) {
			cancel();
		}
	};

	/**
	 * Guarda el valor introducido
	 */
	public void definir() {
		try {
			if (textView != null) {
				CustomDatePicker editor = (CustomDatePicker) findViewById(R.id.ed_date_dialog);
				if (fecha == null)
					fecha = Calendar.getInstance();
				fecha.set(Calendar.YEAR, editor.getYear());
				fecha.set(Calendar.MONTH, editor.getMonth());
				fecha.set(Calendar.DAY_OF_MONTH, editor.getDayOfMonth());
				DateFormat df = SimpleDateFormat.getDateInstance(
						SimpleDateFormat.MEDIUM, Locale.getDefault());
				textView.setText(df.format(fecha.getTime()));
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
			CustomDatePicker editor = (CustomDatePicker) findViewById(R.id.ed_date_dialog);
			if (fecha == null) {
				Calendar fechaAux = Calendar.getInstance();
				editor.updateDate(fechaAux.get(Calendar.YEAR),
						fechaAux.get(Calendar.MONTH),
						fechaAux.get(Calendar.DAY_OF_MONTH));
			} else
				editor.updateDate(fecha.get(Calendar.YEAR),
						fecha.get(Calendar.MONTH),
						fecha.get(Calendar.DAY_OF_MONTH));
		} catch (Exception ex) {
			Log.e(TAG, "Ha ocurrido un error al actualizar la fecha", ex);
		}
	}
}
