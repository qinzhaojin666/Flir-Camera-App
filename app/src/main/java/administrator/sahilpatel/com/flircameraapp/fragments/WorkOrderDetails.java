package administrator.sahilpatel.com.flircameraapp.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import administrator.sahilpatel.com.flircameraapp.R;
import administrator.sahilpatel.com.flircameraapp.adapters.UpdatesRecyclerAdapter;
import administrator.sahilpatel.com.flircameraapp.listeners.OnEditingWorkOrder;
import administrator.sahilpatel.com.flircameraapp.model.Order;
import administrator.sahilpatel.com.flircameraapp.support.CustomLayoutManager;

/**
 * A simple {@link Fragment} subclass.
 */
public class WorkOrderDetails extends Fragment {

    private Order order;

    private OnEditingWorkOrder mCallback;

    private RecyclerView recyclerView;
    //private RecyclerView recyclerView;
    private UpdatesRecyclerAdapter adapter;

    private LinearLayout buttonSet;
    private LinearLayout button;

    public WorkOrderDetails() {
        // Required empty public constructor
    }

    public void setmCallback(OnEditingWorkOrder mCallback) {
        this.mCallback = mCallback;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView = inflater.inflate(R.layout.fragment_work_order_details, container, false);

        ((TextView)rootView.findViewById(R.id.field_order_id))
                .setText(
                        getActivity().getResources().getString(R.string.work_order_suffix)+order.getWorkOrderNumber()
                );

        buttonSet = (LinearLayout)rootView.findViewById(R.id.details_container_buttons);
        button = (LinearLayout)rootView.findViewById(R.id.details_container_button);

        adapter = new UpdatesRecyclerAdapter(order,getContext());
        recyclerView = (RecyclerView)rootView.findViewById(R.id.details_recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        setListeners(rootView);
        return rootView;
    }

    private void setListeners(View rootView) {

        switch (order.getStatus()) {
            case Order.STATUS_OPEN :
                buttonSet.setVisibility(View.VISIBLE);
                button.setVisibility(View.GONE);
                break;
            case Order.STAUS_CLOSED :
                buttonSet.setVisibility(View.GONE);
                button.setVisibility(View.VISIBLE);
        }

        rootView.findViewById(R.id.button_details_update_order).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openUpdateWorkOrderWindow();
            }
        });

        rootView.findViewById(R.id.button_details_close_order).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openClosureWindow();
            }
        });

        rootView.findViewById(R.id.button_details_reopen_work_order).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reOpenWorkOrder();
            }
        });

    }

    private void reOpenWorkOrder() {

        if(mCallback == null) {
            mCallback = (OnEditingWorkOrder)getActivity();
        }

        mCallback.reOpenWorkOrder();
    }

    private void openUpdateWorkOrderWindow() {

        if(mCallback == null) {
            mCallback = (OnEditingWorkOrder)getActivity();
        }

        mCallback.updateWorkOrder(null);
    }

    private void openClosureWindow() {

        if(mCallback == null) {
            mCallback = (OnEditingWorkOrder)getActivity();
        }

        mCallback.closeWorkOrder(null);
    }
}
