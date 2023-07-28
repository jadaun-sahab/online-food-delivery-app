package com.masai.model;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Entity
public class Restaurant {

	  @Id
	  @JsonProperty(access = Access.READ_ONLY)
	    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "userGenerator")
	    @SequenceGenerator(name = "userGenerator",sequenceName = "usergen",allocationSize = 1,initialValue = 1)
		private Integer restaurantId;
		private String restaurantName;

		@Valid
		@NotNull(message = "Address can not be null")
		@Embedded
		private Address address;
		@Email
		private String email;
		
		@JsonProperty(access = Access.WRITE_ONLY)
		@NotNull(message = "Password can not be null")
		@NotBlank(message = "Password can not be blank")
		@NotEmpty(message = "Password can not be empty")
		@Size(min = 8, max = 15, message = "Password length should be 8 to 15")
		private String password;
		
		@JsonIgnore
		@OneToMany(cascade=CascadeType.ALL, mappedBy = "restaurant")
		private List<Item> items = new ArrayList<>();
		
		private String managerName;
		
		@JsonProperty(access = Access.WRITE_ONLY)
		@NotNull(message = "Mobile Number is required")
		@NotBlank(message = "Enter vaild Mobile Number")
		@Size(min = 10,max = 10,message = "Mobile Number Should Be 10 digit's")
		@Column(unique = true)
		private String mobileNumber;
		
		@JsonIgnore
		@ElementCollection
		private Set<Integer> customers = new HashSet<>();
		
		@JsonFormat(pattern = "HH:mm:ss")
		@NotNull(message = "Opening time is required")
		private LocalTime openTime;
		@JsonFormat(pattern = "HH:mm:ss")
		@NotNull(message = "Closing time is required")
		private LocalTime closeTime;
		
		@JsonIgnore
		@Embedded
		@ElementCollection
		private List<Suggestion> suggestions;

}
