package com.souche.niu.model.userInformation;

import com.souche.optimus.dao.annotation.SqlTable;

import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;

@SqlTable("subscribe")
public class SubscribeDo implements Serializable {

    private int id;
    private String userId;
    private String userFrom;
    private String series;
    private String seriesMap;
    private int priceLow;
    private int priceHigh;
    private int yearStart;
    private int yearEnd;
    private String emission;
    private String emissionMap;
    private String location;
    private ArrayList locationMap;
    private String title;
    private String dtUpdate;
    private Date dtCreate;
    private String seriesName;
    private String emissionName;
    private int deletedAt;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserFrom() {
        return userFrom;
    }

    public void setUserFrom(String userFrom) {
        this.userFrom = userFrom;
    }

    public String getSeries() {
        return series;
    }

    public void setSeries(String series) {
        this.series = series;
    }

    public String getSeriesMap() {
        return seriesMap;
    }

    public void setSeriesMap(String seriesMap) {
        this.seriesMap = seriesMap;
    }

    public int getPriceLow() {
        return priceLow;
    }

    public void setPriceLow(int priceLow) {
        this.priceLow = priceLow;
    }

    public int getPriceHigh() {
        return priceHigh;
    }

    public void setPriceHigh(int priceHigh) {
        this.priceHigh = priceHigh;
    }

    public int getYearStart() {
        return yearStart;
    }

    public void setYearStart(int yearStart) {
        this.yearStart = yearStart;
    }

    public int getYearEnd() {
        return yearEnd;
    }

    public void setYearEnd(int yearEnd) {
        this.yearEnd = yearEnd;
    }

    public String getEmission() {
        return emission;
    }

    public void setEmission(String emission) {
        this.emission = emission;
    }

    public String getEmissionMap() {
        return emissionMap;
    }

    public void setEmissionMap(String emissionMap) {
        this.emissionMap = emissionMap;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public ArrayList getLocationMap() {
        return locationMap;
    }

    public void setLocationMap(ArrayList locationMap) {
        this.locationMap = locationMap;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDtUpdate() {
        return dtUpdate;
    }

    public void setDtUpdate(String dtUpdate) {
        this.dtUpdate = dtUpdate;
    }

    public Date getDtCreate() {
        return dtCreate;
    }

    public void setDtCreate(Date dtCreate) {
        this.dtCreate = dtCreate;
    }

    public String getSeriesName() {
        return seriesName;
    }

    public void setSeriesName(String seriesName) {
        this.seriesName = seriesName;
    }

    public String getEmissionName() {
        return emissionName;
    }

    public void setEmissionName(String emissionName) {
        this.emissionName = emissionName;
    }

    public int getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(int deletedAt) {
        this.deletedAt = deletedAt;
    }

    @Override
    public String toString() {
        return "SubscribeDo{" +
                "id=" + id +
                ", userId='" + userId + '\'' +
                ", userFrom='" + userFrom + '\'' +
                ", series='" + series + '\'' +
                ", seriesMap='" + seriesMap + '\'' +
                ", priceLow=" + priceLow +
                ", priceHigh=" + priceHigh +
                ", yearStart=" + yearStart +
                ", yearEnd=" + yearEnd +
                ", emission='" + emission + '\'' +
                ", emissionMap='" + emissionMap + '\'' +
                ", location='" + location + '\'' +
                ", locationMap=" + locationMap +
                ", title='" + title + '\'' +
                ", dtUpdate='" + dtUpdate + '\'' +
                ", dtCreate=" + dtCreate +
                ", seriesName='" + seriesName + '\'' +
                ", emissionName='" + emissionName + '\'' +
                ", deletedAt=" + deletedAt +
                '}';
    }
}
