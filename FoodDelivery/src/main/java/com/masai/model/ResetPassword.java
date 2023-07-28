package com.masai.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResetPassword{
	@NotNull(message = "Current password can not be null")
	@NotBlank(message = "Current password can not be blank")
	@NotEmpty(message = "Current password can not be empty")
	@Size(min = 8, max = 20, message = "Current password length should be 8 to 20")
	private String currentPassword;
	
	@NotNull(message = "New password can not be null")
	@NotBlank(message = "New password can not be blank")
	@NotEmpty(message = "New password can not be empty")
	@Size(min = 8, max = 20, message = "New password length should be 8 to 20")
	private String newPassword;

}
