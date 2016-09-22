package administrator.sahilpatel.com.flircameraapp.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import administrator.sahilpatel.com.flircameraapp.model.Closure;
import administrator.sahilpatel.com.flircameraapp.model.Order;

/**
 * A simple {@link Fragment} subclass.
 */
public class WorkOrderClosure extends Fragment {

    /**
     * Called when the user clicks on close work order. Here we will ask
     * the user to enter a small closure note and add some more images in
     * reference to closure. To add images, we will open FlirCameraFragment
     * from here. All the data related to Closure is kept in closure object.
     * Order data is kept in order object, Order object is set before loading
     * the fragment. Closure object is empty initially, however, if the user
     * clicks on add images button, the closure object is then initialized by
     * the calling activity so that data is not lost.
     */

    private static final String TAG = "WorkOrderClosure";


    /**
     * Closure object to store fresh data. Order object to store
     * User data, specifically order number. mCallback is used to
     * request opening camera and submit closure when its done.
     */
    private Closure closure;
    private Order order;
    private OnEditingWorkOrder mCallback;

    /**
     * To store closure notes, store images and populate images.
     */
    private EditText field_closure_notes;
    private RecyclerView recyclerView;
    private MyRecyclerAdapter recyclerAdapter;


    public WorkOrderClosure() {
        // Required empty public constructor
        closure = new Closure();
    }


    /**
     * Setters for closure and order. setOrder will be called all the time, however
     * setClosure will only be called when we comeback after selecting images.
     */
    public void setClosure(Closure closure) {
        this.closure = closure;
    }

    public void setOrder(Order order) {
        this.order = order;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_work_order_closure, container, false);

        /**
         * This is the only reason why we need order object. For
         * the order number to be displayed on top.
         */
        ((TextView)rootView.findViewById(R.id.field_order_id))
                .setText(
                    getActivity().getResources().getString(R.string.work_order_suffix)+order.getWorkOrderNumber()
        );

        /**
         * The recycler will show images that are newly selected.
         * The data for this adapter is present in ImagePairList
         * of closure object. Initially it is empty.
         */
        recyclerView = (RecyclerView)rootView.findViewById(R.id.add_more_recycler_view);
        recyclerAdapter = new MyRecyclerAdapter(closure.getImagePairList());

        /**
         * Jargon for RecyclerView
         */
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(),
                LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(recyclerAdapter);

        /**
         * Stores the closure notes. We will fetch it later.
         */
        field_closure_notes = (EditText)rootView.findViewById(R.id.field_work_order_closure_notes);
        setListeners(rootView);
        return rootView;
    }

    private void setListeners(View rootView) {

        /**
         * click listeners for opening Camera and saving closure data.
         */
        rootView.findViewById(R.id.button_take_images).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fetchEnteredData();
                openCamera();
            }
        });

        rootView.findViewById(R.id.button_save_close_work_order).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fetchEnteredData();
                submitClosure();
            }
        });
    }


    /**
     * Makes a callback to calling activity to close the work order.
     * passes the closure activity along with it.
     */
    private void submitClosure() {

        if(mCallback == null) {
            mCallback = (OnEditingWorkOrder)getActivity();
        }
        mCallback.closeWorkOrder(closure);
    }

    /**
     * Fetch all the data that is stores. In this case it is just
     * the data present in closure notes field. All the validation
     * for the data will also be done here.
     *
     * The entered data is saved to global closure object.
     */
    private void fetchEnteredData() {

        String closureNotes = field_closure_notes.getText().toString();

        //Todo Some validation on the data
        closure.setClosureNotes(closureNotes);
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
        mCallback.openCamera(null,closure, UpdateFragmentContainerActivity.FRAGMENT_TYPE_WORK_ORDER_CLOSURE);
    }
}
