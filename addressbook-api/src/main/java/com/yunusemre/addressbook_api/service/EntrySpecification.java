package com.yunusemre.addressbook_api.service;

import com.yunusemre.addressbook_api.model.Company;
import com.yunusemre.addressbook_api.model.Entry;
import com.yunusemre.addressbook_api.model.Person;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class EntrySpecification {

    public static Specification<Entry> findByCriteria(String entryType, String searchField, String searchValue) {

        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            String lowerSearchValue = searchValue.toLowerCase();

            if (entryType.equalsIgnoreCase("person")) {
                predicates.add(criteriaBuilder.equal(root.type(), Person.class));
            } else if (entryType.equalsIgnoreCase("company")) {
                predicates.add(criteriaBuilder.equal(root.type(), Company.class));
            }

            switch (searchField.toLowerCase()) {
                case "name":
                    predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + lowerSearchValue + "%"));
                    break;
                case "phone":
                    predicates.add(criteriaBuilder.like(root.get("phoneNumber"), "%" + searchValue + "%"));
                    break;
                case "lastname":
                    predicates.add(criteriaBuilder.like(criteriaBuilder.lower(criteriaBuilder.treat(root, Person.class).get("lastName")), "%" + lowerSearchValue + "%"));
                    break;
                case "taxnumber":
                    predicates.add(criteriaBuilder.equal(criteriaBuilder.treat(root, Company.class).get("taxNumber"), searchValue));
                    break;
                case "address":
                    predicates.add(criteriaBuilder.like(criteriaBuilder.lower(criteriaBuilder.treat(root, Company.class).get("address")), "%" + lowerSearchValue + "%"));
                    break;
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
    }