package project.listener;

import com.attackonarchitect.listener.WebListener;
import com.attackonarchitect.listener.request.ServletRequestAttributeEvent;
import com.attackonarchitect.listener.request.ServletRequestAttributeListener;

/**
 * @description:
 */
@WebListener
public class SimpleRequestListener implements ServletRequestAttributeListener {
    @Override
    public void requestAttributeAdded(ServletRequestAttributeEvent srae) {
        System.out.println("inside request attribute added listener : \n"+ srae.getName()+"="+srae.getValue());
    }
}
