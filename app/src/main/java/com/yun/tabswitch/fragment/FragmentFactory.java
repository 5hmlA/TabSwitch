package com.yun.tabswitch.fragment;

import android.support.v4.app.Fragment;

import java.util.HashMap;
import java.util.Map;

public class FragmentFactory {
	
	static Map<Integer, Fragment> fmgCache = new HashMap<Integer, Fragment>();
	public static Fragment createFragment(int index){
		Fragment fragment = fmgCache.get(index);
		if (fragment==null) {
			switch (index) {
			case 0:
				fragment = new OneFragment();
				break;
			case 1:
				fragment = new TwoFragment();
				break;
			case 2:
				fragment = new ThreeFragment();
				break;
			case 3:
				fragment = new FourFragment();
				break;
			case 4:
				fragment = new FiveFragment();
				break;
			}
			fmgCache.put(index, fragment);
		}
		return fragment;
	}
}
