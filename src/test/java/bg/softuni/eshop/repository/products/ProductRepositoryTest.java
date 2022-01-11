package bg.softuni.eshop.repository.products;

import bg.softuni.eshop.product.dao.ProductRepository;
import bg.softuni.eshop.product.model.entity.Product;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import static bg.softuni.eshop.product.model.enums.ProductType.*;
import static bg.softuni.eshop.utils.providers.ProductSpecificationProvider.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class ProductRepositoryTest {

	private static final String ASSASSINS_CREED = "Assassin's Creed";
    private static final BigDecimal ASSASSINS_CREED_PRICE = new BigDecimal("60.00");
    private static final String ASSASSINS_CREED_DESCRIPTION = "Action/adventure game exploring different ages of human history.";

    private static final String DESPERATION = "Desperation";
    private static final BigDecimal DESPERATION_PRICE = new BigDecimal("22.00");
    private static final String DESPERATION_DESCRIPTION = "King brings us true horror in the little town of Desperation.";

    private static final String IT = "It";
    private static final BigDecimal IT_PRICE = new BigDecimal("22.00");
    private static final String IT_DESCRIPTION = "Horror book about a creature that eats children.";

    private static final String TRANSFORMERS = "Transformers";
    private static final BigDecimal TRANSFORMERS_PRICE = new BigDecimal("10.00");
    private static final String TRANSFORMERS_DESCRIPTION = "Sci-fi blockbuster movie for alien robots.";

    private static final int THREE = 3;
    private static final int TWO = 2;
    private static final int ZERO = 0;
    private static final int ONE = 1;
    private static final BigDecimal INVALID_PRICE = new BigDecimal("-22.00");
    private static final BigDecimal DECIMAL_ZERO = new BigDecimal("0");
    private static final String VALID_PARTIAL = "r";
    private static final String INVALID_TITLE = "00";
    private static final String BOOK_VALID_PARTIAL_DESC = "Desperation";

    @Autowired
    private ProductRepository productRepository;

    private Product assasinsCreed, desperation, it, transformers;

    @BeforeEach
    void setUp() {
        // Given
        this.assasinsCreed = new Product();
        this.assasinsCreed.setTitle(ASSASSINS_CREED);
        this.assasinsCreed.setType(GAME);
        this.assasinsCreed.setPrice(ASSASSINS_CREED_PRICE);
        this.assasinsCreed.setDescription(ASSASSINS_CREED_DESCRIPTION);

        this.desperation = new Product();
        this.desperation.setTitle(DESPERATION);
        this.desperation.setType(BOOK);
        this.desperation.setPrice(DESPERATION_PRICE);
        this.desperation.setDescription(DESPERATION_DESCRIPTION);

        this.it = new Product();
        this.it.setTitle(IT);
        this.it.setType(BOOK);
        this.it.setPrice(IT_PRICE);
        this.it.setDescription(IT_DESCRIPTION);

        this.transformers = new Product();
        this.transformers.setTitle(TRANSFORMERS);
        this.transformers.setType(MOVIE);
        this.transformers.setPrice(TRANSFORMERS_PRICE);
        this.transformers.setDescription(TRANSFORMERS_DESCRIPTION);

        this.productRepository.saveAll(List.of(assasinsCreed, desperation, it, transformers));
    }

    // Tests for findAllOrderedByTitle() method
    @Test
    void findAllOrderedByTitle_givenProducts_ShouldOrderCorrectly() {
        // When
        List<String> actualTitles = this.productRepository
                .findAllOrderedByTitle()
                .stream()
                .map(Product::getTitle)
                .collect(Collectors.toList());

        List<String> expectedTitles = List.of(
                ASSASSINS_CREED,
                DESPERATION,
                IT,
                TRANSFORMERS
        );
        // Then
        assertThat(actualTitles).isEqualTo(expectedTitles);
    }

    @Test
    void findAllOrderedByTitle_givenNoProducts_ShouldReturnEmptyList() {
        // When
        this.productRepository.deleteAll();
        List<String> actualTitles = this.productRepository
                .findAllOrderedByTitle()
                .stream()
                .map(Product::getTitle)
                .collect(Collectors.toList());

        List<String> expectedTitles = List.of();
        // Then
        assertThat(actualTitles).isEqualTo(expectedTitles);
    }

    // Tests for findByType(ProductType type) method

    @Test
    void findByType_givenValidType_ShouldReturnProductsOfGivenType() {
        // When
        List<Product> actualProducts = this.productRepository.findByType(BOOK);

        List<Product> expectedProducts = List.of(this.desperation, this.it);

        // Then
        assertThat(actualProducts).isEqualTo(expectedProducts);
    }

    @Test
    void findByType_givenNullType_ShouldReturnEmptyList() {
        // When
        List<Product> actualProducts = this.productRepository.findByType(null);

        List<Product> expectedProducts = List.of();

        // Then
        assertThat(actualProducts).isEqualTo(expectedProducts);
    }

    @Nested
    class NestedTests {

        private final String ALTER_OF_EDEN = "Alter of Eden";
        private final BigDecimal ALTER_OF_EDEN_PRICE = new BigDecimal("11");

        private Product testProduct;

        @BeforeEach
        void setUp() {
            //Given
            this.testProduct = new Product();
            this.testProduct.setType(BOOK);
            this.testProduct.setTitle(ALTER_OF_EDEN);
            this.testProduct.setPrice(ALTER_OF_EDEN_PRICE);
            productRepository.save(this.testProduct);
        }

        @Test
        void findByType_givenInvalidType_() {
            // When
            List<Product> actualProducts = productRepository.findByType(MOVIE);

            List<Product> expectedProducts = List.of();
            // Then
            assertThat(actualProducts).isNotEqualTo(expectedProducts);
        }
    }

    // Tests for findProductTypes() method
    @Test
    void findProductTypes_givenNonEmptyDB_shouldReturnCorrectProductsCount() {
        // When
        List<String> actualTypes = this.productRepository.findProductTypes();
        // Then
        assertThat(actualTypes.size()).isEqualTo(THREE);
    }

    @Test
    void findProductTypes_givenEmptyDB_shouldReturnEmptyList() {
        // When
        this.productRepository.deleteAll();
        List<String> actualTypes = this.productRepository.findProductTypes();

        List<String> expectedTypes = List.of();
        // Then
        assertThat(actualTypes).isEqualTo(expectedTypes);
    }

    // Test for findByPrice(BigDecimal price) method
    @Test
    void findByPrice_givenPositivePrice_shouldReturnCorrectProductCount() {
        // When
        List<Product> actualProducts = this.productRepository.findByPrice(IT_PRICE);
        // Then
        assertThat(actualProducts.size()).isEqualTo(TWO);
    }

    @Test
    void findByPrice_givenNegativePrice_shouldReturnEmptyList() {
        // When
        List<Product> actualProducts = this.productRepository.findByPrice(INVALID_PRICE);
        // Then
        assertThat(actualProducts.size()).isEqualTo(ZERO);
    }

    @Test
    void findByPrice_givenZeroPrice_shouldReturnEmptyList() {
        // When
        List<Product> actualProducts = this.productRepository.findByPrice(DECIMAL_ZERO);
        // Then
        assertThat(actualProducts.size()).isEqualTo(ZERO);
    }

    @Test
    void findByPrice_givenNullPrice_shouldReturnEmptyList() {
        // When
        List<Product> actualProducts = this.productRepository.findByPrice(null);
        // Then
        assertThat(actualProducts.size()).isEqualTo(ZERO);
    }

    // Tests for findByTitle(String title) method

    @Test
    void findByTitle_givenValidPartialOfTitle_shouldReturnCorrectProductsCount() {
        // When
        List<Product> products = this.productRepository.findByTitle(VALID_PARTIAL);
        // Then
        assertThat(products.size()).isEqualTo(THREE);
    }

    @Test
    void findByTitle_givenValidTitle_shouldReturnCorrectProductsCount() {
        // When
        List<Product> products = this.productRepository.findByTitle(DESPERATION);
        // Then
        assertThat(products.size()).isEqualTo(ONE);
    }

    @Test
    void findByTitle_givenInvalidTitle_shouldReturnEmptyList() {
        // When
        List<Product> products = this.productRepository.findByTitle(INVALID_TITLE);
        // Then
        assertThat(products.size()).isEqualTo(ZERO);
    }

    @Test
    void findByTitle_givenNullTitle_shouldReturnEmptyList() {
        // When
        List<Product> products = this.productRepository.findByTitle(null);
        // Then
        assertThat(products.size()).isEqualTo(ZERO);
    }

    // Tests for findByDescription(String desc) method
    @Test
    void findByTitle_givenValidDescription_shouldReturnCorrectProductsCount() {
        // When
        List<Product> products = this.productRepository
                .findByDescription(TRANSFORMERS_DESCRIPTION);
        // Then
        assertThat(products.size()).isEqualTo(ONE);
    }

    @Test
    void findByTitle_givenPartialDescription_shouldReturnCorrectProductsCount() {
        // When
        List<Product> products = this.productRepository.findByDescription(BOOK_VALID_PARTIAL_DESC);
        // Then
        assertThat(products.size()).isEqualTo(ONE);
    }

    @Test
    void findByTitle_givenInvalidDescription_shouldReturnEmptyList() {
        // When
        List<Product> products = this.productRepository.findByDescription(INVALID_TITLE);
        // Then
        assertThat(products.size()).isEqualTo(ZERO);
    }

    @Test
    void findByTitle_givenNullDescription_shouldReturnEmptyList() {
        // When
        List<Product> products = this.productRepository.findByDescription(null);
        // Then
        assertThat(products.size()).isEqualTo(ZERO);
    }

    @Test
    void findAllByPriceInRange_givenValidPrices_ShouldReturnCorrectListSize() {
        List<Product> products = this.productRepository
                .findAll(priceBetween(new BigDecimal("10"), new BigDecimal("22")));

        assertThat(products.size()).isEqualTo(3);
    }

    @Test
    void findLessThan_givenValidPriceExclusive_ShouldReturnCorrectListSize() {
        List<Product> products = this.productRepository
                .findAll(lessThan(new BigDecimal("22"), false));

        assertThat(products.size()).isEqualTo(1);
    }

    @Test
    void findLessThan_givenValidPriceInclusive_ShouldReturnCorrectListSize() {
        List<Product> products = this.productRepository
                .findAll(lessThan(new BigDecimal("22"), true));

        assertThat(products.size()).isEqualTo(3);
    }

    @Test
    void findGreaterThan_givenValidPriceExclusive_ShouldReturnCorrectListSize() {
        List<Product> products = this.productRepository
                .findAll(greaterThan(new BigDecimal("22"), false));

        assertThat(products.size()).isEqualTo(1);
    }

    @Test
    void findGreaterThan_givenValidPriceInclusive_ShouldReturnCorrectListSize() {
        List<Product> products = this.productRepository
                .findAll(greaterThan(new BigDecimal("22"), true));

        assertThat(products.size()).isEqualTo(3);
    }

    @Test
    void findTwoProductsWithPagination_ShouldReturnCorrectResultSetSize() {
        Page<Product> productsPage = this.productRepository.findAll(PageRequest.of(0, 2));

        assertThat(productsPage.getSize()).isEqualTo(2);
        assertThat(productsPage.getTotalElements()).isEqualTo(4);
        assertThat(productsPage.getContent().size()).isEqualTo(2);
    }
}