package com.example.biblio.model;

import com.example.biblio.model.token.RefreshToken;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import com.vladmihalcea.hibernate.type.json.JsonNodeBinaryType;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

@Data
@Entity
@NoArgsConstructor
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = {
                "username", "email"
        })})
@TypeDef(
        name = "jsonb-node",
        typeClass = JsonNodeBinaryType.class
)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "name", nullable = false, length = 40)
    String name;

    @Size(min = 4, max = 60, message = "Minimum username length: 4 characters")
    @Column(name = "username", nullable = false)
    String username;

    @Email
    @Column(name = "email", nullable = false, length = 100)
    String email;

    @Column(name = "password", nullable = false)
    String password;

    @Type( type = "jsonb-node" )
    @Column(columnDefinition = "jsonb")
    JsonNode role;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL,
            fetch = FetchType.LAZY, optional = false)
    @JsonIgnore
    RefreshToken refreshToken;

}

