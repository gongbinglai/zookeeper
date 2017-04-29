package com.jd.zk;

import java.util.List;

import org.I0Itec.zkclient.ZkClient;
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
		zkClient.deleteRecursive("/zookeeper");
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
	
	
}
