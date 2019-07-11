package com.xinlan.imclient.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.xinlan.imclient.fragment.AblumSelectItemFragment;
import com.xinlan.imclient.model.AblumImageItem;
import com.xinlan.imsdk.Bean;
import com.xinlan.imsdk.core.TActivity;

import java.util.ArrayList;
import java.util.List;

public class SelectAblumActivity extends TActivity {
    public static final String RESULT_PATH = "path";
    public static final String RESULT_SIZE = "size";

    private static final String BUCKET_ID = "bucket_id";

    public static void startForResult(Activity context, int requestCode) {
        Intent it = new Intent(context, SelectAblumActivity.class);
        context.startActivityForResult(it, requestCode);
    }


    @Override
    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        Fragment newFragment = new FolderImageFragment();
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        transaction.replace(android.R.id.content, newFragment);
        transaction.commit();
    }

    public void showBucket(final int bucketId) {
        Bundle b = new Bundle();
        b.putInt(BUCKET_ID, bucketId);
        Fragment f = new ImagesFragment();
        f.setArguments(b);
        getSupportFragmentManager().beginTransaction()
                .replace(android.R.id.content, f).addToBackStack(null).commit();
    }

    public void selectImage(final AblumImageItem item){
        if(item == null)
            return;

        Intent result = new Intent();
        result.putExtra(RESULT_PATH, item.path);
        result.putExtra(RESULT_SIZE, item.size);
        setResult(RESULT_OK, result);
        finish();
    }

    @Override
    public void onReceivedMsg(Bean bean) {

    }

    public static class FolderImageFragment extends AblumSelectItemFragment{
        @Override
        public List<AblumImageItem> selectFromDataSource(Bundle savedInstanceState) {
            final List<AblumImageItem> list = new ArrayList<AblumImageItem>();

            String[] projection = new String[]{
                    MediaStore.Images.Media.DATA,
                    MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
                    MediaStore.Images.Media.BUCKET_ID};
            Cursor cur = getActivity().getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection,
                    null, null,
                    MediaStore.Images.Media.BUCKET_DISPLAY_NAME + " ASC, " + MediaStore.Images.Media.DATE_MODIFIED + " DESC");

            AblumImageItem lastBucket = null;
            if (cur != null) {
                if (cur.moveToFirst()) {
                    while (!cur.isAfterLast()) {
                        if (lastBucket == null || !lastBucket.name.equals(cur.getString(1))) {
                            lastBucket = new AblumImageItem(cur.getString(0),
                                    cur.getString(1),  cur.getInt(2));
                            list.add(lastBucket);
                            lastBucket.images = 1;
                            lastBucket.isFolder = true;
                        } else {
                            lastBucket.images++;
                        }
                        cur.moveToNext();
                    }
                }
                cur.close();
            }
            return list;
        }
    }//end inner class

    public static class ImagesFragment extends AblumSelectItemFragment{
        @Override
        public List<AblumImageItem> selectFromDataSource(Bundle savedInstanceState) {

            Cursor cur = getActivity().getContentResolver()
                    .query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                            new String[] { MediaStore.Images.Media.DATA,
                                    MediaStore.Images.Media.DISPLAY_NAME,
                                    MediaStore.Images.Media.SIZE },
                            MediaStore.Images.Media.BUCKET_ID + " = ?",
                            new String[] { String.valueOf(getArguments().getInt(BUCKET_ID)) },
                            MediaStore.Images.Media.DATE_MODIFIED + " DESC");

            final List<AblumImageItem> images = new ArrayList<AblumImageItem>();
            if (cur != null) {
                if (cur.moveToFirst()) {
                    while (!cur.isAfterLast()) {
                        AblumImageItem item = new AblumImageItem(cur.getString(0),cur.getString(1));
                        item.size = cur.getInt(2);
                        item.isFolder = false;
                        images.add(item);
                        cur.moveToNext();
                    }
                }
                cur.close();
            }
            return images;
        }
    }
}//end class
