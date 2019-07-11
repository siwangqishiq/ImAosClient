package com.xinlan.imclient.fragment;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.xinlan.imclient.R;
import com.xinlan.imclient.activity.SelectAblumActivity;
import com.xinlan.imclient.model.AblumImageItem;
import com.xinlan.imclient.util.ToastUtil;
import com.xinlan.imclient.widget.SpacesItemDecoration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public abstract class AblumSelectItemFragment  extends Fragment {
    private RecyclerView mGirdView;

    public abstract List<AblumImageItem> selectFromDataSource(final Bundle savedInstanceState);

    @Override
    public View onCreateView(final LayoutInflater inflater,
                             final ViewGroup container, final Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.frag_gallery, null);

        final List<AblumImageItem> dataList =selectFromDataSource(savedInstanceState);

        if (dataList == null || dataList.isEmpty()) {
            ToastUtil.show(getActivity() , R.string.no_images);
        } else {
            mGirdView = v.findViewById(R.id.grid);
            mGirdView.setLayoutManager(new GridLayoutManager(getContext(), 3));
            mGirdView.addItemDecoration(new SpacesItemDecoration(5));
            mGirdView.setAdapter(new ItemAdapter(dataList , (SelectAblumActivity) getActivity()));
        }
        return v;
    }

    public static class ItemAdapter extends RecyclerView.Adapter<ItemViewHolder> {
        private SelectAblumActivity activity;
        private List<AblumImageItem> mDataList;
        private LayoutInflater mLayoutInflater;

        public ItemAdapter(List<AblumImageItem> data , SelectAblumActivity context) {
            mDataList = data;
            mLayoutInflater = LayoutInflater.from(context);
            activity = context;
        }

        @Override
        public ItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int pos) {
            View itemView = mLayoutInflater.inflate(R.layout.view_ablum_item , viewGroup , false);
            return new ItemViewHolder(itemView , activity);
        }

        @Override
        public void onBindViewHolder(ItemViewHolder itemViewHolder, int pos) {
            final AblumImageItem item = mDataList.get(pos);
            itemViewHolder.refresh(item);
        }

        @Override
        public int getItemCount() {
            return mDataList == null ? 0 : mDataList.size();
        }
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView nameText;
        SelectAblumActivity activity;

        public ItemViewHolder(View itemView , SelectAblumActivity activity) {
            super(itemView);
            this.activity = activity;
            imageView = itemView.findViewById(R.id.img);
            nameText = itemView.findViewById(R.id.name);
        }

        public void refresh(final AblumImageItem item){
            Uri imageUri = Uri.fromFile(new File(item.path));
            Glide.with(imageView.getContext()).load(imageUri).into(imageView);

            if(item.isFolder){
                nameText.setVisibility(View.VISIBLE);
                final String text = String.format("%s - %d",item.name,item.images);
                nameText.setText(text);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        activity.showBucket(item.id);
                    }
                });
            }else{
                nameText.setVisibility(View.INVISIBLE);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        activity.selectImage(item);
                    }
                });
            }
        }
    }//end inner class

}
