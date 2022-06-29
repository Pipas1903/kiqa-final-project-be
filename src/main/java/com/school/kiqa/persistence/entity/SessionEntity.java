package com.school.kiqa.persistence.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

//TODO: criar esta tabela e coisas necessárias para gerir sessões
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Builder
@Table(name = "session")
public class SessionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "session_id", nullable = false)
    private Long id;

    @Column(nullable = false)
    private String tokenUuid;

    @OneToMany(
            mappedBy = "session",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL
    )
    private List<OrderEntity> orders;

}
