package bg.softuni.eshop.utils.parsers.converters;

import bg.softuni.eshop.product.model.entity.ReferencedData;
import org.modelmapper.spi.MappingContext;

public abstract class BaseReferencedDataToStringConverter<T extends ReferencedData> implements ReferencedDataToStringConverter<T, String> {
    @Override
    public String convert(MappingContext<T, String> mappingContext) {
        return mappingContext.getSource() == null ? null : mappingContext.getSource().getKey();
    }
}
