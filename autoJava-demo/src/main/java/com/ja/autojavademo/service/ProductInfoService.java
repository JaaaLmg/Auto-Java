package com.ja.autojavademo.service;

import com.ja.autojavademo.entity.po.ProductInfo;
import com.ja.autojavademo.entity.query.ProductInfoQuery;
import com.ja.autojavademo.vo.PaginationResultVO;
import java.util.List;

/**
 * @Description 商品信息Service接口
 * @Author LumingJia
 * @Date 2026/03/19
 */
public interface ProductInfoService {
	/**
	 * 根据查询条件查询列表
	 */
	List<ProductInfo> queryList(ProductInfoQuery query);
	/**
	 * 根据查询条件查询数量
	 */
	Long queryCount(ProductInfoQuery query);
	/**
	 * 分页查询
	 */
	PaginationResultVO<ProductInfo> queryPage(ProductInfoQuery query);
}