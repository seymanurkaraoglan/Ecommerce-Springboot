package com.ecommerce.library.service.impl;

import com.ecommerce.library.dto.ProductDto;
import com.ecommerce.library.model.Product;
import com.ecommerce.library.repository.ProductRepository;
import com.ecommerce.library.service.ProductService;
import com.ecommerce.library.utils.ImageUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.data.domain.Pageable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ImageUpload imageUpload;
    @Autowired
    private ProductRepository productRepository;
    /*ADMIN*/
    @Override
    public List<ProductDto> findAll() {
        List<Product> products = productRepository.findAll();
        List<ProductDto> productDtoList = transfer(products);
        return productDtoList;
    }

    @Override
    public Product save(MultipartFile imageProduct,ProductDto productDto){
        try{
            Product product = new Product();
            if(imageProduct == null){
                product.setImage(null);
            }else{
                if(imageUpload.uploadImage(imageProduct)){
                    System.out.println("Upload successfully!");
                }
                product.setImage(Base64.getEncoder().encodeToString(imageProduct.getBytes()));
            }
            product.setName(productDto.getName());
            product.setDescription(productDto.getDescription());
            product.setSalePrice(productDto.getSalePrice());
            product.setCategory(productDto.getCategory());
            product.setCostPrice(productDto.getCostPrice());
            product.setCurrentQuantity(productDto.getCurrentQuantity());
            product.set_activated(true);
            product.set_deleted(false);
            return  productRepository.save(product);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public Product update(MultipartFile imageProduct,ProductDto productDto) {
        try{
            Product product = productRepository.getById(productDto.getId());
            if(imageProduct == null){
                product.setImage(product.getImage());
            }else{
                if(imageUpload.checkExisted(imageProduct) == false){
                    imageUpload.uploadImage(imageProduct);
                }
                System.out.println("Image existed");
                product.setImage(Base64.getEncoder().encodeToString(imageProduct.getBytes()));
            }
            product.setName(productDto.getName());
            product.setDescription(productDto.getDescription());
            product.setSalePrice(productDto.getSalePrice());
            product.setCostPrice(productDto.getCostPrice());
            product.setCurrentQuantity(productDto.getCurrentQuantity());
            product.setCategory(productDto.getCategory());
            return productRepository.save(product);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public void deleteById(Long id) {
        Product product = productRepository.getById(id);
        product.set_deleted(true);
        product.set_activated(false);
        productRepository.save(product);

    }

    @Override
    public void enableById(Long id) {
    Product product = productRepository.getById(id);
    product.set_deleted(false);
    product.set_activated(true);
    productRepository.save(product);
    }

    @Override
    public ProductDto getById(Long id) {
        Product product = productRepository.getById(id);
        ProductDto productDto = new ProductDto();
        productDto.setId(product.getId());
        productDto.setName(product.getName());
        productDto.setDescription(product.getDescription());
        productDto.setCurrentQuantity(product.getCurrentQuantity());
        productDto.setCategory(product.getCategory());
        productDto.setSalePrice(product.getSalePrice());
        productDto.setCostPrice(product.getCostPrice());
        productDto.setImage(product.getImage());
        productDto.set_deleted(product.is_deleted());
        productDto.set_activated(product.is_activated());
        return productDto;
    }

    @Override
    public Page<ProductDto> pageProducts(int pageNo) {
        Pageable pageable = PageRequest.of(pageNo,5);
        List<ProductDto> products = transfer(productRepository.findAll());
        Page<ProductDto> productPages = toPage(products,pageable);
        return productPages;
    }

    @Override
    public Page<ProductDto> searchProducts(int pageNo,String keyword) {
        Pageable pageable = PageRequest.of(pageNo,5);
        List<ProductDto> productDtoList = transfer(productRepository.searchProductsList(keyword));
        Page<ProductDto> products = toPage(productDtoList,pageable);
        return products;
    }

    private Page toPage(List<ProductDto> list,Pageable pageable){
        if(pageable.getOffset() >= list.size()){
            return  Page.empty();
        }
        int startIndex = (int) pageable.getOffset();
        int endIndex = ((pageable.getOffset() + pageable.getPageSize()) > list.size())
                ? list.size()
                : (int) (pageable.getOffset() + pageable.getPageSize());
        List subList = list.subList(startIndex,endIndex);
        return new PageImpl(subList,pageable,list.size());
    }

    private List<ProductDto> transfer(List<Product> products){
        List<ProductDto> productDtoList = new ArrayList<>();
        for(Product product : products){
            ProductDto productDto = new ProductDto();
            productDto.setId(product.getId());
            productDto.setName(product.getName());
            productDto.setDescription(product.getDescription());
            productDto.setCurrentQuantity(product.getCurrentQuantity());
            productDto.setCategory(product.getCategory());
            productDto.setSalePrice(product.getSalePrice());
            productDto.setCostPrice(product.getCostPrice());
            productDto.setImage(product.getImage());
            productDto.set_deleted(product.is_deleted());
            productDto.set_activated(product.is_activated());
            productDtoList.add(productDto);
        }
        return productDtoList;
    }

/*CUSTOMER */

    @Override
    public List<Product> getAllProducts() {
        return productRepository.getAllProducts();
    }

    @Override
    public List<Product> listViewProducts() {
        return productRepository.listViewProducts();
    }

    @Override
    public Product getProductById(Long id) {
        return productRepository.getById(id);
    }

    @Override
    public List<Product> getRelatedProducts(Long categoryId) {
        return productRepository.getRelatedProducts(categoryId);
    }

    @Override
    public List<Product> getProductsInCategory(Long categoryId) {
        return productRepository.getProductsInCategory(categoryId);
    }

    @Override
    public List<Product> filterHighPrice() {
        return productRepository.filterHighPrice();
    }

    @Override
    public List<Product> filterLowPrice() {
        return productRepository.filterLowPrice();
    }







































}
