package administrator.sahilpatel.com.flircameraapp.listeners;

import java.io.File;

/**
 * Created by Administrator on 9/26/2016.
 */
public interface ImageUploadCallback {

    void nextImage(int uploadedImageIndex);
    void imageUploadProgress(int index,String title, double progress);
    void onUploadFailure(int index,String title, String reason);
    void onImageUploaded(int index,String title,String imagePath);
}
