package com.ja.autojavademo.mappers;

import org.apache.ibatis.annotations.Param;

/**
 * @Description 商品信息Mapper
 * @Author LumingJia
 * @Date 2026/03/19
 */
public interface ProductInfoMapper<T, P> extends BaseMapper {
	/**
	 * 根据Id查询
	 */
	T selectById(@Param("id") Integer id);

	/**
	 * 根据Id更新
	 */
	Long updateById(@Param("bean") T t, @Param("id") Integer id);

	/**
	 * 根据Id删除
	 */
	Long deleteById(@Param("id") Integer id);

	/**
	 * 根据Code查询
	 */
	T selectByCode(@Param("code") String code);

	/**
	 * 根据Code更新
	 */
	Long updateByCode(@Param("bean") T t, @Param("code") String code);

	/**
	 * 根据Code删除
	 */
	Long deleteByCode(@Param("code") String code);

	/**
	 * 根据SkuType和ColorType查询
	 */
	T selectBySkuTypeAndColorType(@Param("skuType") Integer skuType, @Param("colorType") Integer colorType);

	/**
	 * 根据SkuType和ColorType更新
	 */
	Long updateBySkuTypeAndColorType(@Param("bean") T t, @Param("skuType") Integer skuType, @Param("colorType") Integer colorType);

	/**
	 * 根据SkuType和ColorType删除
	 */
	Long deleteBySkuTypeAndColorType(@Param("skuType") Integer skuType, @Param("colorType") Integer colorType);

}