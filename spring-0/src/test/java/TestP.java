import com.jedreck.entity.Student;
import com.jedreck.entity.User;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestP {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring-p.xml");
        User userP = (User) applicationContext.getBean("userP");
        User userP2 = (User) applicationContext.getBean("userP2");
        System.out.println(userP);
        System.out.println(userP2);

        Student studentP = (Student) applicationContext.getBean("studentP");
        System.out.println(studentP);

    }
}
