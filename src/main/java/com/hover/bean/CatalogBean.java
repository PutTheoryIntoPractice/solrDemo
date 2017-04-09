package com.hover.bean;

import org.apache.solr.client.solrj.beans.Field;

/**
 * Created by SZP on 2017/4/6.
 */
public class CatalogBean {
    @Field
    private String catalogid;
    @Field
    private String catalogname;
    @Field
    private String fatherid;
    @Field
    private String photo;
    @Field
    private String iyear;
    @Field
    private String iway;
    @Field
    private String onsale;

    public String getIyear() {
        return iyear;
    }

    public void setIyear(String iyear) {
        this.iyear = iyear;
    }

    public String getIway() {
        return iway;
    }

    public void setIway(String iway) {
        this.iway = iway;
    }

    public String getOnsale() {
        return onsale;
    }

    public void setOnsale(String onsale) {
        this.onsale = onsale;
    }

    public String getCatalogid() {
        return catalogid;
    }

    public void setCatalogid(String catalogid) {
        this.catalogid = catalogid;
    }

    public String getCatalogname() {
        return catalogname;
    }

    public void setCatalogname(String catalogname) {
        this.catalogname = catalogname;
    }

    public String getFatherid() {
        return fatherid;
    }

    public void setFatherid(String fatherid) {
        this.fatherid = fatherid;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
