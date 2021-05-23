import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestDep {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring-dependent.xml");
    }
}
