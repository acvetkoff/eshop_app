package bg.softuni.eshop.utils.parsers.converters.impl;

import bg.softuni.eshop.product.model.entity.MediaEntity;
import bg.softuni.eshop.utils.parsers.converters.BaseReferencedDataToStringConverter;
import org.springframework.stereotype.Component;

@Component("mediaTypeToString")
public class MediaTypeToStringConverter  extends BaseReferencedDataToStringConverter<MediaEntity> {
}
