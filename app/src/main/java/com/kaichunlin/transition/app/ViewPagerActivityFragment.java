package com.kaichunlin.transition.app;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import kaichunlin.transition.app.R;


/**
 * A placeholder fragment containing a simple view.
 */
public class ViewPagerActivityFragment extends Fragment {
    public static final String ID = "mId";
    public static final String RES_ID = "res_id";

    public ViewPagerActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(
                R.layout.fragment_view_pager, container, false);
        Bundle args = getArguments();
        rootView.findViewById(R.id.pager_img).setBackgroundResource(
                args.getInt(RES_ID));
        return rootView;
    }
}
