package bg.softuni.eshop.utils.parsers.converters.impl;

import bg.softuni.eshop.product.model.entity.GenreEntity;
import bg.softuni.eshop.utils.parsers.converters.BaseReferencedDataToStringConverter;
import org.springframework.stereotype.Component;

@Component("genreToString")
public class GenreToStringConverter extends BaseReferencedDataToStringConverter<GenreEntity> {

}
