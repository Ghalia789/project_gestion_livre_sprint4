package com.nadhem.livres.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import com.nadhem.livres.entities.User;

public interface UserRepository  extends JpaRepository<User, Long>{
	User findByUsername (String username);
}
