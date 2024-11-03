package com.example.eLibrary.entity.book;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "book_images")
public class BookImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @Column(name = "image_name", unique = true)
    private String imageName;

    @NotNull
    @Column(name = "value", columnDefinition = "LONGBLOB", unique = true)
    @Lob
    private byte[] value;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "book_id")
    @JsonManagedReference
    private Book book;
}
