package com.detrening.detrening;

import android.content.Context;

public class adapterAPIDeTrening {
    public String user;
    public String progress;
    public String berat;
    public String nama;
    public String tinggi;
    public String imgURL;
    /*public String snippetAPI;
    public String idAPI;
    public String telpAPI;
    public String websiteAPI;*/

    public adapterAPIDeTrening() {

    }

    public adapterAPIDeTrening(String user, String progress, String berat, String nama, String tinggi, String imgURL) {
        this.user = user;
        this.progress = progress;
        this.berat = berat;
        this.nama = nama;
        this.tinggi = tinggi;
        this.imgURL = imgURL;
        /*this.snippetAPI = snippetAPI;
        this.idAPI = idAPI;
        this.telpAPI = telpAPI;
        this.websiteAPI = websiteAPI;*/
    }

    public String getUser() {
        return user;
    }

    public String getProgress() {
        return progress;
    }

    public String getBerat() {
        return berat;
    }

    public String getNama() {
        return nama;
    }

    public String getTinggi() {
        return tinggi;
    }

    public String getImgURL() {
        return imgURL;
    }

    /*public String getSnippetAPI() {
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
    }*/

    public void setUser(String user) {
        this.user = user;
    }

    public void setProgress(String progress) {
        this.progress = progress;
    }

    public void setBerat(String berat) {
        this.berat = berat;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public void setTinggi(String tinggi) {
        this.tinggi = tinggi;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }

    /*public void setSnippetAPI(String snippetAPI) {
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
    }*/
}
