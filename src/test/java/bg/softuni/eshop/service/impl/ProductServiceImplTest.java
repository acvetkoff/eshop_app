package bg.softuni.eshop.service.impl;

import bg.softuni.eshop.admin.service.AdminProductService;
import bg.softuni.eshop.admin.service.impl.AdminProductServiceImpl;
import bg.softuni.eshop.product.dao.ProductRepository;
import bg.softuni.eshop.product.dao.ReferencedDataRepository;
import bg.softuni.eshop.product.model.entity.*;
import bg.softuni.eshop.product.model.enums.GenreType;
import bg.softuni.eshop.product.model.enums.ProductType;
import bg.softuni.eshop.product.model.service.BookServiceModel;
import bg.softuni.eshop.product.model.service.GameServiceModel;
import bg.softuni.eshop.product.model.service.MovieServiceModel;
import bg.softuni.eshop.product.model.service.ProductServiceModel;
import bg.softuni.eshop.product.service.ProductService;
import bg.softuni.eshop.product.service.impl.ProductServiceImpl;
import bg.softuni.eshop.utils.parsers.ModelParser;
import bg.softuni.eshop.utils.parsers.converters.impl.*;
import bg.softuni.eshop.utils.parsers.impl.ModelParserImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Set;

import static bg.softuni.eshop.product.model.enums.ProductType.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceImplTest {

    private static final ProductType GAME_TYPE = GAME;
    private static final ProductType MOVIE_TYPE = MOVIE;
    private static final ProductType BOOK_TYPE = BOOK;

    private static final String GAME_ID = "1";
    private static final String GAME_TITLE = "Last of Us";
    private static final BigDecimal GAME_PRICE = new BigDecimal("60.00");
    private static final String GAME_COMPANY = "Naughty Dog";

    private static final String MOVIE_ID = "2";
    private static final String MOVIE_TITLE = "Prisoners";
    private static final BigDecimal MOVIE_PRICE = new BigDecimal("6.00");
    private static final String MOVIE_DIRECTOR = "Denis Villeneuve";
    private static final String MOVIE_IMDB_URL = "https://www.imdb.com/title/tt1392214/?ref_=nv_sr_srsg_0";
    private static final String MOVIE_STUDIO = "Alcon Entertainmen";

    private static final String BOOK_ID = "3";
    private static final String BOOK_TITLE = "1984 Oracule";
    private static final BigDecimal BOOK_PRICE = new BigDecimal("20.00");
    private static final String BOOK_AUTHOR = "George Orwell";
    private static final int BOOK_PAGES = 416;

    private static final int METHOD_INVOCATIONS = 1;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ReferencedDataRepository<GenreEntity> genreRepo;
    @Mock
    private ReferencedDataRepository<MediaEntity> mediaRepo;
    @Mock
    private ReferencedDataRepository<PlatformEntity> platformRepo;

    private ProductService productService;
    private AdminProductService adminProductService;

    @Autowired
    private ModelParser modelParser;
    private GameServiceModel game;
    private MovieServiceModel movie;
    private BookServiceModel book;

    @BeforeEach
    void setUp() {
        this.modelParser = new ModelParserImpl(
                new PlatformToStringConverter(),
                new GenreToStringConverter(),
                new MediaTypeToStringConverter(),
                new StringToGenreConverter(this.genreRepo),
                new StringToMediaConverter(this.mediaRepo),
                new StringToPlatformConverter(this.platformRepo));
        this.productService = new ProductServiceImpl(this.modelParser, this.productRepository);
        this.adminProductService = new AdminProductServiceImpl(this.modelParser, this.productRepository, null, null);

        this.game = new GameServiceModel();
        this.game.setId(GAME_ID);
        this.game.setType(GAME_TYPE);
        this.game.setTitle(GAME_TITLE);
        this.game.setPrice(GAME_PRICE);
        this.game.setCompany(GAME_COMPANY);

        this.movie = new MovieServiceModel();
        this.movie.setId(MOVIE_ID);
        this.movie.setType(MOVIE_TYPE);
        this.movie.setTitle(MOVIE_TITLE);
        this.movie.setPrice(MOVIE_PRICE);
        this.movie.setDirector(MOVIE_DIRECTOR);
        this.movie.setImdbUrl(MOVIE_IMDB_URL);
        this.movie.setStudio(MOVIE_STUDIO);

        this.book = new BookServiceModel();
        this.book.setId(BOOK_ID);
        this.book.setType(BOOK_TYPE);
        this.book.setTitle(BOOK_TITLE);
        this.book.setPrice(BOOK_PRICE);
        this.book.setAuthor(BOOK_AUTHOR);
        this.book.setPages(BOOK_PAGES);
    }

    // Tests for getAll() method
    @Test
    void getAll_shouldCallFindAll() {
        // When
        this.productService.getAll();
        // Then
        verify(this.productRepository, times(METHOD_INVOCATIONS)).findAll();
    }

    // Tests for getAllOrderedByTitle() method
    @Test
    void getAllOrderedByTitle_ShouldCallFindAllOrderedByTitle() {
        // When
        this.productService.getAllOrderedByTitle();
        // Then
        verify(this.productRepository, times(METHOD_INVOCATIONS)).findAllOrderedByTitle();
    }

    // TODO no need to read from repository
    // Tests for getProductTypes() method
//    @Test
//    void getProductTypes_shouldCallFindProductTypes() {
//        // When
//        this.productService.getProductTypes();
//        // Then
//        verify(this.productRepository, times(METHOD_INVOCATIONS)).findProductTypes();
//    }

    // Tests for add(ProductServiceModel productServiceModel) method
    @Test
    void add_givenGameProductType_shouldAddGameProduct() {
        // Given
        GameEntity gameEntity = this.modelParser.convert(this.game, GameEntity.class);
        // When
        when(productRepository.save(any(GameEntity.class))).thenReturn(gameEntity);
        this.adminProductService.add(this.game);
        // Then
        ArgumentCaptor<GameEntity> productCaptor = ArgumentCaptor.forClass(GameEntity.class);
        verify(this.productRepository).save(productCaptor.capture());

        GameEntity capturedGame = productCaptor.getValue();
        assertThat(capturedGame).isEqualTo(gameEntity);
    }

    @Test
    void add_givenMovieProductType_shouldAddMovieProduct() {
        // Given
        MovieEntity movieEntity = this.modelParser.convert(this.movie, MovieEntity.class);
        // When
        when(productRepository.save(any(MovieEntity.class))).thenReturn(movieEntity);
        this.adminProductService.add(this.movie);
        // Then
        ArgumentCaptor<MovieEntity> productCaptor = ArgumentCaptor.forClass(MovieEntity.class);
        verify(this.productRepository).save(productCaptor.capture());

        MovieEntity capturedGame = productCaptor.getValue();
        assertThat(capturedGame).isEqualTo(movieEntity);
    }

    @Test
    void add_givenBookProductType_shouldAddBookProduct() {
        // Given
        BookEntity bookEntity = this.modelParser.convert(this.book, BookEntity.class);
        // When
        when(productRepository.save(any(BookEntity.class))).thenReturn(bookEntity);
        this.adminProductService.add(this.book);
        // Then
        ArgumentCaptor<BookEntity> productCaptor = ArgumentCaptor.forClass(BookEntity.class);
        verify(this.productRepository).save(productCaptor.capture());

        BookEntity capturedGame = productCaptor.getValue();
        assertThat(capturedGame).isEqualTo(bookEntity);
    }

    // Tests for getByType(ProductType productType) method
    @Test
    void getByType_givenValidType_ShouldReturnCorrectProduct() {
        // When
        this.productService.getByType("GAME");
        // Then
        verify(this.productRepository, times(METHOD_INVOCATIONS)).findByType(GAME);
    }

    // Tests for getById(String id) method
    @Test
    void getById() {
        // Given
        BookEntity bookEntity = this.modelParser.convert(this.book, BookEntity.class);
        bookEntity.setGenres(Set.of(new GenreEntity("ACTION","Action")));


        Mockito.when(genreRepo.findByKey(GenreType.ACTION.name())).thenReturn(Optional.of(new GenreEntity("ACTION","Action")));

        when(this.productRepository.findById(BOOK_ID)).thenReturn(Optional.of(bookEntity));
        // When
        ProductServiceModel productServiceModel = this.productService.getById(BOOK_ID);

        assertThat(productServiceModel.getGenres().length).isEqualTo(1);
        assertThat(productServiceModel.getGenres()[0]).isEqualTo("ACTION");

        // Then
        assertThat(this.modelParser.convert(productServiceModel, BookEntity.class))
                .isEqualTo(bookEntity);
        // TODO add check or Genre
    }
}