package administrator.sahilpatel.com.flircameraapp.activities;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;

import administrator.sahilpatel.com.flircameraapp.R;
import administrator.sahilpatel.com.flircameraapp.fragments.FlirCameraFragment;
import administrator.sahilpatel.com.flircameraapp.fragments.WorkOrderDetails;
import administrator.sahilpatel.com.flircameraapp.fragments.WorkOrderFormFragment;
import administrator.sahilpatel.com.flircameraapp.fragments.WorkOrderSummary;
import administrator.sahilpatel.com.flircameraapp.listeners.OnFormFilled;
import administrator.sahilpatel.com.flircameraapp.model.Order;

public class FragmentContainerActivity extends AppCompatActivity implements OnFormFilled{

    private WorkOrderFormFragment workOrderFormFragment;
    private FlirCameraFragment flirCameraFragment;
    private WorkOrderSummary workOrderSummaryFragment;

    private FrameLayout container_frame_layout;
    private FragmentTransaction transaction;
    private Order order;

    public static final String FRAGMENT_TYPE_WORK_ORDER_DETAILS = "WORK_ORDER_DETAILS_FRAGMENT";
    public static final String FRAGMENT_TYPE_FLIR_CAMERA = "FLIR_CAMERA_FRAGMENT";
    public static final String FRAGMENT_TYPE_WORK_ORDER_SUMMARY = "WORK_ORDER_SUMMARY";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_container);

        order = new Order();

        workOrderFormFragment = new WorkOrderFormFragment();
        flirCameraFragment = new FlirCameraFragment();
        workOrderSummaryFragment = new WorkOrderSummary();

        container_frame_layout = (FrameLayout) findViewById(R.id.fragment_container);

        setListeners();
    }

    private void setListeners() {

        workOrderFormFragment.setOrder(order);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.fragment_container,workOrderFormFragment);
        container_frame_layout.setTag(FRAGMENT_TYPE_WORK_ORDER_DETAILS);
        transaction.commit();


//        final FrameLayout container_frame_layout = (FrameLayout) findViewById(R.id.fragment_container);
//        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        transaction.add(R.id.fragment_container,flirCameraFragment);
//        container_frame_layout.setTag(FRAGMENT_TYPE_FLIR_CAMERA);
//        transaction.commit();

//        final FrameLayout container_frame_layout = (FrameLayout) findViewById(R.id.fragment_container);
//        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        transaction.add(R.id.fragment_container,new WorkOrderDetails());
//        container_frame_layout.setTag(FRAGMENT_TYPE_FLIR_CAMERA);
//        transaction.commit();

    }

    private void openNextFragment(String nextFragment) {

        switch (nextFragment) {
            case FRAGMENT_TYPE_FLIR_CAMERA :
                flirCameraFragment.setOrder(order);
                transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container,flirCameraFragment);
                container_frame_layout.setTag(FRAGMENT_TYPE_FLIR_CAMERA);
                transaction.commit();
                break;

            case FRAGMENT_TYPE_WORK_ORDER_SUMMARY :
                workOrderSummaryFragment.setOrder(order);
                transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container,workOrderSummaryFragment);
                container_frame_layout.setTag(FRAGMENT_TYPE_WORK_ORDER_SUMMARY);
                transaction.commit();
        }

    }

    @Override
    public void onCompletion(Order order, boolean hasNextPage, String nextPage) {
        this.order = order;

        if(hasNextPage) {
            openNextFragment(nextPage);
        }
        else {
            finish();
        }

    }
}
