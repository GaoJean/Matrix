package com.baidu.disconf.web.service.config.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import org.springframework.stereotype.Service;

import com.baidu.disconf.core.common.constants.DisConfigTypeEnum;
import com.baidu.disconf.web.common.Constants;
import com.baidu.disconf.web.service.config.bo.Config;
import com.baidu.disconf.web.service.config.bo.ConfigEntity;
import com.baidu.disconf.web.service.config.dao.ConfigDao;
import com.baidu.dsp.common.constant.DataFormatConstants;
import com.baidu.dsp.common.constant.FrontEndInterfaceConstant;
import com.baidu.dsp.common.dao.AbstractDao;
import com.baidu.dsp.common.dao.Columns;
import com.baidu.dsp.common.form.RequestListBase.Page;
import com.baidu.dsp.common.utils.DaoUtils;
import com.baidu.ub.common.commons.ThreadContext;
import com.baidu.ub.common.db.DaoPage;
import com.baidu.ub.common.db.DaoPageResult;
import com.baidu.unbiz.common.genericdao.operator.Match;
import com.baidu.unbiz.common.genericdao.operator.Modify;
import com.baidu.unbiz.common.genericdao.operator.Order;
import com.github.knightliao.apollo.utils.time.DateUtils;

/**
 * @author liaoqiqi
 * @version 2014-6-16
 */
@Service
public class ConfigDaoImpl extends AbstractDao<Long, Config> implements ConfigDao {

    /**
     *
     */
    @Override
    public Config getByParameter(Long appId, Long envId, String version, String key,
                                 DisConfigTypeEnum disConfigTypeEnum) {

        return findOne(new Match(Columns.APP_ID, appId), new Match(Columns.ENV_ID, envId),
            new Match(Columns.VERSION, version),
            new Match(Columns.TYPE, disConfigTypeEnum.getType()), new Match(Columns.NAME, key),
            new Match(Columns.STATUS, Constants.STATUS_NORMAL));
    }

    /**
     *
     */
    @Override
    public List<Config> getConfByAppEnv(Long appId, Long envId) {
        if (envId == null) {
            return find(new Match(Columns.APP_ID, appId),
                new Match(Columns.STATUS, Constants.STATUS_NORMAL));
        } else {

            return find(new Match(Columns.APP_ID, appId), new Match(Columns.ENV_ID, envId),
                new Match(Columns.STATUS, Constants.STATUS_NORMAL));

        }
    }

    /**
     *
     */
    @Override
    public DaoPageResult<Config> getConfigList(Long appId, Long envId, String version, Page page) {

        DaoPage daoPage = DaoUtils.daoPageAdapter(page);
        List<Match> matchs = new ArrayList<Match>();

        matchs.add(new Match(Columns.APP_ID, appId));

        matchs.add(new Match(Columns.ENV_ID, envId));

        matchs.add(new Match(Columns.VERSION, version));

        matchs.add(new Match(Columns.STATUS, Constants.STATUS_NORMAL));

        return page2(matchs, daoPage);
    }

    /**
     *
     */
    @Override
    public List<Config> getConfigList(Long appId, Long envId, String version, Boolean hasValue) {

        List<Match> matchs = new ArrayList<Match>();
        matchs.add(new Match(Columns.APP_ID, appId));
        matchs.add(new Match(Columns.ENV_ID, envId));
        matchs.add(new Match(Columns.VERSION, version));
        matchs.add(new Match(Columns.STATUS, Constants.STATUS_NORMAL));
        if (hasValue) {
            return find(matchs, new ArrayList<Order>());
        } else {
            return findColumns(matchs,
                new String[] { Columns.CONFIG_ID, Columns.TYPE, Columns.NAME, Columns.CREATE_TIME,
                               Columns.UPDATE_TIME, Columns.STATUS, Columns.APP_ID, Columns.ENV_ID,
                               Columns.VERSION });
        }
    }

