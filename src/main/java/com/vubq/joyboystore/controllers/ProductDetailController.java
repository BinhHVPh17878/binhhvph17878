package com.vubq.joyboystore.controllers;

import com.vubq.joyboystore.dtos.*;
import com.vubq.joyboystore.entities.ProductDetail;
import com.vubq.joyboystore.services.ImageService;
import com.vubq.joyboystore.services.ProductDetailService;
import com.vubq.joyboystore.utils.DataTableRequest;
import com.vubq.joyboystore.utils.DataTableResponse;
import com.vubq.joyboystore.utils.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/webapi/product-detail")
public class ProductDetailController extends BaseController {

    @Autowired
    private ProductDetailService productDetailService;

    @Autowired
    private ImageService imageService;

    @GetMapping
    public DataTableResponse getAllBySearchCriteria(DataTableRequest dataTableRequest) {
        Page<ProductDetail> result = null;
        result = this.productDetailService.getAllBySearchCriteria(dataTableRequest);

        return DataTableResponse.build().ok()
                .totalRows(result.getTotalElements())
                .items(result.get().toList());
    }

    @GetMapping("/get-all-product-detail-in-of-stock")
    public Response getAllProductDetailInOfStock() {
        return Response.build().ok().data(this.productDetailService.getAllProductDetailInOfStock());
    }

    @PostMapping("/filter-product-attribute")
    public Response filterProductAttributes(@RequestBody FilterProductAttributeDTO filter) {
        List<ProductDetail> productDetails = this.productDetailService.filterProductAttributes(filter);
        List<String> listOfAvailableSizes = new ArrayList<>();
        List<String> listOfAvailableColors = new ArrayList<>();
        List<String> listOfAvailableMaterials = new ArrayList<>();
        Integer totalProductsAvailable = 0;
        for (ProductDetail productDetail : productDetails) {
            totalProductsAvailable += productDetail.getQuantity();
            if (!listOfAvailableSizes.contains(productDetail.getSize().getId())) {
                listOfAvailableSizes.add(productDetail.getSize().getId());
            }
            if (!listOfAvailableColors.contains(productDetail.getColor().getId())) {
                listOfAvailableColors.add(productDetail.getColor().getId());
            }
            if (!listOfAvailableMaterials.contains(productDetail.getMaterial().getId())) {
                listOfAvailableMaterials.add(productDetail.getMaterial().getId());
            }
        }
        Map<String, Object> productFilterList = new HashMap<>();
        productFilterList.put("listOfAvailableSizes", listOfAvailableSizes);
        productFilterList.put("listOfAvailableColors", listOfAvailableColors);
        productFilterList.put("listOfAvailableMaterials", listOfAvailableMaterials);
        productFilterList.put("totalProductsAvailable", totalProductsAvailable);
        return Response.build().ok().data(productFilterList);
    }

    @GetMapping("/get-product-detail-by-attributes")
    public Response getProductDetailByAttributes(
            @RequestParam(value = "productId") String productId,
            @RequestParam(value = "sizeId") String sizeId,
            @RequestParam(value = "colorId") String colorId,
            @RequestParam(value = "materialId") String materialId) {
        ProductDetail productDetail = this.productDetailService.getProductDetailByAttributes(
                productId,
                sizeId,
                colorId,
                materialId
        );
        if (productDetail != null) {
            return Response.build().ok().data(ProductDetailWebViewDto.builder()
                    .id(productDetail.getId())
                    .price(productDetail.getPrice())
                    .priceNet(productDetail.getPriceNet())
                    .listImage(this.imageService.getAllUrlBySecondaryId(productDetail.getId()))
                    .build());
        }
        return Response.build().ok().data(null);
    }

    @PostMapping("/get-all-product-detail-by-list-id")
    public Response getAllProductDetailByListId(@RequestBody ProductDetailSearchForm searchForm) {
        List<ProductDetail> productDetails = new ArrayList<>();
        searchForm.getListProductDetailId().forEach(productDetailId -> {
            productDetails.add(this.productDetailService.getById(productDetailId));
        });
        List<ProductDetailWebViewDto> listProductDetailWebViewDto = new ArrayList<>();
        productDetails.forEach(pd -> {
            listProductDetailWebViewDto.add(ProductDetailWebViewDto.builder()
                    .id(pd.getId())
                    .price(pd.getPrice())
                    .priceNet(pd.getPriceNet())
                    .quantity(pd.getQuantity())
                    .size(
                            SizeDto.builder()
                                    .id(pd.getSize().getId())
                                    .name(pd.getSize().getName())
                                    .build()
                    )
                    .color(
                            ColorDto.builder()
                                    .id(pd.getColor().getId())
                                    .name(pd.getColor().getName())
                                    .build()
                    )
                    .material(
                            MaterialDto.builder()
                                    .id(pd.getMaterial().getId())
                                    .name(pd.getMaterial().getName())
                                    .build()
                    )
                    .listImage(this.imageService.getAllUrlBySecondaryId(pd.getId()))
                    .product(
                            ProductWebViewDto.builder()
                                    .id(pd.getProduct().getId())
                                    .name(pd.getProduct().getName())
                                    .description(pd.getProduct().getDescription())
                                    .priceNet(pd.getProduct().getPriceNet())
                                    .price(pd.getProduct().getPrice())
                                    .category(
                                            CategoryDto.builder()
                                                    .id(pd.getProduct().getCategory().getId())
                                                    .name(pd.getProduct().getCategory().getName())
                                                    .build()
                                    )
                                    .brand(
                                            BrandDto.builder()
                                                    .id(pd.getProduct().getBrand().getId())
                                                    .name(pd.getProduct().getBrand().getName())
                                                    .build()
                                    )
                                    .listImage(this.imageService.getAllUrlBySecondaryId(pd.getProduct().getId()))
                                    .build()
                    )
                    .build());
        });
        return Response.build().ok().data(listProductDetailWebViewDto);
    }
}
