package administrator.sahilpatel.com.flircameraapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import administrator.sahilpatel.com.flircameraapp.R;
import administrator.sahilpatel.com.flircameraapp.adapters.holders.OrderClosureHolder;
import administrator.sahilpatel.com.flircameraapp.adapters.holders.OrderDetailsHolder;
import administrator.sahilpatel.com.flircameraapp.adapters.holders.OrderHolder;
import administrator.sahilpatel.com.flircameraapp.adapters.holders.OrderUpdateHolder;
import administrator.sahilpatel.com.flircameraapp.model.Order;
import administrator.sahilpatel.com.flircameraapp.model.Update;

/**
 * Created by Administrator on 9/19/2016.
 */
public class UpdatesRecyclerAdapter extends RecyclerView.Adapter<OrderHolder>{

    private Order order;
    private List<Update> updates;
    private Context context;

    public static final int VIEW_TYPE_DETAILS = 1;
    public static final int VIEW_TYPE_UPDATE = 2;
    public static final int VIEW_TYPE_CLOSURE = 3;


    public UpdatesRecyclerAdapter(Order order, Context context) {
        this.order = order;
        updates = order.getUpdates();
        this.context = context;
    }

    @Override
    public OrderHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = null;

        switch (viewType) {
            case VIEW_TYPE_DETAILS :
                itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.details_item_layout,parent,false);
                return new OrderDetailsHolder(itemView, context);
            case VIEW_TYPE_CLOSURE :
                itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_closure_item_layout,parent,false);
                return  new OrderClosureHolder(itemView, context);
            case VIEW_TYPE_UPDATE :
                itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_update_item_layout,parent,false);
                return new OrderUpdateHolder(itemView, context);
        }

        return new OrderDetailsHolder(itemView, context);
    }

    @Override
    public void onBindViewHolder(OrderHolder holder, int position) {
        holder.bindData(order,position);
    }


    @Override
    public int getItemViewType(int position) {


        if(position == 0) {
            return VIEW_TYPE_DETAILS;
        }


        if(order.getClosure() != null) {
            if(position == getItemCount()-1){
                return VIEW_TYPE_CLOSURE;
            }
        }
        return VIEW_TYPE_UPDATE;
    }

    @Override
    public int getItemCount() {

        /**
         * If we no updates and no closure, Item count is 1
         * If we have no updates and closure, Item count is 2
         * If we have updates but no closure, Item count is 1 + updates.size
         * If we have updates and closure both, Item count is 2 + updates.size
         */

        if(updates == null && order.getClosure() == null) {
            return 1;
        }

        if(updates == null && order.getClosure() != null) {
            return 2;
        }

        if(updates != null && order.getClosure() == null) {
            return 1 + updates.size();
        }

       if(updates != null && order.getClosure() != null) {
           return 2 + updates.size();
       }

        return 1;
    }



//    @Override
//    public int getItemCount() {
//
//        /**
//         * If we no updates and no closure, Item count is 1
//         * If we have no updates and closure, Item count is 2
//         * If we have updates but no closure, Item count is 1 + updates.size
//         * If we have updates and closure both, Item count is 2 + updates.size
//         */
//
//        if(updates == null && order.getClosure() == null) {
//            return 0;
//        }
//
//        if(updates == null && order.getClosure() != null) {
//            return 1;
//        }
//
//        if(updates != null && order.getClosure() == null) {
//            return 0 + updates.size();
//        }
//
//        if(updates != null && order.getClosure() != null) {
//            return 1 + updates.size();
//        }
//
//        return 0;
//    }
}
