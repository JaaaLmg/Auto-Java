package com.ja.autojavademo.entity.query;

public class BaseQuery {
    private SimplePage simplePage;
    private Integer pageNO;
    private Integer pageSize;
    private String orderBy;

    public SimplePage getSimplePage() {
        return simplePage;
    }

    public void setSimplePage(SimplePage simplePage) {
        this.simplePage = simplePage;
    }

    public Integer getPageNO() {
        return pageNO;
    }

    public void setPageNO(Integer pageNO) {
        this.pageNO = pageNO;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

}
