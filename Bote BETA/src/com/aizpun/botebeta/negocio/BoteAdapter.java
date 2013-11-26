package com.aizpun.botebeta.negocio;

import java.util.List;

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
 * Adaptador de la tabla Bote
 * 
 * @author Jorge
 * 
 */
public class BoteAdapter extends ArrayAdapter<Bote> {

	Context context;
	int layoutResourceId;
	List<Bote> data = null;

	public BoteAdapter(Context context, int layoutResourceId, List<Bote> data) {
		super(context, layoutResourceId, data);
		this.layoutResourceId = layoutResourceId;
		this.context = context;
		this.data = data;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		BoteHolder holder = null;

		if (row == null) {
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			row = inflater.inflate(layoutResourceId, parent, false);
			holder = new BoteHolder();
			holder.imgIcono = (ImageView) row
					.findViewById(R.id.img_elemento_lista_bote_icono);
			holder.txtNombre = (TextView) row
					.findViewById(R.id.txt_elemento_lista_bote_nombre);
			holder.txtTotal = (TextView) row
					.findViewById(R.id.txt_elemento_lista_bote_total);
			row.setTag(holder);
		} else {
			holder = (BoteHolder) row.getTag();
		}

		Bote bote = data.get(position);
		holder.imgIcono.setImageResource(R.drawable.ic_list_bote);
		holder.txtNombre.setText(bote.getNombre());
		holder.txtTotal.setText(Utilidades.formatearMoneda(bote.getTotal()));
		return row;
	}

	static class BoteHolder {
		ImageView imgIcono;
		TextView txtNombre;
		TextView txtTotal;
	}
}