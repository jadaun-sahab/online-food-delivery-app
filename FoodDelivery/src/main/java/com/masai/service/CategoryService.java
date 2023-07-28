package com.masai.service;

import java.util.List;

import com.masai.exception.CategoryException;
import com.masai.model.Item;

public interface CategoryService {
	
	public List<Item> getItemsByCategoryName(String categoryName, String pincode) throws CategoryException;
	
}
