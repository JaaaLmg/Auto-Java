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
	Integer queryCount(ProductInfoQuery query);

	/**
	 * 分页查询
	 */
	PaginationResultVO<ProductInfo> queryPage(ProductInfoQuery query);

	/**
	 * 新增
	 */
	Integer add(ProductInfo bean);

	/**
	 * 批量新增
	 */
	Integer addBatch(List<ProductInfo> listBean);

	/**
	 * 批量新增或修改
	 */
	Integer addOrUpdateBatch(List<ProductInfo> listBean);

	/**
	 * 根据Id查询
	 */
	ProductInfo getProductInfoById(Integer id);


	/**
	 * 根据Id更新
	 */
	Integer updateProductInfoById(ProductInfo bean, Integer id);


	/**
	 * 根据Id删除
	 */
	Integer deleteProductInfoById(Integer id);


	/**
	 * 根据Code查询
	 */
	ProductInfo getProductInfoByCode(String code);


	/**
	 * 根据Code更新
	 */
	Integer updateProductInfoByCode(ProductInfo bean, String code);


	/**
	 * 根据Code删除
	 */
	Integer deleteProductInfoByCode(String code);


	/**
	 * 根据SkuType和ColorType查询
	 */
	ProductInfo getProductInfoBySkuTypeAndColorType(Integer skuType, Integer colorType);


	/**
	 * 根据SkuType和ColorType更新
	 */
	Integer updateProductInfoBySkuTypeAndColorType(ProductInfo bean, Integer skuType, Integer colorType);


	/**
	 * 根据SkuType和ColorType删除
	 */
	Integer deleteProductInfoBySkuTypeAndColorType(Integer skuType, Integer colorType);


}