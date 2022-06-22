package org.fullstack.dheeraj.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="User")
public class User {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="user_name", nullable = false , length=50)
	private String username;
	
	@Column(name="user_phone", nullable = false , length=10)
	private String phoneNumber;
	
	@Column(name="user_email", nullable = true , length=50)
	private String email;

	@Column(name="user_password", nullable = false , length=100)
	private String password;
	
}
