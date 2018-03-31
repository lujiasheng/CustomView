package com.ljs.customview.listener;

public abstract class Listener<Status, Result> {
	private String tag = "";
	private boolean cancel = false;

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public boolean isCancel() {
		return cancel;
	}

	public void setCancel(boolean cancel) {
		this.cancel = cancel;
	}

	public Listener() {
	}

	public Listener(String tag) {
		this.tag = tag;
	}

	public abstract void onCallBack(Status status, Result reply);

}
