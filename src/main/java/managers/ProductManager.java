package managers;

import java.time.LocalDateTime;
import java.util.TreeSet;

public class ProductManager<T> {
    private TreeSet<T> collection;
    private final LocalDateTime initializationDate;

    public ProductManager() {
        this.collection = new TreeSet<>();
        this.initializationDate = LocalDateTime.now().withNano(0);
    }

    public TreeSet<T> getCollection() {
        return this.collection;
    }

    public void setCollection(TreeSet<T> productCollection) {
        this.collection = productCollection;
    }

    public void addProdut(T product) {
        this.collection.add(product);
    }

    @Override
    public String toString() {
        return "TreeSet";
    }

    public LocalDateTime getInitTime() {
        return this.initializationDate;
    }
}
