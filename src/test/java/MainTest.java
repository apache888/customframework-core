import com.customframework.core.AnnotationConfigApplicationContext;
import com.customframework.core.ApplicationContext;
import org.junit.Test;

/**
 * Created on 22.02.2018
 *
 * @author Roman Hayda
 */

public class MainTest {

    @Test
    public void test() throws IllegalAccessException, ClassNotFoundException, InstantiationException {
        ApplicationContext context = new AnnotationConfigApplicationContext("src.test.java.com.customframework.core");
        context.init();
        System.out.println(context.getBeanNames().toString());
        System.out.println();
        context.getBean(ClassA.class).test();
        System.out.println();
        context.getBean(ClassB.class).test();

        System.out.println();
        context.getBean(ClassC.class).findByName("Name");
        context.getBean(ClassC.class).findByNameAndAge("Name", 30);
        context.getBean(ClassC.class).findByNameOrLastName("Name", "LastName");


//        ClassA bean = context.getBean(ClassA.class);
//        bean.test();
    }

}
