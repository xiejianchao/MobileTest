package com.huhuo.mobiletest.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.SystemClock;
import android.widget.ImageView;

import com.huhuo.mobiletest.R;
import com.huhuo.mobiletest.constants.Constants;
import com.huhuo.mobiletest.db.DatabaseHelper;
import com.huhuo.mobiletest.db.MessageDao;
import com.huhuo.mobiletest.db.RecentMessageDao;
import com.huhuo.mobiletest.model.MessageModel;
import com.huhuo.mobiletest.model.RecentMessageModel;
import com.huhuo.mobiletest.utils.ImageUtil;
import com.huhuo.mobiletest.utils.Logger;
import com.huhuo.mobiletest.utils.ToastUtil;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.Date;
import java.util.List;


@ContentView(R.layout.fragment_report)
public class ReportFragment extends BaseFragment {

    private static final String TAG = ReportFragment.class.getSimpleName();
    @ViewInject(R.id.iv_test)
    private ImageView ivTest;

    private RecentMessageDao recentMessageDao;
    private MessageDao messageDao;


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //TestCode
        String url = "http://pic33.nipic.com/20131008/13661616_190558208000_2.jpg";
//        ImageUtil.displayImage(ivTest, url);

        if (DatabaseHelper.getInstance() != null) {
            recentMessageDao = DatabaseHelper.getInstance().conversationDao;
            messageDao = DatabaseHelper.getInstance().messageDao;
            Logger.d(TAG, "数据库初始化成功");
        } else {
            Logger.e(TAG, "DatabaseHelper.getInstance() is null");
        }
//        testInsertMessage2Db();
    }

    /**
     * 测试代码，随时删除
     */
    private void testInsertMessage2Db() {
        //3.再插入第三条数据，自己发送给用户3
        MessageModel msgModel = new MessageModel();
        msgModel.setFromName(Constants.DEFAULT_USER_NAME);
        msgModel.setFromId(Constants.DEFAULT_USER_ID);
        msgModel.setToId(Constants.OTHER_USER_ID_3);
        msgModel.setUserId(Constants.OTHER_USER_ID_3);
        msgModel.setToName(Constants.OTHER_USER_NAME_3);
        msgModel.setMessage("明天开房去吧");
        msgModel.setMessageDate(new Date());
        messageDao.insert(msgModel);
        recentMessageDao.insertOrUpdate(new RecentMessageModel(msgModel));

        SystemClock.sleep(100);
        //4.再插入第三条数据，用户3发送给自己
        msgModel = new MessageModel();
        msgModel.setFromName(Constants.OTHER_USER_NAME_3);
        msgModel.setFromId(Constants.OTHER_USER_ID_3);
        msgModel.setToId(Constants.DEFAULT_USER_NAME);
        msgModel.setUserId(Constants.OTHER_USER_ID_3);
        msgModel.setToName(Constants.DEFAULT_USER_NAME);
        msgModel.setMessage("啊，前天不是刚去吗");
        msgModel.setMessageDate(new Date());
        messageDao.insert(msgModel);
        recentMessageDao.insertOrUpdate(new RecentMessageModel(msgModel));

        /**
         * 插入消息数据，自动生成消息列表数据
         */
        final List<RecentMessageModel> models = recentMessageDao.queryAll();
        if (models != null && !models.isEmpty()) {
            for (RecentMessageModel model : models) {
                Logger.d(TAG,"插入的消息列表内容：" + model.toString());
            }
        }

        //查看插入结果
        messageDao.query(Constants.OTHER_USER_ID_3,0,2);
        messageDao.query(Constants.OTHER_USER_ID_3,2,4);

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
