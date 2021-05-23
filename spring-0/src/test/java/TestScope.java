import com.jedreck.entity.User;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestScope {
    /**
     * Scope
     */
    public static void main(String[] args) {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring-scope.xml");
        // singleton
        User user = (User) applicationContext.getBean("user1");
        User user2 = (User) applicationContext.getBean("user1");
        System.out.println(user == user2);
        System.out.println("--------------------");
        // prototype
        User user3 = (User) applicationContext.getBean("user2");
        User user4 = (User) applicationContext.getBean("user2");

        System.out.println(user3 == user4);

    }
}
