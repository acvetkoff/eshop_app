package bg.softuni.eshop.utils.parsers.impl;


import bg.softuni.eshop.utils.io.FileReader;
import bg.softuni.eshop.utils.parsers.FileParser;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.Collection;

public class FileJsonParser implements FileParser {

    private final Gson gson;
    private final FileReader fileReader;

    @Autowired
    public FileJsonParser(Gson gson, FileReader fileReader) {
        this.gson = gson;
        this.fileReader = fileReader;
    }

    @Override
    public <T> T read(String filePath, Class<T> objectClass) throws IOException {
        String jsonContent = this.fileReader.read(filePath);
        T object = this.gson.fromJson(jsonContent, objectClass);
        return object;
    }
}
