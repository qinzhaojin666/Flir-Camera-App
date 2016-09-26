package administrator.sahilpatel.com.flircameraapp.fragments;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import administrator.sahilpatel.com.flircameraapp.R;
import administrator.sahilpatel.com.flircameraapp.activities.UpdateFragmentContainerActivity;
import administrator.sahilpatel.com.flircameraapp.adapters.MyRecyclerAdapter;
import administrator.sahilpatel.com.flircameraapp.listeners.OnEditingWorkOrder;
import administrator.sahilpatel.com.flircameraapp.listeners.OnFormFilled;
import administrator.sahilpatel.com.flircameraapp.model.Order;
import administrator.sahilpatel.com.flircameraapp.model.Update;

/**
 * A simple {@link Fragment} subclass.
 */
public class UpdateWorkOrder extends Fragment {


    /**
     * Adds an Update to the Order object. Works same like WorkClosureFragment
     * the only difference being updates are stored as a List. We save the current
     * update in an Update object which is then returned and saved to server.
     */
    private static final String TAG = "UpdateWorkOrder";


    /**
     * Update object stores the current update, Order object is the snapshot
     * that we will be using to get users data. The callback will be used to
     * open the camera and submitting update to parent activity
     */
    private Update update;
    private Order order;
    private OnEditingWorkOrder mCallback;


    /**
     * We receive order_update, time_spent and images from this Fragment.
     * These are entered in the following fields. The order number is added
     * by us when we open this activity.
     */
    private TextView field_order_number;
    private TextView field_order_update;
    private TextView field_time_spent;
    private RecyclerView recyclerView;
    private MyRecyclerAdapter recyclerAdapter;


    public UpdateWorkOrder() {
        // Required empty public constructor
        update = new Update();
    }


    /**
     * These setters are used to initialize model for this fragment.
     * setOrder will always be called before making the fragment. update
     * object will only be set if we are opening this frag for the second time.
     * setMCallback is never called, however we can use it.
     */
    public void setOrder(Order order) {
        this.order = order;
    }

    public void setUpdate(Update update) {
        this.update = update;
    }

    public void setmCallback(OnEditingWorkOrder mCallback) { this.mCallback = mCallback; }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_update_work_order, container, false);

        /**
         * Showing the order number on top
         */
        field_order_number = (TextView)rootView.findViewById(R.id.field_order_id);
        field_order_number.setText(
                getActivity().getResources().getString(R.string.work_order_suffix)+order.getWorkOrderNumber()
        );

        /**
         * Initializing order update field, time spent field and recycler adapter.
         * The recycler adapter here as well works like the recycler in closure window.
         * Initially the adapter has no data, the data will be added when we click
         * openCamera and click some pictures.
         */
        field_order_update = (EditText)rootView.findViewById(R.id.field_work_order_updates);
        field_time_spent = (EditText)rootView.findViewById(R.id.field_time_spent);

        recyclerView = (RecyclerView)rootView.findViewById(R.id.add_more_recycler_view);
        recyclerAdapter = new MyRecyclerAdapter(update.getImagePairList());

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(recyclerAdapter);

        setListeners(rootView);
        return rootView;
    }

    private void setListeners(View rootView) {
        /**
         * Opens the camera, saves the update.
         */
        rootView.findViewById(R.id.button_take_images).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fetchEnteredData();
                openCamera();
            }
        });

        rootView.findViewById(R.id.button_save_update).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fetchEnteredData();
                saveUpdates();
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
     * Fetch all the data that is stores. In this case it is the data that
     * is saved in field_update_notes and field_time_spent. All the validation
     * for the data will also be done here.
     *
     * The entered data is saved to global update object.
     */
    private void fetchEnteredData() {

        String timeSpent = field_time_spent.getText().toString();
        String updateNotes = field_order_update.getText().toString();

        if(timeSpent!= null)
            update.setTimeSpent(timeSpent);
        if(updateNotes != null)
            update.setUpdateNotes(updateNotes);

        // TODO some validation of data here.
    }

    /**
     * Opens the FlirCameraActivity. This fragment will be replaced by
     * FlirCameraFragment and will then be reloaded with all the selected
     * images. This is happening with the help of a callback that tells the
     * parent activity to open the camera.
     *
     * We also need to pass the requesting Fragment name to parent activity as
     * other fragments can also request to openCamera()
     */
    private void openCamera() {

        if(mCallback == null) {
            mCallback = (OnEditingWorkOrder)getActivity();
        }
        mCallback.openCamera(update,null, UpdateFragmentContainerActivity.FRAGMENT_TYPE_UPDATE_WORK_ORDER);
    }

    /**
     * Makes a callback to calling activity to update the work order.
     * passes the update object activity along with it.
     */
    private void saveUpdates() {

        if(mCallback == null) {
            mCallback = (OnEditingWorkOrder)getActivity();
        }
        mCallback.updateWorkOrder(update);
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
