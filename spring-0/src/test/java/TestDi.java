import com.jedreck.entity.Classes;
import com.jedreck.entity.Student;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestDi {
    /**
     * DI
     * DI 指 bean 之间的依赖注⼊，设置对象之间的级联关系。
     */
    public static void main(String[] args) {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring-di.xml");
        String[] beanDefinitionNames = applicationContext.getBeanDefinitionNames();

        Student student = (Student) applicationContext.getBean("student");
        System.out.println(student);
        Classes classes = (Classes) applicationContext.getBean("classes");
        System.out.println(classes);

        // 获取全部
        System.out.println("---------------------");
        for (String name : beanDefinitionNames) {
            Object bean = applicationContext.getBean(name);
            System.out.println(bean);
        }
    }
}
