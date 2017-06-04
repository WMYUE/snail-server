package com.snail.fitment.protocol.client.support;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.snail.fitment.api.notify.INotifyManager;
import com.snail.fitment.api.registry.IRegistryService;
import com.snail.fitment.common.constants.OperationConstants;
import com.snail.fitment.common.json.JsonConvert;
import com.snail.fitment.common.timer.TimerManager;
import com.snail.fitment.protocol.bpackage.BpPackage;
import com.snail.fitment.protocol.bpackage.IBpPackage;
import com.snail.fitment.protocol.client.IChannel;
import com.snail.fitment.protocol.client.IChannelFactory;
import com.snail.fitment.protocol.client.IChannelListener;
import com.snail.fitment.protocol.client.IChannelManager;

@Lazy(false)
@Component("channelManager")
public class ClientChannelManager implements IChannelManager, IChannelListener {
	private Map<String, IChannel> channelMap = new ConcurrentHashMap<>();

	private Timer timer = TimerManager.getTimer("clientChannel");

	@Autowired
	private IChannelFactory channelFactory;

	@Autowired
	@Qualifier("composeNotifyManager")
	protected INotifyManager notifyManager;
	// 需要根据注册表变化
	@Autowired
	private IRegistryService registryServiceImpl;

	@Override
	public IChannel getChannel(String domain) {
		IChannel channel = channelMap.get(domain);
		if (channel == null) {
			List<String> hosts = this.registryServiceImpl.queryHostsByDomain(domain);
			if (hosts != null && hosts.size() > 0) {
				String hostUrl = hosts.get(0);
				channel = channelFactory.build(this, domain);
				String hostargs[] = hostUrl.split(":");/// arg0,arg1
				channel.connect(new InetSocketAddress(hostargs[0], Integer.valueOf(hostargs[1])));
				if (channel.state() == State.ChannelConnected) {
					return channel;
				} else {
					return null;
				}
			}
		}
		return channel;
	}

	@Override
	public void onChannelStateChanged(IChannel channel, int stateCode, String stateDescr, State state) {
		if (channel.state() != State.ChannelConnected) {
			channelMap.remove(channel.domain(), channel);
		} else {
			channelMap.put(channel.domain(), channel);
		}

	}

	private Set<String> currentServerUser(Set<String> userSet) {
		Set<String> uniqUserSet = new HashSet<>();
		for (String userUniId : userSet) {
			uniqUserSet.add(userUniId);
		}
		return uniqUserSet;
	}

	@Override
	public void onReceive(IChannel channel, IBpPackage bpPackage) {
		/*if (bpPackage.getBpHeader().getOpCode() == OperationConstants.OP_SYNCH_BATCH) {
			// sync with server
			IBpPackage reply = channel.sendPackageSync(genSyncPackage(), null);
			Iterable<BehaviorData> datas = (Iterable<BehaviorData>) reply.getBpBody().getParams().get("update");
		}*/
	}

	// @PostConstruct
	public void initTest() {
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				try {
					IChannel channel = getChannel("1.audi01-lanxin");
					IBpPackage res = channel.sendPackageSync(genOauthPackage(), null);
					sessionId = (String) res.getBpBody().getParams().get("sessionId");
					res = channel.sendPackageSync(genSyncPackage(), null);
					System.out.println("sync:" + res);
					Iterable<Map> datas = (Iterable<Map>) res.getBpBody().getParams().get("data");
					for (Map value : datas) {
						testPlogId = (Integer) value.get("pLogId");
					}

				} catch (Throwable t) {
					t.printStackTrace();
				}
			}

		}, 1000, 10 * 1000);
	}

	private int seq;
	private String userUniId = "proxy-lanxin@test";
	private String sessionId;
	private long testPlogId;

	private IBpPackage genOauthPackage() {
		Map<String, String> tokens = new HashMap<String, String>();
		tokens.put("authToken", userUniId);
		tokens.put("appKey", "test");

		IBpPackage bpPackage1 = new BpPackage(JsonConvert.SerializeObject(tokens), null);
		bpPackage1.getBpHeader().setOpCode(OperationConstants.OP_SESSION_OAUTH);
		bpPackage1.getBpHeader().setSeq(seq++);
		bpPackage1.fillPackageBuffer();
		return bpPackage1;
	}

	private IBpPackage genSyncPackage() {
		Map<String, Object> tokens = new HashMap<String, Object>();
		tokens.put("sessionId", sessionId);
		List<Map<String, Object>> control = new ArrayList<>();

		Map<String, Object> tab5 = new HashMap<>();
		tab5.put("tableName", "dsf&1111112@2.lanxin");
		tab5.put("pLogId", testPlogId);
		control.add(tab5);

		tokens.put("synControl", control);
		tokens.put("userUniId", userUniId);
		tokens.put("count", 100);

		IBpPackage bpPackage1 = new BpPackage(JsonConvert.SerializeObject(tokens), null);
		bpPackage1.getBpHeader().setOpCode(OperationConstants.OP_DOWN_SYNC_DATA);
		bpPackage1.getBpHeader().setSeq(seq++);
		return bpPackage1;
	}
}
