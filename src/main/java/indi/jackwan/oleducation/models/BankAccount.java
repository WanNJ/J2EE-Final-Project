package indi.jackwan.oleducation.models;

import javax.persistence.*;
import java.util.List;

@Entity
public class BankAccount {
    @Id
    @GeneratedValue
    private int id;
    @Column(nullable = false)
    private String accountAddress;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private double balence;

    @OneToMany(mappedBy = "bankAccount")
    private List<UserOrder> order;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAccountAddress() {
        return accountAddress;
    }

    public void setAccountAddress(String accountAddress) {
        this.accountAddress = accountAddress;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public double getBalence() {
        return balence;
    }

    public void setBalence(double balence) {
        this.balence = balence;
    }

    public List<UserOrder> getOrder() {
        return order;
    }

    public void setOrder(List<UserOrder> order) {
        this.order = order;
    }

    public void pay(double amount) {
        balence = balence - amount;
    }

    public void receive(double amount) {
        balence = balence + amount;
    }
}
