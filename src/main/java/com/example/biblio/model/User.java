package com.example.biblio.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Entity
@NoArgsConstructor
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = {
                "username", "email"
        })})
@TypeDef(
        name = "jsonb",
        typeClass = JsonBinaryType.class
)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "name", nullable = false, length = 40)
    String name;

    @Size(min = 4, max = 50, message = "Minimum username length: 4 characters")
    @Column(name = "username", nullable = false)
    String username;

    @Email
    @Column(name = "email", nullable = false, length = 50)
    String email;

    @Column(name = "password", nullable = false)
    String password;

    @Type( type = "jsonb" )
    @Column(columnDefinition = "jsonb")
    Set<Role> roles = new HashSet<>();

    @JsonBackReference
    @OneToMany(mappedBy = "user")
    List<Borrowed> borroweds;

}


