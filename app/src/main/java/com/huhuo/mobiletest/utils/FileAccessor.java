/*
 *  Copyright (c) 2015 The CCP project authors. All Rights Reserved.
 *
 *  Use of this source code is governed by a Beijing Speedtong Information Technology Co.,Ltd license
 *  that can be found in the LICENSE file in the root of the web site.
 *
 *   http://www.yuntongxun.com
 *
 *  An additional intellectual property rights grant can be found
 *  in the file PATENTS.  All contributing project authors may
 *  be found in the AUTHORS file in the root of the source tree.
 */package com.huhuo.mobiletest.utils;

import android.os.Environment;
import android.text.TextUtils;

import com.huhuo.mobiletest.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * 文件操作工具类
 * Created by Jorstin on 2015/3/17.
 */
public class FileAccessor {


    public static final String TAG = FileAccessor.class.getName();
    public static String EXTERNAL_STOREPATH = getExternalStorePath();
    
    public static String BASE_ROOT_DIR = EXTERNAL_STOREPATH + File.separator + "MobileTest";
    
    public static final String APP_ROOT_DIR = BASE_ROOT_DIR;
    public static final String APP_IMAGE_CACHE__DIR =BASE_ROOT_DIR + File.separator+ "cache";
    public static final String APP_ROOT_VOICE_DIR = BASE_ROOT_DIR + File.separator+ "voice";
    public static final String APP_VOICE_SEND = BASE_ROOT_DIR + File.separator+ "voice"+ File.separator +"send";
    public static final String APP_VOICE_RECEIVE = BASE_ROOT_DIR + File.separator+"voice"+ File.separator +"receive";
    public static final String APP_IMAGE = BASE_ROOT_DIR + File.separator+ "image";
    public static final String APP_MESSAGE_PHOTO = BASE_ROOT_DIR + File.separator+ "image"+ File.separator +"photo";
    public static final String APP_AVATAR = BASE_ROOT_DIR + File.separator+ "avatar";
    public static final String APP_FILE = BASE_ROOT_DIR + File.separator+ "file";
    public static final String APP_DOWNLOAD_DIR = BASE_ROOT_DIR + File.separator+ "download";

    /**
     * 初始化应用文件夹目录
     */
    public static void initFileAccess() {
        long start = System.currentTimeMillis();
        File rootDir = new File(APP_ROOT_DIR);
        if (!rootDir.exists()) {
            final boolean mkdirs = rootDir.mkdirs();
            final boolean canRead = rootDir.canRead();
            final boolean canWrite = rootDir.canWrite();
            Logger.d(TAG,"crate APP_ROOT_DIR status:" + mkdirs);
            Logger.d(TAG,"crate APP_ROOT_DIR canRead:" + canRead);
            Logger.d(TAG,"crate APP_ROOT_DIR canWrite:" + canWrite);
        }

        File imgCache = new File(APP_IMAGE_CACHE__DIR);
        if (!imgCache.exists()) {
            final boolean mkdirs = imgCache.mkdirs();
            Logger.d(TAG, "crate " + APP_IMAGE_CACHE__DIR + " status:" + mkdirs);
        }

        File voiceRootDir = new File(APP_ROOT_VOICE_DIR);
        if (!voiceRootDir.exists()) {
            voiceRootDir.mkdirs();
        }

        File voiceSendDir = new File(APP_VOICE_SEND);
        if (!voiceSendDir.exists()) {
            voiceSendDir.mkdirs();
        }

        File voiceReceiveDir = new File(APP_VOICE_RECEIVE);
        if (!voiceReceiveDir.exists()) {
            voiceReceiveDir.mkdirs();
        }

        File imageDir = new File(APP_IMAGE);
        if (!imageDir.exists()) {
            imageDir.mkdirs();
        }

        File fileDir = new File(APP_FILE);
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }

        File avatarDir = new File(APP_AVATAR);
        if (!avatarDir.exists()) {
            avatarDir.mkdirs();
        }

        File downloadDir = new File(APP_DOWNLOAD_DIR);
        if (!downloadDir.exists()) {
            downloadDir.mkdirs();
        }

