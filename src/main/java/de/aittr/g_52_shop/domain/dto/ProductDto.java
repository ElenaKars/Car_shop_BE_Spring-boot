package de.aittr.g_52_shop.domain.dto;
import jakarta.persistence.Column;

import java.math.BigDecimal;
import java.util.Objects;

public class ProductDto {
    private Long id;
    private String title;
    private BigDecimal price;
    private String image;

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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProductDto that = (ProductDto) o;
        return Objects.equals(id, that.id) && Objects.equals(title, that.title) && Objects.equals(price, that.price) && Objects.equals(image, that.image);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(id);
        result = 31 * result + Objects.hashCode(title);
        result = 31 * result + Objects.hashCode(price);
        result = 31 * result + Objects.hashCode(image);
        return result;
    }

    @Override
    public String toString() {
        return String.format("Product: id - %d, title - %s, price - %.2f", id, title, price);
    }
}
