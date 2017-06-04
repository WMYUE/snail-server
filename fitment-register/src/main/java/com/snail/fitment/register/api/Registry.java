package com.snail.fitment.register.api;


import com.snail.fitment.common.namespace.Node;
import com.snail.fitment.common.namespace.URL;

/**
 * Registry. (SPI, Prototype, ThreadSafe)
 * 
 * @see com.comisys.lanxin.blueprint.registry.api.RegistryFactory#getRegistry(URL)
 * @see com.comisys.lanxin.blueprint.registry.support.AbstractRegistry
 * @author Dengyiping
 */
public interface Registry extends Node, RegistryService {
	
}