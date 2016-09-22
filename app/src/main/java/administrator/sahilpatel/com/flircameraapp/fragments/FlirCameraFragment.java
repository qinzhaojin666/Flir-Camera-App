package administrator.sahilpatel.com.flircameraapp.fragments;


import android.content.DialogInterface;

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
import administrator.sahilpatel.com.flircameraapp.activities.AddOrderFragmentContainerActivity;
import administrator.sahilpatel.com.flircameraapp.adapters.MyRecyclerAdapter;
import administrator.sahilpatel.com.flircameraapp.camera.CameraFragment;
import administrator.sahilpatel.com.flircameraapp.listeners.ImageCaptureListener;
import administrator.sahilpatel.com.flircameraapp.listeners.OnCameraStarted;
import administrator.sahilpatel.com.flircameraapp.listeners.OnFormFilled;
import administrator.sahilpatel.com.flircameraapp.model.ImagePair;
import administrator.sahilpatel.com.flircameraapp.model.Order;

/**
 * A simple {@link Fragment} subclass.
 */
public class FlirCameraFragment extends Fragment implements ImageCaptureListener{

    private CameraFragment cameraFragment;
    private static final String TAG = "FlirCameraFragment";
    private String directory_name = "Flir Camera App";


    private Order order;
    private OnCameraStarted mCallback;
    View rootView;

    private List<ImagePair> fileNames;
    private RecyclerView recyclerView;
    private MyRecyclerAdapter adapter;

    public FlirCameraFragment() {
        // Required empty public constructor
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public void setmCallback(OnCameraStarted mCallback) {
        this.mCallback = mCallback;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_flair_camera, container, false);
        this.rootView = rootView;
        cameraFragment = new CameraFragment();

        fileNames = new ArrayList<>();


        recyclerView = (RecyclerView)rootView.findViewById(R.id.flair_recycler_view);
        adapter = new MyRecyclerAdapter(fileNames);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);


        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.add(R.id.camera_container,cameraFragment);
        transaction.commit();

        cameraFragment.setmCallback(this);


        ((TextView)rootView.findViewById(R.id.field_order_id)).setText(order.getWorkOrderNumber());

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

        return rootView;
    }

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

    @Override
    public void onStop() {
        cameraFragment.closeCamera();
        super.onStop();
    }

    @Override
    public void onImageCaptured(String name) {

        Toast.makeText(getContext(), "Image saved : "+name, Toast.LENGTH_SHORT).show();
        String path = new File(Environment.getExternalStorageDirectory()+"/"+directory_name+"/"+name).getPath();
        fileNames.add(new ImagePair(path,path));
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
