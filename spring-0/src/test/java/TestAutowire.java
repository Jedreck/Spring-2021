import com.jedreck.entity.Person;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestAutowire {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring-autowire.xml");

        // byName
        Person person = (Person) applicationContext.getBean("person");
        System.out.println(person);

        // byType
        Person person2 = (Person) applicationContext.getBean("person2");
        System.out.println(person);

    }
}
