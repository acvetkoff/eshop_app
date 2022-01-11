package bg.softuni.eshop.config;
 
import java.nio.file.Path;
import java.nio.file.Paths;

import bg.softuni.eshop.product.service.ProductConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
 
@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Autowired
    private ProductConfigurationService productConfigurationService;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        exposeDirectory(productConfigurationService.getConfiguration().getPosterPublicDirectory(), productConfigurationService.getConfiguration().getPosterPrivateDirectory(), registry);
    }
     
    private void exposeDirectory(String publicDirName, String privateDirName, ResourceHandlerRegistry registry) {
        String publicPath = Paths.get(publicDirName).toFile().getAbsolutePath();

        registry.addResourceHandler( privateDirName + "/**").addResourceLocations("file:"+ publicPath + "/");
    }
}