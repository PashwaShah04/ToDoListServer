package com.headstrait.todolist.model;

import com.headstrait.todolist.entity.Project;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateTaskRequest {
	private String taskName;
	private Status status;
	private String startDate;
	private String endDate;
	private Project project;
}
