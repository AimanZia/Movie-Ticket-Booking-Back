package User.entities.DTOs;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestDTO {
    @NotBlank(message="Name Should Not be empty")
    private String name;
    @NotBlank(message = "Email should not be empty")
    private String email;
}
