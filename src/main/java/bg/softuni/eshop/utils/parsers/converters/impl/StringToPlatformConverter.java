package bg.softuni.eshop.utils.parsers.converters.impl;

import bg.softuni.eshop.product.dao.ReferencedDataRepository;
import bg.softuni.eshop.product.model.entity.PlatformEntity;
import bg.softuni.eshop.utils.parsers.converters.BaseStringToReferencedDataConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("stringToPlatform")
public class StringToPlatformConverter extends BaseStringToReferencedDataConverter<PlatformEntity> {

    @Autowired
    public StringToPlatformConverter(ReferencedDataRepository<PlatformEntity> referencedDataRepository) {
        super(referencedDataRepository);
    }
}
