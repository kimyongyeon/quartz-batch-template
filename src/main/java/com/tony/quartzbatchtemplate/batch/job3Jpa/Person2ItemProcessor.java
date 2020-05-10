package com.tony.quartzbatchtemplate.batch.job3Jpa;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;

@Slf4j
public class Person2ItemProcessor implements ItemProcessor<Person2, Person2> {

   @Override
   public Person2 process(Person2 person2) throws Exception {

      final String firstName = person2.getFirstName().toUpperCase();
      final String lastName = person2.getLastName().toUpperCase();

      final Person2 transformedPerson = new Person2(0L, firstName, lastName);

      log.info("Converting (" + person2 + ") into (" + transformedPerson + ")");

      return transformedPerson;
   }
}
