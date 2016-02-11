package com.huhuo.mobiletest.constants;

import android.content.Context;

import com.huhuo.mobiletest.MobileTestApplication;
import com.huhuo.mobiletest.R;

/**
 * Created by xiejianchao on 16/2/8.
 */
public class TestCode {

    public static final int TEST_TYPE_WEBPAGE = 1;
    public static final int TEST_TYPE_SPEED = 2;
    public static final int TEST_TYPE_VIDEO = 3;
    public static final int TEST_TYPE_VOICE = 4;
    public static final int TEST_TYPE_CONNECTION = 5;

    public static String getTestName(int testType) {
        Context context = MobileTestApplication.getInstance().getApplicationContext();
        String typeName = null;
        switch (testType) {
            case TEST_TYPE_WEBPAGE:
                typeName = context.getString(R.string.test_type_webpage);
                break;
            case TEST_TYPE_SPEED:
                typeName = context.getString(R.string.test_type_speed);
                break;
            case TEST_TYPE_VIDEO:
                typeName = context.getString(R.string.test_type_video);
                break;
            case TEST_TYPE_VOICE:
                typeName = context.getString(R.string.test_type_voice);
                break;
            case TEST_TYPE_CONNECTION:
                typeName = context.getString(R.string.test_type_conn);
                break;
            default:
                typeName = context.getString(R.string.test_type_unknow);
                break;
        }
        return typeName;
    }

}
