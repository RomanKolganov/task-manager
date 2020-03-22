package me.kolganov.taskmanager.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.sql.Date;

@Data
@Entity
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "NAME")
    @NotBlank(message = "Project name is required")
    private String name;

    @Column(name = "IDENTIFIER", updatable = false, unique = true)
    @NotBlank(message = "Project identifier is required")
    @Size(min = 4, max = 5, message = "Please use 4 to 5 characters")
    private String identifier;

    @Column(name = "DESCRIPTION")
    @NotBlank(message = "Project description is required")
    private String description;

    @Column(name = "START_DATE")
    @JsonFormat(pattern = "dd.mm.yyyy")
    private Date startDate;

    @Column(name = "END_DATE")
    @JsonFormat(pattern = "dd.mm.yyyy")
    private Date endDate;

    @Column(name = "CREATE_AT")
    @JsonFormat(pattern = "dd.mm.yyyy")
    private Date createAt;

    @Column(name = "UPDATE_AT")
    @JsonFormat(pattern = "dd.mm.yyyy")
    private Date updateAt;
}
