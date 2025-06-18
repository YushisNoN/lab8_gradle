package utils.files;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import managers.ProductManager;
import models.Product;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;



public class FileReader extends AbstractFile implements Readable<Product> {

    private boolean isValidate = true;

    @Override
    public void read(ProductManager<Product> productManager) throws FileNotFoundException, IOException {
        String jsonData = "";
        try {
            fileToRead = new File(pathToCurrentDirectory + "\\" + filename);
            try (Scanner scanner = new Scanner(fileToRead)) {
                while (scanner.hasNext()) {
                    jsonData += scanner.next();
                }
            }
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            TreeSet<Product> collection = mapper.readValue(jsonData, new TypeReference<TreeSet<Product>>() {
            });
            for (Product product : collection) {
                validate(product);
            }
            if (isValidate) {
                productManager.setCollection(collection);
                System.out.println("Коллекция успешно считана");
            } else {
                System.out.println("Коллекция не была считана из-за проблем в json");
            }
        } catch (FileNotFoundException exception) {
            System.out.println("Данного файла не существует :(");
        } catch (Exception exception) {
            System.out.printf(exception.getMessage());
        }
    }

    public void validate(Product product) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<Product>> violations = validator.validate(product);
        if (!violations.isEmpty()) {
            for (ConstraintViolation<Product> violation : violations) {
                this.isValidate = false;
                System.out.println(
                        "Ошибка при десериализации: " + violation.getPropertyPath() + "': " + violation.getMessage());
            }
        }
    }

    public List<String> read(String filename) throws FileNotFoundException {
        File file = new File(pathToCurrentDirectory + "\\" + filename);
        if (false == file.exists()) {
            throw new FileNotFoundException();
        }
        try {
            List<String> lines = Files.readAllLines(Paths.get(file.toURI()));
            return lines;
        } catch (NullPointerException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;

    }

    public static void setFileName(String fileName) throws FileNotFoundException {
        fileToRead = new File(pathToCurrentDirectory + "\\" + fileName);
        if (false == fileToRead.exists()) {
            throw new FileNotFoundException();
        }

        filename = fileName;
    }
}
