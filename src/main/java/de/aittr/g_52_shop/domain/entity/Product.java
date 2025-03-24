package de.aittr.g_52_shop.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.Objects;

/*
Эта аннотация сообщает Спрингу о том, что перед нами энтити-сущность,
то есть такая сущность, для которой существует таблица в БД.
И надо объекты этого класса сопоставлять с БД.
 */
@Entity
/*
Эта аннотация сообщает Спрингу, в какой таблице в БД лежат продукты.
 */
@Table(name = "product")
public class Product {
    /*
@Id - указываем, что именно это поле является идентификатором
@GeneratedValue - указываем, что генерацией идентификаторов занимается сама БД
@Column - указываем, в какой именно колонке таблицы лежат значения этого поля
 */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    // 1. The title must be > 3 symbols
    // 2. No numbers, and special symbols
    // 3. 1st letter is capital
    // 4. the rest of letters are in the low register
    @Column(name = "title")
    @NotNull(message = "Product title cannot be null")
    @NotBlank(message = "Product title cannot be empty")
    @Pattern(
            regexp = "[A-Z][a-z ]{2,}",
            message = "Product title should be at least 3 characters length and start with the Capital letter"
    )
    private String title;

    @Column(name = "price")
    @DecimalMin(
            value = "1.00",
            message = "Product price should be greater or equal than 1"
    )
    @DecimalMax(
            value = "1000.00",
            inclusive = false,
            message = "Product price should be less than 1000"
    )
    private BigDecimal price;
    @Column(name = "active")
    private boolean active;

    @Column(name = "image")
    private String image;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Product() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Product product = (Product) o;
        return active == product.active && Objects.equals(id, product.id) && Objects.equals(title, product.title) && Objects.equals(price, product.price) && Objects.equals(image, product.image);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(id);
        result = 31 * result + Objects.hashCode(title);
        result = 31 * result + Objects.hashCode(price);
        result = 31 * result + Boolean.hashCode(active);
        result = 31 * result + Objects.hashCode(image);
        return result;
    }

    @Override
    public String toString() {
        return String.format("Product: id - %d, title - %s, price - %.2f, active - %s", id, title, price, active ? "yes" : "no");
    }
}
