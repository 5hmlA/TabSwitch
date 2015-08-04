package com.yun.tabswitch;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.RadioGroup;

import com.yun.tabswitch.Adapter.MyPagerAdapter;

public class RadioButtonFM2 extends AppCompatActivity implements
        ViewPager.OnPageChangeListener, RadioGroup.OnCheckedChangeListener {

    private ViewPager mPager;
    private RadioGroup mGroup;
    /**
     * group内部空间的间距
     */
    private int jianju = 96;
    private int screenWidth;
    private int checkId[] = { R.id.radio0, R.id.radio1, R.id.radio2,
            R.id.radio3, R.id.radio4 };
    private int leftEst;
    private int rightEst;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_radio_button_fm2);
        mPager = (ViewPager) findViewById(R.id.vp);
        mGroup = (RadioGroup) findViewById(R.id.radioGroup1);
        mGroup.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {

                    @Override
                    public void onGlobalLayout() {
                        mGroup.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                        jianju = mGroup.getChildAt(1).getLeft()
                                - mGroup.getChildAt(0).getLeft();
                        screenWidth = getResources().getDisplayMetrics().widthPixels;
                        int width = mGroup.getChildAt(1).getMeasuredWidth();
                        System.out.println("宽："+width);//144
                        rightEst = mGroup.getChildAt(4).getLeft();
                        leftEst = mGroup.getChildAt(0).getLeft();
                    }
                });
        MyPagerAdapter mPagerAdapter = new MyPagerAdapter(
                getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        mPager.setOnPageChangeListener(this);
        mGroup.setOnCheckedChangeListener(this);
        mPager.setCurrentItem(2);

    }

    private int currentPosition;
    private int nextposition;

    @Override
    public void onPageScrollStateChanged(int arg0) {
        //		Log.d("pager", "onPageScrollStateChanged");
    }

    private int tempPosition=-1;
    private boolean right = true;
    @Override
    public void onPageScrolled(int position, float positionOffset,
                               int positionOffsetPixels) {
        Log.d("pager", "onPageScrolled：" + position);
        //		System.out.println( "onPageScrolled："+position);
        if (tempPosition == position) {
            nextposition = position+1;
            right = true;
        }else{
            nextposition = position;
            right = false;//向左
        }
        tempPosition = position;
        allToMove(positionOffset, nextposition, right, position-2);
    }

    @Override
    public void onPageSelected(int position) {
        //		Log.d("pager", "onPageSelected");
        //		Toast.makeText(getApplicationContext(), mGroup.getChildAt(0).getLeft()+"", 0).show();

    }

    private int[] location = new int[2];

    private void allToMove(float positionOffset, int nextposition, boolean right,
                           int position) {
        float moveX = -jianju * (position+positionOffset);
        System.out.println(moveX);
        for (int i = 0; i < mGroup.getChildCount(); i++) {
            View childAt = mGroup.getChildAt(i);
            childAt.getLocationInWindow(location);
            //			move1(moveX, childAt);
            if (childAt.getLeft() + moveX > rightEst) {
                //				move1(moveX, childAt);
                //				if (i == 3) {
                //					childAt.setTranslationX(screenWidth - 4 * moveX);
                //				}
                //				if (i == 4) {
                //					if (moveX <= jianju) {
                //						childAt.setTranslationX(-4 * moveX);
                //					} else {
                //						childAt.setTranslationX(moveX - screenWidth);
                //					}
                //				}
                //				for (int j = 0; j < mGroup.getChildCount(); j++) {
                //					if (i != j) {
                //						mGroup.getChildAt(i).setAlpha(1);
                //					}
                //				}
            } else if (childAt.getLeft() + moveX < leftEst) {
                //				if (i == 0) {
                //					if (Math.abs(moveX) <= jianju) {
                //						childAt.setTranslationX(-4 * moveX);
                //					} else {
                //						childAt.setTranslationX(screenWidth + moveX);
                //					}
                //				}
                //				if (i == 1) {
                //					childAt.setTranslationX(-screenWidth - 4 * moveX);
                //				}
                //				for (int j = 0; j < mGroup.getChildCount(); j++) {
                //					if (i != j) {
                //						mGroup.getChildAt(i).setAlpha(1);
                //					}
                //				}
            }else{
                move1(moveX, childAt);
            }
        }
    }

    public void move1(float moveX, View childAt) {
        RadioGroup.LayoutParams params = (RadioGroup.LayoutParams) childAt.getLayoutParams();
        //		if (moveX<0) {
        //			params.leftMargin = (int) moveX;
        //			params.rightMargin = -(int) moveX;
        //		}else{
        params.leftMargin = (int) moveX;
        params.rightMargin = -(int) moveX;
        //		}
        childAt.setLayoutParams(params);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        int index = -1;
        switch (checkedId) {
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
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_radio_button_fm2, menu);
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
