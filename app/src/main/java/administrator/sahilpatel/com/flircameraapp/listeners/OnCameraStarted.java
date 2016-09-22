package administrator.sahilpatel.com.flircameraapp.listeners;

import java.util.List;

import administrator.sahilpatel.com.flircameraapp.model.ImagePair;

/**
 * Created by Administrator on 9/20/2016.
 */
public interface OnCameraStarted {

    void imagesCaptured(List<ImagePair> images);
}
