package com.souche.niu.result;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * @Description：
 *
 * @remark: Created by wujingtao in 2018/9/14
 **/
public class PageResult<T> implements Serializable {

    private int page;

    private int pageSize;

    private int total;

    private List<T> item = Collections.emptyList();

    public PageResult() {
    }

    public PageResult(int page, int pageSize, int total, List<T> item) {
        this.page = page;
        this.pageSize = pageSize;
        this.total = total;
        this.item = item;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<T> getItem() {
        return item;
    }

    public void setItem(List<T> item) {
        this.item = item;
    }
}
