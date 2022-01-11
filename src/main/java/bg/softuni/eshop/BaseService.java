package bg.softuni.eshop;

import bg.softuni.eshop.utils.parsers.ModelParser;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

public abstract class BaseService {

    private final ModelParser modelParser;

    @Autowired
    protected BaseService(ModelParser modelParser) {
        this.modelParser = modelParser;
    }

    protected <S, D> List<D> map(List<S> sourceList, Class<D> destination) {
        return sourceList.stream().map(source -> this.map(source, destination)).collect(Collectors.toList());
    }

    protected <S, D> D map(S source, Class<D> destination) {
        return this.modelParser.convert(source, destination);
    }

    protected <S, D> void map(S source, D dest) {
        this.modelParser.convert(source, dest);
    }
}
