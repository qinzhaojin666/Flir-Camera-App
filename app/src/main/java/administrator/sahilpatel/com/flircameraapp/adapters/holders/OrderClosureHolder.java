package administrator.sahilpatel.com.flircameraapp.adapters.holders;

import android.content.Context;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import administrator.sahilpatel.com.flircameraapp.R;
import administrator.sahilpatel.com.flircameraapp.adapters.MyRecyclerAdapter;
import administrator.sahilpatel.com.flircameraapp.model.Closure;
import administrator.sahilpatel.com.flircameraapp.model.Order;

/**
 * Created by Administrator on 9/19/2016.
 */
public class OrderClosureHolder extends OrderHolder {

    private TextView field_closure_notes;
    private RecyclerView closure_recycler_view;
    private MyRecyclerAdapter adapter;

    public OrderClosureHolder(View itemView, Context context) {
        super(itemView);

        field_closure_notes = (TextView)itemView.findViewById(R.id.field_closure_notes);
        closure_recycler_view = (RecyclerView)itemView.findViewById(R.id.closure_recycler_view);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false);
        closure_recycler_view.setLayoutManager(layoutManager);
        closure_recycler_view.setItemAnimator(new DefaultItemAnimator());

    }

    @Override
    public void bindData(Order order, int position) {

        Closure closure = order.getClosure();
        field_closure_notes.setText(closure.getClosureNotes());

        adapter = new MyRecyclerAdapter(closure.getImagePairList());
        closure_recycler_view.setAdapter(adapter);
    }
}
