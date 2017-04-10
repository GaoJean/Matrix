package com.baidu.disconf.web.service.log.service.impl;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.http.client.ClientProtocolException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baidu.disconf.web.common.Constants;
import com.baidu.disconf.web.service.app.bo.App;
import com.baidu.disconf.web.service.app.dao.AppDao;
import com.baidu.disconf.web.service.config.bo.Config;
import com.baidu.disconf.web.service.config.dao.ConfigDao;
import com.baidu.disconf.web.service.env.bo.Env;
import com.baidu.disconf.web.service.env.dao.EnvDao;
import com.baidu.disconf.web.service.log.bo.Log;
import com.baidu.disconf.web.service.log.dao.LogHistoryDao;
import com.baidu.disconf.web.service.log.service.AllOpertaerMgr;
import com.baidu.disconf.web.service.user.bo.User;
import com.baidu.disconf.web.service.user.dao.UserDao;
import com.baidu.disconf.web.service.user.dto.Visitor;
import com.baidu.disconf.web.utils.redmine.RedmineService;
import com.baidu.dsp.common.constant.DataFormatConstants;
import com.baidu.ub.common.commons.ThreadContext;
import com.github.knightliao.apollo.utils.time.DateUtils;

@Service
public class AllOperateMgrImpl implements AllOpertaerMgr {
    @Autowired
    private ConfigDao      configDao;

    @Autowired
    private AppDao         appDao;

    @Autowired
    private EnvDao         envDao;
    @Autowired
    private UserDao        userDao;

    @Autowired
    private LogHistoryDao  logHistoryDao;
    @Autowired
    private RedmineService redmineService;

    @Override
    public void updateLog(Map<String, Object> map) {

        try {
            Visitor visitor = ThreadContext.getSessionVisitor();
            App app = new App();
            Env env = new Env();
            String updateTime = DateUtils.format(new Date(), DataFormatConstants.STAND_TIME_FORMAT);
            String description = "";
            long appId = (Long) (map.get("appId") == null ? null : map.get("appId"));
            long envId = (Long) (map.get("envId") == null ? null : map.get("envId"));
            String version = (String) (map.get("version") == null ? null : map.get("version"));
            String operation = (String) (map.get("operation") == null ? null
                : map.get("operation"));

            if (appId != 0) {
                app = getAppById(appId);
            }
            if (envId != 0) {
                env = getEnv(envId);
            }
            //Config config = getConfig(appId, envId, version);
            if (operation.equals(Constants.ADD)) {//复制单个微服务
                long oldAppId = (Long) (map.get("oldAppId") == null ? null : map.get("oldAppId"));
                String oldVersion = (String) (map.get("oldVersion") == null ? null
                    : map.get("oldVersion"));
                App oldApp = getAppById(oldAppId);
                Env oldEnv = getEnv((Long) (map.get("oldEnvId") == null ? null : map.get("oldEnvId")));
                
                description = updateTime + "  ,用户" + visitor.getLoginUserName() + "从 "
                              + oldEnv.getName() + "环境 " + oldApp.getName() + " 微服务" + oldVersion
                              + "版本，复制到了" + env.getName() + "环境" + app.getName() + "微服务" + version
                              + "版本";
            } else if (operation.equals(Constants.DELETE)) {
                if (!StringUtils.isBlank(version)) {
                    //delete version
                    description = updateTime + "  ,用户" + visitor.getLoginUserName() + "删除了 "
                                  + env.getName() + "环境中的服务( " + app.getName() + " )下的版本( " + version+" )";
                } else {
                    //delete app
                    description = updateTime + " ,用户" + visitor.getLoginUserName() + "删除了 "
                                  + env.getName() + "环境中的服务( " + app.getName()+" )";
                }
            } else if (operation.equals(Constants.UPDATE)) {

                //active = "更新";
            }else if (operation.equals(Constants.ADD_ALL)) {//复制所有微服务的配置文件
                long envIdTarget = (Long) (map.get("envIdTarget"));
                String versionSource = (String) map.get("versionSource");
                String versionTarget = (String) map.get("versionTarget");
                Env envTarget = getEnv(envIdTarget);
                //2017/02/10 17:01:57 ,用户Danlu_Admin从 webtest环境  0.0.1版本，复制到了apptest环境 0.0.2版本

                description = updateTime + " ,用户" + visitor.getLoginUserName() + "从 "
                              + env.getName() + " 环境的"+ versionSource +" 版本，复制到了 " + envTarget.getName() + "环境的 " + versionTarget + " 版本";
            }else if(operation.equals(Constants.MERGE_TO_MASTER)){//复制到主版本
                long versionMaster = (Long) (map.get("versionMaster"));
                
                description = updateTime + " ,用户" + visitor.getLoginUserName() + "合并了"
                                + env.getName() + " 环境下的" + app.getName() + "微服务的"
                                + version + " 版本到 " + versionMaster + " 主版本";
            }else if (operation.equals(Constants.MERGE_FROM_MASTER)) {//从主版本复制
                long versionMaster = (Long) (map.get("versionMaster"));
                
                description = updateTime + " ,用户" + visitor.getLoginUserName() + "从主版本"
                                + env.getName() + " 环境下的" + app.getName() + "微服务的"
                                + versionMaster + " 版本到 " + version + " 版本";
            }

            Log log = new Log();
            log.setAppId(appId);
            log.setEnvId(envId);
            log.setUserId(visitor.getLoginUserId());
            log.setVersion(version);
            log.setDescription(description);
            log.setOperation(operation);
            log.setUpdateTime(updateTime);

            Log logResult = logHistoryDao.create(log);
            if (logResult != null) {
                redmineService.send2Redmine(description);//操作日志更新到redmine
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 获得app
     * @param appId
     * @return
     */
    public App getAppById(long appId) {
        return appDao.get(appId);
    }

    /**
     * 获得config
     * @param appId
     * @param envId
     * @param version
     * @return
     */
    public Config getConfig(long appId, long envId, String version) {
        return configDao.getByParameters(appId, envId, version);
    }

    /**
     * 获得用户
     * @param userId
     * @return
     */
    public User getUser(long userId) {
        return userDao.get(userId);
    }

    /**
     * 获得环境
     * @param envId
     * @return
     */
    public Env getEnv(long envId) {
        return envDao.get(envId);
    }

    public void deleteInfo(Map<String, Object> map) {

    }

    @Override
    public List<Log> getLogHistory() {

        return logHistoryDao.findListBySql();
    }
}
