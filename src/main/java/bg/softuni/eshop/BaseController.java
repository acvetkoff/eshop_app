package bg.softuni.eshop;

import bg.softuni.eshop.utils.parsers.ModelParser;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

public abstract class BaseController {

    protected final ModelParser modelParser;

    @Autowired
    protected BaseController(ModelParser modelParser) {
        this.modelParser = modelParser;
    }


    protected String view(String viewName) {
        return viewName;
    }

    protected String redirect(String path, String... args) {
        return String.format(redirect(path), args);
    }

    protected String redirect(String path) {
        return "redirect:" + path;
    }

    protected <S, D> List<D> map(List<S> sourceList, Class<D> destination) {
        return sourceList
                .stream()
                .map(source -> this.map(source, destination))
                .collect(Collectors.toList());
    }

    protected <S, D> D map(S source, Class<D> destination) {
        return modelParser.convert(source, destination);
    }

    protected <S, D> void map(S source, D dest) {
        modelParser.convert(source, dest);
    }

}
