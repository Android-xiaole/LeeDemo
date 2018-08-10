package com.le.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.zhy.autolayout.utils.AutoUtils;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.List;

/**
 * 优化了BaseAdapter的写法 这样省去了布局重用的代码块  提升写代码的效率
 * @param <E>
 * @param <T>
 */
@SuppressWarnings("rawtypes")
public abstract class AbsBaseAdapter<E, T extends AbsBaseAdapter.Holder>
		extends BaseAdapter {

	protected List<E> mList;
	protected Context ctx;
	protected T mHolder;
	protected ViewGroup parent;
	//创建一个引用队列
	ReferenceQueue<Context> rq = new ReferenceQueue<>();
	ReferenceQueue<List> rq2 = new ReferenceQueue<>();

	public AbsBaseAdapter(Context ctx, List<E> list) {
//		this.mList = list;
//		this.ctx = ctx;
		//实现一个软引用，将强引用类型str和是实例化的rq放到软引用实现里面
		WeakReference<Context> srf = new WeakReference<>(ctx,rq);
		this.ctx = srf.get();
		//实现一个软引用，将强引用类型str和是实例化的rq放到软引用实现里面
		WeakReference<List> srf2 = new WeakReference<List>(list,rq2);
		this.mList = srf2.get();
	}

	@Override
	public int getCount() {
		return mList == null ? 0 : mList.size();
	}

	@Override
	public Object getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressWarnings("unchecked")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		this.parent = parent;
		if (v == null) {
			mHolder = getHolder();
			v = mHolder.mView;
//			AutoUtils.autoSize(v);//适配item里面的布局
		} else {
			mHolder = (T) v.getTag();
		}
		getView(mHolder, mList.get(position), position,parent);
		return v;
	}

	/**
	 * 该方法在子类中实现，返回单一Holder实例
	 */
	protected abstract T getHolder();

	/**
	 * 该方法在子类中实现，设置view属性以及数据集的变化
	 */
	protected abstract void getView(T t, E item,int position,ViewGroup parent);

	public class Holder {
		public View mView;

		public Holder(int r) {
			if (r == 0) {
				return;
			}
			mView = LayoutInflater.from(ctx).inflate(r,parent,false);
//			mView = View.inflate(ctx, r, null);
			AutoUtils.autoSize(mView);//适配item里面的布局
			mView.setTag(this);
		}

	}

}
