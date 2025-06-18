package transfer;

import models.Product;
import models.UserProducts;
import java.io.Serializable;
import java.util.List;
import java.util.TreeSet;

public class Response  implements Serializable {
    private String response = null;
    private Status status = null;

    private String command = null;
    private List<UserProducts> userProducts = null;

    private TreeSet<Product> collection = null;

    public TreeSet<Product> getCollection() {return this.collection;}

    public void setCollection(TreeSet<Product> coll) {
        this.collection = coll;
    }
    public void setUserProducts(List<UserProducts> userProducts) {this.userProducts = userProducts;}
    public List<UserProducts> getUserProducts() {return this.userProducts;}
    public Status getStatus() {
        return this.status;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getCommand() {
        return this.command;
    }
    public String getResponse() {
        return this.response;
    }
}
