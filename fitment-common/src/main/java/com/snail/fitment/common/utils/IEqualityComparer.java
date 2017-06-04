package com.snail.fitment.common.utils;

public interface IEqualityComparer<T> {
	public int hashCode(T obj);

	public boolean equals(T x, T y);
}
