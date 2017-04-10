package com.baidu.disconf.web.common;

/**
 * Created by knightliao on 15/12/25.
 */
public class Constants {

    public final static Integer STATUS_NORMAL     = 1;
    public final static Integer STATUS_DELETE     = 0;

    /**
     * 基础版本
     */
    public final static String  VERSION_ROOT      = "0.0.1";
    /**
     * 发送redmine的地址<br>
     * 
     * 
     * add by GaoJean  on 10/10/2016
     */
    public final static String  REDMINE_URL       = "http://redmine.idanlu.com/issues/6369.json";
    /**
     * 登录redmine的用户key<br>
     * 
     * 
     * add by GaoJean  on 10/10/2016
     */
    public final static String  REDMINE_USER_KEY  = "c2a6e7e9bd8e1b3f8cdabe8e64f92c510006efb4";

    /**
     * 操作日志  删除
     */
    public final static String  DELETE            = "1";
    /**
     * 操作日志  新增单个微服务
     */
    public final static String  ADD               = "2";
    /**
     * 操作日志  更新
     */
    public final static String  UPDATE            = "3";
    /**
     * 操作日志  新增某环境所有的微服务
     */
    public final static String  ADD_ALL           = "4";

    /**
     * 操作日志  合并(复制到主版本)
     */
    public final static String  MERGE_TO_MASTER   = "5";

    /**
     * 操作日志 合并(从主版本复制)
     */
    public final static String  MERGE_FROM_MASTER = "6";
}
