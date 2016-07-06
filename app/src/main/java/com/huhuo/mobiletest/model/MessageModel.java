package com.huhuo.mobiletest.model;

import com.huhuo.mobiletest.constants.MessageCode;
import com.huhuo.mobiletest.utils.StringHelper;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.yuntongxun.ecsdk.ECMessage;
import com.yuntongxun.ecsdk.im.ECImageMessageBody;
//import com.yuntongxun.ecsdk.im.ECLocationMessageBody;
import com.yuntongxun.ecsdk.im.ECTextMessageBody;
import com.yuntongxun.ecsdk.im.ECVoiceMessageBody;

import java.util.Date;

/**
 * Created by xiejc on 15/12/16.
 * <p>
 * <br/><br/>
 * 用户id是谁，就意味着是当前用户和该id的对话，此userId不可能是自己的userId
 */
@DatabaseTable(tableName = "tb_message")
public class MessageModel {

    @DatabaseField(generatedId = true)
    private int id;

    //一般情况下，只有发送的消息类型需要有messageId
    @DatabaseField
    private String messageId;

    @DatabaseField
    private String userId;

    //文本消息内容
    @DatabaseField
    private String message;

    @DatabaseField
    private String fromName;

    @DatabaseField
    private String toName;

    @DatabaseField
    private Date messageDate;

    @DatabaseField
    private String fromLogo;

    @DatabaseField
    private String toLogo;

    /**
     * 消息发送者
     */
    @DatabaseField
    private String fromId;

    /**
     * 消息接收者
     */
    @DatabaseField
    private String toId;

    //消息类型，用于区分是文本消息、图片消息、还是语音消息等
    @DatabaseField
    private int msgType;

    @DatabaseField
    private int sendType;

    @DatabaseField
    private String picUrl;

    @DatabaseField
    private String picLocalUrl;

    //本地文件地址，本地发送图片或者语音或者文件时保存的本地地址
    @DatabaseField
    private String path;

    //语音消息地址
    @DatabaseField
    private String voiceUrl;

    //语音消息本地地址
    @DatabaseField
    private String voiceLocalUrl;

    //语音消息时长
    @DatabaseField
    private long voiceDuration;

    //纬度
    @DatabaseField
    private double latitude;

    //经度
    @DatabaseField
    private double longitude;

    //有些消息类型可能
    @DatabaseField
    private String title;

    //标示发送状态，1,成功，其他失败
    @DatabaseField
    private int sendStatus = 0;

    //是否是重新发送的消息标记,1.表示需要重新发送的消息
    @DatabaseField
    private int resendMsg = 0;

    @DatabaseField
    private String reason;

    public MessageModel() {

    }

    public MessageModel(ECMessage msg) {
        this.fromId = msg.getForm();
        this.toId = msg.getTo();
        String messageId = StringHelper.getStringUUID();
        this.messageId = messageId;

        copyMsgTypeData(msg);

        this.messageDate = new Date(msg.getMsgTime());
        this.msgType = getMsgType(msg.getType());
        this.sendType = getMsgSendType(msg.getDirection());

        /**
         * 如果是发送类型的消息，fromName就是当前登录用户自己，toName就是消息接收者
         * 如果是接受类型的消息，fromName就是好友，toName就是当前登陆用户自己
         * 如果姓名为null，name就用userId显示
         */
        this.fromId = msg.getForm();
        this.toId = msg.getTo();

        if (this.sendType == MessageCode.SEND_TYPE) {
            this.userId = msg.getTo();
        } else {
            this.userId = msg.getForm();
        }

    }

