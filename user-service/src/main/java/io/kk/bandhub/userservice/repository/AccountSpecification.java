package io.kk.bandhub.userservice.repository;

import io.kk.bandhub.userservice.model.Account;
import io.kk.bandhub.userservice.model.AccountRole;
import io.kk.bandhub.userservice.model.Role;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.List;

public class AccountSpecification {

    public static Specification<Account> hasEmail(String email) {
        return (root, query, builder) -> {
            if (email == null || email.isEmpty()){
                return builder.conjunction();
            }
            return builder.equal(root.get("email"), email);
        };
    }

    public static Specification<Account> hasName(String name) {
        return (root, query, builder) -> {
            if (name == null || name.isEmpty()) {
                return builder.conjunction();
            }

            return builder.or(
                    builder.equal(root.get("firstName"), name),
                    builder.equal(root.get("lastName"), name)
            );
        };
    }

    public static Specification<Account> hasFullName(String firstName, String lastName) {
        return (root, query, builder) -> {
            if ((firstName == null || firstName.isEmpty()) &&
                    (lastName == null || lastName.isEmpty())) {
                return builder.conjunction();
            }

            return builder.and(
                    firstName == null ? builder.conjunction() : builder.equal(root.get("firstName"), firstName),
                    lastName == null ? builder.conjunction() : builder.equal(root.get("lastName"), lastName)
            );
        };
    }

    public static Specification<Account> hasAgeBetween(Integer minAge, Integer maxAge) {
        return (root, query, builder) -> {
            if (minAge == null || minAge < 0 || maxAge == null || maxAge < 0) {
                return builder.conjunction();
            }
            LocalDate currentDate = LocalDate.now();

            LocalDate startDate = currentDate.minusYears(maxAge);
            LocalDate endDate = currentDate.minusYears(minAge);

            return builder.between(root.get("birthDate"), startDate, endDate);
        };
    }

    public static Specification<Account> hasGender(Boolean gender) {
        return (root, query, builder) -> {
            if (gender == null) {
                return builder.conjunction();
            }
            return builder.equal(root.get("gender"), gender);
        };
    }

    public static Specification<Account> hasCities(List<String> cities) {
        return (root, query, builder) -> {
            if (cities == null || cities.isEmpty()) {
                return builder.conjunction();
            }
            return root.get("city").in(cities);
        };
    }

    public static Specification<Account> hasRoles(List<String> roles) {
        return (root, query, builder) -> {
            if (roles == null || roles.isEmpty()) {
                return builder.conjunction();
            }

            Join<Account, AccountRole> accountRoleJoin = root.join("accountRoles");
            Join<AccountRole, Role> roleJoin = accountRoleJoin.join("role");

            return roleJoin.get("name").in(roles);
        };
    }

}
