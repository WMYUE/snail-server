package com.snail.fitment.api.registry;

import java.util.List;

public interface IRegistryService {
	public List<String> queryHostsByDomain(String domain);
}
