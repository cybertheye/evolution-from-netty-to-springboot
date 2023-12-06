

package com.mimin.springframework.util;

import com.mimin.springframework.lang.Nullable;


@FunctionalInterface
public interface StringValueResolver {

	@Nullable
	String resolveStringValue(String strVal);

}
