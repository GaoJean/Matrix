package com.baidu.disconf.web.service.log.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.baidu.disconf.web.service.log.bo.Log;
import com.baidu.disconf.web.service.log.dao.LogHistoryDao;
import com.baidu.dsp.common.dao.AbstractDao;
@Service
public class LogHistoryDaoImpl extends AbstractDao<Long, Log> implements LogHistoryDao {

    @Override
    public List<Log> findListBySql() {
        String sql  = "SELECT * from log_history ORDER BY id DESC LIMIT 0,5";
        List<Log> logList = new ArrayList<Log>();
        return  findBySQL(sql,logList);
    }

   


}
