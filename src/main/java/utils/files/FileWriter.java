package utils.files;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import managers.ProductManager;
import models.Product;

public class FileWriter extends AbstractFile implements Writeable<Product> {

    @Override
    public void write(ProductManager<Product> productManager) throws FileNotFoundException, IOException {
        fileToRead = new File(pathToCurrentDirectory + "\\" + filename);
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        String jsonData = mapper.writeValueAsString(productManager.getCollection());
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(fileToRead);
            fileOutputStream.write(jsonData.getBytes(StandardCharsets.UTF_8));
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            System.out.println("Файла не существует :(");
        }
    }

}
