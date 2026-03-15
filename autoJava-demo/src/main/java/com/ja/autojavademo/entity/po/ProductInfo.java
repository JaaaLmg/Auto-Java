package com.ja.autojavademo.entity.po;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import com.ja.autojavademo.utils.DateUtils;
import com.ja.autojavademo.enums.DateTimePatternEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @Description 商品信息
 * @Author LumingJia
 * @Date 2026/03/15
 */
public class ProductInfo implements Serializable {
	/**
	 *自增ID
	 */
	private Integer id;

	/**
	 *公司ID
	 */
	@JsonIgnore
	private String companyId;

	/**
	 *商品编号
	 */
	private String code;

	/**
	 *商品名称
	 */
	private String productName;

	/**
	 *价格
	 */
	private BigDecimal price;

	/**
	 *sku类型
	 */
	private Integer skuType;

	/**
	 *颜色类型
	 */
	private Integer colorType;

	/**
	 *创建时间
	 */
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date createTime;

	/**
	 *创建日期
	 */
	@JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date createDate;

	/**
	 *库存
	 */
	private Long stock;

	/**
	 *状态
	 */
	@JsonIgnore
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
	@Override
	public String toString() {
		return "商品信息 [" + "\n\t自增ID:" + (id == null ? "空" : id) + "\n\t公司ID:" + (companyId == null ? "空" : companyId) + "\n\t商品编号:" + (code == null ? "空" : code) + "\n\t商品名称:" + (productName == null ? "空" : productName) + "\n\t价格:" + (price == null ? "空" : price) + "\n\tsku类型:" + (skuType == null ? "空" : skuType) + "\n\t颜色类型:" + (colorType == null ? "空" : colorType) + "\n\t创建时间:" + (createTime == null ? "空" : DateUtils.format(createTime, DateTimePatternEnum.YYYY_MM_DD_HH_MM_SS.getPattern())) + "\n\t创建日期:" + (createDate == null ? "空" : DateUtils.format(createDate, DateTimePatternEnum.YYYY_MM_DD.getPattern())) + "\n\t库存:" + (stock == null ? "空" : stock) + "\n\t状态:" + (status == null ? "空" : status) + "\n]";
	}
}