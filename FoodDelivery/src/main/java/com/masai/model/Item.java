package com.masai.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Entity
public class Item {
      @Id
	 @JsonProperty(access = Access.READ_ONLY)
	    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "itemGenrator")
	    @SequenceGenerator(name = "itemGenrator",sequenceName = "itemgen",allocationSize = 1,initialValue = 11)
		private Integer itemId;
	 
	 	@NotNull(message="Item Name Cannot be Null")
	 	@NotBlank(message="Item Name Cannot be Blank")
	 	@NotEmpty(message="Item Name Cannot be Empty")
		private String itemName;
		
	 	
	 	@NotNull(message="Category Cannot be Null")
		@ManyToOne(cascade = CascadeType.ALL)
		private Category category;
		
		@NotNull(message="Quantity Cannot be Null")
		private Integer quantity;
		
		@NotNull
		private Double cost;
		
		@JsonIgnore
		@ManyToOne(cascade =CascadeType.ALL)
		private Restaurant restaurant;

}
