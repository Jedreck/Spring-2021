import com.jedreck.entity.Account;
import com.jedreck.entity.User;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 继承
 */
public class TestExtend {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring-extend.xml");

        User user = (User) applicationContext.getBean("userE1");
        User user2 = (User) applicationContext.getBean("userE2");

        System.out.println(user);
        System.out.println(user2);

        System.out.println(user == user2);

        Account account = (Account) applicationContext.getBean("account");
        System.out.println(account);
    }
}
