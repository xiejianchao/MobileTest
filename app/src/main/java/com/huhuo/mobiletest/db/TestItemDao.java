package com.huhuo.mobiletest.db;


import com.huhuo.mobiletest.model.TestItemModel;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by xiejc on 15/12/16.
 */
public class TestItemDao {

    private static final String TAG = TestItemDao.class.getSimpleName();

    private Dao<TestItemModel, Integer> testItemDao;
    private DatabaseHelper helper;

    public TestItemDao() {
        helper = DatabaseHelper.getInstance();
        testItemDao = helper.getDao(TestItemModel.class);

    }

    /**
     * 增加一条测试Item
     *
     * @param model
     */
    public void insert(TestItemModel model) {
        try {
            testItemDao.create(model);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    /**
     * 增加一条测试Item
     *
     * @param model
     */
    public void insertOrUpdate(TestItemModel model) {
        try {
            testItemDao.createOrUpdate(model);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public TestItemModel queryById(int id) {
        try {
            final TestItemModel model = testItemDao.queryForId(id);
            return model;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<TestItemModel> queryAll() {
        try {
            final List<TestItemModel> models = testItemDao.queryForAll();
            return models;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
