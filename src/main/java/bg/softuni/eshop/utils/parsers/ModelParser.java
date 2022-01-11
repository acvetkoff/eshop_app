package bg.softuni.eshop.utils.parsers;

import java.util.List;

public interface ModelParser {

    <S, D> D convert(S source, Class<D> destination);

    <S, D> void convert(S source, D dest);

    <S, D> List<D> convert(List<S> sourceList, Class<D> destination);

    //ТОДО: fix this
    //void setGenreRepository(GenreRepository repository);
}
