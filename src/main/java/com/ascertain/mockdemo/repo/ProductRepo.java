package com.ascertain.mockdemo.repo;

import com.ascertain.mockdemo.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepo extends JpaRepository<Product,Integer> {
}