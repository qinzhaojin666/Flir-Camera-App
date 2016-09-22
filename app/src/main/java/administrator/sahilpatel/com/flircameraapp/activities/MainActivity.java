package administrator.sahilpatel.com.flircameraapp.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;

import java.util.ArrayList;
import java.util.List;

import administrator.sahilpatel.com.flircameraapp.R;
import administrator.sahilpatel.com.flircameraapp.adapters.MainActivityRecyclerAdapter;
import administrator.sahilpatel.com.flircameraapp.connection.FirebaseOrdersApi;
import administrator.sahilpatel.com.flircameraapp.connection.FirebaseStorageApi;
import administrator.sahilpatel.com.flircameraapp.listeners.ClickListener;
import administrator.sahilpatel.com.flircameraapp.listeners.RecyclerTouchListener;
import administrator.sahilpatel.com.flircameraapp.model.Order;

public class MainActivity extends AppCompatActivity {

    public static final int REQUEST_FORM_SUBMISSION = 1;
    public static final int REQUEST_FORM_UPDATION = 11;
    public static final String UPDATE_ORDER_ID = "ORDER_ID";

    private static final String TAG = "MainActivity";

    private RecyclerView recyclerView;
    private MainActivityRecyclerAdapter recyclerAdapter;
    private List<Order> orders;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading data....");

        progressDialog.show();

        orders = new ArrayList<>();
        recyclerView = (RecyclerView)findViewById(R.id.main_activity_recycler_view);
        recyclerAdapter = new MainActivityRecyclerAdapter(orders);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(recyclerAdapter);


        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Order order =orders.get(position);
                Toast.makeText(MainActivity.this, order.getWorkOrderNumber(), Toast.LENGTH_SHORT).show();
                openUpdateWindow(order);
            }

            @Override
            public void onLongClick(View view, int position) {
                Toast.makeText(MainActivity.this, "We are not supporting long presses for now.", Toast.LENGTH_SHORT).show();
            }
        }));

        getOrders();

        findViewById(R.id.button_new_order).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent openIntent = new Intent(MainActivity.this,AddOrderFragmentContainerActivity.class);
                startActivityForResult(openIntent,REQUEST_FORM_SUBMISSION);
            }
        });

        //  todo REMOVE THIS AFTERWARDS.
        testFileUpload();
    }


    private void testFileUpload() {
        String path = "/storage/emulated/0/Flir Camera App/android.jpg";
        new FirebaseStorageApi().uploadImage(path);
    }

    private void openUpdateWindow(Order order) {

        Intent openIntent = new Intent(MainActivity.this,UpdateFragmentContainerActivity.class);
        openIntent.putExtra(UPDATE_ORDER_ID,order.getOrderId());
        startActivityForResult(openIntent,REQUEST_FORM_UPDATION);
    }

    private void getOrders() {

        new FirebaseOrdersApi().getAllOrders(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Order order = dataSnapshot.getValue(Order.class);
                order.setOrderId(dataSnapshot.getKey());
                orders.add(order);
                recyclerAdapter.notifyDataSetChanged();
                progressDialog.dismiss();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

                Order order = dataSnapshot.getValue(Order.class);
                orders.remove(order);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RESULT_OK) {

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
