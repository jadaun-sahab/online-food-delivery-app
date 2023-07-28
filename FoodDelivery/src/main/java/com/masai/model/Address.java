package com.masai.model;

import jakarta.validation.constraints.Min;
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
public class Address {
	@NotNull(message = "House number is required")
	@Min(value = 1, message = "Invalid House No")
	private Integer HouseNO;
	
	@NotNull(message = "Street name can not null")
	@NotBlank(message = "Street name can not blank")
	@NotEmpty(message = "Street name can not empty")
	@Size(min = 5, max = 20, message = "Street name should be of minimum 5 and maximum 20 characters")
	private String streetName;

	@NotNull(message = "Area can not null")
	@NotBlank(message = "Area can not blank")
	@NotEmpty(message = "Area can not empty")
	@Size(min = 2, max = 20, message = "Area should be of minimum 2 and maximum 20 characters")
	private String area;
	
	@NotNull(message = "City is required")
	@NotBlank(message = "City can not blank")
	@NotEmpty(message = "City can not empty")
	@Size(min = 2, max = 20, message = "City should be of minimum 2 and maximum 20 characters")
	private String city;
	
	@NotNull(message = "State is required")
	@NotBlank(message = "State can not blank")
	@NotEmpty(message = "State can not empty")
	@Size(min = 5, max = 20, message = "State should be of minimum 5 and maximum 20 characters")
	private String state;
	
	@NotNull(message = "Country is required")
	@NotBlank(message = "Country can not blank")
	@NotEmpty(message = "Country can not empty")
	private String country;
	
	@NotNull(message = "Pincode is required")
	@NotBlank(message = "Pincode can not blank")
	@NotEmpty(message = "Pincode can not empty")
	@Size(min = 6,max = 6,message = "Pincode Should be 6 Digits")
	private String pincode;
}
