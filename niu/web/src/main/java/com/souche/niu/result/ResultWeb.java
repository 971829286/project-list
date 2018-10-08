package com.souche.niu.result;

import com.souche.optimus.core.web.Result;

public class ResultWeb<T>  extends Result<T> {

	private int status=200;

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
}
