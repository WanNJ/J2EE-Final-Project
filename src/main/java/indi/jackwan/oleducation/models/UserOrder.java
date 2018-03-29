package indi.jackwan.oleducation.models;

import indi.jackwan.oleducation.utils.Enums.OrderStatus;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;


/**
 * "ORDER" is an reserved word in SQL. User "UserOrder" to escape it.
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
public class UserOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @CreatedDate
    private Date createDate;
    @LastModifiedDate
    private Date updateDate;
    @Column(nullable = false)
    private OrderStatus status;
    private double actualPrice;
    @Column(nullable = false)
    private boolean paidToOrg;
    @Column(nullable = false)
    private int studentNumber;

    @ManyToOne
    private User user;

    @ManyToOne
    private Course course;

    @ManyToOne
    private Class aClass;

    @ManyToOne
    private Organization organization;

    public int getId() { return id; }

    public void setId(int id) {
        this.id = id;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public double getActualPrice() {
        return actualPrice;
    }

    public void setActualPrice(double actualPrice) {
        this.actualPrice = actualPrice;
    }

    public boolean isPaidToOrg() {
        return paidToOrg;
    }

    public void setPaidToOrg(boolean paidToOrg) {
        this.paidToOrg = paidToOrg;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Class getaClass() {
        return aClass;
    }

    public void setaClass(Class aClass) {
        this.aClass = aClass;
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    public int getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(int studentNumber) {
        this.studentNumber = studentNumber;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }
}
