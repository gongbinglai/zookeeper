package com.jd.zk;

import org.I0Itec.zkclient.ZkClient;

public class ZkClientTest {

	public static void main(String[] args) {
		
		ZkClient zkClient = new ZkClient("localhost:2181");
		
		//创建持久节点
		zkClient.createPersistent("/createPersistent", "createPersistent");
		
		//创建临时节点
		zkClient.createEphemeral("/createEphemeral", "createEphemeral");
		
	}

}
