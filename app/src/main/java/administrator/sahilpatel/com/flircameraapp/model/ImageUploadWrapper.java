package administrator.sahilpatel.com.flircameraapp.model;

import java.io.File;

/**
 * Created by Administrator on 9/26/2016.
 */
public class ImageUploadWrapper {


    private String index;
    private boolean status;
    private File file;

    public ImageUploadWrapper(String index,File file) {
        this.status = false;
        this.file = file;
        this.index = index;
    }

    public boolean isStatus() {
        return status;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public synchronized void setStatus(boolean status) {
        this.status = status;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }
}
