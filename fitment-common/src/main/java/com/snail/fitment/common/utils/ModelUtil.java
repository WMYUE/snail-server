package com.snail.fitment.common.utils;
/*package com.comisys.lanxin.blueprint.util;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;

public class ModelUtil {

	protected <T> T getBean(HttpServletRequest request, Class<T> c) {
		try {
			T obj = c.newInstance();
			BeanUtils.copyProperties(obj, getBean(request));
			return obj;
		} catch (Exception e) {
			//logger.error(e);
		}
		return null;
	}

	protected Map getBean(HttpServletRequest request) {
		Map bm = new HashMap();
		Map<String, String[]> tmp = request.getParameterMap();
		if (tmp != null) {
			for (String key : tmp.keySet()) {
				String[] values = tmp.get(key);
				bm.put(key, values.length == 1 ? values[0].trim() : values);
			}
		}
		return bm;
	}

}
*/