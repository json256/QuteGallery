package com.illusory.qutegallery.util;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class PermissionHelper {
    public static final String STORAGE_PERMISSION = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    public static final String AUDIO_PERMISSION = Manifest.permission.RECORD_AUDIO;
    public static final String CAMERA_PERMISSION = Manifest.permission.CAMERA;
    public static final String ACCESS_FINE_LOCATION_PERMISSION = Manifest.permission.ACCESS_FINE_LOCATION;
    public static final String ACCESS_WIFI_STATE_PERMISSION = Manifest.permission.ACCESS_WIFI_STATE;
    public static final int REQUEST_CODE = 0;

    public static boolean permissionsGranted(@NonNull Activity context, @NonNull String[] permissions) {
        for (String permission : permissions) {
            int permissionStatus = ContextCompat.checkSelfPermission(context, permission);
            if (permissionStatus != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    public static void checkAndRequestPermissions(@NonNull Activity context, @NonNull String[] permissions) {
        if (!permissionsGranted(context, permissions)) {
            ActivityCompat.requestPermissions(context, permissions, REQUEST_CODE);
        }
    }

    public static boolean storagePermissionsGranted(@NonNull Activity context) {
        return permissionsGranted(context, new String[]{STORAGE_PERMISSION});
    }

    public static void checkAndRequestStoragePermissions(@NonNull Activity context) {
        Log.i("checkAndRequestStoragePermissions");
        checkAndRequestPermissions(context, new String[]{STORAGE_PERMISSION});
    }

    public static boolean cameraPermissionsGranted(@NonNull Activity context) {
        return permissionsGranted(context, new String[]{CAMERA_PERMISSION});
    }

    public static void checkAndRequestCameraPermissions(@NonNull Activity context) {
        Log.i("checkAndRequestCameraPermissions");
        checkAndRequestPermissions(context, new String[]{CAMERA_PERMISSION});
    }

    public static boolean audioPermissionsGranted(@NonNull Activity context) {
        return permissionsGranted(context, new String[]{AUDIO_PERMISSION});
    }

    public static void checkAndRequestAudioPermissions(@NonNull Activity context) {
        Log.i("checkAndRequestAudioPermissions");
        checkAndRequestPermissions(context, new String[]{AUDIO_PERMISSION});
    }

    public static void onRequestPermissionsResult(
            int requestCode, String[] permissions, int[] grantResults) {
        Log.i("onRequestPermissionsResult");
        if (permissions.length > 0 && grantResults.length != permissions.length) {
            Log.i("Permission denied");
            return;
        }
        for (int i = 0; i < grantResults.length; ++i) {
            if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                Log.i(permissions[i] + " permission granted");
            }
        }
    }
}
