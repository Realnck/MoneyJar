package com.aizpun.botebeta.cliente;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.aizpun.botebeta.R;
import com.aizpun.botebeta.datos.BoteDataSource;
import com.aizpun.botebeta.negocio.Bote;
import com.aizpun.botebeta.utilidades.DateDialog;
import com.aizpun.botebeta.utilidades.DecimalDialog;
import com.aizpun.botebeta.utilidades.Utilidades;

public class BoteMantenimientoActivity extends FragmentActivity {

	public static final String MODO_EDICION = "edit mode";
	public static final String BOTE = "bote";
	private final String[] mTipos = new String[] { "Individual", "Colectivo" };
	private DecimalDialog dialogoDecimal = null;
	private DateDialog dialogoFecha = null;
	private Bote bote = null;
	private Boolean modoEdicion = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bote_mantenimiento);

		// ActionBar actionBar = getActionBar();
		// if (actionBar != null)
		// actionBar.setDisplayHomeAsUpEnabled(true);

		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

		// Asigna el adaptador de tipos al Spinner
		Spinner spinner = (Spinner) findViewById(R.id.spin_mnt_bote_tipo);
		ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(
				this, R.layout.editor, mTipos);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);

		// Asigna el dialogo decimal
		dialogoDecimal = new DecimalDialog(this);
		dialogoDecimal.textView = (TextView) findViewById(R.id.txt_mnt_bote_importe_tope);

		// Asigna la fecha
		dialogoFecha = new DateDialog(this);
		dialogoFecha.textView = (TextView) findViewById(R.id.txt_mnt_bote_fecha_limite);

		LinearLayout linearLayoutFechaLimite = (LinearLayout) findViewById(R.id.linear_layout_mnt_bote_fecha_limite);
		linearLayoutFechaLimite.setEnabled(false);
		// Importe tope
		LinearLayout linearLayoutImporte = (LinearLayout) findViewById(R.id.linear_layout_mnt_bote_importe_tope);
		linearLayoutImporte.setEnabled(false);

		if (savedInstanceState != null) {
			modoEdicion = savedInstanceState.getBoolean(MODO_EDICION);
			bote = savedInstanceState.getParcelable(BOTE);
			cargarDatos(modoEdicion);
		} else {
			Intent intent = getIntent();
			modoEdicion = intent.getBooleanExtra(MODO_EDICION, false);
			bote = intent.getParcelableExtra(BOTE);
			if (modoEdicion) {
				cargarDatos(modoEdicion);
			}
		}
		// // capture our View elements
		// mDateDisplay = (TextView)
		// findViewById(R.id.txt_mnt_bote_fecha_limite);
		//
		// // add a click listener to the button
		// mDateDisplay.setOnClickListener(new View.OnClickListener() {
		// public void onClick(View v) {
		// // get the current date
		// final Calendar c = Calendar.getInstance();
		// mYear = c.get(Calendar.YEAR);
		// mMonth = c.get(Calendar.MONTH);
		// mDay = c.get(Calendar.DAY_OF_MONTH);
		// DialogFragment dialogo = new DatePickerFragment();
		// ((DatePickerFragment) dialogo).fecha = c.getTime();
		// dialogo.show(getSupportFragmentManager(), "datePicker");
		// TextView textView = (TextView) v
		// .findViewById(R.id.txt_mnt_bote_fecha_limite);
		// if (textView != null
		// && ((DatePickerFragment) dialogo).fecha != null) {
		// textView.setText(((DatePickerFragment) dialogo).fecha
		// .toString());
		// }
		// ;
		// }
		// });
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		// Guarda los elementos del bote en caso de que sea necesario recrear el
		// fragmento
		outState.putBoolean(MODO_EDICION, modoEdicion);
		asignarBote();
		outState.putParcelable(BOTE, bote);
	}

	@Override
	public void onPause() {
		super.onPause();
		if (dialogoDecimal != null && dialogoDecimal.isShowing()) {
			dialogoDecimal.dismiss();
		}
		if (dialogoFecha != null && dialogoFecha.isShowing()) {
			dialogoFecha.dismiss();
		}
	}

	public void cargarDatos(Boolean edicion) {
		DateFormat datef = SimpleDateFormat.getDateInstance(
				SimpleDateFormat.MEDIUM, Locale.getDefault());
		// Título
		setTitle(getString(R.string.title_activity_bote_edit));
		// Nombre
		EditText editTextNombre = (EditText) findViewById(R.id.ed_mnt_bote_nombre);
		editTextNombre.setText(bote.getNombre());
		// Tope
		CheckBox checkEditTope = (CheckBox) findViewById(R.id.check_mnt_bote_tope);
		LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linear_layout_mnt_bote_importe_tope);
		TextView textViewTope = (TextView) findViewById(R.id.txt_mnt_bote_importe_tope);
		if (bote.getTope()) {
			linearLayout.setEnabled(true);
			linearLayout.setClickable(true);
			textViewTope.setHint(R.string.toca_para_editar);
			if (bote.getImporteTope() != -1) {
				// editTextTope.setText(format.format(bote.getImporteTope()));
				textViewTope.setText(Utilidades.formatearDouble(bote
						.getImporteTope()));
				dialogoDecimal.valor = bote.getImporteTope();
				checkEditTope.setChecked(true);
			} else {
				textViewTope.setText("");
				dialogoDecimal.valor = null;
				checkEditTope.setChecked(false);
			}
		} else {
			linearLayout.setEnabled(false);
			textViewTope.setHint(R.string.tope_editor_sin_tope);
			textViewTope.setText("");
			dialogoDecimal.valor = null;
			checkEditTope.setChecked(false);
		}
		// Tipo
		Spinner spinnerTipo = (Spinner) findViewById(R.id.spin_mnt_bote_tipo);
		if (bote.getTipo().equals("Colectivo"))
			spinnerTipo.setSelection(1);
		else
			spinnerTipo.setSelection(0);
		// Fecha límite
		TextView textViewFechaLimite = (TextView) findViewById(R.id.txt_mnt_bote_fecha_limite);
		CheckBox checkBoxFechaLimite = (CheckBox) findViewById(R.id.check_mnt_bote_fecha_limite);
		LinearLayout linearLayoutFechaLimite = (LinearLayout) findViewById(R.id.linear_layout_mnt_bote_fecha_limite);
		if (bote.getFechaLimite() != null
				&& bote.getFechaLimite().getTime() != 0) {
			linearLayoutFechaLimite.setEnabled(true);
			linearLayoutFechaLimite.setClickable(true);
			textViewFechaLimite.setHint(R.string.toca_para_seleccionar);
			textViewFechaLimite.setText(datef.format(bote.getFechaLimite()
					.getTime()));
			checkBoxFechaLimite.setChecked(true);
			dialogoFecha.fecha = Calendar.getInstance();
			dialogoFecha.fecha.setTime(bote.getFechaLimite());
		} else {
			linearLayoutFechaLimite.setEnabled(false);
			textViewFechaLimite
					.setHint(R.string.fecha_limite_editor_sin_limite);
			textViewFechaLimite.setText("");
			checkBoxFechaLimite.setChecked(false);
			dialogoFecha.fecha = null;
		}
		if (edicion) {
			// Total
			TextView textViewTitulo = (TextView) findViewById(R.id.txt_mnt_bote_total_titulo);
			TextView textViewTotal = (TextView) findViewById(R.id.txt_mnt_bote_total);
			textViewTotal.setText(Utilidades.formatearDouble(bote.getTotal()));
			textViewTitulo.setVisibility(View.VISIBLE);
			textViewTotal.setVisibility(View.VISIBLE);
			// Fecha creación
			TextView textViewFechaCreacionTitulo = (TextView) findViewById(R.id.txt_mnt_bote_fecha_creacion_titulo);
			TextView textViewFechaCreacion = (TextView) findViewById(R.id.txt_mnt_bote_fecha_creacion);
			textViewFechaCreacion.setText(datef.format(bote.getFechaCreacion()
					.getTime()));
			textViewFechaCreacionTitulo.setVisibility(View.VISIBLE);
			textViewFechaCreacion.setVisibility(View.VISIBLE);
			// Administrador
			TextView textViewAdministradorTitulo = (TextView) findViewById(R.id.txt_mnt_bote_admnistrador_titulo);
			TextView textViewAdministrador = (TextView) findViewById(R.id.txt_mnt_bote_admnistrador);
			textViewAdministrador.setText("");
			textViewAdministradorTitulo.setVisibility(View.VISIBLE);
			textViewAdministrador.setVisibility(View.VISIBLE);
		}
	}

	public void asignarBote() {
		if (bote == null)
			bote = new Bote();
		// Icono
		bote.setIcono(1);
		// Nombre
		EditText edNombre = (EditText) findViewById(R.id.ed_mnt_bote_nombre);
		String nombre = edNombre.getText().toString();
		if (nombre != null && !nombre.equals(""))
			bote.setNombre(nombre);
		else
			bote.setNombre(getString(R.string.sin_nombre));
		// // Total
		// bote.setTotal(0);
		// Tope
		TextView edImporteTope = (TextView) findViewById(R.id.txt_mnt_bote_importe_tope);
		CheckBox checkEditTope = (CheckBox) findViewById(R.id.check_mnt_bote_tope);
		if (checkEditTope.isChecked()) {
			bote.setTope(true);
			String importeTope = edImporteTope.getText().toString();
			if (importeTope != null && !importeTope.equals("")) {
				bote.setImporteTope(dialogoDecimal.valor);
			} else {
				bote.setTope(false);
				bote.setImporteTope(-1);
			}
		} else {
			bote.setTope(false);
			bote.setImporteTope(-1);
		}
		// Tipo
		Spinner edTipo = (Spinner) findViewById(R.id.spin_mnt_bote_tipo);
		bote.setTipo(edTipo.getSelectedItem().toString());
		// Fecha creación
		bote.setFechaCreacion(new Date());
		// Fecha límite
		CheckBox checkEditFechaLimite = (CheckBox) findViewById(R.id.check_mnt_bote_fecha_limite);
		TextView textViewFechaLimite = (TextView) findViewById(R.id.txt_mnt_bote_fecha_limite);
		if (checkEditFechaLimite.isChecked()) {
			if (textViewFechaLimite.getText() != null
					&& !textViewFechaLimite.getText().equals(""))
				bote.setFechaLimite(dialogoFecha.fecha.getTime());
			else
				bote.setFechaLimite(null);
		} else
			bote.setFechaLimite(null);
		// Administrador
		bote.setAdministrador(-1);
	}

	public void guardarBote(View v) {
		asignarBote();
		BoteDataSource datasource = new BoteDataSource(v.getContext());
		datasource.open();
		if (!modoEdicion) {
			if (datasource.createBote(bote)) {
				Context context = v.getContext().getApplicationContext();
				CharSequence text = getString(R.string.bote_inserted);
				int duration = Toast.LENGTH_SHORT;
				Toast toast = Toast.makeText(context, text, duration);
				toast.show();
			}
		} else {
			if (datasource.updateBote(bote)) {
				Context context = v.getContext().getApplicationContext();
				CharSequence text = getString(R.string.bote_updated);
				int duration = Toast.LENGTH_SHORT;
				Toast toast = Toast.makeText(context, text, duration);
				toast.show();
			}
		}
		datasource.close();
		setResult(Activity.RESULT_OK);
		finish();
	}

	public void cancelar(View v) {
		setResult(Activity.RESULT_CANCELED);
		this.finish();
	}

	public void showDatePickerDialog(View v) {
		TextView textView = (TextView) findViewById(R.id.txt_mnt_bote_fecha_limite);
		if (textView.getText() == null
				|| textView.getText().toString().equals("")) {
			dialogoFecha.fecha = Calendar.getInstance();
		}
		dialogoFecha.show();
	}

	public void mostrarDecimalDialog(View v) {
		dialogoDecimal.show();
	}

	public void topeCheck(View v) {
		CheckBox checkEdit = (CheckBox) findViewById(R.id.check_mnt_bote_tope);
		LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linear_layout_mnt_bote_importe_tope);
		TextView textView = (TextView) findViewById(R.id.txt_mnt_bote_importe_tope);
		if (checkEdit.isChecked()) {
			linearLayout.setEnabled(true);
			linearLayout.setClickable(true);
			textView.setHint(R.string.toca_para_editar);
		} else {
			linearLayout.setEnabled(false);
			textView.setHint(R.string.tope_editor_sin_tope);
			textView.setText("");
			dialogoDecimal.valor = null;
		}
		// ToggleButton toggleButton = (ToggleButton)
		// findViewById(R.id.toggle_mnt_bote_tope);
		// EditText editText = (EditText)
		// findViewById(R.id.ed_mnt_bote_importe_tope);
		// if (toggleButton.isChecked())
		// editText.setEnabled(true);
		// else
		// editText.setEnabled(false);
	}

	public void fechaLimiteCheck(View v) {
		CheckBox checkEdit = (CheckBox) findViewById(R.id.check_mnt_bote_fecha_limite);
		LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linear_layout_mnt_bote_fecha_limite);
		TextView textView = (TextView) findViewById(R.id.txt_mnt_bote_fecha_limite);
		if (checkEdit.isChecked()) {
			linearLayout.setEnabled(true);
			linearLayout.setClickable(true);
			textView.setHint(R.string.toca_para_seleccionar);
		} else {
			{
				linearLayout.setEnabled(false);
				textView.setHint(R.string.fecha_limite_editor_sin_limite);
				textView.setText("");
				dialogoFecha.fecha = null;
			}
		}
	}

	public void showTipo(View v) {
		Spinner spinner = (Spinner) v.findViewById(R.id.spin_mnt_bote_tipo);
		spinner.performClick();
	}
}
