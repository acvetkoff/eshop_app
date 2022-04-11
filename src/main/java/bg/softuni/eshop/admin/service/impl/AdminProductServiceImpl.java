package bg.softuni.eshop.admin.service.impl;

import bg.softuni.eshop.BaseService;
import bg.softuni.eshop.admin.service.AdminProductService;
import bg.softuni.eshop.exceptions.ResourceNotFoundException;
import bg.softuni.eshop.order.service.OrderService;
import bg.softuni.eshop.product.dao.ProductRepository;
import bg.softuni.eshop.product.model.entity.ImageEntity;
import bg.softuni.eshop.product.model.entity.Product;
import bg.softuni.eshop.product.model.enums.ProductType;
import bg.softuni.eshop.product.model.service.ProductServiceModel;
import bg.softuni.eshop.product.service.ProductConfigurationService;
import bg.softuni.eshop.utils.parsers.ModelParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static bg.softuni.eshop.common.ExceptionMessages.PRODUCT_NOT_FOUND_MESSAGE;

@Service
public class AdminProductServiceImpl extends BaseService implements AdminProductService {

    private final ProductRepository productRepository;
    private final ProductConfigurationService productConfigurationService;
    private final OrderService orderService;


    @Autowired
    public AdminProductServiceImpl(ModelParser modelParser, ProductRepository productRepository, ProductConfigurationService productConfigurationService, OrderService orderService) {
        super(modelParser);
        this.productRepository = productRepository;
        this.productConfigurationService = productConfigurationService;
        this.orderService = orderService;
    }

    @Override
    public List<ProductServiceModel> findAll() {
        return null;
    }

    @Override
    @Transactional
    public void uploadPoster(String productId, String fileName, InputStream content) throws IOException {
        String normalizedFileName = StringUtils.cleanPath(fileName);

        ProductServiceModel productServiceModel = getById(productId);
        String posterId = UUID.randomUUID().toString();;
        String productPosterFolderPublicPath =  productConfigurationService.getConfiguration().getPosterPublicDirectory() + "/" + productId;
        String productPosterFolderPrivatePath = productConfigurationService.getConfiguration().getPosterPrivateDirectory() + "/" + productId;
        String productPosterPrivatePath = productPosterFolderPrivatePath + "/" + posterId;
        productServiceModel.setImage(new ImageEntity(productPosterPrivatePath, normalizedFileName));

        saveFile(productPosterFolderPublicPath, posterId, content);

        try {
            productRepository.save(map(productServiceModel, Product.class));
        } catch (Exception e) {
            Path publicPosterPath = Path.of(productPosterFolderPublicPath + "/" + posterId);
            if (Files.exists(publicPosterPath)) {
                Files.delete(publicPosterPath);
            }

            throw e;
        }
    }

    public void saveFile(String uploadDir, String fileName,
                         InputStream content) throws IOException {
        Path uploadPath = Paths.get(uploadDir);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        try (InputStream inputStream = content) {
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        }
    }

    @Transactional
    @Override
    public ProductServiceModel add(ProductServiceModel productServiceModel) {
        Product product = this.productRepository.save(this.map(productServiceModel, Product.class));
        return this.map(product, ProductServiceModel.class);
    }

    @Override
    public void edit(String id, ProductServiceModel productServiceModel) {
        Product productEntity = this.map(productServiceModel, Product.class);

        this.productRepository.save(productEntity);
    }

    public void patch(String id, ProductServiceModel productServiceModel) {
        Optional<Product> existingProduct = this.productRepository.findById(productServiceModel.getId());
        ProductType productType = productServiceModel.getType();

        if (!productType.equals(existingProduct.get().getType())) {
            throw new IllegalArgumentException("Types are not equal");
        }

        Product product = productConfigurationService
                .getConfiguration()
                .getProductEntities()
                .get(productType)
                .cast(existingProduct.get());

        this.map(productServiceModel, product);
        this.productRepository.save(existingProduct.get());
    }

    @Override
    public ProductServiceModel getById(String id) {
        ProductServiceModel productServiceModel = this.productRepository.findById(id)
                .map(product -> this.map(product, ProductServiceModel.class))
                .orElseThrow(() -> new ResourceNotFoundException(PRODUCT_NOT_FOUND_MESSAGE));

        return productServiceModel;
    }

    @Override
    @Transactional
    public void deleteById(String id) {
        Product product = productRepository.getById(id);

        this.orderService.deleteOrderItemByProduct(id);

        this.productRepository.delete(product);
    }
}
