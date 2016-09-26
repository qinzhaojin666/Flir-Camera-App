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

import com.badoualy.stepperindicator.StepperIndicator;

import java.util.List;
import java.util.Map;

import administrator.sahilpatel.com.flircameraapp.R;
import administrator.sahilpatel.com.flircameraapp.model.ImagePair;
import administrator.sahilpatel.com.flircameraapp.model.Order;


public class MainActivityRecyclerAdapter extends RecyclerView.Adapter<MainActivityRecyclerAdapter.MyViewHolder> {

    private static final String TAG = "MainActivityAdapter";
    private List<Order> orderList;

    public MainActivityRecyclerAdapter(List<Order> orderList) {
        this.orderList = orderList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item_layout,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        Order order = orderList.get(position);

        holder.work_order_title.setText(order.getWorkOrderTitle());
        holder.work_order_number.setText("WO#: "+order.getWorkOrderNumber());
        if(order.getAssignedTo() == null){
            order.setAssignedTo("Not assigned");
        }
        holder.work_order_assigned_to.setText("Assigned to: "+order.getAssignedTo());

        ImagePair firstImage = order.getImages().get(0);
//        Log.d(TAG, "onBindViewHolder: "+firstImage.getFlirImage());
        Bitmap scaled = this.scaleDownImage(BitmapFactory.decodeFile(firstImage.getFlirImage()));
        holder.flir_image.setImageBitmap(scaled);
        scaled = this.scaleDownImage(BitmapFactory.decodeFile(firstImage.getRegularImage()));
        holder.regular_image.setImageBitmap(scaled);
        holder.indicator.setCurrentStep(this.stepperIndicatorValue(order.getStatus()));
        holder.work_order_status.setText(order.getStatus());
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
        TextView work_order_status;
        StepperIndicator indicator;

        public MyViewHolder(View itemView) {

            super(itemView);
            flir_image = (ImageView)itemView.findViewById(R.id.field_order_image_flair);
            regular_image = (ImageView)itemView.findViewById(R.id.field_order_image_regular);
            work_order_title = (TextView)itemView.findViewById(R.id.field_item_work_order_title);
            work_order_number = (TextView)itemView.findViewById(R.id.field_item_work_order_number);
            work_order_assigned_to = (TextView)itemView.findViewById(R.id.field_item_work_order_assigned_to);
            work_order_status = (TextView)itemView.findViewById(R.id.field_indicator_status);
            indicator = (StepperIndicator) itemView.findViewById(R.id.stepper_indicator);
            indicator.setStepCount(3);
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

    private int stepperIndicatorValue(String status) {

        switch (status) {
            case Order.STATUS_NEW : return 1;
            case Order.STATUS_IN_PROGRESS : return 2;
            case Order.STAUS_CLOSED : return 3;
        }

        return 2;
    }
}