    /**
     * @param configId
     */
    @Override
    public void deleteItem(Long configId) {
        String curTime = DateUtils.format(new Date(), DataFormatConstants.COMMON_TIME_FORMAT);
        List<Modify> modifyList = new ArrayList<Modify>();
        modifyList.add(modify(Columns.STATUS, Constants.STATUS_DELETE));
        modifyList.add(modify(Columns.UPDATE_TIME, curTime));

        update(modifyList, match(Columns.CONFIG_ID, configId));
    }

    /**
     *
     */
    @Override
    public void updateValue(Long configId, String value) {

        // 时间
        String curTime = DateUtils.format(new Date(), DataFormatConstants.COMMON_TIME_FORMAT);

        List<Modify> modifyList = new ArrayList<Modify>();
        modifyList.add(modify(Columns.VALUE, value));
        modifyList.add(modify(Columns.UPDATE_TIME, curTime));

        update(modifyList, match(Columns.CONFIG_ID, configId));
    }

    @Override
    public String getValue(Long configId) {
        Config config = get(configId);
        return config.getValue();
    }

    @Override
    public void delete(Long appId, Long envId) {
        String sql = "DELETE FROM config WHERE app_id=? and env_id =?";
        List<String> list = new ArrayList<String>();
        list.add(String.valueOf(appId));
        list.add(String.valueOf(envId));
        executeSQL(sql, list);
    }

    @Override
    public void deleteVersion(Long appId, Long envId, String version) {
        String sql = "DELETE FROM config WHERE app_id=? and env_id =? and version=?";
        List<String> list = new ArrayList<String>();
        list.add(String.valueOf(appId));
        list.add(String.valueOf(envId));
        list.add(version);
        executeSQL(sql, list);
    }

    @Override
    public void deleteConfig(Long envId, String version) {
        String sql = "DELETE FROM config WHERE env_id =? and version=?";
        List<String> list = new ArrayList<String>();
        list.add(String.valueOf(envId));
        list.add(version);
        executeSQL(sql, list);
    }
    
    @Override
    public List<Config> getByParameter(Long envId) {
        return find(new Match(Columns.ENV_ID, envId));
    }
    
    @Override
    public List<Config> getByParameter(Long envId, String version) {
        return find(new Match(Columns.ENV_ID, envId),
            new Match(Columns.VERSION, version));
    }
    @Override
    public List<Config> getByParameter(Long appId, Long envId, String version) {

        return find(new Match(Columns.APP_ID, appId), new Match(Columns.ENV_ID, envId),
            new Match(Columns.VERSION, version));
    }

    @Override
    public Config getByParameters(Long appId, Long envId, String version) {

        return findOne(new Match(Columns.APP_ID, appId), new Match(Columns.ENV_ID, envId),
            new Match(Columns.VERSION, version));
    }

    @Override
    public List<Config> find(List<Object> list) {
        String sql = "SELECT * from config where app_id=? and env_id =? GROUP BY version limit ?,? ";
        String sqlAll = "SELECT * from config where app_id=? and env_id =? GROUP BY version";
        List<Object> allList = new ArrayList<Object>();
        allList.add(list.get(0));
        allList.add(list.get(1));
        List<Config> configAll = findBySQL(sqlAll, allList);
        int pageSize = 0;
        if ((configAll.size() % (Integer) list.get(3)) == 0) {
            pageSize = configAll.size() / (Integer) list.get(3);
        } else {
            pageSize = (configAll.size() / (Integer) list.get(3)) + 1;
        }
        ThreadContext.putContext(FrontEndInterfaceConstant.PAGE_SIZE, pageSize);
        ThreadContext.putContext(FrontEndInterfaceConstant.PAGE_NO, list.get(3));
        return findBySQL(sql, list);
    }

    @Override
    public List<String> getAllVersionByEnvId(Long envId) {
        String sql = "SELECT  * from config where env_id = ?";
        List<Object> paramList = new ArrayList<Object>();
        List<String> resultList = new ArrayList<String>();
        paramList.add(envId);
        List<Config> configAll = findBySQL(sql, paramList);
        for (int i = 0; i < configAll.size(); i++) {
            resultList.add(configAll.get(i).getVersion());
        }
        HashSet hashSet  =   new  HashSet(resultList); 
        resultList.clear(); 
        resultList.addAll(hashSet); 
        return resultList;
    }

    
}
