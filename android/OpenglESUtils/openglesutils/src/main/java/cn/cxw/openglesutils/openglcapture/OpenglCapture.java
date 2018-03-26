package cn.cxw.openglesutils.openglcapture;

import java.nio.ByteBuffer;

import cn.cxw.openglesutils.OpenglCommon;


/**
 * Created by user on 2017/12/5.
 */

public abstract class OpenglCapture {

    public static OpenglCapture createCapturer(Class<? extends OpenglCapture> cls)
    {
        try {
            return cls.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
            return null;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }
    public interface IFrameCaptured {
        void onPreviewFrame(ByteBuffer directFrameBuffer, int stride, int width, int height, long ptsMS);
    }
    ByteBuffer mCaptureBuffer = null;
    boolean mbInitOk = false;
    int mTextureId = OpenglCommon.NO_TEXTURE;

    int mWidth = 0;
    int mHeight = 0;
    IFrameCaptured mCallback = null;
    public void setOnCapture(IFrameCaptured cb)
    {
        mCallback = cb;
    }
    public void setTextureId(int textureId)
    {
        mTextureId = textureId;
    }
    abstract public boolean initCapture(int width, int height);

    abstract public void onCapture();

    abstract public void destroy();

}
