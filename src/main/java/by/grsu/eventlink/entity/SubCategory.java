package by.grsu.eventlink.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "sub_categories")
public class SubCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "short_description")
    private String shortDescription;

    @Column(name = "image_url")
    private String imageUrl;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY,
            cascade = CascadeType.DETACH)
    @JoinColumn(name = "categories_id")
    private Category categoryOwner;

    @OneToMany(
            fetch = FetchType.LAZY,
            mappedBy = "subCategory",
            cascade = CascadeType.REMOVE,
            orphanRemoval = true
    )
    private List<Node> nodes;


}