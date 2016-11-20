package com.dankideacentral.dic.util;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

public class Fragmenter {
    FragmentManager fm;

    public Fragmenter (FragmentManager fm) {
        this.fm = fm;
    }
    public void create(int id, Fragment currentFragment) {
        create(id, currentFragment, null);
    }

    public void create(int id, Fragment currentFragment, String tag) {
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(id, currentFragment, tag);
        transaction.addToBackStack(tag);
        transaction.commit();
    }

    public Fragment find(int id) {
        return fm.findFragmentById(id);
    }

    public Fragment find(String tag) {
        return fm.findFragmentByTag(tag);
    }
}
