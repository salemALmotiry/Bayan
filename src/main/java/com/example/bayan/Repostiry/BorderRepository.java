package com.example.bayan.Repostiry;

import com.example.bayan.Model.Border;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BorderRepository extends JpaRepository<Border, Integer> {

    Border findBorderByName(String name);
}
