package com.example.yyy.medicinekit.MyDrug;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;

/**
 * Created by YYY on 2016/8/29.
 */

public class DailyListViewCompat extends ListView {

        private static final String TAG = "ListViewCompat";

        private SlideView mFocusedItemView;

        public DailyListViewCompat(Context context) {
            super(context);
        }

        public DailyListViewCompat(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public DailyListViewCompat(Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);
        }

        public void shrinkListItem(int position) {
            View item = getChildAt(position);

            if (item != null) {
                try {
                    ((SlideView) item).shrink();
                } catch (ClassCastException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN: {
                    int x = (int) event.getX();
                    int y = (int) event.getY();
                    int position = pointToPosition(x, y);
//            Log.e(TAG, "postion=" + position);
                    if (position != INVALID_POSITION) {
                        Daily.MessageItem data = (Daily.MessageItem) getItemAtPosition(position);
                        mFocusedItemView = data.slideView;
                        // Log.e(TAG, "FocusedItemView=" + mFocusedItemView);
                    }
                }
                default:
                    break;
            }

            if (mFocusedItemView != null) {
                mFocusedItemView.onRequireTouchEvent(event);
            }

            return super.onTouchEvent(event);
        }



}
