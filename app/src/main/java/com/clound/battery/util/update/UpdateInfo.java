package com.clound.battery.util.update;

/**
 * Created by Administrator on 2018/9/29.
 */
public class UpdateInfo {

    /**
     * 应用名称
     **/
    private String appName;
    /**
     * 版本号
     **/
    private int versionCode;
    /**
     * 版本名
     **/
    private String versionName;
    /**
     * 更新地址
     **/
    private String downUrl;
    /**
     * 更新描述
     **/
    private String description;

    /**
     * @param versionCode
     * @param versionName
     * @param downUrl
     * @param description
     */
    public UpdateInfo(String appName, int versionCode, String versionName,
                      String downUrl, String description) {
        super();
        this.appName = appName;
        this.versionCode = versionCode;
        this.versionName = versionName;
        this.downUrl = downUrl;
        this.description = description;
    }

    public UpdateInfo() {
        super();
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getDownUrl() {
        return downUrl;
    }

    public void setDownUrl(String downUrl) {
        this.downUrl = downUrl;
    }

    public StringBuilder getDescription() {
        String[] tips = description.split(";");
        StringBuilder updateContent = new StringBuilder();
        for (int i = 0; i < tips.length; i++) {
            if (i != tips.length - 1) {
                updateContent.append(tips[i] + "\n");
            } else {
                updateContent.append(tips[i]);
            }
        }
        return updateContent;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    @Override
    public String toString() {
        return "UpdateInfo{" +
                "appName='" + appName + '\'' +
                ", versionCode=" + versionCode +
                ", versionName='" + versionName + '\'' +
                ", downUrl='" + downUrl + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}