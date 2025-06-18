package utils.files;

import managers.ProductManager;

import java.io.FileNotFoundException;
import java.io.IOException;

public interface Readable<T> {

    public void read(ProductManager<T> productManager) throws FileNotFoundException, IOException;
}
