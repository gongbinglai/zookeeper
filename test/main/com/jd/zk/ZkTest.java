package com.jd.zk;

import java.util.List;

import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.IZkStateListener;
import org.I0Itec.zkclient.ZkClient;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.junit.Test;

public class ZkTest {

	
	ZkClient zkClient = new ZkClient("localhost:2181");
	
	@Test
	public void sayHello(){
		System.out.println("sayHello");
	}
	
	
	@Test
	public void createNode(){
		//创建持久节点
		//zkClient.createPersistent("/createPersistent", "createPersistent");
		
		//创建临时节点
		//zkClient.createEphemeral("/createEphemeral", "createEphemeral");
		
		
		//createPersistentSequential0000000005, createEphemeralSequential0000000006
		
		//创建持久顺序性节点
		zkClient.createPersistentSequential("/createPersistentSequential", "createPersistentSequential");
		
		//创建临时顺序性节点
		zkClient.createEphemeralSequential("/createEphemeralSequential", "createEphemeralSequential");
	}
	
	
	@Test
	public void createPath(){
		
		
		//zkClient.createPersistent("/subpath");
		
		zkClient.createPersistent("/subpath/node1","node1");
	
	}
	
	
	@Test
	public void deleteNode(){
		
		//删除节点
		zkClient.delete("/createPersistent");
		
		//删除节点
		//zkClient.deleteRecursive("/zookeeper");
	}
	
	@Test
	public void getNodeList(){
		this.printNodes("/");
	}
	
	
	private void printNodes(String path){
		List<String> nodeList = zkClient.getChildren(path);
		
		
		
		for(String nodeName:nodeList){
			
			if("zookeeper".equals(nodeName))continue;
			
			int children = 0 ;
			String subPath = "";
			
			
			if(!path.endsWith("/")){
				children = zkClient.getChildren(path+"/"+nodeName).size();
				subPath = path+"/"+nodeName;
			}else{
				children = zkClient.getChildren(path+nodeName).size();
				subPath = path+nodeName;
			}
			
			
			if(children>0){
				System.out.println("节点"+nodeName+"下有子节点");
				this.printNodes(subPath);
			
				
			}else{
				
				if(!path.endsWith("/")){
					System.out.println(nodeName+","+zkClient.readData(path+"/"+nodeName));
				}else{
					System.out.println(nodeName+","+zkClient.readData(path+nodeName));
				}
				
			}
			
			
		}
	}
	
	

	@Test
	public void updateNode(){
		
		zkClient.writeData("/createPersistent", "newData");
	}
	
	
	

	@Test
	public void handleDataChange(){
		
		zkClient.subscribeDataChanges("/createPersistent", new IZkDataListener(){

			public void handleDataChange(String dataPath, Object data)
					throws Exception {
				
				System.out.println(dataPath+" 对应的新值："+data);
			}

			public void handleDataDeleted(String dataPath) throws Exception {
				System.out.println(dataPath+" 删除了");
			}
			
		} );
		
		
		
		
		try {
			Thread.sleep(1000000000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}	
	}
	
	
	@Test
	public void subscribeStateChanges(){
		
		zkClient.subscribeStateChanges(new IZkStateListener(){

			public void handleStateChanged(KeeperState state) throws Exception {
				String stateStr = null;
				switch (state) {
					case Disconnected:
						stateStr = "Disconnected";
						break;
					case Expired:
						stateStr = "Expired";
						break;
					case NoSyncConnected:
						stateStr = "NoSyncConnected";
						break;
					case SyncConnected:
						stateStr = "SyncConnected";
						break;
					case Unknown:
					default:
						stateStr = "Unknow";
						break;
				}
				System.out.println("[Callback]State changed to [" + stateStr + "]");
			}

			public void handleNewSession() throws Exception {
				
				System.out.println("[Callback]New session created..");
			}
			
		});
		
		
		
		
		try {
			Thread.sleep(1000000000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}	
	}
	
	@Test
	public void subscribeChildChanges(){
		

		zkClient.subscribeChildChanges("/", new IZkChildListener(){

			public void handleChildChange(String parentPath,
					List<String> currentChilds) throws Exception {
				
			}
			
		});
		
		try {
			Thread.sleep(1000000000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}	
	}
}
