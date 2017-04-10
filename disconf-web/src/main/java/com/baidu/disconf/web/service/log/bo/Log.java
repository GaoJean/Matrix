package com.baidu.disconf.web.service.log.bo;

import lombok.Data;

import com.baidu.dsp.common.dao.Columns;
import com.baidu.dsp.common.dao.DB;
import com.baidu.unbiz.common.genericdao.annotation.Column;
import com.baidu.unbiz.common.genericdao.annotation.Table;
import com.github.knightliao.apollo.db.bo.BaseObject;

/**
 *  操作日志
 * @author GaoJean
 * @version 2016-10-11
 *
 */
@Data
@Table(db = DB.DB_NAME, name = "log_history", keyColumn = "id")
public class Log extends BaseObject<Long> {

    @Column(value = Columns.USER_ID)
    private Long   userId;

    @Column(value = Columns.APP_ID)
    private Long   appId;

    @Column(value = Columns.ENV_ID)
    private Long   envId;

    @Column(value = Columns.VERSION)
    private String version;

    @Column(value = "operation")
    private String operation;

    @Column(value = "description")
    private String description;

    @Column(value = Columns.UPDATE_TIME)
    private String updateTime;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }

    public Long getEnvId() {
        return envId;
    }

    public void setEnvId(Long envId) {
        this.envId = envId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

}
