package administrator.sahilpatel.com.flircameraapp.connection;

import android.content.Context;
import android.net.Uri;

import com.firebase.client.ChildEventListener;
import com.firebase.client.Firebase;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import administrator.sahilpatel.com.flircameraapp.model.Closure;
import administrator.sahilpatel.com.flircameraapp.model.ImagePair;
import administrator.sahilpatel.com.flircameraapp.model.Order;
import administrator.sahilpatel.com.flircameraapp.model.Update;

/**
 * Created by Administrator on 9/19/2016.
 */
public class FirebaseOrdersApi {

    private static final String TAG = "FirebaseOrdersApi";

    private final String url = "/orders";
    private final Firebase mRef = FirebaseApi.getRef(url);

    public FirebaseOrdersApi() {}

    public String addOrder(Order order) {
        Firebase newRef = mRef.push();
        newRef.setValue(order);
        return newRef.getKey();
    }

    public String addOrder(Order order, OnSuccessListener<UploadTask.TaskSnapshot> successListener,
                           OnProgressListener<UploadTask.TaskSnapshot> progressListener) {

        return "";
    }

    public void getAllOrders(ChildEventListener listener) {
       mRef.addChildEventListener(listener);

    }


    public void fetchOrder(String key,ValueEventListener listener) {
        Firebase tRef = mRef.child("/"+key);

        tRef.addListenerForSingleValueEvent(listener);
    }


    public void updateOrder(String key, Update update,int childKey) {
        Firebase newRef = mRef.child(key+"/updates/"+childKey);

        newRef.setValue(update);
    }

    public void reOpenWorkOrder(String key) {
        Firebase newRef = mRef.child(key+"/closure");
        newRef.removeValue();

        changeOrderStatus(key, Order.STATUS_NEW);
    }

    public void closeOrder(String key, Closure closure) {
        Firebase newRef = mRef.child("/"+key+"/closure");
        newRef.setValue(closure);
        changeOrderStatus(key,Order.STAUS_CLOSED);
    }

    public void changeOrderStatus(String key,String status) {
        Firebase newRef = mRef.child("/"+key+"/status");
        newRef.setValue(status);
    }

}
