package com.aizpun.botebeta.negocio;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.aizpun.botebeta.utilidades.Fragmento;

/**
 * The <code>PagerAdapter</code> serves the fragments when paging.
 * 
 * @author mwho
 */
public class TabPagerAdapter extends FragmentPagerAdapter {

	private List<Fragmento> fragments;

	/**
	 * @param fm
	 * @param fragments
	 */
	public TabPagerAdapter(FragmentManager fm, List<Fragmento> fragments) {
		super(fm);
		this.fragments = fragments;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.FragmentPagerAdapter#getItem(int)
	 */
	@Override
	public Fragment getItem(int position) {
		return this.fragments.get(position).getFragmento();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.view.PagerAdapter#getCount()
	 */
	@Override
	public int getCount() {
		return this.fragments.size();
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return fragments.get(position).getNombre();
	}
}
