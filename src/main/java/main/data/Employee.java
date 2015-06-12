package main.data;

import java.io.Serializable;

public class Employee implements Serializable {

	private String name;
	private int age;
	private boolean active;
	private double sales;
	private int department;

	public Employee(String name, int age, boolean live, double sales, int department) {
		this.name = name;
		this.age = age;
		this.active = live;
		this.sales = sales;
		this.department = department;
	}

	public Employee() {
	}

	public String getName() {
		return name;
	}

	public int getAge() {
		return age;
	}

	public double getSales() {
		return sales;
	}

	public boolean isActive() {
		return active;
	}

	public int getDepartment() {
        return department;
    }

	public void setDepartment(int department) {
        this.department = department;
    }
}
