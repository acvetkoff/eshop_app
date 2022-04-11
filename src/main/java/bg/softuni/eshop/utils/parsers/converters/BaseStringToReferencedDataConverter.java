
package bg.softuni.eshop.utils.parsers.converters;

        import bg.softuni.eshop.product.dao.ReferencedDataRepository;
        import bg.softuni.eshop.product.model.entity.ReferencedData;
        import org.modelmapper.spi.MappingContext;
        import org.springframework.beans.factory.annotation.Autowired;

        import java.util.Optional;

public abstract class BaseStringToReferencedDataConverter<T extends ReferencedData> implements StringToReferencedDataConverter<T>{

    private final ReferencedDataRepository<T> referencedDataRepository;

    @Autowired
    public BaseStringToReferencedDataConverter(ReferencedDataRepository<T> referencedDataRepository) {
        this.referencedDataRepository = referencedDataRepository;
    }

    @Override
    public T convert(MappingContext<String, T> mappingContext) {
        if (mappingContext.getSource() == null) {
            return null;
        }

        Optional<T> optionalReferencedData = this.referencedDataRepository.findByKey(mappingContext.getSource());

        if (optionalReferencedData.isEmpty()) {
            throw new IllegalArgumentException();
        }

        return optionalReferencedData.get();
    }
}