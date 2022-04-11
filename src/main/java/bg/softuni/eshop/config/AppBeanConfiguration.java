package bg.softuni.eshop.config;

import bg.softuni.eshop.product.model.binding.ProductBindingModel;
import bg.softuni.eshop.utils.io.FileReader;
import bg.softuni.eshop.utils.io.impl.FileReaderImpl;
import bg.softuni.eshop.utils.parsers.FileParser;
import bg.softuni.eshop.utils.parsers.impl.FileJsonParser;
import com.google.gson.*;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.List;

import static java.util.Locale.ENGLISH;

@Configuration

public class AppBeanConfiguration {
    private final ProductConfiguration productConfiguration;

    @Autowired
    public AppBeanConfiguration(ProductConfiguration productConfiguration) {
        this.productConfiguration = productConfiguration;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public Gson gson() {
        return this.createGson();
    }

    @Bean
    public FileReader fileReader() {
        return new FileReaderImpl();
    }

    @Bean
    public FileParser fileParser() {
        return new FileJsonParser(this.createGson(), new FileReaderImpl());
    }

    private Gson createGson() {
        return new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDate.class, new JsonDeserializer<LocalDate>() {
                    @Override
                    public LocalDate deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext)
                            throws JsonParseException {
                        DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                                .appendPattern("dd-MM-yyyy")
                                .toFormatter(ENGLISH);

                        return LocalDate.parse(jsonElement.getAsString(), formatter);
                    }
                })
                .registerTypeAdapter(ProductBindingModel.class, new JsonDeserializer<>() {
                    @SneakyThrows
                    @Override
                    public ProductBindingModel deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
                        JsonObject jsonObject = json.getAsJsonObject();
                        JsonElement jsonElement = jsonObject.get("type");
                        String productType = jsonElement.getAsString();

                        String productClassPrefix = ProductBindingModel.class.getPackageName();
                        String productClassName = productConfiguration.getProductPaths().get(productType);
                        String productSubtypeClassName = String.join(".", List.of(productClassPrefix, productClassName));
                        Class<?> productSubtype = Class.forName(productSubtypeClassName);

                        return context.deserialize(json, productSubtype);
                    }
                })
                .create();
    }
}
