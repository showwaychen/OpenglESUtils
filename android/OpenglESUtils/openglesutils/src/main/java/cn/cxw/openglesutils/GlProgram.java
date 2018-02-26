package cn.cxw.openglesutils;

import android.opengl.GLES20;
import android.util.Log;


/**
 * Created by cxw on 2017/12/16.
 */

public class GlProgram {

    public static int GLINVALID = 0;
    int mProgram;
    int mState;
    public GlProgram(String vertexShader, String fragmentShader)
    {
        mProgram = OpenglCommon.loadProgram(vertexShader, fragmentShader);
        if (mProgram == GLINVALID)
        {
            Log.e("GlProgram","loadProgram  error");
        }
    }
    public void useProgram()
    {
        GLES20.glUseProgram(mProgram);
    }

    public void release()
    {
        GLES20.glDeleteProgram(mProgram);
    }

    public int getProgram() {
        return mProgram;
    }

    public int getAttribLocation(String label)
    {
        int ret = GLES20.glGetAttribLocation(mProgram, label);
        OpenglCommon.checkNoGLES2Error("getAttribLocation");
        return ret;
    }
    public int getUniformLocation(String label)
    {
        int ret = GLES20.glGetUniformLocation(mProgram, label);
        OpenglCommon.checkNoGLES2Error("getUniformLocation");
        return ret;
    }
}
