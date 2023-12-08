package com.example.citytoday.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.Size;

@Entity
@Table(name = "news")
@Getter
@Setter
public class News {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    @JoinColumn(name = "image_id")
    private Image image;
    @Column(name = "text")
    @Size(message = "Не менее 10 символов", min = 10)
    private String text;
    @Column(name = "description")
    @Size(message = "Не менее 50 символов", min = 50)
    private String description;
    @Column(name = "status")
    private String status;
    @Column(name = "datetime")
    private String dateTime;
    @Column(name = "count_of_guests")
    private Long count;
}
