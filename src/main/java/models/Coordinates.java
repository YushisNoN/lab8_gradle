package models;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
@Entity

@Table(name = "coordinates")
public class Coordinates implements Comparable<Coordinates>, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "coordinates_id_seq")
    @SequenceGenerator(name="coordinates_id_seq", sequenceName = "coordinates_id_seq", allocationSize = 1)
    @Column(name="id")
    private long id; // Значение поля должно быть больше 0, Значение этого поля должно быть //
    // уникальным, Значение этого поля должно генерироваться автоматически
    @NotNull(message = "Не может быть переменная Х быть null")
    @Min(value = -851, message = "Не может быть меньше -851 значение Х")
    @JsonProperty("x")
    @Column(name = "x", nullable = false)
    private Long x; // Значение поля должно быть больше -852, Поле не может быть null

    @NotNull(message = "Не может быть переменная Y быть null")
    @JsonProperty("y")
    @Column(name = "y", nullable = false)
    private Integer y; // Поле не может быть null


    public void setX(Long newValueX) throws NullValueException, CoordinateWrongValueException {
        if (null == newValueX) {
            throw new NullValueException();
        }
        if (newValueX <= -852) {
            throw new CoordinateWrongValueException();
        }
        this.x = newValueX;
    }

    public void setY(Integer newValueY) throws NullValueException {
        if (null == newValueY) {
            throw new NullValueException();
        }
        this.y = newValueY;
    }
    public Long getX() {
        return this.x;
    }

    public Integer getY() {
        return this.y;
    }

    @Override
    public String toString() {
        return "Coordinates{" +
                "x=" + this.x +
                ", y=" + this.y +
                "}";
    }

    @Override
    public int compareTo(Coordinates other) {
        int xComparison = this.x.compareTo(other.x);
        if (xComparison != 0) {
            return xComparison;
        }
        return this.y.compareTo(other.y);
    }
}