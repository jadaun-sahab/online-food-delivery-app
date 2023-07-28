package com.masai.model;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Date {
	
	@JsonFormat(pattern = "yyyy-MM-dd")
	@NotNull
	private LocalDate startDate;
	
	@JsonFormat(pattern = "yyyy-MM-dd")
	@NotNull
	private LocalDate endDate;

}
