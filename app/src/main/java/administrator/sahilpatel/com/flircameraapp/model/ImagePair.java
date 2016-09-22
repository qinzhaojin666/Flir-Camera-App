package administrator.sahilpatel.com.flircameraapp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by Administrator on 9/16/2016.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class ImagePair {

    private String flirImage;
    private String regularImage;

    public ImagePair() {
    }

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
