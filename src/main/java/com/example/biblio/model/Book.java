package com.example.biblio.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    @Column(name = "title", nullable = false, length = 90)
    String title;

    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "\"yyyy-MM-dd\"")
    @Column(name = "releaseTime", nullable = false, length = 40)
    Date releaseTime;

    @JsonBackReference
    @OneToMany(mappedBy = "book")
    List<Borrowed> borroweds;

    @JsonIgnore
    Boolean access;

}