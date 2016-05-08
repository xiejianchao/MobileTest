package com.huhuo.mobiletest.db;


import com.huhuo.mobiletest.model.TestResultSummaryModel;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by xiejc on 15/12/16.
 */
public class TestResultDao {

    private static final String TAG = TestResultDao.class.getSimpleName();

    private Dao<TestResultSummaryModel, Integer> testSummaryDao;
    private DatabaseHelper helper;

    public TestResultDao() {
        helper = DatabaseHelper.getInstance();
        testSummaryDao = helper.getDao(TestResultSummaryModel.class);

    }

    /**
     * 增加一条测试结果
     *
     * @param model
     */
//    public void insert(TestResultSummaryModel model) {
//        try {
//            testSummaryDao.create(model);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
    /**
     * 增加一条测试结果
     *
     * @param model
     */
    public void insertOrUpdate(TestResultSummaryModel model) {
        try {
            testSummaryDao.createOrUpdate(model);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除一条记录
     * @param model
     */
    public void delete(TestResultSummaryModel model) {
        try {
            testSummaryDao.delete(model);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public TestResultSummaryModel queryById(int id) {
        try {
            final TestResultSummaryModel model = testSummaryDao.queryForId(id);
            return model;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<TestResultSummaryModel> queryAll() {
        try {
            final List<TestResultSummaryModel> models = testSummaryDao.queryForAll();
            return models;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
