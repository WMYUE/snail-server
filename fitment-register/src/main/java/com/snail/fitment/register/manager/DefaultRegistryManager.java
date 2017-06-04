package com.snail.fitment.register.manager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.snail.fitment.common.cache.redis.RedisManager;
import com.snail.fitment.common.collection.CollectionUtils;
import com.snail.fitment.common.config.FitmentConfig;
import com.snail.fitment.common.namespace.URL;
import com.snail.fitment.common.thread.BluePrintThreadFactory;
import com.snail.fitment.register.api.Registry;
import com.snail.fitment.register.support.RegistryConfig;
import com.snail.fitment.register.zookeeper.ZookeeperRegistryFactory;

@Component
@Lazy(false)
public class DefaultRegistryManager{
	private  Registry registry = null;
	
	@Autowired
	private RegistryConfig registryConfig;
	
	private List<URL> registryServiceURL = new ArrayList<URL>();
	
	private ExecutorService executor = Executors.newFixedThreadPool(1, new BluePrintThreadFactory("BluePrintReistryConnectionThread"));

	@PostConstruct
	public void initRegistry(){
		if(FitmentConfig.REGISTRY_SWITCH){			
			executor.execute(new Runnable() {
				public void run() {
					if(StringUtils.equals(registryConfig.getProtocol(), "zookeeper")){
						ZookeeperRegistryFactory zookeeperFactory = new ZookeeperRegistryFactory();
						registry = zookeeperFactory.createRegistry(registryConfig.getRegistryURL());
						
					}else if(StringUtils.equals(registryConfig.getProtocol(), "mysql")){
					}
					
					//init services
					List<URL> urlList = registryConfig.getServiceURL();
					for(URL url:urlList){
						registry.register(url);
						registryServiceURL.add(url);
					}
				}
			});
		}
	}
	
	public URL getServiceURL(){		
		if(CollectionUtils.isEmpty(registryServiceURL)){
			return null;
		}
	
		return registryServiceURL.get(0);
	}
	
	@PreDestroy
	public void destory(){
		System.out.println("----------------DefaultRegistryManager ShutDown-----------------");
		for(URL url : registryServiceURL){
			registry.unregister(url);
			RedisManager.deleteFromCachedServer(url.getParameter("domain"));
		}
	}

	public Registry getRegistry() {
		return registry;
	}

	public  void setRegistry(Registry registry) {
		this.registry = registry;
	}
}
