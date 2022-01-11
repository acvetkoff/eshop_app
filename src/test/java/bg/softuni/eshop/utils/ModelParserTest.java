package bg.softuni.eshop.utils;

import bg.softuni.eshop.product.model.entity.*;
import bg.softuni.eshop.product.model.service.GameServiceModel;
import bg.softuni.eshop.product.model.service.MovieServiceModel;
import bg.softuni.eshop.product.model.service.ProductServiceModel;
import bg.softuni.eshop.product.model.view.ProductViewModel;
import bg.softuni.eshop.utils.parsers.ModelParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class ModelParserTest {

    @Autowired
    private ModelParser modelParser;

    private Product gameEntity;

    private ProductViewModel productViewModel;

    @BeforeEach
    void setUp() {
        this.gameEntity = new GameEntity();
        this.gameEntity.setId("1");
        this.gameEntity.setTitle("Title");
        this.gameEntity.setGenres(Set.of(new GenreEntity("ACTION", "Action"),
                                         new GenreEntity("HORROR", "Horror"),
                                         new GenreEntity("ADVENTURE", "Adventure")));
        this.productViewModel = new ProductViewModel();
        this.productViewModel.setTitle("View Title");
        this.productViewModel.setGenres(new String[] { "ACTION", "HORROR"});
    }

    @Test
    void testConverters_shouldConvertToViewModelCorrectly() {
        ProductServiceModel productServiceModel = this.modelParser.convert(this.gameEntity, ProductServiceModel.class);

        assertThat(productServiceModel.getId()).isEqualTo("1");
        assertThat(productServiceModel.getGenres().length).isEqualTo(3);
        assertThat(productServiceModel.getTitle()).isEqualTo("Title");

        ProductViewModel productViewModel = this.modelParser.convert(productServiceModel, ProductViewModel.class);

        assertThat(productViewModel.getId()).isEqualTo("1");
        assertThat(productViewModel.getGenres().length).isEqualTo(3);
        assertThat(productServiceModel.getTitle()).isEqualTo("Title");
    }

    @Test
    void testConverters_shouldConvertToToEntityCorrectly() {
        Product product = this.modelParser.convert(this.productViewModel, Product.class);

        assertThat(product.getTitle()).isEqualTo("View Title");
        assertThat(product.getGenres().size()).isEqualTo(2);
    }

    @Test
    void testConverters_shouldConvertPlatformCorrectly() {
        GameEntity gameEntity = new GameEntity();
        gameEntity.setPlatforms(Set.of(new PlatformEntity("PC", "Pc")));

        GameServiceModel gameServiceModel = this.modelParser.convert(gameEntity, GameServiceModel.class);

        assertThat(gameServiceModel.getPlatforms().length).isEqualTo(1);
    }

    @Test
    void testConverters_shouldConvertMediaTypesCorrectly() {
        MovieEntity movieEntity = new MovieEntity();
        movieEntity.setMediaTypes(Set.of(new MediaEntity("BLU_RAY", "Blu-Ray")));

        MovieServiceModel movieServiceModel = this.modelParser.convert(movieEntity, MovieServiceModel.class);

        assertThat(movieServiceModel.getMediaTypes().length).isEqualTo(1);
    }
}
