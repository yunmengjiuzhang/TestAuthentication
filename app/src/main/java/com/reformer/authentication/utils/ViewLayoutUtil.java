package com.reformer.authentication.utils;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.lang.reflect.Field;

/**
 * Created by Administrator on 2015-09-23.
 */
public class ViewLayoutUtil {

    public static int getStatusBarHeight(Context context) {
        try {
            Class<?> c = Class.forName("com.android.internal.R$dimen");
            Object obj = c.newInstance();
            Field field = c.getField("status_bar_height");
            int x = Integer.parseInt(field.get(obj).toString());
            return context.getResources().getDimensionPixelSize(x);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static void setMargins(View v, int l, int t, int r, int b) {
        if (v.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            p.setMargins(l, t, r, b);
            v.requestLayout();
        }
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    public static void setListViewHeightRelativeLayoutOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        View listItem;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            if (i < 5) {
                listItem = listAdapter.getView(i, null, listView);
                listItem.setLayoutParams(new ListView.LayoutParams(
                        ListView.LayoutParams.WRAP_CONTENT,
                        ListView.LayoutParams.WRAP_CONTENT));
                listItem.measure(0, 0);
                totalHeight += listItem.getMeasuredHeight();
            }
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

//    public static void setRecyclerViewHeightRelativeLayoutOnChildren(RecyclerView recyclerView, int spanCount) {
//        RecyclerView.Adapter adapter = recyclerView.getAdapter();
//        if (adapter == null) {
//            return;
//        }
//        int itemHeight = 0;
//        if (adapter.getItemCount() > 0) {
//            itemHeight = DensityUtil.dip2px(recyclerView.getContext(), 85);
//        }
//
//        ViewGroup.LayoutParams params = recyclerView.getLayoutParams();
//        if (adapter.getItemCount() > 2 * spanCount) {
//            params.height = (3 * itemHeight) + 2;
//        } else if (adapter.getItemCount() > spanCount) {
//            params.height = (2 * itemHeight) + 1;
//        } else {
//            params.height = itemHeight;
//        }
//        recyclerView.setLayoutParams(params);
//    }
}