        long end = System.currentTimeMillis();
        Logger.d(TAG,"create cache dir time:" + (end - start) + "毫秒");



    }

    public static String readContentByFile(String path) {
        BufferedReader reader = null;
        String line = null;
        try {
            File file = new File(path);
            if (file.exists()) {
                StringBuilder sb = new StringBuilder();
                reader = new BufferedReader(new FileReader(file));
                while ((line = reader.readLine()) != null) {
                    sb.append(line.trim());
                }
                return sb.toString().trim();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }


    /**
     * 获取发送的语音文件存储目录
     * @return
     */
    public static File getVoicePathName() {
        if (!isExistExternalStore()) {
            ToastUtil.showMessage(R.string.media_ejected);
            return null;
        }

        File directory = new File(APP_VOICE_SEND);
        if (!directory.exists() && !directory.mkdirs()) {
            ToastUtil.showMessage("Path to file could not be created");
            return null;
        }

        return directory;
    }

    /**
     * 获取接收的语音文件存储目录
     * @return
     */
    public static File getReceiveVoicePathName() {
        if (!isExistExternalStore()) {
            ToastUtil.showMessage(R.string.media_ejected);
            return null;
        }

        File directory = new File(APP_VOICE_RECEIVE);
        if (!directory.exists() && !directory.mkdirs()) {
            ToastUtil.showMessage("Path to file could not be created");
            return null;
        }

        return directory;
    }

    /**
     * 头像
     * @return
     */
    public static File getAvatarPathName() {
        if (!isExistExternalStore()) {
            ToastUtil.showMessage(R.string.media_ejected);
            return null;
        }

        File directory = new File(APP_AVATAR);
        if (!directory.exists() && !directory.mkdirs()) {
            ToastUtil.showMessage("Path to file could not be created");
            return null;
        }

        return directory;
    }



    /**
     * 获取文件目录
     * @return
     */
    public static File getFilePathName() {
        if (!isExistExternalStore()) {
            ToastUtil.showMessage(R.string.media_ejected);
            return null;
        }

        File directory = new File(APP_FILE);
        if (!directory.exists() && !directory.mkdirs()) {
            ToastUtil.showMessage("Path to file could not be created");
            return null;
        }

        return directory;
    }

    /**
     * 返回图片存放目录
     * @return
     */
    public static File getImagePathName() {
        if (!isExistExternalStore()) {
            ToastUtil.showMessage(R.string.media_ejected);
            return null;
        }

        File directory = new File(APP_IMAGE);
        if (!directory.exists() && !directory.mkdirs()) {
            ToastUtil.showMessage("Path to file could not be created");
            return null;
        }

        return directory;
    }
    /**
     * 返回图片存放目录
     * @return
     */
    public static File getCameraPath() {
        if (!isExistExternalStore()) {
            ToastUtil.showMessage(R.string.media_ejected);
            return null;
        }

        File directory = new File(APP_MESSAGE_PHOTO);
        if (!directory.exists() && !directory.mkdirs()) {
            ToastUtil.showMessage("Path to file could not be created");
            return null;
        }

        return directory;
    }

    /**
     * 获取下载文件存储目录
     * @return
     */
    public static File getDownloadPath() {
        if (!isExistExternalStore()) {
            ToastUtil.showMessage(R.string.media_ejected);
            return null;
        }

        File directory = new File(APP_DOWNLOAD_DIR);
        if (!directory.exists() && !directory.mkdirs()) {
            ToastUtil.showMessage("Path to file could not be created");
            return null;
        }

        return directory;
    }

    public static String getDownloadPathByUrl(String url){
        final File file = getDownloadPath();
        final String fileName = FileUtil.getFileNameByUrl(url);
        if (file != null && !file.exists() ) {
            file.mkdirs();
        }

        Logger.d(TAG,"fileName:" + fileName);
        return file.getAbsolutePath() + File.separator + fileName;

    }

    /**
     * 获取文件名
     * @param pathName
     * @return
     */
    public static String getFileName(String pathName) {

        int start = pathName.lastIndexOf("/");
        if (start != -1) {
            return pathName.substring(start + 1, pathName.length());
        }
        return pathName;

    }

    /**
     * 外置存储卡的路径
     * @return
     */
    public static String getExternalStorePath() {
        if (isExistExternalStore()) {
            return Environment.getExternalStorageDirectory().getAbsolutePath();
        }
        return null;
    }

    /**
     * 是否有外存卡
     * @return
     */
    public static boolean isExistExternalStore() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     *
     * @param fileName
     * @return
     */
    public static String getFileUrlByFileName(String fileName) {
        return FileAccessor.APP_IMAGE + File.separator + FileAccessor.getSecondLevelDirectory(fileName)+ File.separator + fileName;
    }

    /**
     *
     * @param filePaths
     */
    public static void delFiles(ArrayList<String> filePaths) {
        for(String url : filePaths) {
            if(!TextUtils.isEmpty(url))
                delFile(url);
        }
    }


    public static boolean delFile(String filePath){
        File file = new File(filePath);
        if (file == null || !file.exists()) {
            return true;
        }

        return file.delete();
    }

    /**
     *
     * @param fileName
     * @return
     */
    public static String getSecondLevelDirectory(String fileName) {
        if(TextUtils.isEmpty(fileName) || fileName.length() < 4) {
            return null;
        }

        String sub1 = fileName.substring(0, 2);
        String sub2 = fileName.substring(2, 4);
        return sub1 + File.separator + sub2;
    }

    /**
     *
     * @param root
     * @param srcName
     * @param destName
     */
    public static void renameTo(String root , String srcName , String destName) {
        if(TextUtils.isEmpty(root) || TextUtils.isEmpty(srcName) || TextUtils.isEmpty(destName)){
            return;
        }

        File srcFile = new File(root + srcName);
        File newPath = new File(root + destName);

        if(srcFile.exists()) {
            srcFile.renameTo(newPath);
        }
    }

    public static File getTackPicFilePath() {
        File localFile = new File(APP_MESSAGE_PHOTO, "temp.jpg");
        if ((!localFile.getParentFile().exists())
                && (!localFile.getParentFile().mkdirs())) {
            Logger.e(TAG, "SD卡不存在");
            localFile = null;
        }
        return localFile;
    }
}
