package cn.cxw.openglesutils;

import android.databinding.DataBindingUtil;
import android.opengl.GLES20;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.nio.ByteBuffer;
import java.util.concurrent.TimeUnit;

import cn.cxw.openglesutils.databinding.ActivityCaptureTestBinding;
import cn.cxw.openglesutils.glthread.GlRenderThread;
import cn.cxw.openglesutils.glthread.GlThreadPreview;
import cn.cxw.openglesutils.glthread.IPreviewView;
import cn.cxw.openglesutils.openglcapture.GraphicBufferCapture;
import cn.cxw.openglesutils.openglcapture.OpenglCapture;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class CaptureTestActivity extends AppCompatActivity {
    GlThreadPreview mGlPreview = null;
    OpenglCapture mCapture = null;

    class RenderCallback implements GlRenderThread.GLRenderer
    {
        GlTextureFrameBuffer mTexFramebuffer = null;
        @Override
        public void onGlInit(int width, int height) {
        }

        @Override
        public void onGlResize(int width, int height) {
            GLES20.glViewport(0, 0, width, height);
            if (mCapture == null)
            {
                return;
            }
            mCapture.initCapture(width, height);
            mTexFramebuffer = new GlTextureFrameBuffer(GLES20.GL_RGBA);
            mTexFramebuffer.setSize(width, height);
        }

        @Override
        public void onGlDrawFrame() {
            GLES20.glClearColor(255,0,0, 0);
            GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
            mTexFramebuffer.activeFrameBuffer();
            if(mCapture == null) return ;
            GLES20.glClearColor(255,0,0, 0);
            GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
            mCapture.setTextureId(mTexFramebuffer.getTextureId());
            mCapture.onCapture();
            mTexFramebuffer.disactiveFrameBuffer();

        }

        @Override
        public void onGlDeinit() {
            if(mCapture == null) return ;
            mCapture.destroy();
            mCapture = null;
            cancel();
            mTexFramebuffer.release();
            mTexFramebuffer = null;
        }
    }
    RenderCallback mRenderCB = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCaptureTestBinding binding=  DataBindingUtil.setContentView(this, R.layout.activity_capture_test);
        IPreviewView preview = new SurfaceViewPreview(this);
        mGlPreview = new GlThreadPreview(preview);
        binding.rlTest.addView(preview.getView());

        mRenderCB = new RenderCallback();
        mGlPreview.setRender(mRenderCB);

        mCapture = OpenglCapture.createCapturer(GraphicBufferCapture.class);
        mCapture.setOnCapture(new OpenglCapture.IFrameCaptured() {
            @Override
            public void onPreviewFrame(ByteBuffer directFrameBuffer, int stride, int width, int height, long ptsMS) {
                Log.d("test", "frame callback");
            }
        });

        mGlPreview.start();

    }
    private static Disposable mDisposable;
    /**
     * 取消订阅
     */
    public static void cancel() {
        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.dispose();
//            LogUtil.e("====定时器取消======");
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
//        Observable.timer(1000, TimeUnit.MILLISECONDS)
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Observer<Long>() {
//                    @Override
//                    public void onSubscribe(Disposable d) {
//                        mDisposable = d;
//                    }
//
//                    @Override
//                    public void onNext(Long aLong) {
//                        if (mGlPreview != null)
//                        {
//                            mGlPreview.requestRender();
//                        }
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
////                        cancel();
//                    }
//
//                    @Override
//                    public void onComplete() {
////                        cancel();
//                    }
//                });

        Observable.interval(0,100, TimeUnit.MILLISECONDS).subscribe(new Observer<Long>() {

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }

            @Override
            public void onSubscribe(Disposable d) {
                mDisposable = d;
            }

            @Override
            public void onNext(Long aLong) {
                if (mGlPreview != null)
                        {
                            mGlPreview.requestRender();
                        }
//                LogUtils.d("------>along："+aLong+" time:"+SystemClock.elapsedRealtime());
            }
        });

//        Observable.timer(100, TimeUnit.MILLISECONDS).subscribe(new Observer<Long>() {
//
//
//            @Override
//            public void onSubscribe(Disposable d) {
//
//            }
//
//            @Override
//            public void onNext(Long aLong) {
//                if (mGlPreview != null)
//                        {
//                            mGlPreview.requestRender();
//                        }
//            }
//
//            @Override
//            public void onError(Throwable e) {
//
//            }
//
//            @Override
//            public void onComplete() {
//
//            }
//        });
    }

}
