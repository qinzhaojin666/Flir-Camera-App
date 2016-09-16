package administrator.sahilpatel.com.flircameraapp.adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

import administrator.sahilpatel.com.flircameraapp.R;
import administrator.sahilpatel.com.flircameraapp.model.ImagePair;
import administrator.sahilpatel.com.flircameraapp.model.Order;

/**
 * Created by Administrator on 9/16/2016.
 */
public class MainActivityRecyclerAdapter extends RecyclerView.Adapter<MainActivityRecyclerAdapter.MyViewHolder> {

    private static final String TAG = "MainActivityRecyclerAdapter";
    private List<Order> orderList;

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item_layout,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        Order order = orderList.get(position);

        holder.work_order_title.setText(order.getWorkOrderTitle());
        holder.work_order_number.setText(order.getWorkOrderNumber());
        holder.work_order_assigned_to.setText(order.getAssignedTo());

        ImagePair firstImage = order.getImages().get(0);
        Bitmap scaled = this.scaleDownImage(BitmapFactory.decodeFile(firstImage.getFlirImage()));
        holder.flir_image.setImageBitmap(scaled);
        scaled = this.scaleDownImage(BitmapFactory.decodeFile(firstImage.getRegularImage()));
        holder.regular_image.setImageBitmap(scaled);
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView flir_image,regular_image;
        TextView work_order_title;
        TextView work_order_number;
        TextView work_order_assigned_to;

        public MyViewHolder(View itemView) {

            super(itemView);
            flir_image = (ImageView)itemView.findViewById(R.id.field_order_image_flair);
            regular_image = (ImageView)itemView.findViewById(R.id.field_order_image_regular);
            work_order_title = (TextView)itemView.findViewById(R.id.field_item_work_order_title);
            work_order_number = (TextView)itemView.findViewById(R.id.field_work_order_number);
            work_order_assigned_to = (TextView)itemView.findViewById(R.id.field_order_work_order_assigned_to);

        }
    }

    private Bitmap scaleDownImage(Bitmap bitmap) {
        try {
            int nh = (int) ( bitmap.getHeight() * (512.0 / bitmap.getWidth()) );
            Bitmap scaled = Bitmap.createScaledBitmap(bitmap, 512, nh, true);
            return scaled;

        } catch (Throwable e) {
            e.printStackTrace();
        }
        return bitmap;
    }
}
