package com.huhuo.mobiletest.db;


import com.huhuo.mobiletest.model.MessageModel;
import com.huhuo.mobiletest.utils.Logger;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by xiejc on 15/12/16.
 */
public class MessageDao {

    private static final String TAG = MessageDao.class.getSimpleName();

    private Dao<MessageModel, Integer> messageDao;
    private DatabaseHelper helper;

    public MessageDao() {
        helper = DatabaseHelper.getInstance();
        messageDao = helper.getDao(MessageModel.class);

    }

    /**
     * 增加一条消息
     *
     * @param model
     */
    public void insert(MessageModel model) {
        try {
            messageDao.create(model);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    /**
     * 增加一条消息
     *
     * @param model
     */
    public void insertOrUpdate(MessageModel model) {
        try {
            messageDao.createOrUpdate(model);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param userId 要查询和哪个userId的聊天记录
     * @param start 从哪行数据开始查询
     * @param count 一共查询多少条数据
     */
    public List<MessageModel> query(String userId,long start,long count) {
        long startTime = System.currentTimeMillis();
        List<MessageModel> msgs = null;
        try {
            final QueryBuilder<MessageModel, Integer> query = messageDao.queryBuilder();

            query.limit(count);
            query.offset(start);
            query.orderBy("messageDate",true);
            query.where().eq("userId", userId);

            msgs = messageDao.query(query.prepare());
            if (msgs != null && msgs.size() > 0) {
                for (MessageModel msg : msgs) {
                    Logger.d(TAG, "数据库中查出的消息：" + msg.getMessage() + ",msg:" + msg.toString());
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        long endTime = System.currentTimeMillis();
        Logger.d(TAG,"查询消息用了：" + (endTime - startTime) + " 毫秒时间");
        return msgs;
    }

    public MessageModel queryById(int id) {
        try {
            final MessageModel model = messageDao.queryForId(id);
            return model;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<MessageModel> queryAll() {
        try {
            final List<MessageModel> models = messageDao.queryForAll();
            return models;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
