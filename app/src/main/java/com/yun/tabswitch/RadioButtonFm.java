package com.yun.tabswitch;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.yun.tabswitch.Adapter.MyPagerAdapter;

public class RadioButtonFm extends AppCompatActivity implements ViewPager.OnPageChangeListener, View.OnClickListener {

    private ViewPager mPager;
    private LinearLayout mGroup;
    /**
     * group内部空间的间距
     */
    private int jianju = 96;
    private int screenWidth;
    private int checkId[] = { R.id.radio0, R.id.radio1, R.id.radio2,
            R.id.radio3, R.id.radio4 };
    /**
     * 临时变量 用来存放viewpager滑动过程中多次调用onPageScrolled的每上一次position的位置
     */
    private int tempPosition = -1;
    /**
     * 临时变量 用来判断viewpager的滑动方向
     */
    private int tpOffset = 0;
    /**
     * 滑动过程中当前的页面 以及停止时切换到的页面
     */
    private int currentPosition = -1;

    /**
     * view 滑动的方向 true为右 往当前position+1的方向
     */
    private boolean right = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_radio_button_fm);
        mPager = (ViewPager) findViewById(R.id.vp);
        mGroup = (LinearLayout) findViewById(R.id.radioGroup1);
        mGroup.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    // 每次布局变动都会执行一次
                    @Override
                    public void onGlobalLayout() {
                        // 每次布局变动都会执行一次 所以执行一次就移除监听
                        mGroup.getViewTreeObserver()
                                .removeGlobalOnLayoutListener(this);
                        jianju = mGroup.getChildAt(1).getLeft()
                                - mGroup.getChildAt(0).getLeft();
                        screenWidth = getResources().getDisplayMetrics().widthPixels;
                        int width = mGroup.getChildAt(1).getMeasuredWidth();
                        setCurrents(2);
                    }
                });
        MyPagerAdapter mPagerAdapter = new MyPagerAdapter(
                getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        mPager.setOnPageChangeListener(this);
        setOnClickListener();
    }

    private int nextposition;

    private void setOnClickListener() {
        for (int i = 0; i < mGroup.getChildCount(); i++) {
            mGroup.getChildAt(i).setOnClickListener(RadioButtonFm.this);
        }

    }

    @Override
    public void onPageScrollStateChanged(int arg0) {
        //		Log.e("pager", "onPageScrollStateChanged：" + arg0);
    }


    /**
     * 移动到 某页
     *
     * @param position
     */
    public void setCurrents(int position) {
        mPager.setCurrentItem(position);//会先执行onPageSelected上一个，当前-然后onPageScrolled()的positionOffset渐变(相对于前一个判断向左向右(向左递减当前相差
        //上一个多少则递减(0-0.9)多少次，最后几个值为递减向右则相反))无论向左向右positionOffset最终都归0,
        //当设置的position和当前显示的position一样的时候不会执行那两个方法
        //第一次setCurrent为0的时候 会只执行一次onPageScrolled()，position和positionOffset都为o
        //当第一次设置为其他非0  效果是相对于0的移动 会先执行onPageSelected(0然后当前position)-然后onPageScrolled()渐变(相对于0都是右所以递增是多次递增最后几个必然是递增)
        if (position==0) {
            allToMove(position-2, -1, true, 1);
            showOthers(position);
        }
        //		showOthers(position);
    }

    /**
     * 向左滑动
     * 	position的值一直是 下一个页面的位置
     * 	positionOffset滑动过程中 从0.9变为0
     * 向右滑动
     * 	position的值 在滑动过程中一直是 当前页面的位置，当up之后完成切换的瞬间 变为下一个页面(切换完的当前页面)
     * 	positionOffset滑动过程中 从0变为0.9 切换完成(up的瞬间)转为0  值得注意
     *
     * positionOffset == 0的时候 表示 滑动停止
     *
     * 比如：手指向右滑动viewpager (是无法刚好滑倒下一页的)所以在滑动-->positionOffset渐增到a-->提手onPageSelected执行判断是回到当前还是到下一页-->
     *->1，到下一页 onPageScrolled执行positionOffset从a继续增加到0.9最后归0
     *->2,回到当前	onPageScrolled执行positionOffset从a递减到0
     */
    @Override
    public void onPageScrolled(int position, float positionOffset,
                               int positionOffsetPixels) {
        //		 Log.e("pager", "onPageScrolled："+position+"---positionOffset："+positionOffset);

        if (tempPosition == position) {
            // 如果当前是2 向右滑动切换到3，之间position先手指滑动过程中一直是2
            // 当up之后切换到3则position瞬间转为3
            nextposition = position + 1;
            currentPosition = position;
            right2 = true;
        } else {
            // 如果当前是3 向左滑动切换到2，之间position一直是3-1=2
            nextposition = position;
            currentPosition = position + 1;
            right2 = false;
        }
        //positionOffset != 0表示正在滑动过程中，，positionOffset 逐渐增加表示向右滑动 反之向左滑动
        if (positionOffset != 0) {
            //上一次 的 offset大于当前的offset 则为递减 向左划
            //必须在positionOffset != 0的时候判断，因为最终必须在positionOffset都会等于0的
            if (tpOffset < positionOffsetPixels) {
                //必须先判断 向右  因为向左从0.9到0 而最开始tpOffset为0
                //但是 滑动的时候positionOffsetPixels确切上不是从0开始，所以第一次值作废
                if (rightFirst) {
                    rightFirst = false;
                }else{
                    right = true;
                    Log.v("方向", "右");
                }
            }else{
                right = false;// 向左
                Log.v("方向", "左");
            }
            allToMove(position - 2 + positionOffset, nextposition, right,
                    positionOffset);
            //			showOthers(position);
        } else {
            //positionOffset等于0的时候 停止滚动的时候
            allToMove(position - 2, -1, true, 1);
            //			showOthers(position);
            if (right&&(tempPosition!=position)) {//tempPosition!=position表示 当前是有滑动的
                currentPosition = position;
            } else {
                currentPosition = position;
            }
            //临时变量 初始化
            tempPosition = -1;
            tpOffset = 0;
            rightFirst = true;
        }
        tempPosition = position;
        tpOffset = positionOffsetPixels;
    }
    private boolean rightFirst = true;
    @Override
    public void onPageSelected(int position) {
        //		Log.e("pager", "onPageSelected........==........：" + position);
    }

    /**
     * 显示其他
     *
     * @param position
     *            不显示的那个
     */
    public void showOthers(int position) {
        Log.e("showOthers...", ""+position);
        // 将其他的球显示
        for (int i = 0; i < mGroup.getChildCount(); i++) {
            mGroup.getChildAt(i).clearAnimation();
            if (i != position) {
                mGroup.getChildAt(i).setAlpha(1);
            } else {
                mGroup.getChildAt(i).setAlpha(0);
                //				ObjectAnimator animator = ObjectAnimator.ofFloat(
                //						mGroup.getChildAt(i), "alpha", 0, 1);
                //				animator.cancel();
                //				animator.setInterpolator(new CycleInterpolator(1));
                //				animator.setDuration(5000).start();
            }
        }
    }

    @Override
    public void onClick(View v) {
        int index = -1;
        switch (v.getId()) {
            case R.id.radio0:
                index = 0;
                break;
            case R.id.radio1:
                index = 1;
                break;
            case R.id.radio2:
                index = 2;
                break;
            case R.id.radio3:
                index = 3;
                break;
            case R.id.radio4:
                index = 4;
                break;
        }
        mPager.setCurrentItem(index);
        Toast.makeText(getApplicationContext(), "选中" + index, Toast.LENGTH_SHORT).show();

    }

    private int[] location = new int[2];

    private void allToMove(float rate, int nextposition, boolean right,
                           float alpha) {
        //		Log.e("allToMove：", "allToMove执行了，下一页为："+nextposition);
        //		Log.d("alpha", ""+alpha);
        float moveX = jianju * rate * -1;
        for (int i = 0; i < mGroup.getChildCount(); i++) {
            View childAt = mGroup.getChildAt(i);
            childAt.getLocationInWindow(location);
            if (childAt.getLeft() + moveX > mGroup.getChildAt(4).getLeft()) {
                if (i == 3) {
                    childAt.setTranslationX(screenWidth - 4 * moveX);
                }
                if (i == 4) {
                    if (moveX <= jianju) {
                        childAt.setTranslationX(-4 * moveX);
                    } else {
                        childAt.setTranslationX(moveX - screenWidth);
                    }
                }
            } else if (childAt.getLeft() + moveX < mGroup.getChildAt(0)
                    .getLeft()) {
                if (i == 0) {
                    if (Math.abs(moveX) <= jianju) {
                        childAt.setTranslationX(-4 * moveX);
                    } else {
                        childAt.setTranslationX(screenWidth + moveX);
                    }
                }
                if (i == 1) {
                    childAt.setTranslationX(-screenWidth - 4 * moveX);
                }
            } else {
                childAt.setTranslationX(moveX);
            }
        }
        if (nextposition == -1) {
            //			int current = mPager.getCurrentItem();
            //			 mGroup.getChildAt(current).setAlpha(0);
            //			 if (!right) {
            //				 if (current==0) {
            //					 mGroup.getChildAt(4).setAlpha(1);
            //				}else{
            //					mGroup.getChildAt(current-1).setAlpha(1);
            //				}
            //			}else{
            //				if (current==4) {
            //					mGroup.getChildAt(0).setAlpha(1);
            //				}else{
            //					mGroup.getChildAt(current+1).setAlpha(1);
            //				}
            //			}
        } else {
            // 中心隐藏
            //			if (!right2) {
            //				mGroup.getChildAt(mPager.getCurrentItem()).setAlpha(1 - alpha);
            //				mGroup.getChildAt(nextposition).setAlpha(alpha);
            //			} else {
            //				mGroup.getChildAt(mPager.getCurrentItem()).setAlpha(alpha);
            //				mGroup.getChildAt(nextposition).setAlpha(1 - alpha);
            //			}
        }
    }
    private boolean right2 = false;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_radio_button_fm, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
