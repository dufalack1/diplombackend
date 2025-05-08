package by.grsu.eventlink.entity;

import lombok.*;

import javax.persistence.*;
import java.sql.Date;
import java.util.List;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "nodes")
public class Node {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "required_people")
    private Integer requiredPeople;

    @Column(name = "place")
    private String place;

    @Column(name = "age_limit")
    private Integer ageLimit;

    @Column(name = "publish_time")
    private Date publishTime;

    @Column(name = "expiration_time")
    private Date expirationTime;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY,
            cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST })
    @JoinColumn(name = "users_id")
    private User owner;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY,
            cascade = { CascadeType.DETACH, CascadeType.MERGE })
    @JoinColumn(name = "sub_categories_id")
    private SubCategory subCategory;


    @ToString.Exclude
    @OneToMany(
            fetch = FetchType.LAZY,
            mappedBy = "node",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Comment> comments;

    @ToString.Exclude
    @ManyToMany(mappedBy = "joinedEvents",
            fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<User> joinedUsers;

    @ToString.Exclude
    @OneToMany(mappedBy = "node",
            fetch = FetchType.LAZY,cascade = CascadeType.ALL,orphanRemoval = true)
    private List<JoinRequest> joinRequests;

}