package com.incidentmanager.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.incidentmanager.enums.Priority;
import com.incidentmanager.enums.Status;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
    name = "incident",
    indexes = {
        @Index(
            name = "idx_incident_status",
            columnList = "status"
        ),
        @Index(
            name = "idx_incident_priority",
            columnList = "priority"
        )
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Incident {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 120)
    private String titulo;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private Priority priority;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 15)
    private Status status;

    @ManyToOne
    @JoinColumn(name = "autor_id", nullable = false)
    private User autor;

    @Column(name = "responsavel_email", nullable = false, length = 255)
    private String responsavelEmail;

    @ElementCollection
    @CollectionTable(
        name = "incident_tags",
        joinColumns = @JoinColumn(name = "incident_id")
    )
    @Column(name = "tag")
    private List<String> tags = new ArrayList<>();

    @Column(name = "data_abertura", nullable = false)
    private LocalDateTime dataAbertura;

    @Column(name = "data_atualizacao", nullable = false)
    private LocalDateTime dataAtualizacao;

    @OneToMany(
        mappedBy = "incident",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    private List<Comment> comments = new ArrayList<>();

}