package com.automagia.autoShop;

public class User {
    private String login;
    private String password;
    private int salary;
    private String role;
    
    public User(String login, String role, int salary) {
        this.login = login;
        this.role = role;
        this.salary = salary;
    }
    
    public User(String login, String password) {
        this.login = login;
        this.password = password;
    }
    
    public String getLogin() {
        return login;
    }
    
    public void setLogin(String login) {
        this.login = login;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getRole() {
        return role;
    }
    
    public void setRole(String role) {
        this.role = role;
    }
    
    public int getSalary() {
        return salary;
    }
    
    public void seSalary(int salary) {
        this.salary = salary;
    }
}
