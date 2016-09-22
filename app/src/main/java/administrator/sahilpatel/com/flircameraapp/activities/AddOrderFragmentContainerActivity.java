package administrator.sahilpatel.com.flircameraapp.activities;

import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;

import java.util.List;

import administrator.sahilpatel.com.flircameraapp.R;
import administrator.sahilpatel.com.flircameraapp.connection.FirebaseOrdersApi;
import administrator.sahilpatel.com.flircameraapp.fragments.FlirCameraFragment;
import administrator.sahilpatel.com.flircameraapp.fragments.UpdateWorkOrder;
import administrator.sahilpatel.com.flircameraapp.fragments.WorkOrderClosure;
import administrator.sahilpatel.com.flircameraapp.fragments.WorkOrderDetails;
import administrator.sahilpatel.com.flircameraapp.fragments.WorkOrderFormFragment;
import administrator.sahilpatel.com.flircameraapp.fragments.WorkOrderSummary;
import administrator.sahilpatel.com.flircameraapp.listeners.OnCameraStarted;
import administrator.sahilpatel.com.flircameraapp.listeners.OnFormFilled;
import administrator.sahilpatel.com.flircameraapp.model.ImagePair;
import administrator.sahilpatel.com.flircameraapp.model.Order;

public class AddOrderFragmentContainerActivity extends AppCompatActivity implements OnFormFilled, OnCameraStarted{

    private WorkOrderFormFragment workOrderFormFragment;
    private FlirCameraFragment flirCameraFragment;
    private WorkOrderSummary workOrderSummaryFragment;


    private FrameLayout container_frame_layout;
    private FragmentTransaction transaction;
    private Order order;

    public static final String FRAGMENT_TYPE_WORK_ORDER_FORM = "WORK_ORDER_FORM_FRAGMENT";
    public static final String FRAGMENT_TYPE_FLIR_CAMERA = "FLIR_CAMERA_FRAGMENT";
    public static final String FRAGMENT_TYPE_WORK_ORDER_SUMMARY = "WORK_ORDER_SUMMARY";

    public static final String FORM_SUBMISSION_SUCCESS = "SUCCESS";
    public static final String FORM_SUBMISSION_FAILED = "FAILED";
    public static final String FORM_SUBMISSION_STATUS = "STATUS";

    public static final String FORM_SUBMISSION_CANCELLED_REASON = "REASON";

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
        container_frame_layout.setTag(FRAGMENT_TYPE_WORK_ORDER_FORM);
        transaction.commit();

    }

    private void openNextFragment(String nextFragment) {

        switch (nextFragment) {

            case FRAGMENT_TYPE_WORK_ORDER_FORM:
                flirCameraFragment.setOrder(order);
                transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container,workOrderFormFragment);
                container_frame_layout.setTag(FRAGMENT_TYPE_WORK_ORDER_FORM);
                transaction.commit();
                break;

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

            new FirebaseOrdersApi().addOrder(this.order);
            Intent resultIntent = new Intent();
            resultIntent.putExtra(FORM_SUBMISSION_STATUS,FORM_SUBMISSION_SUCCESS);
            setResult(RESULT_OK,resultIntent);
            finish();
        }
    }

    @Override
    public void onClickedCancel(String reason) {

        Intent resultIntent = new Intent();
        resultIntent.putExtra(FORM_SUBMISSION_STATUS,FORM_SUBMISSION_FAILED);
        resultIntent.putExtra(FORM_SUBMISSION_CANCELLED_REASON,reason);
        setResult(RESULT_OK,resultIntent);
        finish();
    }


    @Override
    public void imagesCaptured(List<ImagePair> images) {

        this.order.setImages(images);
        openNextFragment(FRAGMENT_TYPE_WORK_ORDER_SUMMARY);
    }
}
