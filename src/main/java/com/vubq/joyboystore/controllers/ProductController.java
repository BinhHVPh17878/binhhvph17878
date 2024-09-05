package com.vubq.joyboystore.controllers;

import com.vubq.joyboystore.dtos.*;
import com.vubq.joyboystore.entities.*;
import com.vubq.joyboystore.enums.EImageType;
import com.vubq.joyboystore.enums.EStatus;
import com.vubq.joyboystore.services.*;
import com.vubq.joyboystore.utils.DataTableRequest;
import com.vubq.joyboystore.utils.DataTableResponse;
import com.vubq.joyboystore.utils.Response;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/webapi/product")
public class ProductController extends BaseController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductDetailService productDetailService;

    @Autowired
    private ImageService imageService;

    @Autowired
    private CloudinaryService cloudinaryService;

    @Autowired
    private BrandService brandService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private SizeService sizeService;

    @Autowired
    private ColorService colorService;

    @Autowired
    private MaterialService materialService;

    @Autowired
    private FeedbackService feedbackService;

    @Autowired
    private OrderDetailService orderDetailService;

    @PostMapping
    public Response createOrUpdate(@RequestBody ProductCorUDto dto) {
        List<Image> listImageNew = new ArrayList<>();
        List<Image> listImageDelete = new ArrayList<>();

        Product productNew = Product.builder()
                .name(dto.getName())
                .price(dto.getPrice())
                .priceNet(dto.getPriceNet())
                .description(dto.getDescription())
                .brand(this.brandService.getById(dto.getBrandId()))
                .category(this.categoryService.getById((dto.getCategoryId())))
                .build();
        if (StringUtils.isEmpty(dto.getId())) {
            productNew.setCreatedAt(new Date());
            productNew.setCreatedBy(this.getTheCurrentlyLoggedInUserId());
            productNew.setStatus(EStatus.ACTIVE);
        } else {
            Product productOld = this.productService.getById(dto.getId());
            productNew.setId(productOld.getId());
            productNew.setCreatedAt(productOld.getCreatedAt());
            productNew.setCreatedBy(productOld.getCreatedBy());
            productNew.setUpdatedAt(new Date());
            productNew.setUpdatedBy(this.getTheCurrentlyLoggedInUserId());
            productNew.setStatus(dto.getStatus());
        }
        Product product = this.productService.save(productNew);
        if (!dto.getListImage().isEmpty()) {
            dto.getListImage().forEach(i -> {
                if (StringUtils.isEmpty(i.getId())) {
                    Image imageNew = Image.builder()
                            .url(i.getUrl())
                            .type(EImageType.PRODUCT)
                            .publicId(i.getPublicId())
                            .secondaryId(product.getId())
                            .build();
                    listImageNew.add(imageNew);
                }
            });
        }
        if (!dto.getListImageDelete().isEmpty()) {
            dto.getListImageDelete().forEach(i -> {
                this.cloudinaryService.deleteByPublicId(i.getPublicId());
                Image imageDelete = Image.builder()
                        .id(i.getId())
                        .url(i.getUrl())
                        .type(i.getType())
                        .publicId(i.getPublicId())
                        .secondaryId(i.getSecondaryId())
                        .build();
                listImageDelete.add(imageDelete);
            });
        }

        if (!dto.getListProductDetail().isEmpty()) {
            dto.getListProductDetail().forEach(pd -> {
                ProductDetail productDetailNew = ProductDetail.builder()
                        .product(product)
                        .size(this.sizeService.getById(pd.getSizeId()))
                        .color(this.colorService.getById(pd.getColorId()))
                        .material(this.materialService.getById(pd.getMaterialId()))
                        .price(pd.getPrice())
                        .priceNet(pd.getPriceNet())
                        .quantity(pd.getQuantity())
                        .build();
                if (StringUtils.isEmpty(pd.getId())) {
                    productDetailNew.setCreatedAt(new Date());
                    productDetailNew.setCreatedBy(this.getTheCurrentlyLoggedInUserId());
                    productDetailNew.setStatus(EStatus.ACTIVE);
                } else {
                    ProductDetail productDetailOld = this.productDetailService.getById(pd.getId());
                    productDetailNew.setId(productDetailOld.getId());
                    productDetailNew.setCreatedAt(productDetailOld.getCreatedAt());
                    productDetailNew.setCreatedBy(productDetailOld.getCreatedBy());
                    productDetailNew.setUpdatedAt(new Date());
                    productDetailNew.setUpdatedBy(this.getTheCurrentlyLoggedInUserId());
                    productDetailNew.setStatus(pd.getStatus());
                }
                ProductDetail productDetail = this.productDetailService.save(productDetailNew);
                if (!pd.getListImage().isEmpty()) {
                    pd.getListImage().forEach(i -> {
                        if (StringUtils.isEmpty(i.getId())) {
                            Image imageNew = Image.builder()
                                    .url(i.getUrl())
                                    .type(EImageType.PRODUCT_DETAIL)
                                    .publicId(i.getPublicId())
                                    .secondaryId(productDetail.getId())
                                    .build();
                            listImageNew.add(imageNew);
                        }
                    });
                }
                if (!pd.getListImageDelete().isEmpty()) {
                    pd.getListImageDelete().forEach(i -> {
                        this.cloudinaryService.deleteByPublicId(i.getPublicId());
                        Image imageDelete = Image.builder()
                                .id(i.getId())
                                .url(i.getUrl())
                                .type(i.getType())
                                .publicId(i.getPublicId())
                                .secondaryId(i.getSecondaryId())
                                .build();
                        listImageDelete.add(imageDelete);
                    });
                }
            });
        }

        this.imageService.saveAll(listImageNew);
        this.imageService.deleteAll(listImageDelete);
        return Response.build().ok().data(product);
    }

    @GetMapping("{id}")
    public Response getById(@PathVariable String id) {
        Product product = this.productService.getById(id);
        if (product == null) {
            ProductCorUDto productCorUDto = new ProductCorUDto();
            productCorUDto.setListImage(new ArrayList<>());
            productCorUDto.setListImageDelete(new ArrayList<>());
            productCorUDto.setListProductDetail(new ArrayList<>());
            return Response.build().ok().data(productCorUDto);
        }
        ProductCorUDto productCorUDto = ProductCorUDto.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .brandId(product.getBrand().getId())
                .categoryId(product.getCategory().getId())
                .price(product.getPrice())
                .priceNet(product.getPriceNet())
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .createdBy(product.getCreatedBy())
                .updatedBy(product.getUpdatedBy())
                .listImage(new ArrayList<>())
                .listImageDelete(new ArrayList<>())
                .listProductDetail(new ArrayList<>())
                .status(product.getStatus())
                .build();
        this.imageService.getAllBySecondaryId(product.getId()).forEach(i -> {
            productCorUDto.addImageToList(ImageDto.builder()
                    .id(i.getId())
                    .url(i.getUrl())
                    .publicId(i.getPublicId())
                    .secondaryId(i.getSecondaryId())
                    .type(i.getType())
                    .build());
        });
        List<ProductDetail> listProductDetail = this.productDetailService.getAllByProductId(product.getId());
        listProductDetail.forEach(pd -> {
            ProductDetailCorUDto productDetailCorUDto = ProductDetailCorUDto.builder()
                    .id(pd.getId())
                    .price(pd.getPrice())
                    .priceNet(pd.getPriceNet())
                    .quantity(pd.getQuantity())
                    .productId(pd.getProduct().getId())
                    .colorId(pd.getColor().getId())
                    .sizeId(pd.getSize().getId())
                    .materialId(pd.getMaterial().getId())
                    .createdAt(pd.getCreatedAt())
                    .updatedAt(pd.getUpdatedAt())
                    .createdBy(pd.getCreatedBy())
                    .updatedBy(pd.getUpdatedBy())
                    .listImage(new ArrayList<>())
                    .listImageDelete(new ArrayList<>())
                    .status(pd.getStatus())
                    .build();
            this.imageService.getAllBySecondaryId(pd.getId()).forEach(i -> {
                productDetailCorUDto.addImageToList(ImageDto.builder()
                        .id(i.getId())
                        .url(i.getUrl())
                        .publicId(i.getPublicId())
                        .secondaryId(i.getSecondaryId())
                        .type(i.getType())
                        .build());
            });
            productCorUDto.addProductDetailToList(productDetailCorUDto);
        });
        return Response.build().ok().data(productCorUDto);
    }

    @GetMapping("get-all-page")
    public DataTableResponse getAllPage(
            DataTableRequest request,
            @RequestParam(value = "status") String status,
            @RequestParam(value = "brandId") String brandId,
            @RequestParam(value = "categoryId") String categoryId,
            @RequestParam(value = "sizeId") String sizeId,
            @RequestParam(value = "colorId") String colorId,
            @RequestParam(value = "materialId") String materialId,
            @RequestParam(value = "minPrice") Double minPrice,
            @RequestParam(value = "maxPrice") Double maxPrice
    ) {
        Page<ProductDetail> result = this.productDetailService.getAllPage(
                request,
                status,
                brandId,
                categoryId,
                sizeId,
                colorId,
                materialId,
                minPrice,
                maxPrice
        );

        List<ProductViewDto> listProductView = new ArrayList<>();
        result.getContent().forEach(pd -> {
            ProductViewDto productView = ProductViewDto.builder()
                    .id(pd.getProduct().getId())
                    .name(pd.getProduct().getName())
                    .description(pd.getProduct().getDescription())
                    .categoryName(pd.getProduct().getCategory().getName())
                    .brandName(pd.getProduct().getBrand().getName())
                    .status(pd.getProduct().getStatus())
                    .createdAt(pd.getProduct().getCreatedAt())
                    .updatedAt(pd.getProduct().getUpdatedAt())
                    .createdBy(pd.getProduct().getCreatedBy())
                    .updatedBy(pd.getProduct().getUpdatedBy())
                    .build();

            StringBuilder listColorString = new StringBuilder();
            List<Color> colors = this.colorService.getAllByIdIn(this.colorService.getAllByProductId(pd.getProduct().getId()));
            for (int i = 0; i < colors.size(); i++) {
                Color c = colors.get(i);
                if (i > 0) {
                    listColorString.append(", ");
                }
                listColorString.append(c.getName());
            }
            productView.setListColorString(listColorString.toString());

            StringBuilder listMaterialString = new StringBuilder();
            List<Material> materials = this.materialService.getAllByIdIn(this.materialService.getAllByProductId(pd.getProduct().getId()));
            for (int i = 0; i < materials.size(); i++) {
                Material m = materials.get(i);
                if (i > 0) {
                    listMaterialString.append(", ");
                }
                listMaterialString.append(m.getName());
            }
            productView.setListMaterialString(listMaterialString.toString());

            StringBuilder listSizeString = new StringBuilder();
            List<Size> sizes = this.sizeService.getAllByIdIn(this.sizeService.getAllByProductId(pd.getProduct().getId()));
            for (int i = 0; i < sizes.size(); i++) {
                Size s = sizes.get(i);
                if (i > 0) {
                    listSizeString.append(", ");
                }
                listSizeString.append(s.getName());
            }
            productView.setListSizeString(listSizeString.toString());

            listProductView.add(productView);
        });
        return DataTableResponse.build().ok()
                .totalRows(result.getTotalElements())
                .items(listProductView);
    }

    @PostMapping("update-status-in-active-by-id/{id}")
    public Response updateStatusInActiveById(@PathVariable String id) {
        Product product = this.productService.getById(id);
        product.setStatus(EStatus.IN_ACTIVE);
        this.productService.save(product);
        return Response.build().ok();
    }

    @GetMapping("get-all-by-sales-at-the-counter")
    public Response getAllBySalesAtTheCounter(@RequestParam String searchBy) {
        List<ProductDetail> listProductDetail = this.productDetailService.getAllByByStatusActiveAndQuantityGreaterThan0AndSearchBy(searchBy);
        List<ProductDetailSATCDto> listProductDetailSATC = new ArrayList<>();
        listProductDetail.forEach(pd -> {
            ProductDetailSATCDto productDetailSATC = ProductDetailSATCDto.builder()
                    .id(pd.getId())
                    .price(pd.getPrice())
                    .priceNet(pd.getPriceNet())
                    .quantity(pd.getQuantity())
                    .product(
                            ProductSATCDto.builder()
                                    .id(pd.getProduct().getId())
                                    .name(pd.getProduct().getName())
                                    .price(pd.getProduct().getPrice())
                                    .priceNet(pd.getPriceNet())
                                    .description(pd.getProduct().getDescription())
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
                    .listImage(this.imageService.getAllUrlBySecondaryId(pd.getProduct().getId()))
                    .build();
            listProductDetailSATC.add(productDetailSATC);
        });
        return Response.build().ok().data(listProductDetailSATC);
    }

    @GetMapping("get-top-5-product-create-at-desc")
    public Response getTop5ProductCreatedAtDESC() {
        List<Product> listProduct = this.productService.getTop5ProductCreatedAtDESC();
        List<ProductWebViewDto> listProductWebViewDto = new ArrayList<>();
        listProduct.forEach(p -> {
            ProductWebViewDto productWebViewDto = ProductWebViewDto.builder()
                    .id(p.getId())
                    .name(p.getName())
                    .description(p.getDescription())
                    .price(p.getPrice())
                    .priceNet(p.getPriceNet())
                    .category(
                            CategoryDto.builder()
                                    .id(p.getCategory().getId())
                                    .name(p.getCategory().getName())
                                    .build()
                    )
                    .brand(
                            BrandDto.builder()
                                    .id(p.getBrand().getId())
                                    .name(p.getBrand().getName())
                                    .build()
                    )
                    .rate(this.feedbackService.getRateProduct(p.getId()))
                    .listImage(this.imageService.getAllUrlBySecondaryId(p.getId()))
                    .build();
            List<ProductDetail> listProductDetail = this.productDetailService.getAllByProductId(p.getId());
            List<ProductDetailWebViewDto> listProductDetailWebViewDto = new ArrayList<>();
            listProductDetail.forEach(pd -> {
                ProductDetailWebViewDto productDetailWebViewDto = ProductDetailWebViewDto.builder()
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
                        .build();
                listProductDetailWebViewDto.add(productDetailWebViewDto);
            });
            productWebViewDto.setListProductDetail(listProductDetailWebViewDto);
            listProductWebViewDto.add(productWebViewDto);
        });
        return Response.build().ok().data(listProductWebViewDto);
    }

    @GetMapping("get-all-product-detail-view/{id}")
    public Response getProductViewById(@PathVariable String id) {
        Product product = this.productService.getById(id);
        ProductWebViewDto productWebViewDto = ProductWebViewDto.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .priceNet(product.getPriceNet())
                .category(
                        CategoryDto.builder()
                                .id(product.getCategory().getId())
                                .name(product.getCategory().getName())
                                .build()
                )
                .brand(
                        BrandDto.builder()
                                .id(product.getBrand().getId())
                                .name(product.getBrand().getName())
                                .build()
                )
                .rate(this.feedbackService.getRateProduct(product.getId()))
                .listImage(this.imageService.getAllUrlBySecondaryId(product.getId()))
                .build();
        List<ProductDetail> listProductDetail = this.productDetailService.getAllProductDetailView(id);
        List<ProductDetailWebViewDto> listProductDetailWebViewDto = new ArrayList<>();
        listProductDetail.forEach(pd -> {
            ProductDetailWebViewDto productDetailWebViewDto = ProductDetailWebViewDto.builder()
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
                    .build();
            listProductDetailWebViewDto.add(productDetailWebViewDto);
        });
        productWebViewDto.setListProductDetail(listProductDetailWebViewDto);
        return Response.build().ok().data(productWebViewDto);
    }

    @GetMapping("get-product-by-id-web-shop/{id}")
    public Response getProductByIdWebShop(@PathVariable(value = "id") String id) {
        Product product = this.productService.getById(id);
        List<String> listSizeId = this.sizeService.getAllByProductId(product.getId());
        List<SizeDto> sizes = new ArrayList<>();
        if(listSizeId.size() > 0) {
            listSizeId.forEach(sizeId -> {
                SizeDto size = SizeDto.toDto(this.sizeService.getById(sizeId));
                size.setIsOutOfStock(this.sizeService.getProductInStock(product.getId(), size.getId()).size() > 0 ? false : true);
                sizes.add(size);
            });
        }
        List<String> listColorId = this.colorService.getAllByProductId(product.getId());
        List<ColorDto> colors = new ArrayList<>();
        if(listColorId.size() > 0) {
            listColorId.forEach(colorId -> {
                ColorDto color = ColorDto.toDto(this.colorService.getById(colorId));
                color.setIsOutOfStock(this.colorService.getProductInStock(product.getId(), color.getId()).size() > 0 ? false : true);
                colors.add(color);
            });
        }
        List<String> listMaterialId = this.materialService.getAllByProductId(product.getId());
        List<MaterialDto> materials = new ArrayList<>();
        if(listMaterialId.size() > 0) {
            listMaterialId.forEach(materialId -> {
                MaterialDto material = MaterialDto.toDto(this.materialService.getById(materialId));
                material.setIsOutOfStock(this.materialService.getProductInStock(product.getId(), material.getId()).size() > 0 ? false : true);
                materials.add(material);
            });
        }
        FilterProductAttributeDTO filter = FilterProductAttributeDTO.builder().productId(product.getId()).build();
        List<ProductDetail> productDetails = this.productDetailService.filterProductAttributes(filter);
        Integer totalProductsAvailable = 0;
        for (ProductDetail productDetail : productDetails) {
            totalProductsAvailable += productDetail.getQuantity();
        }
        return Response.build().ok().data(ProductWebShopDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .priceNet(product.getPriceNet())
                .brand(product.getBrand())
                .category(product.getCategory())
                .createdAt(product.getCreatedAt())
                .rate(this.feedbackService.getRateProduct(product.getId()))
                .quantitySold(this.orderDetailService.getQuantitySold(product.getId()))
                .status(product.getStatus())
                .quantityOfProductAvailable(this.productDetailService.getQuantityOfProductAvailable(product.getId()))
                .listImage(this.imageService.getAllUrlBySecondaryId(product.getId()))
                .colors(colors)
                .sizes(sizes)
                .materials(materials)
                .totalProductsAvailable(totalProductsAvailable)
                .build());
    }
}
