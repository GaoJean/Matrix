package com.baidu.disconf.web.service.log.dao;

import java.util.List;

import com.baidu.disconf.web.service.log.bo.Log;
import com.baidu.unbiz.common.genericdao.dao.BaseDao;

public interface LogHistoryDao  extends BaseDao<Long, Log>{

    List<Log> findListBySql();

   
}
