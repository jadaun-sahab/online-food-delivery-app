package com.masai.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Suggestion {

	@NotBlank(message = "Item name is required")
	@NotNull(message = "Item name is required")
	@NotEmpty(message = "Item name is required")
	private String itemName;
	
	private String descripton;
}


