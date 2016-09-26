package administrator.sahilpatel.com.flircameraapp.listeners;


/**
 * Created by Administrator on 9/26/2016.
 */
public interface ImageUploadListener {

    void onImageUploaded(int imageIndex,String imagePath);
    void onUploadProgress(int imageIndex, double progress);
    void onUploadFailure(int imageIndex,String reason);
    void onAllImagesUploaded();
}
