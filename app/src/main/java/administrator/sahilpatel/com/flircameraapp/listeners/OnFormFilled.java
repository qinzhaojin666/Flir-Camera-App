package administrator.sahilpatel.com.flircameraapp.listeners;

import administrator.sahilpatel.com.flircameraapp.model.Order;

/**
 * Created by Administrator on 9/16/2016.
 */
public interface OnFormFilled {

    void onCompletion(Order order,boolean hasNextPage, String nextPage);
    void onClickedCancel(String reason);
}
