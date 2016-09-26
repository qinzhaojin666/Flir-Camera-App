package administrator.sahilpatel.com.flircameraapp.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import administrator.sahilpatel.com.flircameraapp.R;
import administrator.sahilpatel.com.flircameraapp.connection.FirebaseOrdersApi;
import administrator.sahilpatel.com.flircameraapp.connection.FirebaseStorageApi;
import administrator.sahilpatel.com.flircameraapp.fragments.FlirCameraFragment;
import administrator.sahilpatel.com.flircameraapp.fragments.WorkOrderFormFragment;
import administrator.sahilpatel.com.flircameraapp.fragments.WorkOrderSummary;
import administrator.sahilpatel.com.flircameraapp.listeners.ImageUploadCallback;
import administrator.sahilpatel.com.flircameraapp.listeners.ImageUploadListener;
import administrator.sahilpatel.com.flircameraapp.listeners.OnCameraStarted;
import administrator.sahilpatel.com.flircameraapp.listeners.OnFormFilled;
import administrator.sahilpatel.com.flircameraapp.model.ImagePair;
import administrator.sahilpatel.com.flircameraapp.model.ImageUploadWrapper;
import administrator.sahilpatel.com.flircameraapp.model.Order;

public class AddOrderFragmentContainerActivity extends AppCompatActivity
        implements OnFormFilled, OnCameraStarted, ImageUploadCallback{

    /**
     * The three fragments that can be opened from this activity, all the
     * fragments are identified with this static value.
     */
    public static final String FRAGMENT_TYPE_WORK_ORDER_FORM = "WORK_ORDER_FORM_FRAGMENT";
    public static final String FRAGMENT_TYPE_FLIR_CAMERA = "FLIR_CAMERA_FRAGMENT";
    public static final String FRAGMENT_TYPE_WORK_ORDER_SUMMARY = "WORK_ORDER_SUMMARY";

    private FirebaseStorageApi firebaseStorageApi;
    private ProgressDialog progressDialog;
    private static final String TAG = "AddOrderActivity";
    
    /**
     *  These constants are used to identify the state of the order that we
     *  are trying to place, two states are defined, success and failed. The
     *  third constant is a label to identify value.
     */
    public static final String FORM_SUBMISSION_SUCCESS = "SUCCESS";
    public static final String FORM_SUBMISSION_FAILED = "FAILED";
    public static final String FORM_SUBMISSION_STATUS = "STATUS";

    /**
     * Would store the reason for cancellation of Form. Not being used right now.
     */
    public static final String FORM_SUBMISSION_CANCELLED_REASON = "REASON";

    /**
     * Fragment objects that we can add from this window.
     */
    private WorkOrderFormFragment workOrderFormFragment;
    private FlirCameraFragment flirCameraFragment;
    private WorkOrderSummary workOrderSummaryFragment;

    /**
     * Fragment container, and model object.
     */
    private FrameLayout container_frame_layout;
    private Order order;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_container);

        order = new Order();

        workOrderFormFragment = new WorkOrderFormFragment();
        flirCameraFragment = new FlirCameraFragment();
        workOrderSummaryFragment = new WorkOrderSummary();

        firebaseStorageApi = new FirebaseStorageApi();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Uploading....");
        progressDialog.setIndeterminate(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setProgress(0);

        container_frame_layout = (FrameLayout) findViewById(R.id.fragment_container);

        setListeners();
    }

    /**
     * As soon as the window is opened, we will add the fragment containing the
     * first form that the user must fill to place order.
     */
    private void setListeners() {

        workOrderFormFragment.setOrder(order);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.fragment_container,workOrderFormFragment);
        container_frame_layout.setTag(FRAGMENT_TYPE_WORK_ORDER_FORM);
        transaction.commit();

    }

    /**
     * We will pass the type of fragment that must be opened, this method
     * would replace the current fragment with this new fragment.
     * @param nextFragment String, the fragment to be opened.
     */
    private void openNextFragment(String nextFragment) {

        FragmentTransaction transaction;

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


    /**
     * This callback is associated with OnFormFilled interface, when the a particular form
     * in order placing phase is over, the onCompletion method is called. This method returns
     * the modified order object, a boolean to track if another page exists and the key
     * value for the next page.
     *
     * If there is no next page, it is the final page, therefore we submit the form and
     * finish this activity.
     * @param order, the modified order object
     * @param hasNextPage, if it has next page
     * @param nextPage, to open
     */
    @Override
    public void onCompletion(Order order, boolean hasNextPage, String nextPage) {
        this.order = order;

        if(hasNextPage) {
            openNextFragment(nextPage);
        }
        else {

           // uploadImages();
//            TODO remove it
            new FirebaseOrdersApi().addOrder(this.order);
            Intent resultIntent = new Intent();
            resultIntent.putExtra(FORM_SUBMISSION_STATUS,FORM_SUBMISSION_SUCCESS);
            setResult(RESULT_OK,resultIntent);

        }
    }

    /**
     * In case the form was not submitted, or the user clicked cancel,
     * this callback is called. We then tell the calling activity about the
     * same.
     * @param reason, why the submission failed
     */
    @Override
    public void onClickedCancel(String reason) {

        Intent resultIntent = new Intent();
        resultIntent.putExtra(FORM_SUBMISSION_STATUS,FORM_SUBMISSION_FAILED);
        resultIntent.putExtra(FORM_SUBMISSION_CANCELLED_REASON,reason);
        setResult(RESULT_OK,resultIntent);
        finish();
    }


    /**
     *  The callback is called to tell the parent fragment that image has
     *  been captured and saved in to internal storage. The url of images in
     *  the form a list of url's is also passed as an argument.
     * @param images, list of image paths.
     */
    @Override
    public void imagesCaptured(List<ImagePair> images) {

        this.order.setImages(images);
        openNextFragment(FRAGMENT_TYPE_WORK_ORDER_SUMMARY);
    }

    
    private void uploadImages() {
        final List<ImagePair> list = order.getImages();

        progressDialog.show();
        int index = 0;
        File file = new File(list.get(index).getFlirImage());
        String title = "0/"+index;

        firebaseStorageApi.uploadImage(file,index,title,this);
    }

    @Override
    public void nextImage(final int uploadedImageIndex) {

        List<ImagePair> list = order.getImages();

        if(uploadedImageIndex == (list.size()*2)-1){
            progressDialog.dismiss();
            return;
        }

        int index;

        //  previous list index
        int listIndex = uploadedImageIndex/2;
        String title;
        File file;


        if(uploadedImageIndex%2 == 0) {

            title = "1/"+uploadedImageIndex;
            index = uploadedImageIndex+1;
            file = new File(order.getImages().get(listIndex).getRegularImage());
        }
        else {
            listIndex = listIndex+1;
            title = "0/"+uploadedImageIndex;
            index = uploadedImageIndex+1;
            file = new File(order.getImages().get(listIndex).getFlirImage());
        }
        progressDialog.setTitle(title);
        progressDialog.setProgress(0);
        firebaseStorageApi.uploadImage(file,index,title,this);
    }

    @Override
    public void imageUploadProgress(int index, String title, double progress) {
        progressDialog.setTitle(title);
        progressDialog.setProgress((int)progress);
    }

    @Override
    public void onUploadFailure(int index, String title, String reason) {
        Log.d(TAG, "onUploadFailure: "+title+reason);
    }

    @Override
    public void onImageUploaded(int index, String title, String imagePath) {
        // TODO: 9/26/2016 send url to Firebase
    }
}
