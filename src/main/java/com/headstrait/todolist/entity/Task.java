package com.headstrait.todolist.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.headstrait.todolist.model.Status;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "task")
public class Task {

	@Id
	@SequenceGenerator(name = "task_id", sequenceName = "task_sequence", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "task_sequence")
	private long taskId;
	@Column(name = "task")
	@NotBlank
	private String taskName;
	@NotNull
	private Status status;
	@Column(name = "start_date")
	@NotBlank
	private String startDate;
	@Column(name = "end_date")
	@NotBlank
	private String endDate;
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, optional = true)
	@JoinColumn(name = "project_id", referencedColumnName = "projectId")
	@NotNull
	private Project project;

}
