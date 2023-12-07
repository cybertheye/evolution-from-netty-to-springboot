package project.listener;

import com.attackonarchitect.listener.*;
import com.attackonarchitect.listener.webcontext.ServletContextAttributeEvent;
import com.attackonarchitect.listener.webcontext.ServletContextAttributeListener;
import com.attackonarchitect.listener.webcontext.ServletContextEvent;
import com.attackonarchitect.listener.webcontext.ServletContextListener;

/**
 * @description:
 */
@WebListener
public class SimpleContextListener implements ServletContextListener, ServletContextAttributeListener {
    @Override
    public void attributeAdded(ServletContextAttributeEvent scae) {
        System.out.println("inside attributeAdded ");
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("inside contextInitialized : contextname = "+sce.getName());
    }


    @Override
    public void doNotify(Event event) {
        //根据逻辑定义attributeAdded或者contextInitialized实现，这里随便写一个
        if (event instanceof ServletContextEvent) {
            contextInitialized((ServletContextEvent) event);
        }
    }
}
