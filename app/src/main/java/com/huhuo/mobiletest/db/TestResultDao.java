package com.huhuo.mobiletest.db;


import com.huhuo.mobiletest.model.TestResultSummaryModel;
import com.huhuo.mobiletest.utils.Logger;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by xiejc on 15/12/16.
 */
public class TestResultDao {

    private static final String TAG = TestResultDao.class.getSimpleName();

    private Dao<TestResultSummaryModel, Integer> messageDao;
    private DatabaseHelper helper;

    public TestResultDao() {
        helper = DatabaseHelper.getInstance();
        messageDao = helper.getDao(TestResultSummaryModel.class);

    }

    /**
     * 增加一条测试结果
     *
     * @param model
     */
    public void insert(TestResultSummaryModel model) {
        try {
            messageDao.create(model);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    /**
     * 增加一条测试结果
     *
     * @param model
     */
    public void insertOrUpdate(TestResultSummaryModel model) {
        try {
            messageDao.createOrUpdate(model);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public TestResultSummaryModel queryById(int id) {
        try {
            final TestResultSummaryModel model = messageDao.queryForId(id);
            return model;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<TestResultSummaryModel> queryAll() {
        try {
            final List<TestResultSummaryModel> models = messageDao.queryForAll();
            return models;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
