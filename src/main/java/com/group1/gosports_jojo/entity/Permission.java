package com.group1.gosports_jojo.entity;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name="permissions")
public class Permission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="permission_id")
    private Integer permissionId;

    @OneToMany(mappedBy = "permission",cascade = CascadeType.ALL)
    @OrderBy("administratorId asc")
    private Set<Administrator> administrator;

    @Column(name="permission_name",nullable = false, unique = true)
    private String permissionName;
    @Column(name="permission_desc")
    private String permissionDesc;

    public Integer getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(Integer permissionId) {
        this.permissionId = permissionId;
    }

    public Set<Administrator> getAdministrator() {
        return administrator;
    }

    public void setAdministrator(Set<Administrator> administrator) {
        this.administrator = administrator;
    }

    public String getPermissionName() {
        return permissionName;
    }

    public void setPermissionName(String permissionName) {
        this.permissionName = permissionName;
    }

    public String getPermissionDesc() {
        return permissionDesc;
    }

    public void setPermissionDesc(String permissionDesc) {
        this.permissionDesc = permissionDesc;
    }

    @Override
    public String toString() {
        return "Permission{" +
                "permissionId=" + permissionId +
                ", administrator=" + administrator +
                ", permissionName='" + permissionName + '\'' +
                ", permissionDesc='" + permissionDesc + '\'' +
                '}';
    }
}
