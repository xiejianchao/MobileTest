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
