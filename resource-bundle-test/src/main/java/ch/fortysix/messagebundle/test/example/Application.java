package ch.fortysix.messagebundle.test.example;

import ch.fortysix.resourcebundle.annotation.ResourceBundle;

@ResourceBundle(bundle = "ch.fortysix.test.newbundle", suffix = "Messages")
public class Application {

	private int age;

	private String firstName;

	private String lastName;

	private String gender;

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

}