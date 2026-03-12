package com.autojava.bean;

/**
 * 字段信息
 */
public class FieldInfo {
    /**
     * 字段名称
     */
    private String fieldName;

    /**
     * 字段类型(java)
     */
    private String javaType;

    /**
     * 字段类型(sql)
     */
    private String sqlType;

    /**
     * bean属性名称
     */
    private String propertyName;

    /**
     * 字段注释
     */
    private String comment;

    /**
     * 字段是否自增长
     */
    private Boolean isAutoIncrement;

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String filedName) {
        this.fieldName = filedName;
    }

    public String getJavaType() {
        return javaType;
    }

    public void setJavaType(String javaType) {
        this.javaType = javaType;
    }

    public String getSqlType() {
        return sqlType;
    }

    public void setSqlType(String sqlType) {
        this.sqlType = sqlType;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Boolean getAutoIncrement() {
        return isAutoIncrement;
    }

    public void setAutoIncrement(Boolean autoIncrement) {
        isAutoIncrement = autoIncrement;
    }

}
