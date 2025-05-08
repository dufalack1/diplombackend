package by.grsu.eventlink.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "join_requests")
public class JoinRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "is_invited", nullable = false)
    private Boolean isInvited;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY,
            cascade = { CascadeType.DETACH , CascadeType.MERGE })
    @JoinColumn(name = "users_id")
    private User invited;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY,
            cascade = { CascadeType.DETACH , CascadeType.MERGE })
    @JoinColumn(name = "nodes_id")
    private Node node;

}
