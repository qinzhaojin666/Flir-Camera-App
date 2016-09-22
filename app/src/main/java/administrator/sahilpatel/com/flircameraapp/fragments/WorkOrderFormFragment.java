package administrator.sahilpatel.com.flircameraapp.fragments;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import administrator.sahilpatel.com.flircameraapp.R;
import administrator.sahilpatel.com.flircameraapp.activities.AddOrderFragmentContainerActivity;
import administrator.sahilpatel.com.flircameraapp.listeners.OnFormFilled;
import administrator.sahilpatel.com.flircameraapp.model.Order;

/**
 * A simple {@link Fragment} subclass.
 */
public class WorkOrderFormFragment extends Fragment {

    private Order order;

    private EditText field_order_title;
    private EditText field_order_number;
    private EditText field_customer_name;
    private EditText field_customer_address;
    private EditText field_order_description;

    private OnFormFilled mCallback;

    public WorkOrderFormFragment() {
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
        View rootView = inflater.inflate(R.layout.fragment_work_order_form, container, false);

        field_order_title = (EditText)rootView.findViewById(R.id.field_work_order_title);
        field_order_number = (EditText)rootView.findViewById(R.id.field_work_order_number);
        field_customer_name = (EditText)rootView.findViewById(R.id.field_customer_name);
        field_customer_address = (EditText)rootView.findViewById(R.id.field_customer_address);
        field_order_description = (EditText)rootView.findViewById(R.id.field_work_order_description);

        setListeners(rootView);
        return rootView;
    }

    private void setListeners(final View rootView) {



        rootView.findViewById(R.id.button_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fetchFormData();
            }
        });

        rootView.findViewById(R.id.button_close_order).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCloseWindowDialog();
            }
        });
    }


    private void fetchFormData() {

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

        String workOrderTitle = field_order_title.getText().toString();
        String workOrderNumber = field_order_number.getText().toString();
        String customerName = field_customer_name.getText().toString();
        String customerAddress = field_customer_address.getText().toString();
        String workOrderDescription = field_order_description.getText().toString();

        //  TODO validations for the data.

        order.setWorkOrderTitle(workOrderTitle);
        order.setWorkOrderNumber(workOrderNumber);
        order.setWorkOrderDescription(workOrderDescription);
        order.setCustomerName(customerName);
        order.setCustomerAddress(customerAddress);

        /**
         * Sending back the order object, a boolean to represent if there
         * is another fragment after it, and the name of next fragment.
         */
        mCallback.onCompletion(order,true, AddOrderFragmentContainerActivity.FRAGMENT_TYPE_FLIR_CAMERA);
    }

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
