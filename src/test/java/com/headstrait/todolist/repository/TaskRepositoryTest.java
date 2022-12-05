package com.headstrait.todolist.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.headstrait.todolist.entity.Project;
import com.headstrait.todolist.entity.Task;
import com.headstrait.todolist.entity.User;
import com.headstrait.todolist.model.Status;

@DataJpaTest
@TestInstance(Lifecycle.PER_CLASS)
class TaskRepositoryTest {

	@Autowired
	private TaskRepository taskRepository;

	@Autowired
	private ProjectRepository projectRepository;

	@Autowired
	private UserRepository userRepository;

	private Task task;
	private Project project;
	private User user;

	@BeforeAll
	void beforeAll() {
		user = User.builder().userName("user name1").password("password 1").fname("fname 1").lname("lname 1")
				.isAdmin(false).build();
		project = Project.builder().projectName("Project Name 1").user(user).build();
		task = Task.builder().taskName("task name 1").status(Status.OnGoing).startDate("10/07/2022")
				.endDate("08/08/2022").project(project).build();
		Task savedTask = taskRepository.save(task);
		project.setProjectId(savedTask.getProject().getProjectId());
		task.setTaskId(savedTask.getTaskId());
		System.out.println(task + "--------");
	}

	@AfterAll
	void afterAll() {
		taskRepository.deleteById(task.getTaskId());
	}

	/*
	 * @Test void testFindByTaskId() { List<Task> tasks = taskRepository.findAll();
	 * System.out.println(tasks); Optional<Task> taskFromDb =
	 * taskRepository.findById(1l); System.out.println(taskFromDb);
	 * assertEquals("Project Name 1",
	 * taskFromDb.get().getProject().getProjectName()); }
	 */

	@Test
	void testFindByStatus() {
		List<Task> tasks = taskRepository.findByStatus(user, 1);
		assertEquals(List.of(task), tasks);
	}

	@Test
	void testFindByProjectId() {
		List<Task> tasks = taskRepository.findByProjectId(user, project.getProjectId());
		assertEquals(List.of(task), tasks);
	}

	@Test
	void testFindByProjectId_ButUserIsDifferent() {
		User anotherUser = User.builder().userName("user name2").password("password 2").fname("fname 2")
				.lname("lname 2").isAdmin(true).build();
		User savedUser = userRepository.save(anotherUser);
		anotherUser.setUserId(savedUser.getUserId());

		List<Task> tasks = taskRepository.findByProjectId(anotherUser, project.getProjectId());

		assertNotEquals(List.of(task), tasks);
		userRepository.deleteById(anotherUser.getUserId());
	}

//	@Test
//	void testUpdateByTaskId() {
//		Project newProject = Project.builder().projectName("Project Name 2").build();
//		projectRepository.save(newProject);
//		taskRepository.updateByTaskId(task.getTaskId(), newProject);
//		Task taskFromDb = taskRepository.findById(task.getTaskId()).get();
//		assertEquals(newProject, taskFromDb.getProject());
//	}

	@Test
	void testDeleteByTaskId() {
		taskRepository.deleteByTaskId(task.getTaskId());
	}

}
