package com.headstrait.todolist.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.headstrait.todolist.entity.Project;
import com.headstrait.todolist.entity.Task;
import com.headstrait.todolist.entity.User;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

	@Query(value = "Select * FROM task where status = ?2 and project_id in (Select project_id FROM project WHERE user_id = ?1)", nativeQuery = true)
	List<Task> findByStatus(User user, Integer status);

	@Query(value = "Select * FROM task where project_id = ?2 and project_id in (Select project_id FROM project WHERE user_id = ?1)", nativeQuery = true)
	List<Task> findByProjectId(User user, long projectId);

	@Modifying
	@Query(value = "UPDATE task SET end_date=?5, start_date=?4, status=?6, task=?3 WHERE task_id=?2 and project_id=?7 and project_id in (Select project_id from project where user_id=?1)", nativeQuery = true)
	void updateByTaskId(User user, long taskId, String taskName, String startDate, String endDate, Integer status,
			Project project);

	@Modifying
	@Query(value = "DELETE FROM task where task_id=?1", nativeQuery = true)
	void deleteByTaskId(long taskId);

	@Query(value = "Select * FROM task where project_id in (Select project_id FROM project WHERE user_id = ?1)", nativeQuery = true)
	List<Task> findAll(User user);

	@Query(value = "Select * FROM task where task_id = ?2 and project_id in (Select project_id FROM project WHERE user_id = ?1)", nativeQuery = true)
	Optional<Task> findById(User user, Long taskId);

	@Query(value = "select exists(select * from task where task_id = ?2 and project_id in (Select project_id from project where user_id=?1))", nativeQuery = true)
	boolean existsTaskByUser(User user, Long taskId);

	@Query(value = "Select * FROM task where status=?3 and project_id = ?2 and project_id in (Select project_id FROM project WHERE user_id = ?1)", nativeQuery = true)
	List<Task> findTaskOfProjectByStatus(User user, long projectId, int status);

	@Query(value = "SELECT EXISTS(select * from task where task =?3 AND project_id in (SELECT project_id from project where project_name=?2 and user_id=?1))", nativeQuery = true)
	boolean existsByTaskNameOfProject(User user, String projectName, String taskName);

}
