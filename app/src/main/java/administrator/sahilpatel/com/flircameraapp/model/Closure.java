package administrator.sahilpatel.com.flircameraapp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 9/19/2016.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class Closure {

    private String closureNotes;
    private List<ImagePair> imagePairList;

    public Closure() {
        imagePairList = new ArrayList<>();
    }

    public String getClosureNotes() {
        return closureNotes;
    }

    public void setClosureNotes(String closureNotes) {
        this.closureNotes = closureNotes;
    }

    public void setImagePairList(List<ImagePair> imagePairList) {
        this.imagePairList = imagePairList;
    }

    public List<ImagePair> getImagePairList() {
        return imagePairList;
    }
}
