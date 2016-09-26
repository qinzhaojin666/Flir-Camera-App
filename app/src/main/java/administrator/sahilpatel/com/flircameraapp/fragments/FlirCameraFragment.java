package administrator.sahilpatel.com.flircameraapp.fragments;


import android.content.DialogInterface;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import administrator.sahilpatel.com.flircameraapp.R;
import administrator.sahilpatel.com.flircameraapp.adapters.MyRecyclerAdapter;
import administrator.sahilpatel.com.flircameraapp.camera.CameraFragment;
import administrator.sahilpatel.com.flircameraapp.connection.FirebaseStorageApi;
import administrator.sahilpatel.com.flircameraapp.listeners.ImageCaptureListener;
import administrator.sahilpatel.com.flircameraapp.listeners.OnCameraStarted;
import administrator.sahilpatel.com.flircameraapp.model.ImagePair;
import administrator.sahilpatel.com.flircameraapp.model.Order;

/**
 * A simple {@link Fragment} subclass.
 */
public class FlirCameraFragment extends Fragment implements ImageCaptureListener{

    /**
     * This fragment handles taking picture and saving it into
     * our default directory.
     */

    private CameraFragment cameraFragment;
    private static final String TAG = "FlirCameraFragment";
    /**
     * Directory where we need to store the images.
     */
    private String directory_name = "Flir Camera App";

    private Order order;
    private OnCameraStarted mCallback;
    View rootView;

    private List<ImagePair> fileNames;
    private MyRecyclerAdapter adapter;

    public FlirCameraFragment() {}

    public void setOrder(Order order) {
        this.order = order;
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_flair_camera, container, false);
        this.rootView = rootView;
        cameraFragment = new CameraFragment();
        fileNames = new ArrayList<>();

        RecyclerView recyclerView;
        recyclerView = (RecyclerView)rootView.findViewById(R.id.flair_recycler_view);
        adapter = new MyRecyclerAdapter(fileNames);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        /**
         * Adding the surfaceView and associated code into a
         * fragment container. The default camera is just a
         * Surface view without any buttons.
         */
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.add(R.id.camera_container,cameraFragment);
        transaction.commit();

        cameraFragment.setmCallback(this);

        //  Order id up top.
        String orderNo = getActivity().getResources().getString(R.string.work_order_suffix)+order.getWorkOrderNumber();
        ((TextView)rootView.findViewById(R.id.field_order_id)).setText(orderNo);

        setListeners(rootView);
        return rootView;
    }

    private void setListeners(View rootView) {

        rootView.findViewById(R.id.button_image_capture).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cameraFragment.takePicture();
            }
        });

        rootView.findViewById(R.id.button_close_order).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCloseWindowDialog();
            }
        });

        rootView.findViewById(R.id.next_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendClickedImages();
            }
        });
    }

    /**
     * All the taken images are saved in an array list. This method
     * sends that array list to the calling activity.
     */
    private void sendClickedImages() {

        /**
         * Checking if mCallback is set by the calling activity or not,
         * if not, then we try to see if the calling activity has implemented
         * the OnFormFilled interface ot not.
         */

        if(mCallback == null) {
            try {
                mCallback = (OnCameraStarted) getActivity();
            }
            catch (ClassCastException e) {
                throw new ClassCastException("You must implement OnFormFilled interface in calling Activity.");
            }
        }

        List<ImagePair> imagePairList = new ArrayList<>();
        for(ImagePair imagePath : fileNames) {
            imagePairList.add(imagePath);
        }

        order.setImages(imagePairList);
        cameraFragment.closeCamera();
        mCallback.imagesCaptured(imagePairList);
    }


    /**
     * When the activity is getting closed, we must manually
     * close the camera. If we fail to do so, the camera would crash
     * the next time user clicks on Add Order.
     */
    @Override
    public void onStop() {
        cameraFragment.closeCamera();
        super.onStop();
    }

    /**
     * Called when we have successfully captured an image. The url where
     * the image was saved is returned. We can then use this url to
     * upload the image, or show this image on recyclerView.
     * @param name, url of image.
     */
    @Override
    public void onImageCaptured(String name) {

        Toast.makeText(getContext(), "Image saved : "+name, Toast.LENGTH_SHORT).show();
        String path = new File(Environment.getExternalStorageDirectory()+"/"+directory_name+"/"+name).getPath();

        Uri file = Uri.fromFile(new File(path));

        new FirebaseStorageApi().uploadImage(path);
//        Log.d(TAG, "onImageCaptured: "+path);
        fileNames.add(new ImagePair(path,path));
        /**
         * onImageCaptured is a callback method which is called asynchronously.
         * However, when we add the image to ArrayList, the process of notifying
         * the adapter about new element must happen on main thread.
         */
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();
                for(ImagePair paths : fileNames) {
                    Log.d(TAG, "run: "+paths);
                }
            }
        });
    }

    /**
     * In case we were not able to take a picture, an error message is returned.
     * This error message is shown on console.
     * @param error, the error message
     */
    @Override
    public void onError(String error) {
        Toast.makeText(getContext(), "Image not saved ", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onError: "+error);
    }

    private void showCloseWindowDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext())
                .setTitle("Are you sure ?")
                .setMessage("All your progress will be lost.")
                .setPositiveButton(R.string.label_dialog_yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        cameraFragment.closeCamera();
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
