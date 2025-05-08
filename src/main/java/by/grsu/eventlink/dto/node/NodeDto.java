package by.grsu.eventlink.dto.node;

import by.grsu.eventlink.dto.comment.CommentDto;
import by.grsu.eventlink.dto.subcategory.SubCategoryDto;
import by.grsu.eventlink.dto.user.TruncatedUserDto;
import by.grsu.eventlink.dto.user.UserDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.sql.Date;
import java.util.List;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class NodeDto {

    private Long id;

    private Long ownerId;

    @NotBlank(message = "Title must be filled")
    private String title;

    @NotBlank(message = "Place must be filled")
    private String place;

    private UserDto owner;

    @NotBlank(message = "Start date must be filled")
    private Date startDate;

    @Min(value = 18, message = "Age should not be less than 18")
    @Max(value = 150, message = "Age should not be greater than 150")
    private Integer ageLimit;

    private Long subCategoryId;

    @NotNull(message = "Description must be filled")
    private String description;

    @Min(value = 2, message = "Required people should not be less than 2")
    @Max(value = 20, message = "Required people should not be greater than 20")
    private Integer requiredPeople;

    private List<CommentDto> comments;

    private SubCategoryDto subCategory;

    private List<TruncatedUserDto> joinedPeople;

    private List<TruncatedUserDto> joinRequests;

}


