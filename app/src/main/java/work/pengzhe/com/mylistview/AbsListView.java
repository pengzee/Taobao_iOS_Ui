package work.pengzhe.com.mylistview;

import android.view.View;

import java.util.Stack;

/**
 * Created on 2017/6/7 15:32
 *
 * @author PengZee
 */

public abstract class AbsListView {

    Stack<View>[] mScrapViews;
    Stack<View> mCurrentScrap;

    int mViewTypeCount;


    public void setViewTypeCount(){

    }


}
