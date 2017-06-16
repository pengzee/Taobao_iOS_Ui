package work.pengzhe.com.mylistview;

import android.view.View;
import android.view.ViewGroup;

/**
 * Created on 2017/6/7 11:34
 *
 * @author PengZee
 */

public interface BaseAdapter {

    int getCount();

    Object getItem(int position);

    long getItemId(int position);

    View getView(int position, View convertView, ViewGroup parent);

    int getItemViewType(int position);

    int getViewTypeCount();

    boolean isEmpty();

}
