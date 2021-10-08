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
import com.e.laxmibrand_admin.R;
import com.e.laxmibrand_admin.admin.AdminMarketing;
import com.e.laxmibrand_admin.admin.AdminPromotional;

import java.util.List;

public class ImageListAdapter extends RecyclerView.Adapter<ImageListAdapter.MyViewHolder> {

    Context context;
    List<Uri> imageList;
    boolean fromCreate;

    public ImageListAdapter(Context context, List<Uri> imageList, boolean fromCreate) {
        this.context = context;
        this.imageList = imageList;
        this.fromCreate = fromCreate;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.image_list_item, viewGroup, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder viewHolder, final int i) {

        try {
  //          Glide.with(context).load(imageList.get(i)).error(R.mipmap.ic_launcher).into(viewHolder.imgView);
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), imageList.get(i));
            viewHolder.imgView.setImageBitmap(bitmap);

            viewHolder.remvBTN.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    imageList.remove(i);
                    if (fromCreate) {
                        if(AddProduct.imgList!=null) {
                            if (AddProduct.imgList.size() > 0) {
                                AddProduct.imgList.remove(i);
                            }
                        }
                        if(AdminMarketing.imgList!=null) {

                            if (AdminMarketing.imgList.size() > 0) {
                                AdminMarketing.imgList.remove(i);
                            }
                        }
                        if(AdminPromotional.imgList!=null) {

                            if (AdminPromotional.imgList.size() > 0) {
                                AdminPromotional.imgList.remove(i);
                            }
                        }
                       /* if (AddProduct.imgList.size() < 5) {
                            AddProduct.nextBTN.setVisibility(View.GONE);
                            AdminCreateCatalogueActivity.submitBTN.setVisibility(View.GONE);
                        }*/
                    } else {
                       /* AdminMarketingFragment.imgList.remove(i);
                        if (AdminMarketingFragment.imgList.size() == 0) {
                            AdminMarketingFragment.saveBTN.setVisibility(View.GONE);
                        } else {
                            AdminMarketingFragment.saveBTN.setVisibility(View.VISIBLE);
                        }
                        if (AdminMarketingFragment.imgList.size() == 5) {
                            AdminMarketingFragment.uploadImgBTN.setVisibility(View.GONE);
                        } else {
                            AdminMarketingFragment.uploadImgBTN.setVisibility(View.VISIBLE);
                        }*/
                    }
                    notifyDataSetChanged();
                }
            });
        } catch (Exception e) {
            Log.e("exc", e.getMessage());
        }

    }


    @Override
    public int getItemCount() {
        return imageList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imgView, remvBTN;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            remvBTN = itemView.findViewById(R.id.remvBTN);
            imgView = itemView.findViewById(R.id.imgView);
        }
    }

}
