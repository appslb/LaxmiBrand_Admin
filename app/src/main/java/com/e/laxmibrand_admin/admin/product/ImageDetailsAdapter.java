package com.e.laxmibrand_admin.admin.product;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/*import com.app.sradesign.R;
import com.app.sradesign.fragment.admin.AdminMarketingFragment;*/

//import com.bumptech.glide.Glid
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.e.laxmibrand_admin.R;
import com.e.laxmibrand_admin.admin.AdminMarketing;
import com.e.laxmibrand_admin.admin.AdminPromotional;

import java.util.List;

public class ImageDetailsAdapter extends RecyclerView.Adapter<ImageDetailsAdapter.MyViewHolder> {

    Context context;
    String[] imageList;
    boolean fromCreate;

    public ImageDetailsAdapter(Context context, String[] imageList, boolean fromCreate) {
        this.context = context;
        this.imageList = imageList;
        this.fromCreate = fromCreate;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.image_list_item2, viewGroup, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder viewHolder, final int i) {

            try {
                    String S1 = imageList[i];
                    if (S1.contains("https"))
                        S1 = S1.replace("https", "http");

                    Log.i("image image :", S1);

                    Glide.with(context)
                            .load(S1)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .placeholder(R.drawable.watermark_icon)
                            .error(R.drawable.watermark_icon)
                            .into(viewHolder.imgView);


            } catch (Exception e) {
                Log.e("exc", e.getMessage());
            }


    }


    @Override
    public int getItemCount() {
        return imageList.length;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imgView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imgView = itemView.findViewById(R.id.imgView);
        }
    }

}
