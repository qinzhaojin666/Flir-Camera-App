package administrator.sahilpatel.com.flircameraapp.model;

import android.media.Image;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 9/19/2016.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class Update {

    private String updateId;
    private String updateNotes;
    private String timeSpent;
    private List<ImagePair> imagePairList;

    public Update() {
        imagePairList = new ArrayList<>();
    }

    public List<ImagePair> getImagePairList() {
        return imagePairList;
    }

    public void setImagePairList(List<ImagePair> imagePairList) {
        this.imagePairList = imagePairList;
    }

    public String getUpdateId() {
        return updateId;
    }

    public void setUpdateId(String updateId) {
        this.updateId = updateId;
    }

    public String getTimeSpent() {
        return timeSpent;
    }

    public void setTimeSpent(String timeSpent) {
        this.timeSpent = timeSpent;
    }

    public String getUpdateNotes() {
        return updateNotes;
    }

    public void setUpdateNotes(String updateNotes) {
        this.updateNotes = updateNotes;
    }
}
