package com.aizpun.botebeta.negocio;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.aizpun.botebeta.R;
import com.aizpun.botebeta.utilidades.Utilidades;

/**
 * Adaptador de la tabla Movimiento
 * 
 * @author Jorge
 * 
 */
public class MovimientoAdapter extends ArrayAdapter<Movimiento> {

	Context context;
	int layoutResourceId;
	List<Movimiento> data = null;

	public MovimientoAdapter(Context context, int layoutResourceId,
			List<Movimiento> data) {
		super(context, layoutResourceId, data);
		this.layoutResourceId = layoutResourceId;
		this.context = context;
		this.data = data;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		MovimientoHolder holder = null;

		if (row == null) {
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			row = inflater.inflate(layoutResourceId, parent, false);
			holder = new MovimientoHolder();
			// holder.imgIcono = (ImageView) row
			// .findViewById(R.id.img_elemento_lista_movimiento_icono);
			holder.txtFechaCreacion = (TextView) row
					.findViewById(R.id.txt_elemento_lista_movimiento_fecha_creacion);
			holder.txtDescripcion = (TextView) row
					.findViewById(R.id.txt_elemento_lista_movimiento_descripcion);
			holder.txtImporte = (TextView) row
					.findViewById(R.id.txt_elemento_lista_movimiento_importe);
			row.setTag(holder);
		} else {
			holder = (MovimientoHolder) row.getTag();
		}

		DateFormat datef = SimpleDateFormat.getDateInstance(
				SimpleDateFormat.MEDIUM, Locale.getDefault());
		Movimiento movimiento = data.get(position);
		// holder.imgIcono.setImageResource(R.drawable.ic_list_bote);
		holder.txtFechaCreacion.setText(datef.format(movimiento
				.getFechaCreacion().getTime()));
		holder.txtDescripcion.setText(movimiento.getDescripcion());
		if (movimiento.getTipo().equals("Ingresar")) {
			holder.txtImporte.setText("+"
					+ Utilidades.formatearMoneda(movimiento.getImporte()));
			holder.txtImporte.setTextColor(((Activity) context).getResources()
					.getColor(R.color.verde));
		} else {
			if (movimiento.getTipo().equals("Retirar"))
				holder.txtImporte.setTextColor(((Activity) context)
						.getResources().getColor(R.color.rojo));
			else
				holder.txtImporte.setTextColor(((Activity) context)
						.getResources().getColor(R.color.azul));
			holder.txtImporte.setText(Utilidades.formatearMoneda(movimiento
					.getImporte()));
		}
		return row;
	}

	static class MovimientoHolder {
		ImageView imgIcono;
		TextView txtFechaCreacion;
		TextView txtDescripcion;
		TextView txtImporte;
	}
}