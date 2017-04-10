package com.baidu.disconf.web.service.config.service;

import java.io.File;
import java.util.List;
import java.util.Map;

import com.baidu.disconf.core.common.constants.DisConfigTypeEnum;
import com.baidu.disconf.web.service.config.bo.Config;
import com.baidu.disconf.web.service.config.form.ConfListForm;
import com.baidu.disconf.web.service.config.form.ConfNewItemForm;
import com.baidu.disconf.web.service.config.form.MergeVersionForm;
import com.baidu.disconf.web.service.config.form.NameAllCopyForm;
import com.baidu.disconf.web.service.config.form.NameCopyForm;
import com.baidu.disconf.web.service.config.vo.ConfListVo;
import com.baidu.disconf.web.service.config.vo.MachineListVo;
import com.baidu.ub.common.db.DaoPageResult;

/**
 * @author liaoqiqi
 * @version 2014-6-16
 */
public interface ConfigMgr {

    /**
     * @param
     *
     * @return
     */
    List<String> getVersionListByAppEnv(Long appId, Long envId);

    /**
     * @return
     */
    DaoPageResult<ConfListVo> getConfigList(ConfListForm confListForm, boolean fetchZk, final boolean getErrorMessage);

    /**
     * @param configId
     *
     * @return
     */
    ConfListVo getConfVo(Long configId);

    MachineListVo getConfVoWithZk(Long configId);

    /**
     * @param configId
     *
     * @return
     */
    Config getConfigById(Long configId);

    /**
     * 更新 配置项/配置文件
     *
     * @param configId
     *
     * @return
     */
    String updateItemValue(Long configId, String value);

    /**
     * @param configId
     *
     * @return
     */
    String getValue(Long configId);

    void notifyZookeeper(Long configId);

    /**
     * @param confNewForm
     * @param disConfigTypeEnum
     */
    void newConfig(ConfNewItemForm confNewForm, DisConfigTypeEnum disConfigTypeEnum);

    void delete(Long configId);

    /**
     * @param confListForm
     *
     * @return
     */
    List<File> getDisconfFileList(ConfListForm confListForm);

    
    /**
     * delete by map
     * @param map
     */
    void delele(Map<String, Object> map);
    /**
     * deleteVersion by map
     * @param map
     */
    void deleleVersion(Map<String, Object> map);

    void deleteConfig(Map<String, Object> map);
    
    void nameCopy(NameCopyForm nameCopyForm);
    void nameAllCopy(NameAllCopyForm nameCopyForm);

    List<Config> getVersionListByAppEnv(Long appId, Long envId, String currentPage, String pageCount);

    /**
     * 返回 env环境下所有版本
     * @param envIdLong
     * @return
     */
    List<String> getAllVersionByEnvId(Long envIdLong);

    /**
     * 合并当前版本到主版本
     * @param mergeVersionForm
     */
    void mergeToMasterVersion(MergeVersionForm mergeVersionForm);

    /**
     * 从主分支复制合并
     * @param mergeVersionForm
     */
    void mergeFromMasterVersion(MergeVersionForm mergeVersionForm);
    

}
