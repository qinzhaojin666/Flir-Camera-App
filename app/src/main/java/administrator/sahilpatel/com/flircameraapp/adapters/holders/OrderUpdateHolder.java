package administrator.sahilpatel.com.flircameraapp.adapters.holders;

import android.content.Context;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import administrator.sahilpatel.com.flircameraapp.R;
import administrator.sahilpatel.com.flircameraapp.adapters.MyRecyclerAdapter;
import administrator.sahilpatel.com.flircameraapp.model.Order;
import administrator.sahilpatel.com.flircameraapp.model.Update;

/**
 * Created by Administrator on 9/19/2016.
 */
public class OrderUpdateHolder extends OrderHolder {

    private TextView field_update_notes;
    private TextView field_time_spent;
    private RecyclerView update_recycler_view;
    MyRecyclerAdapter adapter;


    public OrderUpdateHolder(View itemView, Context context) {
        super(itemView);


        field_update_notes = (TextView)itemView.findViewById(R.id.field_update_notes);
        field_time_spent = (TextView)itemView.findViewById(R.id.update_time_spent);
        update_recycler_view = (RecyclerView)itemView.findViewById(R.id.update_recycler_view);


        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false);
        update_recycler_view.setLayoutManager(layoutManager);
        update_recycler_view.setItemAnimator(new DefaultItemAnimator());
    }


    @Override
    public void bindData(Order order, int position) {

        position = position - 1;
        Update update = order.getUpdates().get(position);

        adapter = new MyRecyclerAdapter(update.getImagePairList());
        update_recycler_view.setAdapter(adapter);
        field_update_notes.setText(update.getUpdateNotes());
        field_time_spent.setText(update.getTimeSpent());

    }
}
