package com.aizpun.botebeta.utilidades;

import android.support.v4.app.Fragment;

public class Fragmento {
	private String mNombre = null;
	private Fragment mFragmento = null;

	public String getNombre() {
		return mNombre;
	}

	public void setNombre(String mNombre) {
		this.mNombre = mNombre;
	}

	public Fragment getFragmento() {
		return mFragmento;
	}

	public void setFragmento(Fragment fragmento) {
		this.mFragmento = fragmento;
	}

	public Fragmento(String mNombre, Fragment mFragmento) {
		super();
		this.mNombre = mNombre;
		this.mFragmento = mFragmento;
	}
}
