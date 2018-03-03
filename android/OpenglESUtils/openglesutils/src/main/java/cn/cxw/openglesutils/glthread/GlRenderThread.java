package cn.cxw.openglesutils.glthread;
import android.opengl.GLES20;
import android.util.Log;
import android.view.Surface;

import java.util.concurrent.atomic.AtomicBoolean;

import cn.cxw.openglesutils.egl.EglBase;


/**
 * Created by user on 2017/12/13.
 */

public class GlRenderThread  extends Thread {
    public static final  String TAG = "GlRenderThread";
    private AtomicBoolean mShouldRender;
    protected Surface mSurface;
    private GLRenderer mRenderer;
    private Object mSyncToken;
    boolean mRequestRender = false;
    boolean mRequestDestroy = false;
    private EglBase mEgl;
    private EglBase.Context mEglSharedContext = null;

    protected int mViewWidth = 0;
    protected int mViewHeight = 0;
    private boolean m_needResize = false;

    protected String mThreadName = "GlRenderThread";
    public interface GLRenderer {
        void onGlInit(int width, int height);
        void onGlResize(int width, int height);
        void onGlDrawFrame();
        void onGlDeinit();
    }

    protected  GlRenderThread()
    {
        mSyncToken = new Object();
    }
    protected void setSurface(Surface surface)
    {
        mSurface = surface;
    }
    public GlRenderThread(Surface surface, GLRenderer renderer) {
        mSurface = surface;
        mRenderer = renderer;
        mSyncToken = new Object();
        Log.d(mThreadName, "new GlRenderThread");
//        mShouldRender = shouldRender;
    }
    public  void setRender(GLRenderer render)
    {
        mRenderer = render;
    }
    public void setSharedContext(EglBase.Context sharedcontext)
    {
        mEglSharedContext = sharedcontext;
    }
    public EglBase.Context getEglContext()
    {
        return mEgl.getEglBaseContext();
    }
    private void initGL() {
        mEgl = EglBase.create(mEglSharedContext, EglBase.CONFIG_RGBA);
        mEgl.createSurface(mSurface);
        mEgl.makeCurrent();
        GLES20.glFlush();
        Log.d(mThreadName, "initGL over");
    }

    public void stopRender()
    {
        Log.i(mThreadName, "requestDestroy");
        synchronized (mSyncToken) {
            mRequestDestroy = true;
            mSyncToken.notifyAll();
        }
    }
    private void destoryGL() {
        mEgl.release();

    }
    public void requestRender() {
        synchronized (mSyncToken) {
            mRequestRender = true;
            mSyncToken.notifyAll();
        }
    }
    public void requestResize(int width, int height)
    {
        mViewHeight = height;
        mViewWidth = width;
        m_needResize = true;
        requestRender();
    }
    public void run() {
        setName(mThreadName + getId());
        initGL();

        Log.d(mThreadName, "gl thread start run ");
        if (mRenderer != null) {
            mRenderer.onGlInit(mViewWidth, mViewHeight);
        }
        while (true) {
            synchronized (mSyncToken) {
                if (mRequestDestroy) {
                    break;
                }
                if (m_needResize)
                {
                    if (mRenderer != null)
                    {
                        Log.d(TAG, "resize callback");
                        mRenderer.onGlResize(mViewWidth, mViewHeight);
                    }
                    m_needResize = false;
                }
                if (!mRequestRender ) {
                    try {
                        mSyncToken.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                mRequestRender = false;
                if (mRequestDestroy) {
                    break;
                }
            }
//                Log.i(TAG, "onDrawFrame ##");
            if (mRenderer != null)
                mRenderer.onGlDrawFrame();
            mEgl.swapBuffers();

        }
        if (mRenderer != null)
        {
            mRenderer.onGlDeinit();
        }
        mSurface = null;
        destoryGL();
        Log.d(mThreadName, "gl thread stop run ");
    }
}
