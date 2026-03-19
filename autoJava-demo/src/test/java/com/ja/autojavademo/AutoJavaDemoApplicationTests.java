package com.ja.autojavademo;

import com.ja.autojavademo.entity.po.ProductInfo;
import com.ja.autojavademo.entity.query.ProductInfoQuery;
import com.ja.autojavademo.mappers.ProductInfoMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class AutoJavaDemoApplicationTests {

    @Resource
    private ProductInfoMapper<ProductInfo, ProductInfoQuery> productInfoMapper;

    @Test
    public void selectList() {
        ProductInfoQuery demoQuery = new ProductInfoQuery();
        demoQuery.setCreateDateStart("2026-03-18");
        demoQuery.setProductNameFuzzy("hua");

        List<ProductInfo> dataList = productInfoMapper.selectList(demoQuery);

        for(ProductInfo data : dataList){
            System.out.println(data);
        }

        Integer count = productInfoMapper.selectCount(demoQuery);
        System.out.println(count);
    }

    @Test
    public void insert() {
        ProductInfo demoProduct = new ProductInfo();
        demoProduct.setCompanyId("0002");
        demoProduct.setCode("005");
        demoProduct.setProductName("vivo");
        demoProduct.setPrice(new BigDecimal(6999));
        demoProduct.setSkuType(1);
        demoProduct.setColorType(4);
        demoProduct.setCreateTime(new java.util.Date());
        demoProduct.setCreateDate(new java.util.Date());
        demoProduct.setStock(1000L);
        demoProduct.setStatus(1);
        Integer result = productInfoMapper.insert(demoProduct);
        System.out.println(result);
        System.out.println(demoProduct.getId());
    }

    @Test
    public void insertOrUpdate() {
        ProductInfo demoProduct = new ProductInfo();
        demoProduct.setCompanyId("0002");
        demoProduct.setCode("006");
        demoProduct.setProductName("oppo");
        demoProduct.setSkuType(1);
        demoProduct.setColorType(5);
        demoProduct.setPrice(new BigDecimal(4999));
        demoProduct.setCreateTime(new java.util.Date());
        demoProduct.setCreateDate(new java.util.Date());
        demoProduct.setStock(1000L);
        demoProduct.setStatus(1);
        productInfoMapper.insertOrUpdate(demoProduct);
        System.out.println(demoProduct.getId());
    }

    @Test
    public void insertBatch() {
        List<ProductInfo> demoProductList = new ArrayList<>();

        ProductInfo demoProduct = new ProductInfo();
        demoProduct.setCode("101");
        demoProduct.setCreateDate(new java.util.Date());
        demoProductList.add(demoProduct);

        ProductInfo demoProduct2 = new ProductInfo();
        demoProduct2.setCode("102");
        demoProduct2.setCreateDate(new java.util.Date());
        demoProductList.add(demoProduct2);

        productInfoMapper.insertBatch(demoProductList);
    }

    @Test
    public void insertOrUpdateBatch() {
        List<ProductInfo> demoProductList = new ArrayList<>();

        ProductInfo demoProduct = new ProductInfo();
        demoProduct.setCode("102");
        demoProduct.setCreateDate(new java.util.Date());
        demoProduct.setCreateTime(new java.util.Date());
        demoProductList.add(demoProduct);

        ProductInfo demoProduct2 = new ProductInfo();
        demoProduct2.setCode("104");
        demoProduct2.setCreateDate(new java.util.Date());
        demoProductList.add(demoProduct2);

        productInfoMapper.insertOrUpdateBatch(demoProductList);
    }

    @Test
    public void selectByKey(){
        ProductInfo demoProduct = productInfoMapper.selectById(3);
        ProductInfo demoProduct2 = productInfoMapper.selectByCode("102");
        ProductInfo demoProduct3 = productInfoMapper.selectBySkuTypeAndColorType(1, 5);

        System.out.println(demoProduct);
        System.out.println(demoProduct2);
        System.out.println(demoProduct3);
    }

    @Test
    public void updateByKey(){
        ProductInfo demoProduct = new ProductInfo();
        demoProduct.setPrice(new BigDecimal(10999));
        productInfoMapper.updateById(demoProduct, 13);
        productInfoMapper.updateByCode(demoProduct, "102");
        productInfoMapper.updateBySkuTypeAndColorType(demoProduct, 1, 5);
    }

    @Test
    public void deleteByKey(){
        productInfoMapper.deleteById(13);
        productInfoMapper.deleteByCode("102");
        productInfoMapper.deleteBySkuTypeAndColorType(1, 5);
    }

}
