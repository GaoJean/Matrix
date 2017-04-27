package com.baidu.disconf.web.web.config.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.baidu.disconf.web.common.Constants;
import com.baidu.disconf.web.service.app.form.AppDeleteForm;
import com.baidu.disconf.web.service.config.bo.Config;
import com.baidu.disconf.web.service.config.form.ConfVersionForm;
import com.baidu.disconf.web.service.config.form.ConfVersoinListForm;
import com.baidu.disconf.web.service.config.form.MergeVersionForm;
import com.baidu.disconf.web.service.config.form.NameAllCopyForm;
import com.baidu.disconf.web.service.config.form.NameCopyForm;
import com.baidu.disconf.web.service.config.service.ConfigMgr;
import com.baidu.disconf.web.service.log.bo.Log;
import com.baidu.disconf.web.service.log.service.AllOpertaerMgr;
import com.baidu.disconf.web.service.role.constant.RoleConstant;
import com.baidu.disconf.web.service.user.dto.Visitor;
import com.baidu.disconf.web.web.config.validator.ConfigValidator;
import com.baidu.disconf.web.web.config.validator.FileUploadValidator;
import com.baidu.dsp.common.constant.WebConstants;
import com.baidu.dsp.common.controller.BaseController;
import com.baidu.dsp.common.exception.FileUploadException;
import com.baidu.dsp.common.vo.JsonObjectBase;
import com.baidu.ub.common.commons.ThreadContext;

/**
 * 专用于配置更新、删除
 *
 * @author liaoqiqi
 * @version 2014-6-24
 */
@Controller
@RequestMapping(WebConstants.API_PREFIX + "/web/config")
public class ConfigUpdateController extends BaseController {

    
    protected static final Logger LOG = LoggerFactory.getLogger(ConfigUpdateController.class);

    @Autowired
    private ConfigMgr configMgr;
    
    @Autowired
    private AllOpertaerMgr allOpertaerMgr;

    @Autowired
    private ConfigValidator configValidator;

    @Autowired
    private FileUploadValidator fileUploadValidator;


    
    /**
     * 配置项的更新
     *
     * @param configId
     * @param value
     *
     * @return
     */
    @RequestMapping(value = "/item/{configId}", method = RequestMethod.PUT)
    @ResponseBody
    public JsonObjectBase updateItem(@PathVariable long configId, String value) {
        if(configValidator.validateRole(configId)){
            return buildSuccess("该用户操作权限不够！");
        }
        // 业务校验
        configValidator.validateUpdateItem(configId, value);

        LOG.info("start to update config: " + configId);

        //
        // 更新, 并写入数据库
        //
        String emailNotification = "";
        emailNotification = configMgr.updateItemValue(configId, value);

        //
        // 通知ZK
        //
        configMgr.notifyZookeeper(configId);

        return buildSuccess(emailNotification);
    }

    /**
     * 配置文件的更新
     *
     * @param configId
     * @param file
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/file/{configId}", method = RequestMethod.POST)
    public JsonObjectBase updateFile(@PathVariable long configId, @RequestParam("myfilerar") MultipartFile file) {
        if(configValidator.validateRole(configId)){
            return buildSuccess("该用户操作权限不够！");
        }
        //
        // 校验
        //
        int fileSize = 1024 * 1024 * 4;
        String[] allowExtName = {".properties", ".xml"};
        fileUploadValidator.validateFile(file, fileSize, allowExtName);

        // 业务校验
        configValidator.validateUpdateFile(configId, file.getOriginalFilename());

        //
        // 更新
        //
        String emailNotification = "";
        try {

            String str = new String(file.getBytes(), "UTF-8");
            LOG.info("receive file: " + str);

            emailNotification = configMgr.updateItemValue(configId, str);
            LOG.info("update " + configId + " ok");

        } catch (Exception e) {

            LOG.error(e.toString());
            throw new FileUploadException("upload file error", e);
        }

        //
        // 通知ZK
        //
        configMgr.notifyZookeeper(configId);

        return buildSuccess(emailNotification);
    }

    /**
     * 配置文件的更新(文本修改)
     *
     * @param configId
     * @param fileContent
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/filetext/{configId}", method = RequestMethod.PUT)
    public JsonObjectBase updateFileWithText(@PathVariable long configId, @NotNull String fileContent) {
        if(configValidator.validateRole(configId)){
            return buildSuccess("该用户操作权限不够！");
        }
        //
        // 更新
        //
        String emailNotification = "";
        try {

            String str = new String(fileContent.getBytes(), "UTF-8");
            LOG.info("receive file: " + str);

            emailNotification = configMgr.updateItemValue(configId, str);
            LOG.info("update " + configId + " ok");

        } catch (Exception e) {

            throw new FileUploadException("upload.file.error", e);
        }

        //
        // 通知ZK
        //
        configMgr.notifyZookeeper(configId);

        return buildSuccess(emailNotification);
    }

    /**
     * delete
     *
     * @return
     */
    @RequestMapping(value = "/{configId}", method = RequestMethod.DELETE)
    @ResponseBody
    public JsonObjectBase delete(@PathVariable long configId) {
        if(configValidator.validateRole(configId)){
            return buildSuccess("该用户操作权限不够！");
        }
        configValidator.validateDelete(configId);

        configMgr.delete(configId);

        return buildSuccess("删除成功");
    }


