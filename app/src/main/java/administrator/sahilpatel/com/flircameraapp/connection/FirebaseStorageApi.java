package administrator.sahilpatel.com.flircameraapp.connection;


import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import com.google.firebase.storage.FirebaseStorage;

import com.google.firebase.storage.OnProgressListener;

import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;

import administrator.sahilpatel.com.flircameraapp.listeners.ImageUploadCallback;


public class FirebaseStorageApi {

    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReferenceFromUrl("gs://flircameraapp.appspot.com");

    private static final String TAG = "FirebaseStorageApi";

    public void uploadImage(String pathToFile) {

        Uri file = Uri.fromFile(new File(pathToFile));
        Log.d(TAG, "uploadImage: "+file.getLastPathSegment());

        StorageReference mRef = storageRef.child("images/"+file.getLastPathSegment());
        UploadTask uploadTask = mRef.putFile(file);

        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Uri downloadUrl = taskSnapshot.getMetadata().getDownloadUrl();
                Log.d(TAG, "onSuccess: "+downloadUrl.toString());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: "+e.getMessage());
            }
        });
    }

    public void uploadImage(String pathToFile, OnSuccessListener<UploadTask.TaskSnapshot> successListener,
                            OnProgressListener<UploadTask.TaskSnapshot> progressListener) {

        Uri file = Uri.fromFile(new File(pathToFile));
        Log.d(TAG, "uploadImage: "+file.getLastPathSegment());

        StorageReference mRef = storageRef.child("images/"+file.getLastPathSegment());
        UploadTask uploadTask = mRef.putFile(file);

        uploadTask.addOnSuccessListener(successListener)
                .addOnProgressListener(progressListener)
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d(TAG, "onFailure: "+e.getMessage());
                            }
                        });

    }


    public void uploadImage(File f, final int index, final String title, final ImageUploadCallback callback) {

        Uri file = Uri.fromFile(f);
        StorageReference mRef = storageRef.child("images/"+file.getLastPathSegment());
        UploadTask uploadTask = mRef.putFile(file);

        uploadTask
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Uri downloadUrl = taskSnapshot.getMetadata().getDownloadUrl();
                        callback.onImageUploaded(index,title,downloadUrl.getPath());
                        callback.nextImage(index);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        callback.onUploadFailure(index,title,e.getLocalizedMessage());
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                        callback.imageUploadProgress(index,title,progress);
                    }
                });
    }
}
