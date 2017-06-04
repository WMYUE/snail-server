package com.snail.fitment.common.profile;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import com.google.common.primitives.Longs;

class ProfileStat implements Comparable<ProfileStat> {
	private String method;
	private AtomicInteger count = new AtomicInteger(0);
	private AtomicLong total = new AtomicLong(0);
	private long max = 0;

	@Override
	public int compareTo(ProfileStat target) {
		return Longs.compare(this.getTotal(), target.getTotal());
	}

	public ProfileStat(String method, long cost) {
		super();
		this.method = method;
		increase(cost);
	}

	public void increase(long cost) {
		total.addAndGet(cost);
		count.incrementAndGet();
		if (cost > max)
			max = cost;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public int getCount() {
		return count.intValue();
	}

	public void setCount(AtomicInteger count) {
		this.count = count;
	}

	public long getTotal() {
		return total.longValue();
	}

	public void setTotal(AtomicLong total) {
		this.total = total;
	}

	public long getMax() {
		return max;
	}

	public long getAvg() {
		return getTotal() / getCount();
	}

}