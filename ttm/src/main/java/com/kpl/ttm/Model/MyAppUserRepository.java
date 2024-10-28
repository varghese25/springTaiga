
package com.kpl.ttm.Model;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/*InterFace Interact with Database Finding saving the Entity, Retive specific user from the database*/
/*Verfiry user during login verifiy passwords*/

@Repository
public interface MyAppUserRepository extends JpaRepository<MyAppUser, Long> {
    Optional<MyAppUser> findByUsername(String username);

}