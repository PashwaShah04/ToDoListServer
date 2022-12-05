package com.headstrait.todolist.services;

import java.util.List;

import com.headstrait.todolist.entity.Project;
import com.headstrait.todolist.entity.Task;
import com.headstrait.todolist.entity.User;
import com.headstrait.todolist.model.Status;

public interface TaskService {

	Task add(User user, Task task);

	Task getTaskById(User user, Long taskId);

	List<Task> getTasksByStatus(User user, Status status);

	List<Task> getTasksByProjectId(User user, Long projectId);

	Task deleteServiceByTaskId(User user, Long taskId);

	List<Task> getTasks(User user);

	List<Task> getTasksOfProjectByStatus(User user, long projectId, Status status);

	boolean checkTaskNameByProject(User user, String projectName, String taskName);

	Task updateProjectByTaskId(User user, long taskId, String taskName, String startDate, String endDate, Status status,
			Project project);

}
