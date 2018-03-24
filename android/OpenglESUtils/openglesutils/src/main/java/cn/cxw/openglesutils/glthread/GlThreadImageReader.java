package cn.cxw.openglesutils.glthread;

import android.graphics.PixelFormat;
import android.media.ImageReader;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;

/**
 * Created by cxw on 2017/11/14.
 */

public class GlThreadImageReader extends GlRenderThread{
    ImageReader mImageReader = null;
    int mWidth;
    int mHeight;
    HandlerThread mHandlerThread = null;
    public GlThreadImageReader(int width, int height, GlRenderThread.GLRenderer renderer)
    {
        mWidth = width;
        mHeight = height;
        mThreadName = "GlThreadImageReader";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mImageReader = ImageReader.newInstance(width, height, PixelFormat.RGBA_8888, 2);
            setSurface(mImageReader.getSurface());
            setRender(renderer);

        }
    }

    public void setImageAvailableListener(ImageReader.OnImageAvailableListener listener)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (mImageReader != null)
            {
                //在工作线程中使用此方法，如何没有handler的话，会抛出“handler is null but the current thread is not a looper”的异常。
                mHandlerThread = new HandlerThread("GlThreadImageReader Hanlder");
                mHandlerThread.start();
                mImageReader.setOnImageAvailableListener(listener, new Handler(mHandlerThread.getLooper()));
            }
        }
    }
    public void release()
    {
        if (mHandlerThread != null)
        {
            mHandlerThread.quit();
            mHandlerThread = null;
        }
        Log.d(mThreadName, "gl thread stop");
        stopRender();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (mImageReader != null)
            {
                mImageReader.setOnImageAvailableListener(null, null);
                mImageReader.close();
                mImageReader = null;
            }
        }

    }

    @Override
    public synchronized void start() {
        Log.d(mThreadName, "gl thread start");
        super.start();
        requestResize(mWidth, mHeight);
    }
}
