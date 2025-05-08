package by.grsu.eventlink.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "message")
    private String message;

    @Column(name = "publication_time")
    private LocalDateTime publicationTime;

//    @ManyToOne(fetch = FetchType.LAZY,
//        cascade = CascadeType.ALL)
//    @JoinColumn(name = "users_id")
//    private User author;
//
//    @ManyToOne(fetch = FetchType.LAZY,
//        cascade = CascadeType.ALL)
//    @JoinColumn(name = "nodes_id")
//    private Node node;
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "users_id")
private User author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nodes_id")
    private Node node;

}
