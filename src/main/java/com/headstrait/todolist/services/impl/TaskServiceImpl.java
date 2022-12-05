package com.headstrait.todolist.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.headstrait.todolist.entity.Project;
import com.headstrait.todolist.entity.Task;
import com.headstrait.todolist.entity.User;
import com.headstrait.todolist.model.Status;
import com.headstrait.todolist.repository.ProjectRepository;
import com.headstrait.todolist.repository.TaskRepository;
import com.headstrait.todolist.services.TaskService;

@Service
public class TaskServiceImpl implements TaskService {

	@Autowired
	private TaskRepository taskRepository;
	@Autowired
	private ProjectRepository projectRepository;

	@Override
	public Task add(User user, Task task) {
		String projectName = task.getProject().getProjectName();
		if (projectRepository.existsByProjectName(user, projectName)) {
			Project project = projectRepository.getByProjectName(user, projectName);
			task.setProject(project);
		}
		task.getProject().setUser(user);
		return taskRepository.save(task);
	}

	@Override
	public List<Task> getTasks(User user) {
		return taskRepository.findAll(user);
	}

	@Override
	public List<Task> getTasksByStatus(User user, Status status) {
		return taskRepository.findByStatus(user, status.ordinal());
	}

	@Override
	public List<Task> getTasksByProjectId(User user, Long projectId) {
		return taskRepository.findByProjectId(user, projectId);
	}

	@Transactional
	@Override
	public Task updateProjectByTaskId(User user, long taskId, String taskName, String startDate, String endDate,
			Status status, Project project) {
		taskRepository.updateByTaskId(user, taskId, taskName, startDate, endDate, status.ordinal(), project);
		return getTaskById(user, taskId);
	}

	@Override
	public Task getTaskById(User user, Long taskId) {
		return taskRepository.findById(user, taskId).get();
	}

	@Transactional
	@Override
	public Task deleteServiceByTaskId(User user, Long taskId) {
		Task task = null;
		if (taskRepository.existsTaskByUser(user, taskId)) {
			task = getTaskById(user, taskId);
			taskRepository.deleteByTaskId(taskId);
		}
		return task;
	}

	@Override
	public List<Task> getTasksOfProjectByStatus(User user, long projectId, Status status) {
		return taskRepository.findTaskOfProjectByStatus(user, projectId, status.ordinal());
	}

	@Override
	public boolean checkTaskNameByProject(User user, String projectName, String taskName) {
		return taskRepository.existsByTaskNameOfProject(user, projectName, taskName);
	}

}