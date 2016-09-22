package administrator.sahilpatel.com.flircameraapp.connection;

import com.firebase.client.Firebase;
import com.google.firebase.storage.StorageReference;

/**
 * Created by Administrator on 9/19/2016.
 */
public final class FirebaseApi {

    private static final String baseUrl = "https://flircameraapp.firebaseio.com/";
    private static final String baseFilesUrl = "gs://flircameraapp.appspot.com";
    private static Firebase rootRef;
    private static StorageReference filesRef;

    public  static Firebase getRootRef(){
        if (rootRef == null) {
            rootRef = new Firebase(baseUrl);
        }
        return rootRef;
    }



    public  static Firebase getRef(String url){
        return new Firebase(baseUrl+url);
    }

}
