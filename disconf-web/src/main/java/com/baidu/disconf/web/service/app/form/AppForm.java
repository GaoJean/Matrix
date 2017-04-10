package com.baidu.disconf.web.service.app.form;


public class AppForm {

    /**
     * 
     */
    private static final long serialVersionUID = -3084891384971712049L;

    private String            envId;

    private String            userId;

    private String            currentPage;                              //当前页数

    private String            pageCount;                               //显示数据项总数

    public String getEnvId() {
        return envId;
    }

    public void setEnvId(String envId) {
        this.envId = envId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(String currentPage) {
        this.currentPage = currentPage;
    }

    public String getPageCount() {
        return pageCount;
    }

    public void setPageCount(String pageCount) {
        this.pageCount = pageCount;
    }

}
