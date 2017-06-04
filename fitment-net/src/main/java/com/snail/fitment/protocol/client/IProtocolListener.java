package com.snail.fitment.protocol.client;

import com.snail.fitment.protocol.bpackage.IBpPackage;

public interface IProtocolListener {
	/**
	 * 响应
	 * @param request
	 * @param response
	 */
    public void onResponse(IBpPackage request, IBpPackage response);

    /**
     * 错误
     * @param request
     * @param stateCode
     * @param descr
     */
    public void onError(IBpPackage request, int stateCode, String descr);

    /**
     * 进度
     * @param process
     */
    public void onProcess(IBpPackage request, double process);
}