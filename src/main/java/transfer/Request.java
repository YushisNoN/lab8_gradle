package transfer;

import models.Product;
import models.User;
import models.UserProducts;

import java.io.Serializable;
import java.util.List;

public class Request  implements Serializable {
    private String command = null;
    private Product product = null;
    private User user = null;
    private List<UserProducts> userProducts = null;

    public List<UserProducts> getUserProducts() {return this.userProducts;}
    public void setUserProducts(List<UserProducts> userProducts) {this.userProducts = userProducts;}

    public void setCommand(String command) {
        this.command = command;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getCommand() {
        return this.command;
    }

    public User getUser() {
        return this.user;
    }

    public Product getProduct() {
        return this.product;
    }
}

