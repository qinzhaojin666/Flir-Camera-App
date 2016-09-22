package administrator.sahilpatel.com.flircameraapp.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class UpdateFragmentContainerActivity extends AppCompatActivity implements OnEditingWorkOrder, OnCameraStarted {

    private UpdateWorkOrder updateWorkOrderFragment;
    private WorkOrderClosure workOrderClosureFragment;
    private WorkOrderDetails workOrderDetailsFragment;
    private FlirCameraFragment flirCameraFragment;

    public static final String FRAGMENT_TYPE_WORK_ORDER_DETAILS = "WORK_ORDER_DETAILS_FRAGMENT";
    public static final String FRAGMENT_TYPE_WORK_ORDER_CLOSURE = "WORD_ORDER_CLOSURE_FRAGMENT";
    public static final String FRAGMENT_TYPE_UPDATE_WORK_ORDER = "UPDATE_WORK_ORDER_FRAGMENT";
    public static final String FRAGMENT_TYPE_FLIR_CAMERA = "FLIR_CAMERA_FRAGMENT";

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

        container_frame_layout = (FrameLayout) findViewById(R.id.fragment_container);

        Intent intent = getIntent();
        String key = intent.getStringExtra(MainActivity.UPDATE_ORDER_ID);

        setListeners(key);
    }

    private void setListeners(String key) {


        new FirebaseOrdersApi().fetchOrder(key, new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Order order = dataSnapshot.getValue(Order.class);
                order.setOrderId(dataSnapshot.getKey());
                UpdateFragmentContainerActivity.this.order = order;
                progressDialog.dismiss();
                workOrderDetailsFragment.setOrder(order);
                workOrderDetailsFragment.setmCallback(UpdateFragmentContainerActivity.this);
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.add(R.id.fragment_container,workOrderDetailsFragment);
                container_frame_layout.setTag(FRAGMENT_TYPE_WORK_ORDER_DETAILS);
                transaction.commit();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    private void openUpdateWorkOrderWindow() {

        updateWorkOrderFragment.setOrder(order);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container,updateWorkOrderFragment);
        container_frame_layout.setTag(FRAGMENT_TYPE_UPDATE_WORK_ORDER);
        transaction.commit();
    }

    private void openClosureWindow() {

        workOrderClosureFragment.setOrder(order);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container,workOrderClosureFragment);
        container_frame_layout.setTag(FRAGMENT_TYPE_WORK_ORDER_CLOSURE);
        transaction.commit();
    }

    private void openCameraWindow() {
        order.setImages(new ArrayList<ImagePair>());
        flirCameraFragment.setOrder(order);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container,flirCameraFragment);
        container_frame_layout.setTag(FRAGMENT_TYPE_FLIR_CAMERA);
        transaction.commit();
    }

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


    @Override
    public void reOpenWorkOrder() {
        new FirebaseOrdersApi().reOpenWorkOrder(order.getOrderId());
        Toast.makeText(UpdateFragmentContainerActivity.this, "We have reopened the order.", Toast.LENGTH_SHORT).show();
        finish();
    }
}
