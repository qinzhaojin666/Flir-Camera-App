package administrator.sahilpatel.com.flircameraapp.listeners;

/**
 * Created by Administrator on 9/15/2016.
 */
public interface ImageCaptureListener {

    void onImageCaptured(String url);
    void onError(String error);
}
