package bg.softuni.eshop.utils.io.impl;

import bg.softuni.eshop.utils.io.FileReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Objects;

public class FileReaderImpl implements FileReader {

    @Override
    public String read(String file) throws IOException {
        StringBuilder content = new StringBuilder();

        try (
                InputStream inputStream = getClass().getResourceAsStream(file);
                BufferedReader reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(inputStream)));
        ) {
            String line = reader.readLine();

            while (line != null) {
                content.append(line);

                line = reader.readLine();
            }
        }

        return content.toString();
    }
}
