package by.grsu.eventlink.dto.user;

import by.grsu.eventlink.dto.category.TruncatedCategoryDto;
import by.grsu.eventlink.entity.enums.RoleHelper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private Long id;

    private Date birthDate;

    private String avatarUrl;

    private String phoneNumber;

    private String displayedName;

    private List<RoleHelper> roles;

    private List<TruncatedCategoryDto> likedCategories;
    private int likesCount;

}
