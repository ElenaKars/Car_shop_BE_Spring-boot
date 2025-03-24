package de.aittr.g_52_shop.domain.entity;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "customer")
public class Customer {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "active")
    private boolean active;

    @Column(name = "name")
    private String name;

    //reference to private Customer customer in Cart class
    @OneToOne(mappedBy = "customer", cascade = CascadeType.ALL)
    private Cart cart;

    public Customer(){

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public Cart getCart() {
        return cart;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Customer customer = (Customer) o;
        return active == customer.active && Objects.equals(id, customer.id) && Objects.equals(name, customer.name) && Objects.equals(cart, customer.cart);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(id);
        result = 31 * result + Boolean.hashCode(active);
        result = 31 * result + Objects.hashCode(name);
        result = 31 * result + Objects.hashCode(cart);
        return result;
    }

    @Override
    public String toString() {
        return String.format("Customer: id - %d, name - %s, active - %s", id, name, active ? "yes" : "no");
    }

}