     /**
     * delete app
     * 
     * return 
     */
    @RequestMapping(value = "/deleteApp", method = RequestMethod.POST)
    @ResponseBody
    public JsonObjectBase deleteApp(@Valid AppDeleteForm appDeleteForm) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("envId", appDeleteForm.getEnvId());
        map.put("appId", appDeleteForm.getAppId());
        configMgr.delele(map);
        return buildSuccess("删除成功");
    }

    /**
     * delete version
     * 
     * return 
     */
    @RequestMapping(value = "/deleteVersion", method = RequestMethod.POST)
    @ResponseBody
    public JsonObjectBase deleteVersion(@Valid ConfVersionForm confVersionForm) {
        if(configValidator.validateRole(confVersionForm.getVersion())){
            return buildSuccess("该用户操作权限不够！");
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("envId", confVersionForm.getEnvId());
        map.put("appId", confVersionForm.getAppId());
        map.put("version", confVersionForm.getVersion());
        configMgr.deleleVersion(map);
        return buildSuccess("删除成功");
    }

    /**
     * delete config by versionList
     * @param confVersionForm
     * @return
     */
    @RequestMapping(value = "/deleteConfig", method = RequestMethod.POST)
    @ResponseBody
    public JsonObjectBase deleteConfig(@Valid ConfVersoinListForm confVersionForm){
        if(configValidator.validateRoleForDelete(confVersionForm.getVersion())){
            return buildSuccess("该用户操作权限不够！");
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("envId", confVersionForm.getEnvId());
        map.put("version", confVersionForm.getVersion());
        configMgr.deleteConfig(map);
        return buildSuccess("删除成功");
    }
    /**
     * copyProperties
     * 
     * return 
     */
    @RequestMapping(value = "/copyProperties", method = RequestMethod.POST)
    @ResponseBody
    public JsonObjectBase copyProperties(@Valid NameCopyForm nameCopyForm) {
        configMgr.nameCopy(nameCopyForm);
        return buildSuccess("复制成功");
    }
    
    /**
     * copyAllProperties
     * 
     * @return
     */
    @RequestMapping(value = "/copyAllProperties", method = RequestMethod.POST)
    @ResponseBody
    public JsonObjectBase copyAllProperties(@Valid NameAllCopyForm nameCopyForm) {
        configMgr.nameAllCopy(nameCopyForm);
        return buildSuccess("复制成功");
    }
    
    /**
     * get  recently  LogHistory
     * @return
     */
    @RequestMapping(value = "/getLogsTop5",method = RequestMethod.GET)
    @ResponseBody
    public  JsonObjectBase getLogHistory(){
        
        List<Log> logs  = allOpertaerMgr.getLogHistory();
        
        return buildListSuccess(logs,logs.size());
    }
    
    /**
     * 合并当前版本到主版本
     * @param mergeVersionForm
     * @return
     */
    @RequestMapping(value = "/mergeToMasterVersion",method = RequestMethod.POST)
    @ResponseBody
    public JsonObjectBase mergeToMasterVersion(@Valid MergeVersionForm mergeVersionForm){
        configMgr.mergeToMasterVersion(mergeVersionForm);
        return buildSuccess("合并成功");
    }
    
    /**
     * 从主版本复制
     * @param mergeVersionForm
     * @return
     */
    @RequestMapping(value = "/mergeFromMasterVersion",method = RequestMethod.POST)
    @ResponseBody
    public JsonObjectBase mergeFromMasterVersion(@Valid MergeVersionForm mergeVersionForm){
        configMgr.mergeFromMasterVersion(mergeVersionForm);
        return buildSuccess("合并成功");
    }

}
