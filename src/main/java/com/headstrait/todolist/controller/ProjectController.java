package com.headstrait.todolist.controller;

import java.util.List;

import javax.validation.Valid;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.headstrait.todolist.entity.Project;
import com.headstrait.todolist.entity.User;
import com.headstrait.todolist.model.MessageResponse;
import com.headstrait.todolist.model.ProjectRequest;
import com.headstrait.todolist.services.ProjectService;
import com.headstrait.todolist.services.UserService;

@CrossOrigin
@RestController
@RequestMapping("/todolist")
public class ProjectController {

	@Autowired
	private ProjectService projectService;

	@Autowired
	private UserService userService;

	@GetMapping("/projects")
	public List<Project> getProjects() {
		User user = getUserFromLoggedInUserDetails();
		return projectService.getProjects(user);
	}

	@GetMapping("/{projectId}")
	public Project getProject(@PathVariable("projectId") long projectId) {
		User user = getUserFromLoggedInUserDetails();
		return projectService.getProject(user, projectId);
	}

	@PostMapping("/projects/add")
	public Project addProject(@Valid @RequestBody ProjectRequest projectRequest) {
		Project project = Project.builder().projectName(projectRequest.getProjectName())
				.projectDescription(projectRequest.getProjectDescription()).user(getUserFromLoggedInUserDetails())
				.build();
		return projectService.addProject(project);

	}

	@PostMapping("/validate/project")
	public ResponseEntity<?> validateTaskByProject(@RequestBody String project) {
		JSONObject jsonObject = new JSONObject(project);
		String projectName = jsonObject.getString("projectName");
		User user = getUserFromLoggedInUserDetails();
		if (projectService.checkProjectName(user, projectName)) {
			return ResponseEntity.badRequest().body(new MessageResponse("Task name is not available"));
		}
		return ResponseEntity.ok("entered");
	}

	private User getUserFromLoggedInUserDetails() {
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return userService.getUserByName(userDetails.getUsername());
	}

}
