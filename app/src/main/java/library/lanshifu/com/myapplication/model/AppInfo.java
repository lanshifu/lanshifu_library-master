package library.lanshifu.com.myapplication.model;

import android.graphics.drawable.Drawable;

/**
 * Created by lanshifu on 2017/12/10.
 */

public class AppInfo {
    private Drawable appIcon;
    private String appLabel;

    public String getFirstActivityName() {
        return firstActivityName;
    }

    public void setFirstActivityName(String firstActivityName) {
        this.firstActivityName = firstActivityName;
    }

    private String firstActivityName;
    private String cachesize;
    private String codesize;
    private String datasize;
    private Boolean isSystem;
    private String location;
    private String mVersion;
    private String pkgName;
    private String sigmd5;
    private String totalSize;

    @Override
    public String toString() {
        return "AppInfo{" +
                "appLabel='" + appLabel + '\'' +
                ", firstActivityName='" + firstActivityName + '\'' +
                ", pkgName='" + pkgName + '\'' +
                '}';
    }

    private long totalSizeLong;

    public Drawable getAppIcon() {
        return appIcon;
    }

    public void setAppIcon(Drawable appIcon) {
        this.appIcon = appIcon;
    }

    public String getAppLabel() {
        return appLabel;
    }

    public void setAppLabel(String appLabel) {
        this.appLabel = appLabel;
    }

    public String getCachesize() {
        return cachesize;
    }

    public void setCachesize(String cachesize) {
        this.cachesize = cachesize;
    }

    public String getCodesize() {
        return codesize;
    }

    public void setCodesize(String codesize) {
        this.codesize = codesize;
    }

    public String getDatasize() {
        return datasize;
    }

    public void setDatasize(String datasize) {
        this.datasize = datasize;
    }

    public Boolean getSystem() {
        return isSystem;
    }

    public void setSystem(Boolean system) {
        isSystem = system;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getmVersion() {
        return mVersion;
    }

    public void setmVersion(String mVersion) {
        this.mVersion = mVersion;
    }

    public String getPkgName() {
        return pkgName;
    }

    public void setPkgName(String pkgName) {
        this.pkgName = pkgName;
    }

    public String getSigmd5() {
        return sigmd5;
    }

    public void setSigmd5(String sigmd5) {
        this.sigmd5 = sigmd5;
    }

    public String getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(String totalSize) {
        this.totalSize = totalSize;
    }

    public long getTotalSizeLong() {
        return totalSizeLong;
    }

    public void setTotalSizeLong(long totalSizeLong) {
        this.totalSizeLong = totalSizeLong;
    }
}
