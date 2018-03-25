package indi.jackwan.oleducation.models;

import javax.persistence.*;

@Entity
public class OrgInfoChangeApplication {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @Column(nullable = false)
    private String orgCode;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String location;
    @Column(nullable = false)
    private int teacherNum;
    private boolean approved;
    private boolean declined;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getTeacherNum() {
        return teacherNum;
    }

    public void setTeacherNum(int teacherNum) {
        this.teacherNum = teacherNum;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    public boolean isDeclined() {
        return declined;
    }

    public void setDeclined(boolean declined) {
        this.declined = declined;
    }
}
