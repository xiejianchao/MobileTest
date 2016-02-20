package com.huhuo.mobiletest.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.huhuo.mobiletest.model.MessageModel;
import com.huhuo.mobiletest.model.RecentMessageModel;
import com.huhuo.mobiletest.model.TestItemModel;
import com.huhuo.mobiletest.model.TestResult;
import com.huhuo.mobiletest.model.TestResultSummaryModel;
import com.huhuo.mobiletest.utils.Logger;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;


public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    private static final int DB_VERSION = 1;
    private static String USER_DB_NAME;
    private static final String DATABASE_NAME_BASE = "mobile_test.db";


    private static final String TAG = DatabaseHelper.class.getSimpleName();

    private Map<String, Dao> daos = new HashMap<String, Dao>();

    private static DatabaseHelper instance;

    public RecentMessageDao conversationDao;
    public MessageDao messageDao;
    public TestResultDao testResultDao;
    public TestItemDao testItemDao;

    /**
     * 用户登录时执行初始化操作
     * @param context
     * @param userId
     */
    public static void init(Context context, String userId) {
        Logger.d(TAG, "执行init");
        if (instance == null) {
            USER_DB_NAME = String.format("%s_%s", userId, DATABASE_NAME_BASE);
            Logger.d(TAG,"database name:" + USER_DB_NAME);
            instance = OpenHelperManager.getHelper(context, DatabaseHelper.class);
            instance.initDao();
        }
    }


    public DatabaseHelper(Context context) {
        super(context, USER_DB_NAME, null, DB_VERSION);
        Logger.d(TAG, "执行DatabaseHelper构造函数");
    }


    public static DatabaseHelper getInstance() {
        return instance;
    }

    private ConnectionSource connectionSource = null;
    @Override
    public ConnectionSource getConnectionSource() {
        if (connectionSource == null) {
            connectionSource = super.getConnectionSource();
        }
        return connectionSource;
    }

    private void initDao() {
        conversationDao = new RecentMessageDao();
        messageDao = new MessageDao();
        testResultDao = new TestResultDao();
        testItemDao = new TestItemDao();
        Logger.w(TAG,"initDao " + testResultDao);
    }


    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            Logger.d(TAG, "onCreate");
            TableUtils.createTableIfNotExists(connectionSource, RecentMessageModel.class);
            TableUtils.createTableIfNotExists(connectionSource, MessageModel.class);
            TableUtils.createTableIfNotExists(connectionSource, TestResultSummaryModel.class);
            TableUtils.createTableIfNotExists(connectionSource, TestItemModel.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int
            oldVersion, int newVersion) {
        try {
            Logger.e(TAG, "onUpgrade" + ",oldver:" + oldVersion + ",newVersion:" + newVersion);
//            TableUtils.dropTable(connectionSource, User.class, true);
//            TableUtils.dropTable(connectionSource, Article.class, true);
//            TableUtils.dropTable(connectionSource, RecentMessageModel.class, true);
//            TableUtils.dropTable(connectionSource, MessageModel.class, true);

//            database.execSQL("ALTER TABLE `tb_message` ADD COLUMN testField VARCHAR;");

//            onCreate(database, connectionSource);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized Dao getDao(Class clazz) {
        Dao dao = null;
        try {
            dao = null;
            String className = clazz.getSimpleName();

            if (daos.containsKey(className)) {
                dao = daos.get(className);
            }
            if (dao == null) {
                dao = super.getDao(clazz);
                daos.put(className, dao);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dao;
    }

    /**
     * 释放资源
     */
    @Override
    public void close() {
        super.close();

        for (String key : daos.keySet()) {
            Dao dao = daos.get(key);
            dao = null;
        }
    }

}
