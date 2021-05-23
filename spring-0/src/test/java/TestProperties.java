import com.jedreck.entity.DataSource;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestProperties {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring-properties.xml");
        DataSource dataSource = (DataSource) applicationContext.getBean("dataSource");
        System.out.println(dataSource);
    }
}
