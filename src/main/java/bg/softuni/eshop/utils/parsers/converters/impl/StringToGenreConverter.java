package bg.softuni.eshop.utils.parsers.converters.impl;

import bg.softuni.eshop.product.dao.ReferencedDataRepository;
import bg.softuni.eshop.product.model.entity.GenreEntity;
import bg.softuni.eshop.utils.parsers.converters.BaseStringToReferencedDataConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("stringToGenre")
public class StringToGenreConverter extends BaseStringToReferencedDataConverter<GenreEntity> {
    @Autowired
    public StringToGenreConverter(ReferencedDataRepository<GenreEntity> referencedDataRepository) {
        super(referencedDataRepository);
    }
}
