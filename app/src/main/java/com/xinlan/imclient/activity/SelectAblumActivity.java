package com.xinlan.imclient.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xinlan.imclient.R;
import com.xinlan.imclient.util.ToastUtil;
import com.xinlan.imsdk.Bean;
import com.xinlan.imsdk.core.TActivity;

import java.util.ArrayList;
import java.util.List;

public class SelectAblumActivity extends TActivity {

    public static void startForResult(Activity context, int requestCode) {
        Intent it = new Intent(context, SelectAblumActivity.class);
        context.startActivityForResult(it, requestCode);
    }


    @Override
    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        Fragment newFragment = new ImageFolderFragment();
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        transaction.replace(android.R.id.content, newFragment);
        transaction.commit();
    }

    @Override
    public void onReceivedMsg(Bean bean) {

    }

    public static class Item {
        String name;
        String path;
        String token;
        long size;
        int id;
        int images;

        public Item(String path, String name, String token, int id) {
            this.path = path;
            this.token = token;
            this.name = name;
            this.id = id;
        }
    }

    public static class ImageFolderFragment extends Fragment {
        private RecyclerView mGirdView;

        @Override
        public View onCreateView(final LayoutInflater inflater,
                                 final ViewGroup container, final Bundle savedInstanceState) {
            final View v = inflater.inflate(R.layout.frag_gallery, null);

            String[] projection = new String[]{
                    MediaStore.Images.Media.DATA,
                    MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
                    MediaStore.Images.Media.BUCKET_ID};
            Cursor cur = getActivity().getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection,
                    null, null, MediaStore.Images.Media.DATE_MODIFIED + " DESC");

            final List<Item> buckets = new ArrayList<Item>();
            Item lastBucket = null;

            if (cur != null) {
                if (cur.moveToFirst()) {
                    while (!cur.isAfterLast()) {
                        if (lastBucket == null || !lastBucket.name.equals(cur.getString(1))) {
                            lastBucket = new Item(cur.getString(0),
                                    cur.getString(1), "", cur.getInt(2));
                            buckets.add(lastBucket);
                        } else {
                            lastBucket.size++;
                        }
                        cur.moveToNext();
                    }
                }
                cur.close();
            }

            ToastUtil.show(getActivity(), "" + buckets.size());

            if (buckets.isEmpty()) {
                //ToastUtil.show(getActivity() , R.string.no_images);
                getActivity().finish();
            } else {
                mGirdView = v.findViewById(R.id.grid);
                mGirdView.setLayoutManager(new GridLayoutManager(getContext(), 3));

            }
            return v;
        }
    }//end inner class

    private static class ItemViewHolder extends RecyclerView.ViewHolder {
        public ItemViewHolder(View itemView) {
            super(itemView);
        }
    }

    private static class ItemAdapter extends RecyclerView.Adapter<ItemViewHolder> {
        private List<Item> mDataList;
        private LayoutInflater mLayoutInflater;

        public ItemAdapter(List<Item> data) {
            mDataList = data;
        }

        @Override
        public ItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int pos) {
            return new ItemViewHolder(null);
        }

        @Override
        public void onBindViewHolder(@NonNull ItemViewHolder itemViewHolder, int pos) {

        }

        @Override
        public int getItemCount() {
            return mDataList == null ? 0 : mDataList.size();
        }
    }
}//end class
