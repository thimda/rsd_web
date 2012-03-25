package nc.uap.lfw.core.serializer.impl;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

public final class DomUtil {
	public static Node[] getChildNodes(Node parent, String tagName) {
		List<Node> nodeList = new ArrayList<Node>();
		NodeList ndList = parent.getChildNodes();
		int length = ndList.getLength();
		for (int i = 0; i < length; i++) {
			Node n = ndList.item(i);
			if(n instanceof Text)
				continue;
			if(n.getNodeName().equals(tagName))
				nodeList.add(n);
		}
		return nodeList.toArray(new Node[0]);
	}
	
	public static Node getChildNode(Node parent, String tagName){
		NodeList ndList = parent.getChildNodes();
		int length = ndList.getLength();
		for (int i = 0; i < length; i++) {
			Node n = ndList.item(i);
			if(tagName.equals(n.getNodeName()))
				return n;
		}
		return null;
	}
}
