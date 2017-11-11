package com.a6020peaks.thingsprimer;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.media.ImageReader;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.nio.ByteBuffer;

public class CameraActivity extends AppCompatActivity implements ImageReader.OnImageAvailableListener,
        View.OnClickListener {
    private static final String TAG = CameraActivity.class.getSimpleName();
    private Camera mCamera;
    private HandlerThread mCameraThread;
    private Handler mCameraHandler;
    private Button mPhotoButton;
    private ImageView mPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        mCameraThread = new HandlerThread("CameraBackground");
        mCameraThread.start();
        mCameraHandler = new Handler(mCameraThread.getLooper());

        mCamera = Camera.getInstance();
        mCamera.initializeCamera(this, mCameraHandler, this);

        mPhotoButton = findViewById(R.id.takePhoto);
        mPhotoButton.setOnClickListener(this);
        mPhoto = findViewById(R.id.photo);

        // This is too early to call this method. The camera seems to be not ready
        //mCamera.takePicture();
    }

    @Override
    public void onImageAvailable(ImageReader reader) {
        Image image = reader.acquireLatestImage();
        // get image bytes
        ByteBuffer imageBuf = image.getPlanes()[0].getBuffer();
        final byte[] imageBytes = new byte[imageBuf.remaining()];
        imageBuf.get(imageBytes);
        image.close();
        onPictureTaken(imageBytes);
    }

    private void onPictureTaken(final byte[] imageBytes) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "Displaying photo...");
                Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                mPhoto.setImageBitmap(bitmap);
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.takePhoto:
                mCamera.takePicture();
                break;
            default:
                throw new RuntimeException("Unknown id");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCamera.shutDown();
        mCameraThread.quitSafely();
    }
}
