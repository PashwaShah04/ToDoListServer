package com.headstrait.todolist.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProjectRequest {

	@NotBlank
	@Size(max = 30)
	private String projectName;
	@Size(max = 500)
	private String projectDescription;
}
