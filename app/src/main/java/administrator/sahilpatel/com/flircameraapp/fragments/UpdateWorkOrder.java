package administrator.sahilpatel.com.flircameraapp.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import administrator.sahilpatel.com.flircameraapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class UpdateWorkOrder extends Fragment {


    public UpdateWorkOrder() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_update_work_order, container, false);
    }

}