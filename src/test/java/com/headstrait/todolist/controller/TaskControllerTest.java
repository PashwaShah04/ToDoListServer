package com.headstrait.todolist.controller;

import static org.mockito.BDDMockito.given;

import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.headstrait.todolist.entity.Project;
import com.headstrait.todolist.entity.Task;
import com.headstrait.todolist.entity.User;
import com.headstrait.todolist.model.Status;
import com.headstrait.todolist.repository.UserRepository;
import com.headstrait.todolist.security.service.UserDetailsServiceImpl;
import com.headstrait.todolist.security.service.jwt.AuthEntryPointJwt;
import com.headstrait.todolist.security.service.jwt.JwtUtils;
import com.headstrait.todolist.services.TaskService;
import com.headstrait.todolist.services.UserService;

@WebMvcTest(TaskController.class)
@TestInstance(Lifecycle.PER_CLASS)
class TaskControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private TaskService taskService;

	@MockBean
	private UserService userService;

	@MockBean
	private UserRepository repository;

	@MockBean
	private UserDetailsServiceImpl userDetailsServiceImpl;

	@MockBean
	private AuthEntryPointJwt authEntryPointJwt;

	@MockBean
	private JwtUtils jwtUtils;

	private Task task;
	private Project project;
	private User user;

	@BeforeAll
	void setUpBeforeClass() {
		user = User.builder().userName("user name1").password("password 1").fname("fname 1").lname("lname 1")
				.isAdmin(false).build();
		project = Project.builder().projectId(1l).projectName("Project Name 1").build();
		task = Task.builder().taskId(1l).taskName("Task 1").status(Status.Completed).startDate("10/07/2022")
				.endDate("08/08/2022").project(project).build();
	}

	@Test
	void testAddTask() throws Exception {
		Project inputPoject = Project.builder().projectName("test project1").build();
		Task inputTask = Task.builder().taskName("addFeature1").status(Status.NotStarted).startDate("2021/06/10")
				.endDate("2022/07/25").project(inputPoject).build();

		given(taskService.add(user, inputTask)).willReturn(inputTask);

		mockMvc.perform(MockMvcRequestBuilders.post("/todolist/add").contentType(MediaType.APPLICATION_JSON)
				.content("{\r\n" + "        \"taskName\": \"addFeature1\",\r\n"
						+ "        \"status\": \"NotStarted\",\r\n" + "        \"startDate\": \"2021/06/10\",\r\n"
						+ "        \"endDate\": \"2022/07/25\",\r\n" + "        \"project\": {\r\n"
						+ "            \"projectName\": \"test project1\"\r\n" + "        }\r\n" + "    }"))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	void testGetTasks() throws Exception {
		given(taskService.getTasks(user)).willReturn(List.of(task));

		mockMvc.perform(MockMvcRequestBuilders.get("/todolist/tasks").contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	void testGetTaskByStatus() throws Exception {
		given(taskService.getTasksByStatus(user, Status.Completed)).willReturn(List.of(task));

		mockMvc.perform(
				MockMvcRequestBuilders.get("/todolist/tasks/status/Completed").contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	void testGetTaskByProjectId() throws Exception {
		given(taskService.getTasksByProjectId(user, 1l)).willReturn(List.of(task));

		mockMvc.perform(MockMvcRequestBuilders.get("/todolist/tasks/project/1").contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

//	@Test
//	void testUpdateByTaskId() throws Exception {
//		Project newProject = Project.builder().projectId(2l).projectName("Project name 2").build();
//		Task newTask = Task.builder().taskId(1l).taskName("Task 1").status(Status.Completed).startDate("10/07/2022")
//				.endDate("08/08/2022").project(newProject).build();
//
//		given(taskService.updateProjectByTaskId(user, 1l, newProject)).willReturn(newTask);
//
//		mockMvc.perform(MockMvcRequestBuilders.put("/todolist/tasks/update/1").contentType(MediaType.APPLICATION_JSON)
//				.content("{\r\n" + "            \"projectId\": 2,\r\n"
//						+ "            \"projectName\": \"Project Name 2\"\r\n" + "        }"))
//				.andExpect(MockMvcResultMatchers.status().isOk());
//	}

	@Test
	void testDeleteByTaskId() throws Exception {
		given(taskService.deleteServiceByTaskId(user, 1l)).willReturn(task);

		mockMvc.perform(
				MockMvcRequestBuilders.delete("/todolist/tasks/delete/1").contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

}
