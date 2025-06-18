package models;


import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "user_product")
public class UserProducts implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_product_id_seq")
    @SequenceGenerator(name = "user_product_id_seq", sequenceName = "user_product_id_seq", allocationSize = 1)
    private long id;

    @Column(name = "user_id", nullable = false)
    private long userId;

    @Column(name = "product_id", nullable = false)
    private long productId;

    public UserProducts() {}

    public long getUserId() {
        return this.userId;
    }

    public long getProductId() {
        return this.productId;
    }

    public void setUserId(long id) {
        this.userId = id;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }
}
