package com.baidu.disconf.web.service.app.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import com.baidu.disconf.web.service.app.bo.App;
import com.baidu.disconf.web.service.app.dao.AppDao;
import com.baidu.dsp.common.constant.FrontEndInterfaceConstant;
import com.baidu.dsp.common.dao.AbstractDao;
import com.baidu.dsp.common.dao.Columns;
import com.baidu.ub.common.commons.ThreadContext;
import com.baidu.unbiz.common.genericdao.operator.Match;

/**
 * @author liaoqiqi
 * @version 2014-6-16
 */
@Service
public class AppDaoImpl extends AbstractDao<Long, App> implements AppDao {

    @Override
    public App getByName(String name) {

        return findOne(new Match(Columns.NAME, name));
    }

    @Override
    public List<App> getByIds(Set<Long> ids) {

        if (CollectionUtils.isEmpty(ids)) {
            return findAll();
        }

        return find(match(Columns.APP_ID, ids));
    }

    @Override
    public List<App> getApp(List<String> list) {
        String sql = "select * FROM app where app_id IN(select DISTINCT(app_id) from config WHERE 1=1"
                     + " and env_id = ?) limit ?,?";
        String sqlAll = "select * FROM app where app_id IN(select DISTINCT(app_id) from config WHERE 1=1"
                        + " and env_id = ?)";
        String envId = (String) list.get(1);
        int startIndex = (Integer.parseInt(list.get(2)) - 1) * Integer.parseInt(list.get(3));
        int endIndex = Integer.parseInt(list.get(3));
        List<Object> list2 = new ArrayList<Object>();

        list2.add(envId);
        list2.add(startIndex);
        list2.add(endIndex);

        List<String> listAll = new ArrayList<String>();
        listAll.add(envId);
        List<App> apps = findBySQL(sqlAll, listAll);
        int pageSize = (apps.size() % endIndex) == 0 ? (apps.size() / endIndex)
            : ((apps.size() / endIndex) + 1);
        ThreadContext.putContext(FrontEndInterfaceConstant.PAGE_SIZE, pageSize);
        ThreadContext.putContext(FrontEndInterfaceConstant.PAGE_NO, list.get(2));
        return findBySQL(sql, list2);
    }
}
