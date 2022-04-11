package bg.softuni.eshop.utils.parsers.impl;

import bg.softuni.eshop.product.model.binding.BookBindingModel;
import bg.softuni.eshop.product.model.binding.GameBindingModel;
import bg.softuni.eshop.product.model.binding.MovieBindingModel;
import bg.softuni.eshop.product.model.binding.ProductBindingModel;
import bg.softuni.eshop.product.model.entity.*;
import bg.softuni.eshop.product.model.service.BookServiceModel;
import bg.softuni.eshop.product.model.service.GameServiceModel;
import bg.softuni.eshop.product.model.service.MovieServiceModel;
import bg.softuni.eshop.product.model.service.ProductServiceModel;
import bg.softuni.eshop.product.model.view.BookViewModel;
import bg.softuni.eshop.product.model.view.GameViewModel;
import bg.softuni.eshop.product.model.view.MovieViewModel;
import bg.softuni.eshop.product.model.view.ProductViewModel;
import bg.softuni.eshop.utils.parsers.ModelParser;
import bg.softuni.eshop.utils.parsers.converters.ReferencedDataToStringConverter;
import bg.softuni.eshop.utils.parsers.converters.StringToReferencedDataConverter;
import bg.softuni.eshop.utils.parsers.converters.impl.StringToGenreConverter;
import org.modelmapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class ModelParserImpl implements ModelParser {

    private final ModelMapper modelMapper;
    private final ReferencedDataToStringConverter<PlatformEntity, String> platformToStringConverter;
    private final ReferencedDataToStringConverter<GenreEntity, String> genreToStringConverter;
    private final ReferencedDataToStringConverter<MediaEntity, String> mediaToStringConverter;
    private final StringToReferencedDataConverter<GenreEntity> stringToGenreConverter;
    private final StringToReferencedDataConverter<MediaEntity> stringToMediaConverter;
    private final StringToReferencedDataConverter<PlatformEntity> stringToPlatformConverter;

    @Autowired
    public ModelParserImpl(
            @Qualifier("platformToString") ReferencedDataToStringConverter<PlatformEntity, String> platformToStringConverter,
            @Qualifier("genreToString") ReferencedDataToStringConverter<GenreEntity, String> genreToStringConverter,
            @Qualifier("mediaTypeToString") ReferencedDataToStringConverter<MediaEntity, String> mediaToStringConverter,
            @Qualifier("stringToGenre") StringToReferencedDataConverter<GenreEntity> stringToGenreConverter,
            @Qualifier("stringToMedia") StringToReferencedDataConverter<MediaEntity> stringToMediaConverter,
            @Qualifier("stringToPlatform") StringToReferencedDataConverter<PlatformEntity> stringToPlatformConverter) throws RuntimeException {
        this.platformToStringConverter = platformToStringConverter;
        this.genreToStringConverter = genreToStringConverter;
        this.mediaToStringConverter = mediaToStringConverter;
        this.stringToGenreConverter = stringToGenreConverter;
        this.stringToMediaConverter = stringToMediaConverter;
        this.stringToPlatformConverter = stringToPlatformConverter;

        this.modelMapper = new ModelMapper();
        this.modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
        this.mapEmptyStringToNull();
        this.configureMappings();
        this.addConverters();
    }

    private void addConverters() {
        this.modelMapper.addConverter(this.platformToStringConverter);
        this.modelMapper.addConverter(this.genreToStringConverter);
        this.modelMapper.addConverter(this.mediaToStringConverter);
        this.modelMapper.addConverter(this.stringToGenreConverter);
        this.modelMapper.addConverter(this.stringToMediaConverter);
        this.modelMapper.addConverter(this.stringToPlatformConverter);
    }

    @Override
    public <S, D> D convert(S source, Class<D> destinationClass) {
        return this.modelMapper.map(source, destinationClass);
    }

    @Override
    public <S, D> void convert(S source, D dest) {
        this.modelMapper.map(source, dest);
    }

	@Override
	public <S, D> List<D> convert(List<S> sourceList, Class<D> destination) {
		return sourceList.stream().map(source -> modelMapper.map(source, destination)).collect(Collectors.toList());
	}

    private void mapServiceToEntity() {
        this.modelMapper.typeMap(GameServiceModel.class, GameEntity.class);
        this.modelMapper.typeMap(BookServiceModel.class, BookEntity.class);
        this.modelMapper.typeMap(MovieServiceModel.class, MovieEntity.class);

        this.modelMapper.typeMap(GameServiceModel.class, Product.class)
                .setProvider(provisionRequest -> this.modelMapper.map(provisionRequest.getSource(), GameEntity.class))
                .includeBase(ProductServiceModel.class, Product.class);

        this.modelMapper.typeMap(BookServiceModel.class, Product.class)
                .setProvider(provisionRequest -> this.modelMapper.map(provisionRequest.getSource(), BookEntity.class))
                .includeBase(ProductServiceModel.class, Product.class);

        this.modelMapper.typeMap(MovieServiceModel.class, Product.class)
                .setProvider(provisionRequest -> this.modelMapper.map(provisionRequest.getSource(), MovieEntity.class))
                .includeBase(ProductServiceModel.class, Product.class);
    }

    private void mapEntityToServiceModel() {
        this.modelMapper.typeMap(GameEntity.class, GameServiceModel.class);
        this.modelMapper.typeMap(BookEntity.class, BookServiceModel.class);
        this.modelMapper.typeMap(MovieEntity.class, MovieServiceModel.class);

        this.modelMapper.typeMap(GameEntity.class, ProductServiceModel.class)
                .setProvider(provisionRequest -> modelMapper.map(provisionRequest.getSource(), GameServiceModel.class))
                .includeBase(Product.class, ProductServiceModel.class);

        this.modelMapper.typeMap(BookEntity.class, ProductServiceModel.class)
                .setProvider(provisionRequest -> modelMapper.map(provisionRequest.getSource(), BookServiceModel.class))
                .includeBase(Product.class, ProductServiceModel.class);

        this.modelMapper.typeMap(MovieEntity.class, ProductServiceModel.class)
                .setProvider(provisionRequest -> modelMapper.map(provisionRequest.getSource(), MovieServiceModel.class))
                .includeBase(Product.class, ProductServiceModel.class);
    }

    private void mapBindingToService() {
        this.modelMapper.createTypeMap(GameBindingModel.class, GameServiceModel.class);
        this.modelMapper.createTypeMap(BookBindingModel.class, BookServiceModel.class);
        this.modelMapper.createTypeMap(MovieBindingModel.class, MovieServiceModel.class);

        this.modelMapper.typeMap(GameBindingModel.class, ProductServiceModel.class)
                .setProvider(provisionRequest -> modelMapper.map(provisionRequest.getSource(), GameServiceModel.class))
                .includeBase(ProductBindingModel.class, ProductServiceModel.class);

        this.modelMapper.typeMap(BookBindingModel.class, ProductServiceModel.class)
                .setProvider(provisionRequest -> modelMapper.map(provisionRequest.getSource(), BookServiceModel.class))
                .includeBase(ProductBindingModel.class, ProductServiceModel.class);

        this.modelMapper.typeMap(MovieBindingModel.class, ProductServiceModel.class)
                .setProvider(provisionRequest -> modelMapper.map(provisionRequest.getSource(), MovieServiceModel.class))
                .includeBase(ProductBindingModel.class, ProductServiceModel.class);
    }

    private void mapServiceToViewModel() {
        this.modelMapper.createTypeMap(GameServiceModel.class, GameViewModel.class);
        this.modelMapper.createTypeMap(BookServiceModel.class, BookViewModel.class);
        this.modelMapper.createTypeMap(MovieServiceModel.class, MovieViewModel.class);

        this.modelMapper.typeMap(GameServiceModel.class, ProductViewModel.class)
                .setProvider(provisionRequest -> this.modelMapper.map(provisionRequest.getSource(), GameViewModel.class))
                .includeBase(ProductServiceModel.class, ProductViewModel.class);

        this.modelMapper.typeMap(BookServiceModel.class, ProductViewModel.class)
                .setProvider(provisionRequest -> this.modelMapper.map(provisionRequest.getSource(), BookViewModel.class))
                .includeBase(ProductServiceModel.class, ProductViewModel.class);

        this.modelMapper.typeMap(MovieServiceModel.class, ProductViewModel.class)
                .setProvider(provisionRequest -> this.modelMapper.map(provisionRequest.getSource(), MovieViewModel.class))
                .includeBase(ProductServiceModel.class, ProductViewModel.class);
    }

    private void mapEmptyStringToNull() {
        Converter<String, String> toNull = new AbstractConverter<>() {
            @Override
            protected String convert(String source) {
                return source.equals("") ? null : source;
            }
        };

        this.modelMapper.addConverter(toNull);
    }

    private void configureAbstractTypeMapping(Class<?> baseSource, Class<?> baseDestination) {
        this.modelMapper.createTypeMap(baseSource, baseDestination);
    }

    private void configureMappings() {
        this.configureAbstractTypeMapping(ProductBindingModel.class, ProductServiceModel.class);
        this.configureAbstractTypeMapping(ProductServiceModel.class, Product.class);
        this.configureAbstractTypeMapping(ProductServiceModel.class, ProductViewModel.class);
        this.configureAbstractTypeMapping(Product.class, ProductServiceModel.class);

        this.mapBindingToService();
        this.mapServiceToEntity();
        this.mapEntityToServiceModel();
        this.mapServiceToViewModel();
    }
}
