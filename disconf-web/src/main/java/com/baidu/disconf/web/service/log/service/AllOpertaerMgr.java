package com.baidu.disconf.web.service.log.service;

import java.util.List;
import java.util.Map;

import com.baidu.disconf.web.service.log.bo.Log;


/**
 * 
 * @author GaoJean
 *
 */
public interface AllOpertaerMgr {
    
    /**
     * 更新操作日志
     * @param map
     * @return
     */
    void updateLog(Map<String, Object> map);

    /**
     * 获得前五条操作日志
     * @return
     */
    List<Log> getLogHistory();

}
