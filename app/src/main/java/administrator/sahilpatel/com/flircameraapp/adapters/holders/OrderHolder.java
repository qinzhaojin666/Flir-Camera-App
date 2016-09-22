package administrator.sahilpatel.com.flircameraapp.adapters.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.List;

import administrator.sahilpatel.com.flircameraapp.model.Order;
import administrator.sahilpatel.com.flircameraapp.model.Update;

/**
 * Created by Administrator on 9/19/2016.
 */
public abstract class OrderHolder extends RecyclerView.ViewHolder{


    public OrderHolder(View itemView) {
        super(itemView);
    }

    public abstract void bindData(Order order, int position);


}
