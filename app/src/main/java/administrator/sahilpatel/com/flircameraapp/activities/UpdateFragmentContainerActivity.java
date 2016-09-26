package administrator.sahilpatel.com.flircameraapp.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import administrator.sahilpatel.com.flircameraapp.R;
import administrator.sahilpatel.com.flircameraapp.connection.FirebaseOrdersApi;
import administrator.sahilpatel.com.flircameraapp.fragments.FlirCameraFragment;
import administrator.sahilpatel.com.flircameraapp.fragments.UpdateWorkOrder;
import administrator.sahilpatel.com.flircameraapp.fragments.WorkOrderClosure;
import administrator.sahilpatel.com.flircameraapp.fragments.WorkOrderDetails;
import administrator.sahilpatel.com.flircameraapp.listeners.OnCameraStarted;
import administrator.sahilpatel.com.flircameraapp.listeners.OnEditingWorkOrder;
import administrator.sahilpatel.com.flircameraapp.listeners.OnFormFilled;
import administrator.sahilpatel.com.flircameraapp.model.Closure;
import administrator.sahilpatel.com.flircameraapp.model.ImagePair;
import administrator.sahilpatel.com.flircameraapp.model.Order;
import administrator.sahilpatel.com.flircameraapp.model.Update;

public class UpdateFragmentContainerActivity extends AppCompatActivity
        implements OnEditingWorkOrder, OnCameraStarted {

    /**
     *  constants to identify different types of fragments that can be
     *  opened from this window.
     */
    private static final String TAG = "UpdateFragmentContainer";
    public static final String FRAGMENT_TYPE_WORK_ORDER_DETAILS = "WORK_ORDER_DETAILS_FRAGMENT";
    public static final String FRAGMENT_TYPE_WORK_ORDER_CLOSURE = "WORD_ORDER_CLOSURE_FRAGMENT";
    public static final String FRAGMENT_TYPE_UPDATE_WORK_ORDER = "UPDATE_WORK_ORDER_FRAGMENT";
    public static final String FRAGMENT_TYPE_FLIR_CAMERA = "FLIR_CAMERA_FRAGMENT";

    /**
     * instance variables for fragments that we are going to open from this
     * container activity.
     */
    private UpdateWorkOrder updateWorkOrderFragment;
    private WorkOrderClosure workOrderClosureFragment;
    private WorkOrderDetails workOrderDetailsFragment;
    private FlirCameraFragment flirCameraFragment;

    /**
     * The fragment requesting camera, both update and closure fragment
     * can request camera fragment.
     */
    private String cameraRequester;

    private FrameLayout container_frame_layout;
    private Order order;

    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_container);

        workOrderDetailsFragment = new WorkOrderDetails();
        updateWorkOrderFragment = new UpdateWorkOrder();
        workOrderClosureFragment = new WorkOrderClosure();
        flirCameraFragment = new FlirCameraFragment();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Fetching data...");
        progressDialog.show();

        container_frame_layout = (FrameLayout) findViewById(R.id.fragment_container);

        Intent intent = getIntent();
        String key = intent.getStringExtra(MainActivity.UPDATE_ORDER_ID);

        setListeners(key);
    }

    /**
     * Fetching data from the server for this particular order.
     * We can not fetch all the data initially because there can be
     * many orders.
     * @param key, the key for a particular order.
     */
    private void setListeners(String key) {

        new FirebaseOrdersApi().fetchOrder(key, new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //  Converting fetched data to Order object
                Order order = dataSnapshot.getValue(Order.class);
                order.setOrderId(dataSnapshot.getKey());
                UpdateFragmentContainerActivity.this.order = order;
                progressDialog.dismiss();

                //  adding fetched data.
                workOrderDetailsFragment.setOrder(order);
                workOrderDetailsFragment.setmCallback(UpdateFragmentContainerActivity.this);
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.add(R.id.fragment_container,workOrderDetailsFragment);
                container_frame_layout.setTag(FRAGMENT_TYPE_WORK_ORDER_DETAILS);
                transaction.commit();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Toast.makeText(UpdateFragmentContainerActivity.this,
                        "Could not fetch the order.",
                        Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onCancelled: "+firebaseError.getMessage());
            }
        });
    }

    /**
     * Opens the UpdateWorkOrder window. Simply replaces the current
     * fragment with updateWorkOrder Fragment.
     */
    private void openUpdateWorkOrderWindow() {

        updateWorkOrderFragment.setOrder(order);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container,updateWorkOrderFragment);
        container_frame_layout.setTag(FRAGMENT_TYPE_UPDATE_WORK_ORDER);
        transaction.commit();
    }

    /**
     * Opens CloseWorkOrder window. Open it in the fragment container.
     */
    private void openClosureWindow() {

        workOrderClosureFragment.setOrder(order);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container,workOrderClosureFragment);
        container_frame_layout.setTag(FRAGMENT_TYPE_WORK_ORDER_CLOSURE);
        transaction.commit();
    }

    /**
     * Open the camera. Replace the fragment with FlirCameraFragment.
     */
    private void openCameraWindow() {
        order.setImages(new ArrayList<ImagePair>());
        flirCameraFragment.setOrder(order);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container,flirCameraFragment);
        container_frame_layout.setTag(FRAGMENT_TYPE_FLIR_CAMERA);
        transaction.commit();
    }

    /**
     * Called by the UpdateWorkOrderFragment when the user has finished
     * updating the order. It returns the update object containing data
     * entered by the user. If this update object is null, we will open
     * the update window.
     * @param update, containing update data.
     */
    @Override
    public void updateWorkOrder(Update update) {

        if(update == null) {
            openUpdateWorkOrderWindow();
            return;
        }
        new FirebaseOrdersApi().updateOrder(order.getOrderId(),update,order.getUpdates().size());
        Toast.makeText(UpdateFragmentContainerActivity.this, "Order Updated", Toast.LENGTH_SHORT).show();
        finish();
    }

    /**
     * Called by closeOrderFragment when the user has finished filling the
     * closure form. if the closure object is empty, we call openClosureWindow once again.
     * @param closure, contains closure data.
     */
    @Override
    public void closeWorkOrder(Closure closure) {

        if(closure == null) {
            openClosureWindow();
            return;
        }
        new FirebaseOrdersApi().closeOrder(order.getOrderId(),closure);
        Toast.makeText(UpdateFragmentContainerActivity.this, "Order closed", Toast.LENGTH_SHORT).show();
        finish();
    }

    /**
     * This can be called by either update or closure fragment. They are
     * requesting the camera. We save who is requesting the camera so that
     * we can know whom to send those taken images.
     */
    @Override
    public void openCamera(Update update,Closure closure,String type) {

        cameraRequester = type;
        switch (type) {
            case FRAGMENT_TYPE_WORK_ORDER_CLOSURE :
                order.setClosure(closure);
                openCameraWindow();
                break;

            case FRAGMENT_TYPE_UPDATE_WORK_ORDER :
                List<Update> updates = order.getUpdates();
                updates.add(update);
                order.setUpdates(updates);
                openCameraWindow();
        }
    }

    /**
     * Required by FlirCameraFragment. The clicked images are
     * returned as a path to their location from this callback.
     * @param images, path of images
     */
    @Override
    public void imagesCaptured(final List<ImagePair> images) {

        List<ImagePair> list;

        switch (cameraRequester) {
            case FRAGMENT_TYPE_WORK_ORDER_CLOSURE :

                Closure closure = order.getClosure();
                list = closure.getImagePairList();
                list.addAll(images);
                closure.setImagePairList(list);
                workOrderClosureFragment.setClosure(closure);
                openClosureWindow();
                break;

            case FRAGMENT_TYPE_UPDATE_WORK_ORDER :

                List<Update> updates = order.getUpdates();
                int size = updates.size();
                Update update = updates.get(size-1);        //  last entry
                list = update.getImagePairList();
                list.addAll(images);
                update.setImagePairList(list);
                order.getUpdates().remove(size-1);
                updateWorkOrderFragment.setUpdate(update);
                openUpdateWorkOrderWindow();
        }
    }

    /**
     * Called when the user wants to reopen a closed window.
     */
    @Override
    public void reOpenWorkOrder() {
        new FirebaseOrdersApi().reOpenWorkOrder(order.getOrderId());
        Toast.makeText(UpdateFragmentContainerActivity.this,
                "We have reopened the order.",
                Toast.LENGTH_SHORT).show();
        finish();
    }
}
