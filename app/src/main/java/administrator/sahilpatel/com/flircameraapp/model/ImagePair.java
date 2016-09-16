package administrator.sahilpatel.com.flircameraapp.model;

/**
 * Created by Administrator on 9/16/2016.
 */
public class ImagePair {

    private String flirImage;
    private String regularImage;

    public ImagePair(String flirImage, String regularImage) {
        this.flirImage = flirImage;
        this.regularImage = regularImage;
    }

    public String getFlirImage() {
        return flirImage;
    }

    public void setFlirImage(String flirImage) {
        this.flirImage = flirImage;
    }

    public String getRegularImage() {
        return regularImage;
    }

    public void setRegularImage(String regularImage) {
        this.regularImage = regularImage;
    }
}
