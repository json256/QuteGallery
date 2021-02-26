package com.illusory.qutegallery.adapter;

import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.illusory.qutegallery.R;
import com.illusory.qutegallery.model.Album;
import com.illusory.qutegallery.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.ViewHolder> {
    public static final int FIX_SIZE = 200;
    public static final int PAYLOAD_GET_SELECT = 0;
    public static final int PAYLOAD_LOSE_SELECT = 1;
    private final Map<String, Album> albums;
    private OnItemClickListener itemClickListener;
    private OnItemFocusChangedListener itemFocusChangedListener;
    private int currentPosition = -1;
    private ViewTreeObserver viewTreeObserver;
    private int column = 6;

    public AlbumAdapter(@NonNull Map<String, Album> albums) {
        this.albums = albums;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        Log.i("onCreateViewHolder");
        ViewHolder viewHolder = new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_album, parent, false));
        // for fixed size
        if (viewTreeObserver == null) {
            viewTreeObserver = viewHolder.itemView.getViewTreeObserver();
//            viewTreeObserver.addOnGlobalFocusChangeListener((oldFocus, newFocus) -> {
//                Log.i("oldFocus:" + oldFocus + " newFocus:" + newFocus);
//            });
//            viewTreeObserver.addOnTouchModeChangeListener(isInTouchMode -> {
//                Log.i("is touch mode:" + isInTouchMode);
//            });
            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    viewHolder.itemView.getViewTreeObserver().removeOnGlobalLayoutListener(this);  // with this, triggered once
                    // when orientation changed, old viewHolder is not destroyed and it's size is not changed
                    Log.i("parent width:" + parent.getWidth() + " item width:" + viewHolder.itemView.getWidth());  // 1896 237
                }
            });
        }

        // for fixed count
        int recyclerViewWidth = parent.getWidth();
        ViewGroup.LayoutParams layoutParams = viewHolder.itemView.getLayoutParams();
        layoutParams.height = layoutParams.width = recyclerViewWidth / column;
        viewHolder.itemView.setLayoutParams(layoutParams);
//        Log.i("parent width:" + parent.getWidth() + " item width:" + viewHolder.itemView.getWidth());  // 1896 0

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        List<String> keys = new ArrayList<>(albums.keySet());
        Album album = albums.get(keys.get(position));
        holder.m_tvFolder.setText(album.folderName);
        holder.m_tvCount.setText(String.valueOf(album.medias.size()));
        Glide.with(holder.m_ivThumb.getContext()).load(new File(album.fullPath, album.medias.get(0)))
                .centerCrop().into(holder.m_ivThumb);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull List<Object> payloads) {
//        super.onBindViewHolder(holder, position, payloads);
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position);
        } else if (payloads.get(0) instanceof Integer) {
            int payLoad = (int) payloads.get(0);
            switch (payLoad) {
                case PAYLOAD_GET_SELECT:
                    Log.i("payload get select:" + position);
                    holder.animate(true);
                    break;
                case PAYLOAD_LOSE_SELECT:
                    Log.i("payload lose select:" + position);
                    holder.animate(false);
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public int getItemCount() {
        return albums.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public interface OnItemFocusChangedListener {
        void onItemFocusChanged(int position, boolean hasFocus);
    }

    public void setOnItemFocusChangedListener(OnItemFocusChangedListener itemFocusChangedListener) {
        this.itemFocusChangedListener = itemFocusChangedListener;
    }

    public int getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(int position) {
        currentPosition = position;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnFocusChangeListener {
        ViewGroup root;
        ImageView m_ivThumb;
        TextView m_tvFolder;
        TextView m_tvCount;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            root = itemView.findViewById(R.id.root);
            root.setOnClickListener(this);
            root.setFocusable(true);
            root.setOnFocusChangeListener(this);
            m_ivThumb = itemView.findViewById(R.id.iv_thumb);
            m_tvFolder = itemView.findViewById(R.id.tv_folder);
            m_tvCount = itemView.findViewById(R.id.tv_count);
        }

        @Override
        public void onClick(View v) {
            if (itemClickListener != null) {
                itemClickListener.onItemClick(getLayoutPosition());
            }
        }

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            animate(hasFocus);

            if (itemFocusChangedListener != null) {
                itemFocusChangedListener.onItemFocusChanged(getLayoutPosition(), hasFocus);
            }
        }

        public void animate(boolean hasFocus) {
            root.setSelected(hasFocus);
        }
    }
}
