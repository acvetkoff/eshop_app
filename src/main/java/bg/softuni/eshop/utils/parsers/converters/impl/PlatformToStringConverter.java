package bg.softuni.eshop.utils.parsers.converters.impl;

import bg.softuni.eshop.product.model.entity.PlatformEntity;
import bg.softuni.eshop.utils.parsers.converters.BaseReferencedDataToStringConverter;
import org.springframework.stereotype.Component;

@Component("platformToString")
public class PlatformToStringConverter extends BaseReferencedDataToStringConverter<PlatformEntity> {
}
