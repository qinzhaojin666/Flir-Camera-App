package administrator.sahilpatel.com.flircameraapp.fragments;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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


    /**
     * Shows the complete summary of the order here. Called
     * by AddOrderFragmentContainerActivity. Shows all the information that is
     * stored in Order object.
     */

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


    public WorkOrderSummary() {}

    /**
     * Would be called right before the fragment is added to its container.
     * Very important that it is assigned first.
     * @param order, contains data
     */
    public void setOrder(Order order) {
        this.order = order;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
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

        //  Order id up top.
        ((TextView)rootView.findViewById(R.id.field_order_id)).setText(
                getActivity().getResources().getString(R.string.work_order_suffix)+order.getWorkOrderNumber()
        );
        setListeners(rootView);

        return rootView;
    }

    private void setListeners(View rootView) {
        rootView.findViewById(R.id.button_summary_submit_order).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitWorkOrderData();
            }
        });

        rootView.findViewById(R.id.button_close_order).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCloseWindowDialog();
            }
        });
    }

    /**
     * We set the data fetched from order object to the
     * respective fields in the form.
     */
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
        Log.d(TAG, "submitWorkOrderData: "+"submiting form");
        mCallback.onCompletion(order,false, "");
    }

    /**
     * Show a popup before closing the window. A warning message.
     */
    private void showCloseWindowDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext())
                .setTitle("Are you sure ?")
                .setMessage("All your progress will be lost.")
                .setPositiveButton(R.string.label_dialog_yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        getActivity().finish();
                    }
                })
                .setNegativeButton(R.string.label_dialog_wait, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //  will dismiss the dialog.
                    }
                });
        builder.show();
    }

}
