package io.byr.streaming.portfolio;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
public class User {

	@Id
	private String id;

	private String github;

	private String name;

	public User() {
	}

	public User(String github, String name) {
		this.github = github;
		this.name = name;
	}
}
