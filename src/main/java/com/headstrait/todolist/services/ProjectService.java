package com.headstrait.todolist.services;

import java.util.List;

import com.headstrait.todolist.entity.Project;
import com.headstrait.todolist.entity.User;

public interface ProjectService {

	List<Project> getProjects(User user);

	Project getProject(User user, long projectId);

	Project addProject(Project project);

	boolean checkProjectName(User user, String projectName);

}
