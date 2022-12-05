package com.headstrait.todolist.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.headstrait.todolist.entity.Project;
import com.headstrait.todolist.entity.User;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

	@Query(value = "Select exists (Select * from project where project_name=?2 and user_id=?1)", nativeQuery = true)
	boolean existsByProjectName(User user, String projectName);

	@Query(value = "Select * from project where project_name=?2 and user_id=?1", nativeQuery = true)
	Project getByProjectName(User user, String projectName);

	@Query(value = "Select * FROM project WHERE user_id = ?1", nativeQuery = true)
	List<Project> findAll(User user);

	@Query(value = "Select * FROM project where project_id=?2 and user_id=?1", nativeQuery = true)
	Project getByProjectId(User user, long projectId);
}