    /**
     * 根据不同的消息类型，拷贝不同的数据到model上
     * @param msg
     */
    private void copyMsgTypeData(ECMessage msg) {
        if (msg.getBody() != null) {
            final ECMessage.Type type = msg.getType();

            switch (type) {
                case NONE:
                //TODO 暂不处理
                    break;

                case TXT:
                    final ECTextMessageBody txtBody = (ECTextMessageBody) msg.getBody();
                    this.message = txtBody.getMessage();
                    break;

                case VOICE:
                    final ECVoiceMessageBody voiceBody = (ECVoiceMessageBody) msg.getBody();
                    this.voiceUrl = voiceBody.getRemoteUrl();
                    this.voiceLocalUrl = voiceBody.getLocalUrl();
                    break;
                case VIDEO:
                    break;

                case IMAGE:
                    final ECImageMessageBody imageBody = (ECImageMessageBody) msg.getBody();
                    this.picUrl = imageBody.getRemoteUrl();
                    this.picLocalUrl = imageBody.getLocalUrl();
                    break;

//                case LOCATION:
//                    final ECLocationMessageBody locationBody = (ECLocationMessageBody) msg.getBody();
//                    this.latitude = locationBody.getLatitude();
//                    this.longitude = locationBody.getLongitude();
//                    this.title = locationBody.getTitle();
//                    break;

                case FILE:
                    //TODO 暂不支持
                    break;
                default:
                    break;
            }
        }
    }

    private int getMsgSendType(ECMessage.Direction direction) {
        int sendType = MessageCode.RECEIVE_TYPE;
        if (direction != null) {
            switch (direction) {
                case SEND:
                    sendType = MessageCode.SEND_TYPE;
                    break;
                case RECEIVE:
                    sendType = MessageCode.RECEIVE_TYPE;
                    break;
                default:
                    break;
            }
        }
        return sendType;
    }

    private int getMsgType(ECMessage.Type type) {
        int msgType = MessageCode.NONE;
        if (type != null) {
            switch (type) {
                case NONE:
                    msgType = MessageCode.NONE;
                    break;

                case TXT:
                    msgType = MessageCode.TXT;
                    break;

                case VOICE:
                    msgType = MessageCode.VOICE;
                    break;

                case VIDEO:
                    msgType = MessageCode.VIDEO;
                    break;

                case IMAGE:
                    msgType = MessageCode.IMAGE;
                    break;

                case LOCATION:
                    msgType = MessageCode.LOCATION;
                    break;

                case FILE:
                    msgType = MessageCode.FILE;
                    break;
                default:
                    break;
            }
        }
        return msgType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getMessageDate() {
        return messageDate;
    }

    public void setMessageDate(Date messageDate) {
        this.messageDate = messageDate;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public String getFromId() {
        return fromId;
    }

    public void setFromId(String fromId) {
        this.fromId = fromId;
    }

    public String getToId() {
        return toId;
    }

    public void setToId(String toId) {
        this.toId = toId;
    }

    public String getFromName() {
        return fromName;
    }

    public void setFromName(String fromName) {
        this.fromName = fromName;
    }

    public String getToName() {
        return toName;
    }

    public void setToName(String toName) {
        this.toName = toName;
    }

    /**
     * 获得当前的消息类型，用于区分是文本消息、图片消息、还是语音消息等
     * @return
     */
    public int getMsgType() {
        return msgType;
    }

    /**
     * 设置当前的消息类型，用于区分是文本消息、图片消息、还是语音消息等
     * @return
     */
    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }


    /**
     * 获得当前消息的类型，是发送的消息，还是接收到得消息
     * @return
     */
    public int getSendType() {
        return sendType;
    }

    /**
     * 设置当前消息的类型，是发送的消息，还是接收到得消息
     * @return
     */
    public void setSendType(int sendType) {
        this.sendType = sendType;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public int getSendStatus() {
        return sendStatus;
    }

    public void setSendStatus(int sendStatus) {
        this.sendStatus = sendStatus;
    }

    public int getResendMsg() {
        return resendMsg;
    }

    public void setResendMsg(int resendMsg) {
        this.resendMsg = resendMsg;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getVoiceUrl() {
        return voiceUrl;
    }

    public void setVoiceUrl(String voiceUrl) {
        this.voiceUrl = voiceUrl;
    }

    public long getVoiceDuration() {
        return voiceDuration;
    }

    public void setVoiceDuration(long voiceDuration) {
        this.voiceDuration = voiceDuration;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }


    public String getVoiceLocalUrl() {
        return voiceLocalUrl;
    }

    public void setVoiceLocalUrl(String voiceLocalUrl) {
        this.voiceLocalUrl = voiceLocalUrl;
    }

    public String getPicLocalUrl() {
        return picLocalUrl;
    }

    public void setPicLocalUrl(String picLocalUrl) {
        this.picLocalUrl = picLocalUrl;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
