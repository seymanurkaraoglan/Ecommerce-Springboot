package com.ecommerce.library.repository;

import com.ecommerce.library.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role,Long> {
    //@Query(value = "query",nativeQuery = true)
    Role findByName(String name);
}
