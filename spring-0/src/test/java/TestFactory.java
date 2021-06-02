import com.jedreck.entity.Car;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 工厂方法
 */
public class TestFactory {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring-factory.xml");

        // 静态⼯⼚⽅法
        Car car1 = (Car) applicationContext.getBean("car1");
        System.out.println(car1);
        // 实例工厂方法
        Car car2 = (Car) applicationContext.getBean("car2");
        System.out.println(car2);
    }
}
