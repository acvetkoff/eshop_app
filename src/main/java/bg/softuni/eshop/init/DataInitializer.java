package bg.softuni.eshop.init;

import bg.softuni.eshop.exceptions.RoleNotFoundException;
import bg.softuni.eshop.product.dao.ProductRepository;
import bg.softuni.eshop.product.dao.ReferencedDataRepository;
import bg.softuni.eshop.product.model.binding.ProductBindingModel;
import bg.softuni.eshop.product.model.entity.*;
import bg.softuni.eshop.product.model.service.BookServiceModel;
import bg.softuni.eshop.product.model.service.GameServiceModel;
import bg.softuni.eshop.product.model.service.MovieServiceModel;
import bg.softuni.eshop.product.model.service.ProductServiceModel;
import bg.softuni.eshop.user.dao.UserRepository;
import bg.softuni.eshop.user.dao.UserRoleRepository;
import bg.softuni.eshop.user.model.binding.UserRegisterBindingModel;
import bg.softuni.eshop.user.model.entity.UserEntity;
import bg.softuni.eshop.user.model.entity.UserRoleEntity;
import bg.softuni.eshop.user.model.enums.UserRole;
import bg.softuni.eshop.user.model.service.UserServiceModel;
import bg.softuni.eshop.utils.parsers.FileParser;
import bg.softuni.eshop.utils.parsers.ModelParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static bg.softuni.eshop.common.ExceptionMessages.ROLE_NOT_FOUND_MESSAGE;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataInitializer.class);

    private final UserRoleRepository userRoleRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final ReferencedDataRepository referencedDataRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelParser modelParser;
    private final FileParser fileParser;

    @Value("${static.data.product-paths}")
    private List<String> productsPaths;

    @Value("${static.data.platforms}")
    private String platformsPath;

    @Value("${static.data.genres}")
    private String genresPath;

    @Value("${static.data.medias}")
    private String mediaPath;

    @Value("${static.data.users-roles}")
    private String userRolesPath;

    @Value("${static.data.users}")
    private String usersPath;

    @Autowired
    public DataInitializer(
            UserRoleRepository userRoleRepository,
            UserRepository userRepository,
            ProductRepository productRepository,
            ReferencedDataRepository referencedDataRepository,
            PasswordEncoder passwordEncoder,
            ModelParser modelParser,
            FileParser fileParser) {
        this.userRoleRepository = userRoleRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.referencedDataRepository = referencedDataRepository;
        this.passwordEncoder = passwordEncoder;
        this.modelParser = modelParser;
        this.fileParser = fileParser;
    }

    @Override
    public void run(String... args) throws IOException {
        this.seedStaticData();
        this.seedInitialProducts();
    }

    private void seedInitialProducts() throws IOException {
        if (this.productRepository.count() == 0) {
            this.importProducts();
        }
    }

    private void importProducts() throws IOException {
        List<ProductBindingModel> products = new ArrayList<>();
        for (String path : this.productsPaths) {
            try {
                ProductBindingModel[] productBindingModels = this.fileParser.read(path, ProductBindingModel[].class);
                products.addAll(List.of(productBindingModels));

            } catch (IOException e) {
                LOGGER.error("Error parsing file with path {}", path);
                throw e;
            }
        }
        this.seedProducts(products);
    }

    private void seedProducts(List<ProductBindingModel> products) {
        Set<Product> productsSet = products.stream()
                .map(bindingModel -> this.modelParser.convert(bindingModel, ProductServiceModel.class))
                .map(productServiceModel -> {
                    Product product = this.modelParser.convert(productServiceModel, Product.class);
                    product = this.updateSubtype(product, productServiceModel);

                    return product;
                })
                .collect(Collectors.toSet());

        this.productRepository.saveAll(productsSet);
    }

    private Product updateSubtype(Product product, ProductServiceModel productServiceModel) {
        String[] genres = productServiceModel.getGenres();

        switch (product.getType()) {
            case GAME:
                return getGameEntity((GameServiceModel) productServiceModel, genres);
            case BOOK:
                return getBookEntity((BookServiceModel) productServiceModel, genres);
            case MOVIE:
                return getMovieEntity((MovieServiceModel) productServiceModel, genres);
            default:
                return product;
        }
    }

    private GameEntity getGameEntity(GameServiceModel productServiceModel, String[] genres) {
        GameServiceModel gameServiceModel = productServiceModel;
        String[] platforms = gameServiceModel.getPlatforms();
        GameEntity gameEntity = this.modelParser.convert(gameServiceModel, GameEntity.class);
        gameEntity.getPlatforms().clear();
        gameEntity.getGenres().clear();
        for (String platform : platforms) {
            Optional<ReferencedData> key = this.referencedDataRepository.findByKey(platform);

            if (key.isEmpty()) {
                throw new IllegalArgumentException();
            }

            gameEntity.addPlatform((PlatformEntity) key.get());
        }

        for (String genre : genres) {
            Optional<ReferencedData> key = this.referencedDataRepository.findByKey(genre);

            if (key.isEmpty()) {
                throw new IllegalArgumentException();
            }

            gameEntity.addGenre((GenreEntity) key.get());
        }

        return gameEntity;
    }

    private BookEntity getBookEntity(BookServiceModel productServiceModel, String[] genres) {
        BookServiceModel bookServiceModel = productServiceModel;
        BookEntity bookEntity = this.modelParser.convert(bookServiceModel, BookEntity.class);
        bookEntity.getGenres().clear();
        for (String genre : genres) {
            Optional<ReferencedData> key = this.referencedDataRepository.findByKey(genre);

            if (key.isEmpty()) {
                throw new IllegalArgumentException();
            }

            bookEntity.addGenre((GenreEntity) key.get());
        }

        return bookEntity;
    }

    private MovieEntity getMovieEntity(MovieServiceModel productServiceModel, String[] genres) {
        MovieServiceModel movieServiceModel = productServiceModel;
        MovieEntity movieEntity = this.modelParser.convert(movieServiceModel, MovieEntity.class);
        movieEntity.getGenres().clear();
        movieEntity.getMediaTypes().clear();
        for (String genre : genres) {
            Optional<ReferencedData> key = this.referencedDataRepository.findByKey(genre);

            if (key.isEmpty()) {
                throw new IllegalArgumentException();
            }

            movieEntity.addGenre((GenreEntity) key.get());
        }

        for (String mediaType : movieServiceModel.getMediaTypes()) {
            Optional<ReferencedData> key = this.referencedDataRepository.findByKey(mediaType);

            if (key.isEmpty()) {
                throw new IllegalArgumentException();
            }

            movieEntity.addMediaType((MediaEntity) key.get());
        }

        return movieEntity;
    }

    private void seedStaticData() throws IOException {
        this.importUserRolesEntities();
        this.importUsersEntities();
        this.importReferencedData();
    }

    private void importUsersEntities() throws IOException {
        try {
            UserRegisterBindingModel[] userRegisterBindingModels =
                    this.fileParser.read(this.usersPath, UserRegisterBindingModel[].class);

            this.seedUsers(userRegisterBindingModels);
        } catch (IOException e) {
            LOGGER.error("Error parsing file with path {}", this.usersPath);
            e.printStackTrace();
            throw e;
        }
    }

    private void seedUsers(UserRegisterBindingModel[] userRegisterBindingModels) {
        Arrays.stream(userRegisterBindingModels)
                .map(userRegisterBindingModel -> this.modelParser.convert(userRegisterBindingModel, UserServiceModel.class))
                .forEach(userServiceModel -> {
                    UserEntity user = this.modelParser.convert(userServiceModel, UserEntity.class);
                    user.getRoles().clear();
                    for (String role : userServiceModel.getRoles()) {
                        Optional<UserRoleEntity> userRoleEntityOptional =
                                this.userRoleRepository.findByUserRole(UserRole.valueOf(role));

                        if (userRoleEntityOptional.isEmpty()) {
                            throw new RoleNotFoundException(String.format(ROLE_NOT_FOUND_MESSAGE, role));
                        }

                        user.addRole(userRoleEntityOptional.get());
                    }

                    user.setPassword(this.passwordEncoder.encode(user.getPassword()));
                    this.userRepository.save(user);
                });
    }

    private void importUserRolesEntities() throws IOException {
        try {
            UserRoleEntity[] userRoles = this.fileParser.read(this.userRolesPath, UserRoleEntity[].class);
            this.userRoleRepository.saveAll(Arrays.asList(userRoles));
        } catch (IOException e) {
            LOGGER.error("Error parsing file with path {}", this.userRolesPath);
            e.printStackTrace();
            throw e;
        }
    }

    private void importReferencedData() throws IOException {
        List<ReferencedData> referencedDataList = new ArrayList<>();
        try {
            referencedDataList.addAll(Arrays.asList(this.readGenres()));
            referencedDataList.addAll(Arrays.asList(this.readMedia()));
            referencedDataList.addAll(Arrays.asList(this.readPlatform()));
        } catch (IOException e) {
            LOGGER.error("Error parsing files with path");
            e.printStackTrace();
            throw e;
        }

        this.referencedDataRepository.saveAll(referencedDataList);
        System.out.println();
    }

    private PlatformEntity[] readPlatform() throws IOException {
        return this.fileParser.read(this.platformsPath, PlatformEntity[].class);

    }

    private MediaEntity[] readMedia() throws IOException {
        return this.fileParser.read(this.mediaPath, MediaEntity[].class);
    }

    private GenreEntity[] readGenres() throws IOException {
        return this.fileParser.read(this.genresPath, GenreEntity[].class);
    }
}
