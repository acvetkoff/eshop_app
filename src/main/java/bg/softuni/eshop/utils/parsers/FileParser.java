package bg.softuni.eshop.utils.parsers;

import java.io.IOException;
import java.util.Collection;

public interface FileParser {

    <T> T read(String file, Class<T> objectClass) throws IOException;
}
