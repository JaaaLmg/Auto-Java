package com.ja.autojavademo.service.impl;

import com.ja.autojavademo.mappers.ProductInfoMapper;
import com.ja.autojavademo.service.ProductInfoService;
import com.ja.autojavademo.entity.po.ProductInfo;
import com.ja.autojavademo.entity.query.ProductInfoQuery;
import com.ja.autojavademo.entity.query.SimplePage;
import com.ja.autojavademo.entity.enums.PageSize;
import com.ja.autojavademo.entity.vo.PaginationResultVO;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;

/**
 * @Description 商品信息业务功能实现类
 * @Author LumingJia
 * @Date 2026/03/20
 */
@Service("ProductInfoService")
public class ProductInfoServiceImpl implements ProductInfoService {
	@Resource
	private ProductInfoMapper<ProductInfo,ProductInfoQuery> productInfoMapper;

	/**
	 * 根据查询条件查询列表
	 */
	@Override
	public List<ProductInfo> queryList(ProductInfoQuery query) {
		 return this.productInfoMapper.selectList(query);
	}

	/**
	 * 根据查询条件查询数量
	 */
	@Override
	public Integer queryCount(ProductInfoQuery query) {
		 return this.productInfoMapper.selectCount(query);
	}

	/**
	 * 分页查询
	 */
	@Override
	public PaginationResultVO<ProductInfo> queryPage(ProductInfoQuery query) {
		Integer count = this.queryCount(query);
		Integer pageSize = query.getPageSize()==null?PageSize.SIZE20.getSize():query.getPageSize();
		SimplePage page = new SimplePage(query.getPageNo(), count, pageSize);
		query.setSimplePage(page);
		List<ProductInfo> list = this.queryList(query);
		PaginationResultVO<ProductInfo> result = new PaginationResultVO<ProductInfo>(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
		return result;
	}

	/**
	 * 新增
	 */
	@Override
	public Integer add(ProductInfo bean) {
		return this.productInfoMapper.insert(bean);
	}

	/**
	 * 批量新增
	 */
	@Override
	public Integer addBatch(List<ProductInfo> listBean) {
		if (listBean == null || listBean.isEmpty()) {
			return 0;
		}
		return this.productInfoMapper.insertBatch(listBean);
	}

	/**
	 * 批量新增或修改
	 */
	@Override
	public Integer addOrUpdateBatch(List<ProductInfo> listBean) {
		if (listBean == null || listBean.isEmpty()) {
			return 0;
		}
		return this.productInfoMapper.insertOrUpdateBatch(listBean);
	}

	/**
	 * 根据Id查询
	 */
	@Override
	public ProductInfo getProductInfoById(Integer id) {
		return this.productInfoMapper.selectById(id);
	}

	/**
	 * 根据Id更新
	 */
	@Override
	public Integer updateProductInfoById(ProductInfo bean, Integer id) {
		return this.productInfoMapper.updateById(bean, id);
	}

	/**
	 * 根据Id删除
	 */
	@Override
	public Integer deleteProductInfoById(Integer id) {
		return this.productInfoMapper.deleteById(id);
	}

	/**
	 * 根据Code查询
	 */
	@Override
	public ProductInfo getProductInfoByCode(String code) {
		return this.productInfoMapper.selectByCode(code);
	}

	/**
	 * 根据Code更新
	 */
	@Override
	public Integer updateProductInfoByCode(ProductInfo bean, String code) {
		return this.productInfoMapper.updateByCode(bean, code);
	}

	/**
	 * 根据Code删除
	 */
	@Override
	public Integer deleteProductInfoByCode(String code) {
		return this.productInfoMapper.deleteByCode(code);
	}

	/**
	 * 根据SkuType和ColorType查询
	 */
	@Override
	public ProductInfo getProductInfoBySkuTypeAndColorType(Integer skuType, Integer colorType) {
		return this.productInfoMapper.selectBySkuTypeAndColorType(skuType, colorType);
	}

	/**
	 * 根据SkuType和ColorType更新
	 */
	@Override
	public Integer updateProductInfoBySkuTypeAndColorType(ProductInfo bean, Integer skuType, Integer colorType) {
		return this.productInfoMapper.updateBySkuTypeAndColorType(bean, skuType, colorType);
	}

	/**
	 * 根据SkuType和ColorType删除
	 */
	@Override
	public Integer deleteProductInfoBySkuTypeAndColorType(Integer skuType, Integer colorType) {
		return this.productInfoMapper.deleteBySkuTypeAndColorType(skuType, colorType);
	}

}