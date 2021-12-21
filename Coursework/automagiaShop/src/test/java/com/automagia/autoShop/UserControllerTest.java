/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package com.automagia.autoShop;

import java.io.IOException;
import java.sql.SQLException;
import org.junit.Test;
import static org.junit.Assert.*;

public class UserControllerTest {
    
    UserController userClass = new UserController();
    
    @Test
    public void checkUserFree() throws SQLException, ClassNotFoundException {
        String login = "user1";
        if (!userClass.checkUserFree(login)){
            return;
        } else {
            fail("That user does not free");
        }
        
    }

    
}