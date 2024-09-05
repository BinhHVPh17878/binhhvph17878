package com.vubq.joyboystore.entities;

import com.vubq.joyboystore.enums.ERole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;
import org.hibernate.annotations.UuidGenerator;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldNameConstants
@Entity
@Table(name = "roles")
public class Role {

    @Id
    @UuidGenerator
    @Column(name = "id")
    private String id;

    @Enumerated(EnumType.STRING)
    @Column(name = "name")
    private ERole name;

    @Column(name = "description")
    private String description;
}
