package com.headstrait.todolist.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.headstrait.todolist.entity.Project;
import com.headstrait.todolist.entity.User;
import com.headstrait.todolist.repository.ProjectRepository;
import com.headstrait.todolist.services.ProjectService;

@Service
public class ProjectServiceImpl implements ProjectService {

	@Autowired
	private ProjectRepository projectRepository;

	@Override
	public List<Project> getProjects(User user) {
		return projectRepository.findAll(user);
	}

	@Override
	public Project getProject(User user, long projectId) {
		return projectRepository.getByProjectId(user, projectId);
	}

	@Override
	public Project addProject(Project project) {
		return projectRepository.save(project);
	}

	@Override
	public boolean checkProjectName(User user, String projectName) {
		return projectRepository.existsByProjectName(user, projectName);
	}

}
