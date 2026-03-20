package com.ja.autojavademo.controller;

import com.ja.autojavademo.mappers.ProductInfoMapper;
import com.ja.autojavademo.service.ProductInfoService;
import com.ja.autojavademo.entity.po.ProductInfo;
import com.ja.autojavademo.entity.query.ProductInfoQuery;
import com.ja.autojavademo.entity.query.SimplePage;
import com.ja.autojavademo.entity.enums.PageSize;
import com.ja.autojavademo.entity.vo.PaginationResultVO;
import com.ja.autojavademo.entity.vo.ResponseVO;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import javax.annotation.Resource;
import java.util.List;

/**
 * @Description 商品信息Controller类
 * @Author LumingJia
 * @Date 2026/03/20
 */
@RestController
@RequestMapping("productInfo")
public class ProductInfoController extends BaseController {
	@Resource
	private ProductInfoService productInfoService;

	/**
	 * 根据查询条件分页查询
	 */
	@RequestMapping("loadDataList")
	public ResponseVO loadDataList(ProductInfoQuery query) {
		 return getSuccessResponseVO(productInfoService.queryPage(query));
	}

	/**
	 * 新增
	 */
	@RequestMapping("add")
	public ResponseVO add(ProductInfo bean) {
		productInfoService.add(bean);
		return getSuccessResponseVO(null);
	}

	/**
	 * 批量新增
	 */
	@RequestMapping("addBatch")
	public ResponseVO addBatch(@RequestBody List<ProductInfo> listBean) {
		productInfoService.addBatch(listBean);
		return getSuccessResponseVO(null);
	}

	/**
	 * 批量新增或修改
	 */
	@RequestMapping("addOrUpdateBatch")
	public ResponseVO addOrUpdateBatch(@RequestBody List<ProductInfo> listBean) {
		productInfoService.addOrUpdateBatch(listBean);
		return getSuccessResponseVO(null);
	}

	/**
	 * 根据Id查询
	 */
	@RequestMapping("getProductInfoById")
	public ResponseVO getProductInfoById(Integer id) {
		return getSuccessResponseVO(productInfoService.getProductInfoById(id));
	}


	/**
	 * 根据Id更新
	 */
	@RequestMapping("updateProductInfoById")
	public ResponseVO updateProductInfoById(ProductInfo bean, Integer id) {
		productInfoService.updateProductInfoById(bean, id);
		return getSuccessResponseVO(null);
	}


	/**
	 * 根据Id删除
	 */
	@RequestMapping("deleteProductInfoById")
	public ResponseVO deleteProductInfoById(Integer id) {
		productInfoService.deleteProductInfoById(id);
		return getSuccessResponseVO(null);
	}


	/**
	 * 根据Code查询
	 */
	@RequestMapping("getProductInfoByCode")
	public ResponseVO getProductInfoByCode(String code) {
		return getSuccessResponseVO(productInfoService.getProductInfoByCode(code));
	}


	/**
	 * 根据Code更新
	 */
	@RequestMapping("updateProductInfoByCode")
	public ResponseVO updateProductInfoByCode(ProductInfo bean, String code) {
		productInfoService.updateProductInfoByCode(bean, code);
		return getSuccessResponseVO(null);
	}


	/**
	 * 根据Code删除
	 */
	@RequestMapping("deleteProductInfoByCode")
	public ResponseVO deleteProductInfoByCode(String code) {
		productInfoService.deleteProductInfoByCode(code);
		return getSuccessResponseVO(null);
	}


	/**
	 * 根据SkuType和ColorType查询
	 */
	@RequestMapping("getProductInfoBySkuTypeAndColorType")
	public ResponseVO getProductInfoBySkuTypeAndColorType(Integer skuType, Integer colorType) {
		return getSuccessResponseVO(productInfoService.getProductInfoBySkuTypeAndColorType(skuType, colorType));
	}


	/**
	 * 根据SkuType和ColorType更新
	 */
	@RequestMapping("updateProductInfoBySkuTypeAndColorType")
	public ResponseVO updateProductInfoBySkuTypeAndColorType(ProductInfo bean, Integer skuType, Integer colorType) {
		productInfoService.updateProductInfoBySkuTypeAndColorType(bean, skuType, colorType);
		return getSuccessResponseVO(null);
	}


	/**
	 * 根据SkuType和ColorType删除
	 */
	@RequestMapping("deleteProductInfoBySkuTypeAndColorType")
	public ResponseVO deleteProductInfoBySkuTypeAndColorType(Integer skuType, Integer colorType) {
		productInfoService.deleteProductInfoBySkuTypeAndColorType(skuType, colorType);
		return getSuccessResponseVO(null);
	}


}