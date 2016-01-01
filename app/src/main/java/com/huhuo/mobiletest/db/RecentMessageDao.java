package com.huhuo.mobiletest.db;

import com.huhuo.mobiletest.model.RecentMessageModel;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by xiejc on 15/12/16.
 */
public class RecentMessageDao {

    private static final String TAG = RecentMessageDao.class.getSimpleName();

    private Dao<RecentMessageModel, Integer> recentMessageDao;
    private DatabaseHelper helper;

    public RecentMessageDao() {
        helper = DatabaseHelper.getInstance();
        recentMessageDao = helper.getDao(RecentMessageModel.class);

    }

    /**
     * 增加一条会话
     *
     * @param model
     */
    private void insert(RecentMessageModel model) {
        try {
            recentMessageDao.create(model);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void insertOrUpdate(RecentMessageModel model) {
        try {
            recentMessageDao.createOrUpdate(model);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

//    public void insertOrUpdate(RecentMessageModel model) {
//        try {
//            final QueryBuilder<RecentMessageModel, Integer> query = recentMessageDao.queryBuilder();
//
//            final Where<RecentMessageModel, Integer> where = query.where();
//            where.eq("userId", model.getUserId());
//
//            final List<RecentMessageModel> recentLists = query.query();
//            if (recentLists != null && recentLists.size() > 0) {
//                Logger.d(TAG, "当前插入的消息列表此前已经插入过，此时只需要更新即可");
//                final UpdateBuilder<RecentMessageModel, Integer> update = recentMessageDao.updateBuilder();
//                update.updateColumnValue("recentMsg",model.getRecentMsg());
//                update.updateColumnValue("fromName",model.getFromName());
//                update.updateColumnValue("toName",model.getToName());
//
//                update.updateColumnValue("recentMsg",model.getRecentMsg()).where().eq("userId",model.getUserId());
//                update.prepare();
//                final int retVal = update.update();
//                Logger.d(TAG,retVal == 1 ? "更新成功" : "更新失败");
////                recentMessageDao.updateRaw("update table tb_recentmessage recentMsg = "+ model.getRecentMsg() +" where userId = "+ model.getUserId() +"");
//            } else {
//                Logger.d(TAG,"当前插入的消息列表没有插入过，此时只需要插入即可");
//                insert(model);
//            }
//        } catch (Exception e) {
//
//        }
//    }

    public RecentMessageModel queryById(int id) {

        try {
            final RecentMessageModel model = recentMessageDao.queryForId(id);
            return model;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<RecentMessageModel> queryAll() {
        try {
            final List<RecentMessageModel> models = recentMessageDao.queryForAll();
            return models;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void destory() {
        if (recentMessageDao != null) {
            recentMessageDao.clearObjectCache();
        }
    }

}
