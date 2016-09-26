package administrator.sahilpatel.com.flircameraapp.adapters;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

import administrator.sahilpatel.com.flircameraapp.R;
import administrator.sahilpatel.com.flircameraapp.model.ImagePair;

public class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.MyViewHolder>{

    private static final String TAG = "MyRecyclerAdapter";
    private List<ImagePair> imagesList;
    public MyRecyclerAdapter(List<ImagePair> imagesList) {
        this.imagesList = imagesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: "+imagesList.size());
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_item_layout,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        Log.d(TAG, "onBindViewHolder: "+imagesList.get(position));
        try {
            ImagePair pair = imagesList.get(position);
            Bitmap bitmap = BitmapFactory.decodeFile(pair.getFlirImage());
            int nh = (int) ( bitmap.getHeight() * (512.0 / bitmap.getWidth()) );
            Bitmap scaled = Bitmap.createScaledBitmap(bitmap, 512, nh, true);
            holder.imgFlirThumbnail.setImageBitmap(scaled);

            bitmap = BitmapFactory.decodeFile(pair.getRegularImage());
            nh = (int) ( bitmap.getHeight() * (512.0 / bitmap.getWidth()) );
            scaled = Bitmap.createScaledBitmap(bitmap, 512, nh, true);
            holder.imgRegularThumbnail.setImageBitmap(scaled);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return imagesList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView imgFlirThumbnail;
        ImageView imgRegularThumbnail;

        public MyViewHolder(View view) {
            super(view);
            imgFlirThumbnail = (ImageView)view.findViewById(R.id.img_flir_thumbnail);
            imgRegularThumbnail = (ImageView)view.findViewById(R.id.img_regular_thumbnail);
        }
    }
}