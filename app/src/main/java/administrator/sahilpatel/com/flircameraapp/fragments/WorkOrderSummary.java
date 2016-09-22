package administrator.sahilpatel.com.flircameraapp.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import administrator.sahilpatel.com.flircameraapp.R;
import administrator.sahilpatel.com.flircameraapp.adapters.MyRecyclerAdapter;
import administrator.sahilpatel.com.flircameraapp.listeners.OnFormFilled;
import administrator.sahilpatel.com.flircameraapp.model.Order;

/**
 * A simple {@link Fragment} subclass.
 */
public class WorkOrderSummary extends Fragment {

    private static final String TAG = "WorkOrderSummary";
    private Order order;
    private OnFormFilled mCallback;

    private TextView field_order_title;
    private TextView field_order_number;
    private TextView field_customer_name;
    private TextView field_customer_address;
    private TextView field_order_description;
    private RecyclerView recyclerView;
    private MyRecyclerAdapter adapter;


    public WorkOrderSummary() {
        // Required empty public constructor
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public void setmCallback(OnFormFilled mCallback) {
        this.mCallback = mCallback;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_work_order_summary, container, false);

        if (order == null)
            return rootView;

        field_order_title = (TextView)rootView.findViewById(R.id.field_summary_work_order_title);
        field_order_number = (TextView)rootView.findViewById(R.id.field_summary_work_order_number);
        field_order_description = (TextView)rootView.findViewById(R.id.field_summary_work_order_description);
        field_customer_name = (TextView)rootView.findViewById(R.id.field_summary_customer_name);
        field_customer_address = (TextView)rootView.findViewById(R.id.field_summary_customer_address);
        recyclerView = (RecyclerView)rootView.findViewById(R.id.summary_recycler_view);

        adapter = new MyRecyclerAdapter(order.getImages());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        setSummaryData();

        ((TextView)rootView.findViewById(R.id.field_order_id)).setText(order.getWorkOrderNumber());

        rootView.findViewById(R.id.button_summary_submit_order).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitWorkOrderData();
            }
        });

        return rootView;
    }

    private void setSummaryData() {

        field_order_title.setText(order.getWorkOrderTitle());
        field_order_number.setText(order.getWorkOrderNumber());
        field_order_description.setText(order.getWorkOrderDescription());
        field_customer_name.setText(order.getCustomerName());
        field_customer_address.setText(order.getCustomerAddress());

        recyclerView.setAdapter(adapter);
    }

    private void submitWorkOrderData() {

        /**
         * Checking if mCallback is set by the calling activity or not,
         * if not, then we try to see if the calling activity has implemented
         * the OnFormFilled interface ot not.
         */

        if(mCallback == null) {
            try {
                mCallback = (OnFormFilled)getActivity();
            }
            catch (ClassCastException e) {
                throw new ClassCastException("You must implement OnFormFilled interface in calling Activity.");
            }
        }

        mCallback.onCompletion(order,false, "");
    }

}
