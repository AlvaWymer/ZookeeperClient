package cn.mldn.serverlist.client;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.data.Stat;

/**
 * 客户端查看服务列表信息
 * 
 * @author mldn
 */
public class ClientForServerListClientListener {
	public static int count = 0; // 进行一个计数统计
	// 如果要基于ZooKeeper访问，那么肯定需要知道ZooKeeper连接地址信息
	public static final String CONNECTION_URL = "192.168.28.166:2181,192.168.28.168,192.168.28.66,192.168.28.97";
	public static final int SESSION_TIMEOUT = 2000; // 连接超时时间
	public static final String AUTH_INFO = "zkuser:admin"; // 进行连接的授权信息
	public static final String GROUPNODE = "/mldn-servers"; // 根节点
	private ZooKeeper zkClient; // 定义一个ZOoKeeper客户端处理对象

	public ClientForServerListClientListener() throws Exception {
		this.connectZooKeeperServer(); // ZooKeeper连接控制，同时进行各个节点的创建
		System.out.println(
				"【第" + count++ + "次取得服务器列表】" + this.updateServerList());
		Thread.sleep(Long.MAX_VALUE);
	}
	/**
	 * 编写一个方法实现所有的服务端信息列表的更新获得
	 * 
	 * @return 最新的服务列表信息项
	 */
	public Set<String> updateServerList() throws Exception {
		Set<String> allServers = new LinkedHashSet<String>(); // 只是进行名称的保存
		List<String> children = this.zkClient.getChildren(GROUPNODE, true); // 必须监听
		Iterator<String> iter = children.iterator();
		while (iter.hasNext()) {
			String path = GROUPNODE + "/" + iter.next(); // 取得每一个子节点
			allServers.add(
					new String(this.zkClient.getData(path, false, new Stat())));
		}
		return allServers;
	}

	/**
	 * 取得一个ZooKeeper的连接信息
	 */
	public void connectZooKeeperServer() throws Exception {
		this.zkClient = new ZooKeeper(CONNECTION_URL, SESSION_TIMEOUT,
				new Watcher() {
					@Override
					public void process(WatchedEvent event) {
						if (event.getPath() != null) { // 表示现在有了访问路径
							if (event
									.getType() == EventType.NodeChildrenChanged) { // 子节点现在发生了改变
								try { // 重新获得最新的服务器列表
									System.out.println("【第" + count++
											+ "次取得服务器列表】"
											+ ClientForServerListClientListener.this
													.updateServerList());
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						}
					}
				});
		zkClient.addAuthInfo("digest", AUTH_INFO.getBytes()); // 进行认证授权
		if (this.zkClient.exists(GROUPNODE, false) == null) { // 现在一定要判断父节点是否存在，如果不存在要创建
			// 创建的父节点一定是一个持久化节点，而父节点里面的内容才属于瞬时节点
			this.zkClient.create(GROUPNODE, "SERVER-LIST".getBytes(),
					ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
		}
	}
}
