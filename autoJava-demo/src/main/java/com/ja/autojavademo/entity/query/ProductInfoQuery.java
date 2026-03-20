package com.ja.autojavademo.entity.query;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Description 商品信息查询对象
 * @Author LumingJia
 * @Date 2026/03/20
 */
public class ProductInfoQuery extends BaseQuery {
	/**
	 * 自增ID
	 */
	private Integer id;

	/**
	 * 公司ID
	 */
	private String companyId;
	private String companyIdFuzzy;

	/**
	 * 商品编号
	 */
	private String code;
	private String codeFuzzy;

	/**
	 * 商品名称
	 */
	private String productName;
	private String productNameFuzzy;

	/**
	 * 价格
	 */
	private BigDecimal price;

	/**
	 * sku类型
	 */
	private Integer skuType;

	/**
	 * 颜色类型
	 */
	private Integer colorType;

	/**
	 * 创建时间
	 */
	private Date createTime;
	private String createTimeStart;
	private String createTimeEnd;

	/**
	 * 创建日期
	 */
	private Date createDate;
	private String createDateStart;
	private String createDateEnd;

	/**
	 * 库存
	 */
	private Long stock;

	/**
	 * 状态
	 */
	private Integer status;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}
	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	public Integer getSkuType() {
		return skuType;
	}

	public void setSkuType(Integer skuType) {
		this.skuType = skuType;
	}
	public Integer getColorType() {
		return colorType;
	}

	public void setColorType(Integer colorType) {
		this.colorType = colorType;
	}
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Long getStock() {
		return stock;
	}

	public void setStock(Long stock) {
		this.stock = stock;
	}
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getCompanyIdFuzzy() {
		return companyIdFuzzy;
	}

	public void setCompanyIdFuzzy(String companyIdFuzzy) {
		this.companyIdFuzzy = companyIdFuzzy;
	}
	public String getCodeFuzzy() {
		return codeFuzzy;
	}

	public void setCodeFuzzy(String codeFuzzy) {
		this.codeFuzzy = codeFuzzy;
	}
	public String getProductNameFuzzy() {
		return productNameFuzzy;
	}

	public void setProductNameFuzzy(String productNameFuzzy) {
		this.productNameFuzzy = productNameFuzzy;
	}
	public String getCreateTimeStart() {
		return createTimeStart;
	}

	public void setCreateTimeStart(String createTimeStart) {
		this.createTimeStart = createTimeStart;
	}
	public String getCreateTimeEnd() {
		return createTimeEnd;
	}

	public void setCreateTimeEnd(String createTimeEnd) {
		this.createTimeEnd = createTimeEnd;
	}
	public String getCreateDateStart() {
		return createDateStart;
	}

	public void setCreateDateStart(String createDateStart) {
		this.createDateStart = createDateStart;
	}
	public String getCreateDateEnd() {
		return createDateEnd;
	}

	public void setCreateDateEnd(String createDateEnd) {
		this.createDateEnd = createDateEnd;
	}
}