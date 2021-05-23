import com.jedreck.entity.Student;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Test01 {
    public static void main(String[] args) {
        test01();
        test02();
    }

    public static void test02(){
        // DI
        // DI 指 bean 之间的依赖注⼊，设置对象之间的级联关系。
    }

    public static void test01(){
        // 传统方式手动创建
        Student student = new Student();
        System.out.println(student);

        // IoC
        // ClassPath: resource目录
        // xml: xml文件
        // 无参
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring.xml");
        // 两个以上的相同 class 的bean时，无法根据class获取
//        Student student0 = (Student) applicationContext.getBean(Student.class);
        Student student1 = (Student) applicationContext.getBean("student");
        System.out.println(student1);

        // 无参+赋值
        Student student2 = (Student) applicationContext.getBean("student2");
        System.out.println(student2);

        // 有参
        Student student3 = (Student) applicationContext.getBean("student3");
        System.out.println(student3);
    }
}
