package by.grsu.eventlink.dto.comment;

import by.grsu.eventlink.dto.user.TruncatedUserDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {

    private Long id;

    private Long nodeId;

    private Long authorId;

    private String message;

    private TruncatedUserDto author;

    private LocalDateTime publicationTime;

}
