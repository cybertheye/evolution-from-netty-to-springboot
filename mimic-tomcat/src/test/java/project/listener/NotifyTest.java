package project.listener;

import com.attackonarchitect.listener.NotifierImpl;
import com.attackonarchitect.listener.request.ServletRequestAttributeEvent;
import com.attackonarchitect.listener.request.ServletRequestEvent;
import com.attackonarchitect.listener.webcontext.ServletContextEvent;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class NotifyTest {


    @Test
    public void shouldAllBeNotify(){
        SimpleContextListener scl = new SimpleContextListener();
        SimpleContextListener.class.getName();
        SimpleRequestListener srl = new SimpleRequestListener();
        List<String> listeners = Arrays.asList(SimpleContextListener.class.getName(), SimpleRequestListener.class.getName());
        NotifierImpl notifier = new NotifierImpl(listeners);
        notifier.notifyListeners(new ServletContextEvent());
        Assert.assertTrue(notifier.getListeners().size() == 2);
        notifier.notifyListeners(new ServletRequestAttributeEvent("a", "b"));

    }

}
