package com.tony.quartzbatchtemplate.batch.job3Jpa;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Person2 {
   @Id
   @GeneratedValue(strategy = GenerationType.AUTO)
   private Long id;

   private String lastName;
   private String firstName;
}
