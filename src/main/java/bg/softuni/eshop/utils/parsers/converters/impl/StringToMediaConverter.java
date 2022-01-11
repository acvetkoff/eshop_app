package bg.softuni.eshop.utils.parsers.converters.impl;

import bg.softuni.eshop.product.dao.ReferencedDataRepository;
import bg.softuni.eshop.product.model.entity.MediaEntity;
import bg.softuni.eshop.utils.parsers.converters.BaseStringToReferencedDataConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("stringToMedia")
public class StringToMediaConverter extends BaseStringToReferencedDataConverter<MediaEntity> {

    @Autowired
    public StringToMediaConverter(ReferencedDataRepository<MediaEntity> referencedDataRepository) {
        super(referencedDataRepository);
    }
}
