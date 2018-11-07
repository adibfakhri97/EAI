package com.detrening.detrening;

public class adapterAPIDeTrening2 {
    public String snippetAPI;
    public String idAPI;
    public String telpAPI;
    public String websiteAPI;
    public String jadwalAPI;

    public adapterAPIDeTrening2(){

    }

    public adapterAPIDeTrening2(String snippetAPI, String idAPI, String telpAPI, String websiteAPI, String jadwalAPI){
        this.snippetAPI = snippetAPI;
        this.idAPI = idAPI;
        this.telpAPI = telpAPI;
        this.websiteAPI = websiteAPI;
        this.jadwalAPI = jadwalAPI;
    }

    public String getSnippetAPI() {
        return snippetAPI;
    }

    public String getIdAPI() {
        return idAPI;
    }

    public String getTelpAPI() {
        return telpAPI;
    }

    public String getWebsiteAPI() {
        return websiteAPI;
    }

    public String getJadwalAPI() {
        return jadwalAPI;
    }

    public void setSnippetAPI(String snippetAPI) {
        this.snippetAPI = snippetAPI;
    }

    public void setIdAPI(String idAPI) {
        this.idAPI = idAPI;
    }

    public void setTelpAPI(String telpAPI) {
        this.telpAPI = telpAPI;
    }

    public void setWebsiteAPI(String websiteAPI) {
        this.websiteAPI = websiteAPI;
    }

    public void setJadwalAPI(String jadwalAPI) {
        this.jadwalAPI = jadwalAPI;
    }
}
