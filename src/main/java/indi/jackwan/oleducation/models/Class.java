package indi.jackwan.oleducation.models;

import javax.persistence.*;
import java.util.List;

@Entity
public class Class {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @Column(nullable = false)
    private String teacherName;
    @Column
    private int currentStudentNumber;
    @Column(nullable = false)
    private int maxStudentNumber;
    @Column(nullable = false)
    private double price;

    @ManyToOne
    private Course course;

    @ManyToOne
    private Organization organization;

    @OneToMany(mappedBy = "aClass")
    private List<UserOrder> userOrders;

    @OneToMany(mappedBy = "aClass")
    private List<ClassSignIn> signInList;

    @OneToMany(mappedBy = "aClass")
    private List<Grade> gradeList;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public int getCurrentStudentNumber() {
        return currentStudentNumber;
    }

    public void setCurrentStudentNumber(int currentStudentNumber) {
        this.currentStudentNumber = currentStudentNumber;
    }

    public int getMaxStudentNumber() {
        return maxStudentNumber;
    }

    public void setMaxStudentNumber(int maxStudentNumber) {
        this.maxStudentNumber = maxStudentNumber;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    public List<UserOrder> getUserOrders() {
        return userOrders;
    }

    public void setUserOrders(List<UserOrder> userOrders) {
        this.userOrders = userOrders;
    }

    public List<ClassSignIn> getSignInList() {
        return signInList;
    }

    public void setSignInList(List<ClassSignIn> signInList) {
        this.signInList = signInList;
    }

    public List<Grade> getGradeList() {
        return gradeList;
    }

    public void setGradeList(List<Grade> gradeList) {
        this.gradeList = gradeList;
    }

    public boolean isAvailable(int studentNumber) {
        return currentStudentNumber + studentNumber <= maxStudentNumber;
    }
}
