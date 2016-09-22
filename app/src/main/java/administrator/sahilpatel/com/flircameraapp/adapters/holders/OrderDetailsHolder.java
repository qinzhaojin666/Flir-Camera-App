package administrator.sahilpatel.com.flircameraapp.adapters.holders;

import android.content.Context;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import administrator.sahilpatel.com.flircameraapp.R;
import administrator.sahilpatel.com.flircameraapp.adapters.MyRecyclerAdapter;
import administrator.sahilpatel.com.flircameraapp.model.Order;

/**
 * Created by Administrator on 9/19/2016.
 */
public class OrderDetailsHolder extends OrderHolder {


    private TextView field_work_order_title;
    private TextView field_work_order_number;
    private TextView field_work_order_description;
    private TextView field_customer_name;
    private TextView field_customer_address;
    private RecyclerView details_recycler_view;
    private MyRecyclerAdapter adapter;


    public OrderDetailsHolder(View itemView, Context context) {

        super(itemView);

        field_work_order_title = (TextView)itemView.findViewById(R.id.field_details_work_order_title);
        field_work_order_number = (TextView)itemView.findViewById(R.id.field_details_work_order_number);
        field_work_order_description = (TextView)itemView.findViewById(R.id.field_details_work_order_description);
        field_customer_name = (TextView)itemView.findViewById(R.id.field_details_customer_name);
        field_customer_address = (TextView)itemView.findViewById(R.id.field_details_customer_address);

        details_recycler_view = (RecyclerView)itemView.findViewById(R.id.details_item_recycler_view);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false);
        details_recycler_view.setLayoutManager(layoutManager);
        details_recycler_view.setItemAnimator(new DefaultItemAnimator());

    }

    @Override
    public void bindData(Order order, int position) {

        field_work_order_title.setText(order.getWorkOrderTitle());
        field_work_order_number.setText("WO# "+order.getWorkOrderNumber());

        adapter = new MyRecyclerAdapter(order.getImages());
        details_recycler_view.setAdapter(adapter);

        field_work_order_description.setText(order.getWorkOrderDescription());
        field_customer_name.setText(order.getCustomerName());
        field_customer_address.setText(order.getCustomerAddress());
    }
}
