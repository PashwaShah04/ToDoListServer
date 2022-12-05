package com.headstrait.todolist.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.headstrait.todolist.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	@Query(value = "Select * from users where user_name=?1", nativeQuery = true)
	Optional<User> getByUserName(String userName);

	boolean existsByUserName(String userName);

	@Modifying(clearAutomatically = true)
	@Query(value = "UPDATE users SET user_name=?2, fname = ?3, lname = ?4, password = ?5 where user_id = ?1", nativeQuery = true)
	void updateUser(long userId, String username, String fname, String lname, String password);

	@Query(value = "Select * from users where user_id=?1", nativeQuery = true)
	Optional<User> getByUserId(long userId);
}
