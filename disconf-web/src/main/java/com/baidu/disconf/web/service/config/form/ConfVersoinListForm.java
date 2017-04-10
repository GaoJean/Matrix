package com.baidu.disconf.web.service.config.form;

import java.util.List;

public class ConfVersoinListForm {
    
    private Long   envId;
    private List<String> versionList;
    public Long getEnvId() {
        return envId;
    }
    public void setEnvId(Long envId) {
        this.envId = envId;
    }
    public List<String> getVersionList() {
        return versionList;
    }
    public void setVersionList(List<String> versionList) {
        this.versionList = versionList;
    }
    

}
