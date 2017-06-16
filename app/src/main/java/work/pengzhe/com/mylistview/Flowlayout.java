package work.pengzhe.com.mylistview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * 动脑学员第一讲
 * 自定义flowlayout：流式布局
 * @author lyb
 * created at 17/6/5 下午1:53
 * be used for
 */

public class Flowlayout extends ViewGroup {

	/**
	 * 创建这两个变量的思路
	 * 因为在测量和计算的时候都需要用到遍历子view，去取子view的各种宽高的信息
	 * 还有某一行中最高的那个
	 * 如果在把她们保存下来就不需要在用的时候重复遍历了
	 * 在使用结束的时候将其清空就ok了
	 */
	private ArrayList<Integer> mHeightList = new ArrayList();
	private ArrayList<ArrayList<View>> mViewsList = new ArrayList();
	public Flowlayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	/**
	 * 可以通过该方法讲xml中设置的margin属性设置进来，并且添加到childview中去
	 * 当前设定支持MarginLayoutParams
	 * @param attrs
	 * @return
	 */
	@Override
	public LayoutParams generateLayoutParams(AttributeSet attrs) {
		return new MarginLayoutParams(getContext(), attrs);
	}

	/**
	 * 测量子view的宽高等数据
	 * @param widthMeasureSpec：父容器传递过来的宽的规格
	 * @param heightMeasureSpec：父容器床底过来的高的规格
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		/**
		 * 获取父容器为FlowLayout设置的宽和高的测量模式和建议大小
		 * 几乎是固定写法
		 */
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);//获取宽度的测量模式
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);//获取高度的测量模式
		int suggestWidth = MeasureSpec.getSize(widthMeasureSpec);//获取父容器传递过来的宽度的建议值：某一些模式下不能超过这个建议值（AT_MOST模式）
		int suggestHeight = MeasureSpec.getSize(heightMeasureSpec);//获取父容器传递过来的高度的建议值：某一些模式下不能超过这个建议值（AT_MOST模式）

		int measureWidth = 0;//最终确定的宽度
		int measureHeight = 0;//最终确定的高度

		int currentLineWidth = 0;//当前行的宽度
		int currentLineHeight = 0;//当前行的高度
		/**
		 * 如果子view的宽度和高度是不确定的值（不是match_parent或着不是写死的固定值，那测量模式就是AT_MOST：不确定的值，但不会超过建议值），需要测量并且进行赋值
		 * 如果子view的宽度和高度是确定的值（是match_parent或着是写死的固定值）， 那就不需要测量了，直接赋值就可以了
		 */

		if (widthMode == MeasureSpec.EXACTLY && heightMode == MeasureSpec.EXACTLY){//如果都是确定的大小，那就不需要测量，直接将建议值赋值给最终大小就可以了
			measureWidth = suggestWidth;
			measureHeight = suggestHeight;
		}else {//如果宽高之中有一个不是确定的大小，那就需要测量，最后将测量之后的值赋值给最终大小
			/**
			 * 思路：
			 * 1：确定一个有多少个子view需要进行测量
			 * 2：循环遍历子view获取子view
			 * 3：对子view进行测量
			 * 4：获取子view周围的margin
			 * 5：通过获取子view的宽度 － getMeasuredWidth（），并且与左右margin进行相加可以得到一个子view所占用的实际宽度
			 * 6：通过获取子view的高度 － getMeasuredHeight()，并且与上下margin进行相加可以得到一个子view所占用的实际高度
			 * 7：创建变量记录当前行的宽度和高度，用以判断是否还有足够的空间进行控件的摆放
			 */
			//1：获取一共有多少个子view
			int childCount = getChildCount();
			int childRealWidth;
			int childRealHeight;
			ArrayList<View> viewlist = new ArrayList<>();
			for (int i = 0; i < childCount; i++) {
				View childView = getChildAt(i);//获取子view
				measureChild(childView, widthMeasureSpec, heightMeasureSpec);//测量子view
				MarginLayoutParams layoutParams = (MarginLayoutParams) childView.getLayoutParams();//获取左右的margin
				childRealWidth = childView.getMeasuredWidth() + layoutParams.leftMargin + layoutParams.rightMargin;//获取子view所占用的实际宽度
				childRealHeight = childView.getMeasuredHeight() + layoutParams.topMargin + layoutParams.bottomMargin;//获取子view所占用的实际高度
				if (currentLineWidth + childRealWidth > suggestWidth){//需要换行
					/**
					 * 换行之后的第一个操作应该是记录该行的信息
					 * 记录新的一行的信息
					 */
					measureWidth = Math.max(measureWidth, childRealWidth);
					measureHeight += childRealHeight;
					mViewsList.add(viewlist);
					mHeightList.add(childRealHeight);

					/**
					 * 记录新的一行的信息
					 */
					currentLineWidth = childRealWidth;
					currentLineHeight = childRealHeight;
					viewlist = new ArrayList<>();
					viewlist.add(childView);
				}else {//不需要换行
					currentLineWidth += childRealWidth;//宽度累加
					currentLineHeight = Math.max(currentLineHeight, childRealHeight);//高度取最大值，因为每一行只需要知道一个最大的值就可以了
					//添加相应的数据
//					mHeightList.add(currentLineHeight);
					viewlist.add(childView);
				}
			}
		}
		/**
		 * 测量的最终目的是调用该方法，该方法是结束测量
		 * 如果有很多的子view，需要循环便利调用onMeasure进行测量，但是每个子view测量完成之后，需要调用该方法结束测量
		 */
		setMeasuredDimension(measureWidth, measureHeight);
	}

	/**
	 * 确定子view的摆放位置
	 * @param changed
	 * @param l
	 * @param t
	 * @param r
	 * @param b
	 */
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		/**
		 * 确定子view摆放的位置首先要确定左上右下的位置
		 * 所以首先创建四个变量分别代表左上右下
		 */
		int left = 0, top = 0, right = 0, bootom = 0;
		/**
		 * 同时需要左上角着一个点，来确定具体的摆放位置
		 * 在换行摆放的时候还需要用到左上角位置的叠加
		 */
		int currentTop = 0;
		int currentLeft = 0;
		View childView = null;
		MarginLayoutParams params = null;
		ArrayList<View> list = new ArrayList<>();
		for (int i = 0, size = mHeightList.size(); i < size; i++) {
			if (list != null && list.size() != 0)
				list.clear();
			list = mViewsList.get(i);
			for (int j = 0, size2 = list.size(); j < size2; j++) {
				childView = list.get(j);
				/**
				 * 下一步就是计算左上右下这几个点的具体位置
				 * 计算这几个位置的时候还需要将margin值计算在内，所以还要拿到margin的值
				 */
				params = (MarginLayoutParams) childView.getLayoutParams();
				left = currentLeft + params.leftMargin;//计算左边的位置
				top = currentTop + params.topMargin;//计算顶部的位置
				right = left + childView.getMeasuredWidth();//计算右边的位置
				bootom = top + childView.getMeasuredHeight();//计算底部的位置
				//最终是需要调用这个方法
				childView.layout(left, top, right, bootom);
				/**
				 * 摆放当前view完成之后需要摆放下一个view
				 * 所以需要将currentTop和currentLeft进行移动
				 */
				currentLeft = currentLeft + childView.getMeasuredWidth() + params.leftMargin + params.rightMargin;
			}
			/**
			 * 为何将高度摆放的位置放在外层循环中
			 * 因为换行的时候只需要记录每一行开头的那个view的高度就可以了
			 * 改行中每一个子view的top值都是一样的
			 */
			currentTop += mHeightList.get(i);
			currentLeft  = 0;//重新设定下一行的当前宽度
		}
		mHeightList.clear();
		mViewsList.clear();
	}
}
