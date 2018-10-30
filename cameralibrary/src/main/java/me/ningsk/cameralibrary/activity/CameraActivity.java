package me.ningsk.cameralibrary.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import me.ningsk.cameralibrary.R;
import me.ningsk.cameralibrary.engine.camera.CameraParam;
import me.ningsk.cameralibrary.engine.model.GalleryType;
import me.ningsk.cameralibrary.fragment.CameraPreviewFragment;
import me.ningsk.cameralibrary.listener.OnPageOperationListener;

/**
 * <p>描述：相机预览界面<p>
 * 作者：ningsk<br>
 * 日期：2018/10/30 17 11<br>
 * 版本：v1.0<br>
 */
public class CameraActivity extends AppCompatActivity implements OnPageOperationListener {

    private static final String FRAGMENT_CAMERA = "fragment_camera";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_camera);
        if (null == savedInstanceState) {
            CameraPreviewFragment fragment = new CameraPreviewFragment();
            fragment.setOnPageOperationListener(this);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, fragment, FRAGMENT_CAMERA)
                    .addToBackStack(FRAGMENT_CAMERA)
                    .commit();
        }
        faceTrackerRequestNetwork();
    }

    private void faceTrackerRequestNetwork() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                FaceTracker.requestFaceNetwork(CameraActivity.this);
            }
        }).start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
    }

    @Override
    public void onBackPressed() {
        // 判断fragment栈中的个数，如果只有一个，则表示当前只处于预览主页面点击返回状态
        int backStackEntryCount = getSupportFragmentManager().getBackStackEntryCount();
        if (backStackEntryCount > 1) {
            getSupportFragmentManager().popBackStack();
        } else if (backStackEntryCount == 1) {
            CameraPreviewFragment fragment = (CameraPreviewFragment) getSupportFragmentManager()
                    .findFragmentByTag(FRAGMENT_CAMERA);
            if (fragment != null) {
                if (!fragment.onBackPressed()) {
                    finish();
                }
            }
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onOpenGalleryPage() {
        if (CameraParam.getInstance().gallerySelectedListener != null) {
            CameraParam.getInstance().gallerySelectedListener.onGalleryClickListener(GalleryType.WITHOUT_GIF);
        }
    }

    @Override
    public void onOpenImageEditPage(String path) {
        if (CameraParam.getInstance().captureListener != null) {
            CameraParam.getInstance().captureListener.onMediaSelectedListener(path, GalleryType.PICTURE);
        }
    }

    @Override
    public void onOpenVideoEditPage(String path) {
        if (CameraParam.getInstance().captureListener != null) {
            CameraParam.getInstance().captureListener.onMediaSelectedListener(path, GalleryType.VIDEO);
        }
    }

    @Override
    public void onOpenCameraSettingPage() {
        Intent intent = new Intent(CameraActivity.this, CameraSettingActivity.class);
        startActivity(intent);
    }
}
