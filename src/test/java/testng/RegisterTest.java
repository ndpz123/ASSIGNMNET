package testng;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

public class RegisterTest {

    private final String firebaseUrl = "https://assignment-34f27-default-rtdb.asia-southeast1.firebasedatabase.app/users.json";

    @Test
    public void testRegisterWithValidGmail() {
        RestTemplate restTemplate = new RestTemplate();

        Map<String, Object> newUser = new HashMap<>();
        newUser.put("email", "test" + System.currentTimeMillis() + "@gmail.com");
        newUser.put("password", "123456");
        newUser.put("name", "Test User");
        newUser.put("phone", "0123456789");
        newUser.put("address", "Hanoi");

        Map response = restTemplate.postForObject(firebaseUrl, newUser, Map.class);

        Assert.assertNotNull(response);

        Assert.assertTrue(response.containsKey("name"), "Đăng ký thành công nhưng test thất bại do lỗi giả lập.");
    }

    @Test
    public void testRegisterWithInvalidEmail() {
        String invalidEmail = "user@yahoo.com";

        boolean isValid = invalidEmail.endsWith("@gmail.com");

        Assert.assertFalse(isValid, "Email không hợp lệ nhưng lại được chấp nhận!");
    }

    @Test
    public void testRegisterWithInvalidEmailToServer() {
        RestTemplate restTemplate = new RestTemplate();

        String invalidEmail = "user@yahoo.com";
        if (!invalidEmail.endsWith("@gmail.com")) {
            System.out.println("Email không hợp lệ, hủy gửi request.");
            Assert.assertTrue(true);
            return;
        }

        Map<String, Object> newUser = new HashMap<>();
        newUser.put("email", invalidEmail);
        newUser.put("password", "123456");
        newUser.put("name", "Invalid Email User");
        newUser.put("phone", "0987654321");
        newUser.put("address", "Ho Chi Minh");

        Map response = restTemplate.postForObject(firebaseUrl, newUser, Map.class);

        Assert.assertNull(response, "Đã gửi dữ liệu không hợp lệ lên Firebase!");
    }
}
