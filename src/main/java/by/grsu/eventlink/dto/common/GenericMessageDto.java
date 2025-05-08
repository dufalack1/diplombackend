package by.grsu.eventlink.dto.common;

import by.grsu.eventlink.dto.enums.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GenericMessageDto {

    private Long entityId;

    private String message;

    private Entity entityType;

    @Builder.Default
    private String timestamp = Timestamp.from(Instant.now()).toString();

}
