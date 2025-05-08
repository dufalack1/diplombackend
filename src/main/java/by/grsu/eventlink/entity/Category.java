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
@Table(name = "categories")
public class Category {

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
    @OneToMany(mappedBy = "categoryOwner",cascade = CascadeType.ALL)
    private List<SubCategory> subCategories;

    @ToString.Exclude
    @ManyToMany(fetch = FetchType.LAZY,
        cascade = CascadeType.DETACH)
    @JoinTable(name = "users_categories",
        joinColumns = @JoinColumn(name = "categories_id"),
        inverseJoinColumns = @JoinColumn(name = "users_id"))
    private List<User> subscribers;

}
