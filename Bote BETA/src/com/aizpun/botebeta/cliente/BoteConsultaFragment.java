package com.aizpun.botebeta.cliente;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aizpun.botebeta.R;
import com.aizpun.botebeta.negocio.Bote;
import com.aizpun.botebeta.utilidades.NuevoMovimientoDialog;
import com.aizpun.botebeta.utilidades.Utilidades;

/**
 * Fragmento consulta de bote
 * 
 * @author Jorge
 * 
 */
public class BoteConsultaFragment extends Fragment {
	public final static String ARG_BOTE = "bote";
	private Bote bote = null;
	private NuevoMovimientoDialog nuevoMovimientoDialog = null;

	OnNuevoMovimientoCreatedListener mCallback;

	// The container Activity must implement this interface so the frag can
	// deliver messages
	public interface OnNuevoMovimientoCreatedListener {
		public void onNuevoMovimientoCreatedListener();
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		// This makes sure that the container activity has implemented
		// the callback interface. If not, it throws an exception.
		try {
			mCallback = (OnNuevoMovimientoCreatedListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " debe implementar OnNuevoMovimientoCreatedListener");
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		if (savedInstanceState != null) {
			bote = savedInstanceState.getParcelable(ARG_BOTE);
		} else {
			Bundle args = getArguments();
			if (args != null)
				bote = (Bote) args.getParcelable(ARG_BOTE);
		}
		if (bote != null)
			bote = Bote.getBote(getActivity(), bote.getId());
		return inflater.inflate(R.layout.bote_consulta, container, false);
	}

	@Override
	public void onStart() {
		super.onStart();
		cargarDatos(bote);
	}

	@Override
	public void onPause() {
		super.onPause();
		if (nuevoMovimientoDialog != null && nuevoMovimientoDialog.isShowing()) {
			nuevoMovimientoDialog.dismiss();
		}
	}

	public void cargarDatos(Bote b) {
		bote = b;
		if (getView() != null) {
			DateFormat datef = SimpleDateFormat.getDateInstance(
					SimpleDateFormat.MEDIUM, Locale.getDefault());
			// // Título
			// setTitle(getString(R.string.title_activity_bote_edit));
			// Nombre
			TextView textViewNombre = (TextView) getView().findViewById(
					R.id.txt_consulta_bote_nombre);
			textViewNombre.setText(bote.getNombre());
			// Total
			// LinearLayout linearLayoutTotal = (LinearLayout) v
			// .findViewById(R.id.linear_Layout_consulta_bote);
			// linearLayoutTotal.setOnClickListener(mOnClickListener);
			// TextView textViewTotal = (TextView) v
			// .findViewById(R.id.lbl_consulta_bote_total);
			// textViewTotal.setText(Utilidades.formatearDouble(bote.getTotal()));
			
			//Ingresado total
			TextView textViewIngresadoTotalTitulo = (TextView) getView().findViewById(
					R.id.txt_consulta_bote_ingresado_total_titulo);
			textViewIngresadoTotalTitulo.setText(getString(R.string.ingresado_total) + ": ");
			TextView textViewIngresadoTotal = (TextView) getView().findViewById(
					R.id.txt_consulta_bote_ingresado_total);
			textViewIngresadoTotal.setText(Utilidades.formatearMoneda(bote.getIngresadoTotal()));
			
			//Devoluciones
			TextView textViewDevolucionesTitulo = (TextView) getView().findViewById(
					R.id.txt_consulta_bote_devoluciones_titulo);
			textViewDevolucionesTitulo.setText(getString(R.string.devoluciones) + ": ");
			TextView textViewDevoluciones = (TextView) getView().findViewById(
					R.id.txt_consulta_bote_devoluciones);
			textViewDevoluciones.setText(Utilidades.formatearMoneda(bote.getDevoluciones()));
			
			//Ingresado actual
			TextView textViewIngresadoActualTitulo = (TextView) getView().findViewById(
					R.id.txt_consulta_bote_ingresado_actual_titulo);
			textViewIngresadoActualTitulo.setText(getString(R.string.ingresado_actual) + ": ");
			TextView textViewIngresadoActual = (TextView) getView().findViewById(
					R.id.txt_consulta_bote_ingresado_actual);
			textViewIngresadoActual.setText(Utilidades.formatearMoneda(bote.getIngresadoActual()));
			
			//Gastado
			TextView textViewGastadoTitulo = (TextView) getView().findViewById(
					R.id.txt_consulta_bote_gastado_titulo);
			textViewGastadoTitulo.setText(getString(R.string.gastado) + ": ");
			TextView textViewGastado = (TextView) getView().findViewById(
					R.id.txt_consulta_bote_gastado);
			textViewGastado.setText(Utilidades.formatearMoneda(bote.getGastado()));
			
			//Disponible
			TextView textViewDisponibleTitulo = (TextView) getView().findViewById(
					R.id.txt_consulta_bote_disponible_titulo);
			textViewDisponibleTitulo.setText(getString(R.string.disponible) + ": ");
			TextView textViewDisponible = (TextView) getView().findViewById(
					R.id.txt_consulta_bote_disponible);
			textViewDisponible.setText(Utilidades.formatearMoneda(bote.getDisponible()));
			// Button buttonTotal = (Button) getView().findViewById(
			// R.id.btn_consulta_bote_total);
			// buttonTotal.setText(Utilidades.formatearMoneda(bote.getTotal()));
			// buttonTotal.setOnClickListener(mOnClickListener);
			
			// Tope
			TextView textViewTope = (TextView) getView().findViewById(
					R.id.txt_consulta_bote_importe_tope);
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
			TextView textViewTipo = (TextView) getView().findViewById(
					R.id.txt_consulta_bote_tipo);
			textViewTipo.setText(bote.getTipo());
			// Fecha creación
			TextView textViewFechaCreacion = (TextView) getView().findViewById(
					R.id.txt_consulta_bote_fecha_creacion);
			textViewFechaCreacion.setText(datef.format(bote.getFechaCreacion()
					.getTime()));
			// Fecha límite
			TextView textViewFechaLimite = (TextView) getView().findViewById(
					R.id.txt_consulta_bote_fecha_limite);
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
			// Button btnTotal = (Button) getView().findViewById(
			// R.id.btn_consulta_bote_total);
			// btnTotal.setOnClickListener(onClickAddMovimientoListener);
			// // Administrador
			// TextView textViewAdministrador = (TextView)
			// findViewById(R.id.txt_consulta_bote_admnistrador);
			// textViewAdministrador.setText("");
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		// Guarda los elementos del bote en caso de que sea necesario recrear el
		// fragmento
		outState.putParcelable(ARG_BOTE, bote);
	}

	public void addMovimiento(View v) {
		nuevoMovimientoDialog = new NuevoMovimientoDialog(getActivity());
		nuevoMovimientoDialog.bote = bote;
		nuevoMovimientoDialog.setOnDismissListener(onDismissListener);
		nuevoMovimientoDialog.show();
	}

	public View.OnClickListener onClickAddMovimientoListener = new View.OnClickListener() {
		public void onClick(View v) {
			addMovimiento(v);
		}
	};

	private DialogInterface.OnDismissListener onDismissListener = new DialogInterface.OnDismissListener() {

		public void onDismiss(DialogInterface dialog) {
			mCallback.onNuevoMovimientoCreatedListener();
		}
	};
}