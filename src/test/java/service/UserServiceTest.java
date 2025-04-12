package service;

import com.jetbrains.assign.model.Users;
import com.jetbrains.assign.service.UserService;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;

public class UserServiceTest {

    private UserService userService;

    @BeforeClass
    public void setUp() {
        userService = new UserService();
    }

    @Test
    public void testSaveAndGetAll() {
        Users user = new Users();
        user.setEmail("test" + System.currentTimeMillis() + "@gmail.com");
        user.setPassword("123456");
        user.setName("Test User");
        user.setPhone("0123456789");
        user.setAddress("Hanoi");

        userService.save(user);

        List<Users> allUsers = userService.getAll();
        boolean found = allUsers.stream()
                .anyMatch(u -> u.getEmail().equals(user.getEmail()));

        Assert.assertTrue(found, "User vừa lưu không tồn tại trong danh sách!");
    }

    @Test
    public void testGetByIdAndUpdate() {
        List<Users> allUsers = userService.getAll();
        if (!allUsers.isEmpty()) {
            Users user = allUsers.get(0);
            String originalName = user.getName();
            user.setName("Updated Name");

            userService.update(user.getId(), user);

            Users updatedUser = userService.getById(user.getId());
            Assert.assertEquals(updatedUser.getName(), "Updated Name");

            // rollback nếu cần
            user.setName(originalName);
            userService.update(user.getId(), user);
        }
    }

    @Test
    public void testDeleteUser() {
        Users user = new Users();
        user.setEmail("delete" + System.currentTimeMillis() + "@gmail.com");
        user.setPassword("123456");
        user.setName("Delete Test");
        user.setPhone("0123456789");
        user.setAddress("HCM");

        userService.save(user);

        List<Users> allUsers = userService.getAll();
        Users toDelete = allUsers.stream()
                .filter(u -> u.getEmail().equals(user.getEmail()))
                .findFirst().orElse(null);

        Assert.assertNotNull(toDelete);

        userService.delete(toDelete.getId());

        List<Users> afterDelete = userService.getAll();
        boolean stillExists = afterDelete.stream()
                .anyMatch(u -> u.getId().equals(toDelete.getId()));

        Assert.assertFalse(stillExists, "User vẫn còn tồn tại sau khi xóa!");
    }
}
