package com.headstrait.todolist.controller;

import java.util.List;

import javax.validation.Valid;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.headstrait.todolist.entity.Task;
import com.headstrait.todolist.entity.User;
import com.headstrait.todolist.model.MessageResponse;
import com.headstrait.todolist.model.Status;
import com.headstrait.todolist.model.UpdateTaskRequest;
import com.headstrait.todolist.services.TaskService;
import com.headstrait.todolist.services.UserService;

@CrossOrigin
@RestController
@RequestMapping("/todolist")
public class TaskController {

	@Autowired
	private TaskService taskService;

	@Autowired
	private UserService userService;

	@PostMapping("/task/add")
	public Task addTask(@Valid @RequestBody Task task) {
		User user = getUserFromLoggedInUserDetails();
		return taskService.add(user, task);
	}

	@GetMapping("/task")
	public List<Task> getTasks() {
		User user = getUserFromLoggedInUserDetails();
		return taskService.getTasks(user);
	}

	@GetMapping("/status/{status}")
	public List<Task> getTaskByStatus(@PathVariable("status") Status status) {
		User user = getUserFromLoggedInUserDetails();
		return taskService.getTasksByStatus(user, status);
	}

	@GetMapping("/project/{project_id}/status/{status}")
	public List<Task> getTaskofProjectByStatus(@PathVariable("project_id") int projectId,
			@PathVariable("status") Status status) {
		User user = getUserFromLoggedInUserDetails();
		return taskService.getTasksOfProjectByStatus(user, (long) projectId, status);
	}

	@GetMapping("/project/{project_id}")
	public List<Task> getTaskByProjectId(@PathVariable("project_id") int projectId) {
		User user = getUserFromLoggedInUserDetails();
		return taskService.getTasksByProjectId(user, (long) projectId);
	}

	@PostMapping("/validate/task")
	public ResponseEntity<?> validateTaskByProject(@RequestBody String validateTask) {
		JSONObject jsonObject = new JSONObject(validateTask);
		String projectName = jsonObject.getString("projectName");
		String taskName = jsonObject.getString("taskName");
		User user = getUserFromLoggedInUserDetails();
		if (taskService.checkTaskNameByProject(user, projectName, taskName)) {
			return ResponseEntity.badRequest().body(new MessageResponse("Task name is not available"));
		}
		return ResponseEntity.ok("");
	}

	@PutMapping("/update/{taskId}")
	public Task updateByTaskId(@PathVariable("taskId") int taskId,
			@Valid @RequestBody UpdateTaskRequest updateTaskRequest) {
		User user = getUserFromLoggedInUserDetails();
		return taskService.updateProjectByTaskId(user, taskId, updateTaskRequest.getTaskName(),
				updateTaskRequest.getStartDate(), updateTaskRequest.getEndDate(), updateTaskRequest.getStatus(),
				updateTaskRequest.getProject());
	}

	@DeleteMapping("/delete/{taskId}")
	public Task deleteByTaskId(@PathVariable("taskId") int taskId) {
		User user = getUserFromLoggedInUserDetails();
		return taskService.deleteServiceByTaskId(user, (long) taskId);
	}

	public User getUserFromLoggedInUserDetails() {
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return userService.getUserByName(userDetails.getUsername());
	}
}
