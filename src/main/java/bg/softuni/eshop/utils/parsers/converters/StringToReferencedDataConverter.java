package bg.softuni.eshop.utils.parsers.converters;

import bg.softuni.eshop.product.model.entity.ReferencedData;
import org.modelmapper.Converter;

public interface StringToReferencedDataConverter<T extends ReferencedData> extends Converter<String, T> {
}
