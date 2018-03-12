package com.pdl.app.tongue_app.CameraUtil;

import android.graphics.SurfaceTexture;
import android.view.TextureView;

/**
 * Created by asus-pc on 2017/10/2.
 */

public abstract class SurfaceTextureListenerAdaptor implements TextureView.SurfaceTextureListener {


        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {

        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
            return false;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surface) {

        }
}
