package me.kolganov.taskmanager.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Date;

@Data
@Entity(name = "PROJECT_TASKS")
public class ProjectTask {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "PROJECT_SEQUENCE", updatable = false, unique = true)
    private String projectSequence;

    @NotBlank(message = "Please include a project summary")
    @Column(name = "SUMMARY")
    private String summary;

    @Column(name = "ACCEPTANCE_CRITERIA")
    private String acceptanceCriteria;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "PRIORITY")
    private Integer priority;

    @Column(name = "DUE_DATE")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date dueDate;

    @Column(name = "PROJECT_IDENTIFIER", updatable = false)
    private String projectIdentifier;

    @Column(name = "CREATE_AT", updatable = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createAt;

    @Column(name = "UPDATE_AT")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateAt;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "BACKLOG_ID", updatable = false, nullable = false)
    @JsonIgnore
    private Backlog backlog;

    @PrePersist
    protected void onCreate() {
        this.createAt = new Date();
    }
    @PreUpdate
    protected void onUpdate() {
        this.updateAt = new Date();
    }
}
