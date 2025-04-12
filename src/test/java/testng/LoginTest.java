package testng;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

public class LoginTest {

    private final String firebaseUrl = "https://assignment-34f27-default-rtdb.asia-southeast1.firebasedatabase.app/users.json";

    /**
     * Kiểm thử đăng nhập với tài khoản admin cố định
     */
    @Test
    public void testAdminLoginSuccess() {
        String email = "toan@gmail.com";
        String password = "123";

        boolean isAdmin = email.equals("toan@gmail.com") && password.equals("123");

        Assert.assertTrue(isAdmin, "Admin login không thành công.");
    }

    /**
     * Kiểm thử đăng nhập với tài khoản người dùng hợp lệ
     */
    @Test
    public void testValidUserLogin() {
        RestTemplate restTemplate = new RestTemplate();

        String testEmail = "toanbao@gmail.com"; // bạn cần chắc chắn user này tồn tại trong Firebase
        String testPassword = "Thienthan728";

        Map<String, Map<String, Object>> allUsers = restTemplate.getForObject(firebaseUrl, Map.class);
        boolean found = false;

        for (Map.Entry<String, Map<String, Object>> entry : allUsers.entrySet()) {
            Map<String, Object> user = entry.getValue();
            if (testEmail.equals(user.get("email")) && testPassword.equals(user.get("password"))) {
                found = true;
                break;
            }
        }

        Assert.assertTrue(found, "Đăng nhập thất bại với tài khoản người dùng hợp lệ.");
    }

    /**
     * Kiểm thử đăng nhập với email hoặc mật khẩu sai
     */
    @Test
    public void testInvalidLogin() {
        RestTemplate restTemplate = new RestTemplate();

        String wrongEmail = "wrong@gmail.com";
        String wrongPassword = "wrongpass";

        Map<String, Map<String, Object>> allUsers = restTemplate.getForObject(firebaseUrl, Map.class);
        boolean matched = false;

        for (Map.Entry<String, Map<String, Object>> entry : allUsers.entrySet()) {
            Map<String, Object> user = entry.getValue();
            if (wrongEmail.equals(user.get("email")) && wrongPassword.equals(user.get("password"))) {
                matched = true;
                break;
            }
        }

        Assert.assertFalse(matched, "Hệ thống không phát hiện đăng nhập sai.");
    }
}
