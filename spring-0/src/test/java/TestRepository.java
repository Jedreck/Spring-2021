import com.jedreck.entity.Repository;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestRepository {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring-annotation.xml");
        String[] beanDefinitionNames = applicationContext.getBeanDefinitionNames();
        for (String name : beanDefinitionNames) {
            Object bean = applicationContext.getBean(name);
            System.out.println(bean);
        }
        System.out.println("-------------------------");
        Repository myRepo = (Repository) applicationContext.getBean("myRepo");
        System.out.println(myRepo);
    }
}
