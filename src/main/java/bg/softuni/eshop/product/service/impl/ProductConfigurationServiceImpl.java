package bg.softuni.eshop.product.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import bg.softuni.eshop.config.ProductConfiguration;
import bg.softuni.eshop.product.service.ProductConfigurationService;
import lombok.Value;

@Service
@Value
public class ProductConfigurationServiceImpl implements ProductConfigurationService {

	private final ProductConfiguration configuration;

	@Autowired
	public ProductConfigurationServiceImpl(ProductConfiguration configuration) {
		this.configuration = configuration;
	}

}
