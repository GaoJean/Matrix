package com.baidu.disconf.web.service.config.dao;

import java.util.List;

import com.baidu.disconf.core.common.constants.DisConfigTypeEnum;
import com.baidu.disconf.web.service.config.bo.Config;
import com.baidu.disconf.web.service.config.bo.ConfigEntity;
import com.baidu.dsp.common.form.RequestListBase.Page;
import com.baidu.ub.common.db.DaoPageResult;
import com.baidu.unbiz.common.genericdao.dao.BaseDao;

/**
 * @author liaoqiqi
 * @version 2014-6-16
 */
public interface ConfigDao extends BaseDao<Long, Config> {

    /**
     * @param appId
     * @param envId
     * @param version
     * @param key
     * @param disConfigTypeEnum
     *
     * @return
     */
    Config getByParameter(Long appId, Long envId, String version, String key, DisConfigTypeEnum disConfigTypeEnum);

    /**
     * @param
     *
     * @return
     */
    List<Config> getConfByAppEnv(Long appId, Long envId);

    /**
     * @param appId
     * @param envId
     * @param version
     *
     * @return
     */
    DaoPageResult<Config> getConfigList(Long appId, Long envId, String version, Page page);

    /**
     * @param configId
     *
     * @return
     */
    void updateValue(Long configId, String value);

    /**
     *
     */
    String getValue(Long configId);

    /**
     * @param appId
     * @param envId
     * @param version
     * @param hasValue
     * @return
     */
    List<Config> getConfigList(Long appId, Long envId, String version, Boolean hasValue);


    /**
     * @param configId
     */
    void deleteItem(Long configId);

    void delete(Long appId, Long envId);
    void deleteVersion(Long appId, Long envId,String version);
    void deleteConfig(Long envId,String version);
    /**
     * 
     * @param appId
     * @param envId
     * @param version
     * @return
     */
    List<Config> getByParameter(Long appId, Long envId, String version);
    List<Config> getByParameter(Long envId, String version);
    List<Config> getByParameter(Long envId);
    Config getByParameters(Long appId, Long envId, String version);

   /**
    * 
    * @param list  appId,envId,currentPage,pageCount
    * @return
    */
    List<Config> find(List<Object> list);
    
    /**
     * 
     * @param envId
     * @return
     */
    List<String> getAllVersionByEnvId(Long envId);

}
