package com.souche.niu.dto;

/**
 * 日期工具类
 *
 * @author ZhangHui
 * @since 2018-09-13
 */
public class BaseDTO {

    private Integer currentPage;
    private Integer pageSize;
    private String selDt;
	private String rrcAgr;
	private String begDt;
    private String endDt;

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public String getSelDt() {
        return selDt;
    }

    public void setSelDt(String selDt) {
        this.selDt = selDt;
    }

    public String getRrcAgr() {
        return rrcAgr;
    }

    public void setRrcAgr(String rrcAgr) {
        this.rrcAgr = rrcAgr;
    }

    public String getBegDt() {
        return begDt;
    }

    public void setBegDt(String begDt) {
        this.begDt = begDt;
    }

    public String getEndDt() {
        return endDt;
    }

    public void setEndDt(String endDt) {
        this.endDt = endDt;
    }
}
