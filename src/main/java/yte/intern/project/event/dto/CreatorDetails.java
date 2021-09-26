package yte.intern.project.event.dto;

import yte.intern.project.user.enums.Departments;

public class CreatorDetails {
    private final String name;
    private final Departments department;
    private final String image;

    public CreatorDetails(String name, Departments department, String image) {
        this.name = name;
        this.department = department;
        this.image = image;
    }
}
