package utils.files;


import managers.ProductManager;

import java.io.FileNotFoundException;
import java.io.IOException;


public interface Writeable<T> {

    public void write(ProductManager<T> productManager) throws FileNotFoundException, IOException;
}
