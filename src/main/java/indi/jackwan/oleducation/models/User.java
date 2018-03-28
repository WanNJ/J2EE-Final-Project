package indi.jackwan.oleducation.models;

import org.springframework.data.annotation.Transient;

import javax.persistence.*;
import java.util.List;

// This tells Hibernate to make a table out of this class.
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private String nickname;
    @Transient
    private String password;
    private boolean enabled;
    private boolean isVip;
    private String confirmationToken;

    @OneToMany(mappedBy = "user")
    private List<UserOrder> userOrders;

    public Integer getId() { return id; }

    public void setId(int id) { this.id = id; }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickname() { return nickname; }

    public void setNickname(String nickname) { this.nickname = nickname; }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean value) {
        this.enabled = value;
    }

    public String getConfirmationToken() {
        return confirmationToken;
    }

    public void setConfirmationToken(String confirmationToken) {
        this.confirmationToken = confirmationToken;
    }

    public boolean isVip() { return isVip; }

    public void setVip(boolean vip) { isVip = vip; }

    public List<UserOrder> getUserOrders() { return userOrders; }

    public void setUserOrders(List<UserOrder> userOrders) { this.userOrders = userOrders; }
}