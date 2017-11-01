package com.reformer.authentication.holder;

import android.app.Activity;
import android.view.View;

/**
 * wf
 */
public abstract class BaseHolder<Data> {
    public View contentView;
    private Data data;
    public Activity mCtx;

    public BaseHolder() {
        this.contentView = initView(null);
        contentView.setTag(this);
    }

    public BaseHolder(View.OnClickListener a) {
        this.contentView = initView(a);
        contentView.setTag(this);
    }


    public BaseHolder(Activity a, View.OnClickListener b) {
        mCtx = a;
        this.contentView = initView(b);
        contentView.setTag(this);
    }


    public View getContentView() {
        return contentView;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
        refreshView(data);
    }

    protected abstract View initView(View.OnClickListener innerOnclick);

    /**
     * 将数据显示到对应的控件上
     */
    protected abstract void refreshView(Data data);

}
