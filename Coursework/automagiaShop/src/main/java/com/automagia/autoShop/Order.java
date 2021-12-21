package com.automagia.autoShop;

public class Order {
    private int id;
    private String title;
    private String description;
    private String loginUser;
    private String status;
    
    Order(int id, String title, String description, String loginUser
            , String status) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.loginUser = loginUser;
        this.status = status;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public int getId() {
        return id;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setUserOrder(String loginUser) {
        this.loginUser = loginUser;
    }
    
    public String getUserOrder() {
        return loginUser;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getStatus() {
        return status;
    }
}
