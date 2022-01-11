package bg.softuni.eshop.utils.parsers.converters;

import bg.softuni.eshop.product.model.entity.ReferencedData;
import org.modelmapper.Converter;

public interface ReferencedDataToStringConverter<T extends ReferencedData, String> extends Converter<T, String> {
}
