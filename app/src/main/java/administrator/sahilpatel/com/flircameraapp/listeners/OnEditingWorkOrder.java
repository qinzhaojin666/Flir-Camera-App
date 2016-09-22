package administrator.sahilpatel.com.flircameraapp.listeners;

import administrator.sahilpatel.com.flircameraapp.model.Closure;
import administrator.sahilpatel.com.flircameraapp.model.Update;

/**
 * Created by Administrator on 9/19/2016.
 */
public interface OnEditingWorkOrder {

    void updateWorkOrder(Update update);
    void closeWorkOrder(Closure closure);
    void openCamera(Update update, Closure closure, String type);
    void reOpenWorkOrder();
}
