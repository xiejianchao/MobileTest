package com.huhuo.mobiletest.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

/**
 * Created by xiejc on 15/12/16.
 */
@DatabaseTable(tableName = "tb_recentmessage")
public class RecentMessageModel {

//    @DatabaseField(generatedId = true)
//    public int id;
    /**
     * 根据userId来决定是否是同一条会话，永远不可能是自己，只能是好友的userId
     */
    @DatabaseField(id = true)
    private String userId;

    @DatabaseField
    private String avatarUrl;

    @DatabaseField
    private String fromLogo;

    @DatabaseField
    private String toLogo;

    @DatabaseField
    private String fromName;

    @DatabaseField
    private String toName;

    @DatabaseField
    private String recentMsg;

    @DatabaseField
    private Date recentMsgDate;

    @DatabaseField
    private int isRead;

    public RecentMessageModel() {

    }

    public RecentMessageModel(MessageModel model) {
        this.userId = model.getUserId();
        this.recentMsg = model.getMessage();
        this.recentMsgDate = model.getMessageDate();
        this.fromName = model.getFromName();
        this.toName = model.getToName();
    }

//    public int getId() {
//        return id;
//    }
//
//    public void setId(int id) {
//        this.id = id;
//    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getFromLogo() {
        return fromLogo;
    }

    public void setFromLogo(String fromLogo) {
        this.fromLogo = fromLogo;
    }

    public String getToLogo() {
        return toLogo;
    }

    public void setToLogo(String toLogo) {
        this.toLogo = toLogo;
    }

    public String getFromName() {
        return fromName;
    }

    public void setFromName(String fromName) {
        this.fromName = fromName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getToName() {
        return toName;
    }

    public void setToName(String toName) {
        this.toName = toName;
    }


    public String getRecentMsg() {
        return recentMsg;
    }

    public void setRecentMsg(String recentMsg) {
        this.recentMsg = recentMsg;
    }

    public Date getRecentMsgDate() {
        return recentMsgDate;
    }

    public void setRecentMsgDate(Date recentMsgDate) {
        this.recentMsgDate = recentMsgDate;
    }

    public int getIsRead() {
        return isRead;
    }

    public void setIsRead(int isRead) {
        this.isRead = isRead;
    }

    @Override
    public String toString() {
        return "RecentMessageModel{" +
                ", avatarUrl='" + avatarUrl + '\'' +
                ", fromLogo='" + fromLogo + '\'' +
                ", toLogo='" + toLogo + '\'' +
                ", fromName='" + fromName + '\'' +
                ", userId='" + userId + '\'' +
                ", toName='" + toName + '\'' +
                ", recentMsg='" + recentMsg + '\'' +
                ", recentMsgDate=" + recentMsgDate +
                ", isRead=" + isRead +
                '}';
    }
}
