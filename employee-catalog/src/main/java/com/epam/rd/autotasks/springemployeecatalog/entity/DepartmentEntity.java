package com.epam.rd.autotasks.springemployeecatalog.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "DEPARTMENT")
public class DepartmentEntity {

    @Id
    @Column(name = "ID")
    private Long departmentId;

    @Column(name = "NAME")
    private String departmentName;

    @Column(name = "LOCATION")
    private String location;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "department")
    private List<EmployeeEntity> employeeEntities;

    public Long getDepartmentId() {
        return departmentId;
    }


    public String getDepartmentName() {
        return departmentName;
    }


    public String getLocation() {
        return location;
    }

}
