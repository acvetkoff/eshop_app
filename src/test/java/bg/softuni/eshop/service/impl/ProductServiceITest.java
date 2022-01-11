package bg.softuni.eshop.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import bg.softuni.eshop.admin.service.AdminProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import bg.softuni.eshop.product.model.binding.SearchProductCriteriaDTO;
import bg.softuni.eshop.product.model.enums.GamePlatform;
import bg.softuni.eshop.product.model.enums.ProductType;
import bg.softuni.eshop.product.model.service.GameServiceModel;
import bg.softuni.eshop.product.model.service.ProductServiceModel;
import bg.softuni.eshop.product.service.ProductService;

@SpringBootTest
@ActiveProfiles("test")
public class ProductServiceITest extends BaseIntegrationTest {

	@Autowired
	private ProductService productService;

	@Autowired
	private AdminProductService adminProductService;

	@Test
	public void testSearchProducts() {
		List<ProductServiceModel> allProducts = productService.getAll();
		assertTrue(allProducts.size() > 0);

		SearchProductCriteriaDTO searchCriteria = new SearchProductCriteriaDTO();
		searchCriteria.setPlatforms(Set.of(GamePlatform.XBOX));

		List<ProductServiceModel> xbox = productService.searchProducts(searchCriteria);
		assertEquals(3, xbox.size());

		searchCriteria.setStartPrice(new BigDecimal(60.0));

		xbox = productService.searchProducts(searchCriteria);
		assertEquals(1, xbox.size());

		searchCriteria.setStartPrice(new BigDecimal(25.0));
		searchCriteria.setEndPrice(new BigDecimal("59.90"));

		xbox = productService.searchProducts(searchCriteria);
		assertEquals(2, xbox.size());

		searchCriteria.setTitle("Elder");

		xbox = productService.searchProducts(searchCriteria);
		assertEquals(1, xbox.size());
		
		
		GameServiceModel productServiceModel = new GameServiceModel();
		productServiceModel.setTitle("NoMatch");
		productServiceModel.setId(xbox.get(0).getId());
		productServiceModel.setType(ProductType.GAME);
		adminProductService.patch(xbox.get(0).getId(), productServiceModel);
		
		xbox = productService.searchProducts(searchCriteria);
		assertEquals(0, xbox.size());
	}
}
