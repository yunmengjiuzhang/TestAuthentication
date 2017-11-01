package com.reformer.authentication.utils;

import android.content.Context;
import android.hardware.Camera;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;
import java.util.List;

/**
 * Created by Administrator on 2017/5/17 0017.
 */

public class CameraUtils {

    private final SurfaceHolder surfaceholder;
    private Camera camera;
    private Context mCtx;
    private OnCameraListener mCameraListener;

    public CameraUtils(SurfaceView surfaceView, final Context ctx) {
        mCtx = ctx;
        surfaceholder = surfaceView.getHolder();
        surfaceholder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        surfaceholder.addCallback(new SurfaceHolder.Callback() {

            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                camera = Camera.open(0);//获取camera对象
                try {
                    //设置预览监听
                    camera.setPreviewDisplay(holder);
                    Camera.Parameters parameters = camera.getParameters();
                    parameters.set("orientation", "portrait");
                    camera.setDisplayOrientation(0);
                    parameters.setRotation(0);

                    List<Camera.Size> supportedPictureSizes = parameters.getSupportedPictureSizes();
                    parameters.setPictureSize(176, 144);
                    camera.setParameters(parameters);
                    camera.startPreview();      //启动摄像头预览
                    System.out.println("camera.startpreview");
                } catch (IOException e) {
                    e.printStackTrace();
                    camera.release();
                    System.out.println("camera.release");
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                if (camera != null) {
                    camera.stopPreview();
                    camera.release();
                }
            }
        });
    }

    // 拍照
    public void takePicture() {
        camera.takePicture(null, null, pictureCallback);
    }

    public void setOnCameraListner(OnCameraListener ocl) {
        mCameraListener = ocl;
    }

    public interface OnCameraListener {
        public void onPictrue(byte[] file);
    }

    private Camera.PictureCallback pictureCallback = new Camera.PictureCallback() {

        @Override
        public void onPictureTaken(final byte[] data, Camera camera) {
            ThreadUtils.runOnBackThread(new Runnable() {
                @Override
                public void run() {
                    if (mCameraListener != null)
                        mCameraListener.onPictrue(data);
                }
            });
            camera.startPreview();
        }
    };

    public void setFaceListener(Camera.FaceDetectionListener faceDetectionListener) {
        camera.setFaceDetectionListener(faceDetectionListener);
    }

    /**
     * 停止预览，释放Camera
     */
    public void close() {
        if (null != camera) {
            camera.setPreviewCallback(null);
            camera.stopPreview();
            camera.release();
            camera = null;
        }
    }
}
