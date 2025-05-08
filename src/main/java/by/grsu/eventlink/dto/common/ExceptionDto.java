package by.grsu.eventlink.dto.common;

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
public class ExceptionDto {

    private String message;

    @Builder.Default
    private String timestamp = Timestamp.from(Instant.now()).toString();

}
