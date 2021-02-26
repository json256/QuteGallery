package com.illusory.qutegallery.activity;

import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.illusory.qutegallery.R;
import com.illusory.qutegallery.fragment.AlbumFragment;
import com.illusory.qutegallery.util.Log;

public class MainActivity extends AppCompatActivity {

    private AlbumFragment albumFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("onCreate");
        setContentView(R.layout.main_activity);

        if (savedInstanceState == null) {
            albumFragment = AlbumFragment.newInstance();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, albumFragment)
                    .commitNow();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("onDestroy");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for (int i = 0; i < grantResults.length; ++i) {
            if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                Log.i(permissions[i] + " permission granted");
                initData();
            }
        }
    }

    private void initData() {
        albumFragment.initData();
    }
}