package com.baidu.disconf.web.web.app.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baidu.disconf.web.service.app.bo.App;
import com.baidu.disconf.web.service.app.form.AppForm;
import com.baidu.disconf.web.service.app.form.AppNewForm;
import com.baidu.disconf.web.service.app.service.AppMgr;
import com.baidu.disconf.web.service.app.vo.AppListVo;
import com.baidu.disconf.web.web.app.validator.AppValidator;
import com.baidu.dsp.common.constant.FrontEndInterfaceConstant;
import com.baidu.dsp.common.constant.WebConstants;
import com.baidu.dsp.common.controller.BaseController;
import com.baidu.dsp.common.vo.JsonObjectBase;
import com.baidu.ub.common.commons.ThreadContext;

/**
 * @author liaoqiqi
 * @version 2014-6-16
 */
@Controller
@RequestMapping(WebConstants.API_PREFIX + "/app")
public class AppController extends BaseController {

    protected static final Logger LOG = LoggerFactory.getLogger(AppController.class);

    @Autowired
    private AppMgr appMgr;

    @Autowired
    private AppValidator appValidator;

    /**
     * list
     *
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public JsonObjectBase list() {

        List<AppListVo> appListVos = appMgr.getAuthAppVoList();

        return buildListSuccess(appListVos, appListVos.size());
    }

    /**
     * create
     *
     * @return
     */
    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseBody
    public JsonObjectBase create(@Valid AppNewForm appNewForm) {

        LOG.info(appNewForm.toString());

        appValidator.validateCreate(appNewForm);

        appMgr.create(appNewForm);

        return buildSuccess("创建成功");
    }

    /**
     * 根据环境获得app
     * 
     * return 
     */
    @RequestMapping(value="/getApp" ,method=RequestMethod.POST)
    @ResponseBody
    public JsonObjectBase getApp(@Valid AppForm appForm){
        List<String> list = new ArrayList<String>();
        list.add(appForm.getUserId());
        list.add(appForm.getEnvId());
        list.add(appForm.getCurrentPage());
        list.add(appForm.getPageCount());
        List<App> apps = appMgr.getApp(list);
        return buildListSuccess(apps,apps.size());
    }

}
