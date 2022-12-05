package com.headstrait.todolist.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.headstrait.todolist.entity.Project;
import com.headstrait.todolist.entity.Task;
import com.headstrait.todolist.entity.User;
import com.headstrait.todolist.model.Status;
import com.headstrait.todolist.repository.ProjectRepository;
import com.headstrait.todolist.repository.TaskRepository;
import com.headstrait.todolist.services.TaskService;

@SpringBootTest
class TaskServiceTest {

	@Autowired
	private TaskService taskService;

	@MockBean
	private TaskRepository taskRepository;

	@MockBean
	private ProjectRepository projectRepository;

	private Task task;
	private User user;

	@BeforeEach
	private void setUp() {
		user = User.builder().userName("user name1").password("password 1").fname("fname 1").lname("lname 1")
				.isAdmin(false).build();
		task = Task.builder().taskId(1l).taskName("task name 1").status(Status.OnGoing).startDate("10/07/2022")
				.endDate("08/08/2022").build();
	}

	@Test
	void testAdd() {
		Project project = Project.builder().projectId(1l).projectName("Project Name 1").build();
		task.setProject(project);
		given(taskRepository.save(task)).willReturn(null);
//		Mockito.when(taskRepository.save(task)).thenReturn(null);
		taskService.add(user, task);

		verify(taskRepository, times(1)).save(Mockito.any(Task.class));
	}

//	@Test
//	void testGetTaskById() {
//		Optional<Task> optionalTask = Optional.of(task);
//		given(taskRepository.findById(user, 1l)).willReturn(optionalTask);
//
//		Task found = taskService.getTaskById(user, 1l);
//
//		verify(taskRepository, times(1)).findById(user, 1l);
//		assertEquals(task, found);
//	}

	@Test
	void testGetTasks() {
		given(taskRepository.findAll(user)).willReturn(List.of(task));

		List<Task> tasks = taskService.getTasks(user);

		verify(taskRepository, times(1)).findAll(user);
		assertEquals(List.of(task), tasks);
	}

	@Test
	void testGetTasksByStatus() {
		given(taskRepository.findByStatus(user, 1)).willReturn(List.of(task));

		List<Task> tasks = taskService.getTasksByStatus(user, Status.OnGoing);

		verify(taskRepository, times(1)).findByStatus(user, 1);
		assertEquals(List.of(task), tasks);
	}

	@Test
	void testGetTasksByProjectId() {
		given(taskRepository.findByProjectId(user, 1)).willReturn(List.of(task));

		List<Task> tasks = taskService.getTasksByProjectId(user, 1l);

		verify(taskRepository, times(1)).findByProjectId(user, 1);
		assertEquals(List.of(task), tasks);
	}

//	@Test
//	void testUpdateProjectByTaskId_whenProjectIsNotPresent() {
//		Project project = Project.builder().projectId(1l).projectName("Project Name 1").build();
//		Task expectedTask = Task.builder().taskId(1l).taskName("task name 1").status(Status.OnGoing)
//				.startDate("10/07/2022").endDate("08/08/2022").project(project).build();
//
//		given(projectRepository.existsByProjectName(user, project.getProjectName())).willReturn(false);
//		given(projectRepository.save(Mockito.any(Project.class))).willReturn(null);
//		willDoNothing().given(taskRepository).updateByTaskId(Mockito.anyLong(), Mockito.any(Project.class));
//		given(taskRepository.findById(Mockito.any(User.class), Mockito.anyLong()))
//				.willReturn(Optional.of(expectedTask));
//
//		Task found = taskService.updateProjectByTaskId(user, 1l, project);
//
//		verify(projectRepository, times(1)).existsByProjectName(user, project.getProjectName());
//		verify(taskRepository, times(1)).updateByTaskId(1l, project);
//		verify(taskRepository, times(1)).findById(user, 1l);
//		assertNotEquals(task, found);
//
//	}
//
//	@Test
//	void testUpdateProjectByTaskId_whenProjectIsPresent() {
//		Project project = Project.builder().projectId(1l).projectName("Project Name 1").build();
//		Task expectedTask = Task.builder().taskId(1l).taskName("task name 1").status(Status.OnGoing)
//				.startDate("10/07/2022").endDate("08/08/2022").project(project).build();
//
//		given(projectRepository.existsByProjectName(user, project.getProjectName())).willReturn(true);
//		willDoNothing().given(taskRepository).updateByTaskId(Mockito.anyLong(), Mockito.any(Project.class));
//		given(taskRepository.findById(Mockito.any(User.class), Mockito.anyLong()))
//				.willReturn(Optional.of(expectedTask));
//
//		Task found = taskService.updateProjectByTaskId(user, 1l, project);
//
//		verify(projectRepository, times(1)).existsByProjectName(user, project.getProjectName());
//		verify(taskRepository, times(1)).findById(user, 1l);
//		assertNotEquals(task, found);
//
//	}

	@Test
	void testDeleteServiceByTaskId() {
		willDoNothing().given(taskRepository).deleteByTaskId(1l);
		given(taskRepository.existsTaskByUser(user, 1l)).willReturn(true);

		taskService.deleteServiceByTaskId(user, 1l);

		verify(taskRepository, times(1)).deleteByTaskId(1l);
		verify(taskRepository, times(1)).existsTaskByUser(user, 1l);

	}

}
