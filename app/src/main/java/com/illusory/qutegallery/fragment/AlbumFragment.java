package com.illusory.qutegallery.fragment;

import androidx.lifecycle.ViewModelProvider;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.illusory.qutegallery.R;
import com.illusory.qutegallery.adapter.AlbumAdapter;
import com.illusory.qutegallery.model.Album;
import com.illusory.qutegallery.util.Log;
import com.illusory.qutegallery.util.PermissionHelper;
import com.illusory.qutegallery.viewmodel.AlbumViewModel;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class AlbumFragment extends Fragment {
    private AlbumViewModel viewModel;

    private RecyclerView m_rvAlbum;
    private AlbumAdapter adapter;
    private int currentPosition = -1;
    private int column;
    private final Map<String, Album> albums = new TreeMap<>(String::compareTo);

    public static AlbumFragment newInstance() {
        return new AlbumFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Log.i("onCreateView");
        View root = inflater.inflate(R.layout.album_fragment, container, false);
        m_rvAlbum = root.findViewById(R.id.rv_album);
        column = getContext().getResources().getDisplayMetrics().widthPixels / AlbumAdapter.FIX_SIZE;
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), column);
        layoutManager.setOrientation(GridLayoutManager.VERTICAL);
        m_rvAlbum.setLayoutManager(layoutManager);

        if (PermissionHelper.storagePermissionsGranted(getActivity()))
            initData();
        else
            PermissionHelper.checkAndRequestStoragePermissions(getActivity());

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.i("onDestroyView");
        currentPosition = -1;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //  viewmodel's lifecycle is the same with provider (fragment or activity)
        viewModel = new ViewModelProvider(this).get(AlbumViewModel.class);
    }

    public void initData() {
        scanMedia();

        adapter = new AlbumAdapter(albums);
        adapter.setColumn(column);
        adapter.setOnItemClickListener(position -> {
            if (currentPosition == position)
                return;

            if (currentPosition != -1)
                adapter.notifyItemChanged(currentPosition, AlbumAdapter.PAYLOAD_LOSE_SELECT);
            adapter.notifyItemChanged(position, AlbumAdapter.PAYLOAD_GET_SELECT);
            currentPosition = position;
            adapter.setCurrentPosition(currentPosition);
        });
        m_rvAlbum.setAdapter(adapter);
    }

    private void scanMedia() {
        Uri external = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        ContentResolver resolver = getContext().getContentResolver();
//        String selection = MediaStore.Images.Media.DISPLAY_NAME + " LIKE ?";  // SELECT _id FROM images WHERE (title LIKE _2%3)
//        String[] args = new String[]{"Image%"};
//        String[] projection = new String[]{MediaStore.Images.Media._ID};
//        try (Cursor cursor = resolver.query(external, projection, selection, args, null)) {
//            if (cursor != null) {
//                while (cursor.moveToNext()) {
//                    Uri uri = ContentUris.withAppendedId(external, cursor.getLong(0));
//                    Log.i(uri.toString());  // content://media/external/images/media/2404
//                }
//            }
//        }

        Cursor cursor = resolver.query(external,
                null, null, null, MediaStore.Images.Media.DATA + " ASC");
        if (cursor != null && cursor.moveToFirst()) {
            do {
//                int count = cursor.getColumnCount();
//                Log.i("column count:" + count);
//                for (int i = 0; i < count; ++i) {
//                    String columnName = cursor.getColumnName(i);
//                    String value = cursor.getString(cursor.getColumnIndex(columnName));
//                    Log.i( columnName + ":" + value);
//                }
                /*
                _id:2197
                _data:/storage/emulated/0/Pictures/5.jpg
                _size:102699
                _display_name:5.jpg
                mime_type:image/jpeg
                title:5
                date_added:1358499025
                date_modified:1358499347
                description:null
                picasa_id:null
                isprivate:null
                latitude:null
                longitude:null
                datetaken:1358499347000
                orientation:0
                mini_thumb_magic:null
                bucket_id:-1617409521
                bucket_display_name:Pictures
                width:1200
                height:1200
                **/
                String filePath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                Log.i("DATA:" + filePath);
                File file = new File(filePath);
                File parent = file.getParentFile();
                String key = parent.getAbsolutePath();
                if (albums.containsKey(key)) {
                    albums.get(key).medias.add(file.getName());
                } else {
                    Album album = new Album();
                    album.folderName = parent.getName();
                    album.fullPath = key;
                    album.medias = new ArrayList<>();
                    album.medias.add(file.getName());
                    albums.put(album.fullPath, album);
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
    }
}