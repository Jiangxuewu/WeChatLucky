package com.UU.uuaccelerator.Utils;

import java.util.ArrayList;
import java.util.List;

import android.view.accessibility.AccessibilityNodeInfo;

public class AccessiblityHandler {

	private static AccessiblityHandler accessiblityHandler;
	
	
	private AccessiblityHandler(){
		
	}
	public static AccessiblityHandler getInstance() {
		
		if (accessiblityHandler==null) {
			accessiblityHandler=new AccessiblityHandler();
		}
		
		
		
		return accessiblityHandler;
		
	}
	
	public void listViewScollAction(AccessibilityNodeInfo event) {
		if (event!=null) {
			AccessibilityNodeInfo node;
			
			for(int i=0; i<event.getChildCount(); i++){
				node = event.getChild(i);
				if (node.getClassName().equals("android.widget.ListView") && node.isEnabled()) {
					boolean b=node.performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
					if (b) {
						break;
					}
				}else if(node.getChildCount()>0){
					listViewScollAction(node);
				}
			}
			
		}
	}

	
	
	
	public boolean clickPerantItemByText(
			AccessibilityNodeInfo accessibilityNodeInfo, String text) {
		
		
		
		boolean b=false;
		if (accessibilityNodeInfo != null) {
			List<AccessibilityNodeInfo> ok_nodes = findItemFromText(
					accessibilityNodeInfo, text);
			if (ok_nodes != null && !ok_nodes.isEmpty()) {
				AccessibilityNodeInfo node;
				for (int i = 0; i < ok_nodes.size(); i++) {
					node = ok_nodes.get(i);
					b=doPerantClick(node);
					if (b) {
						break;
					}
				}
				
			}
		}
		return b;
	}

	public boolean doPerantClick(AccessibilityNodeInfo node) {
		boolean b=	node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
		if (!b&&node.getParent()!=null) {
			
			return	doPerantClick(node.getParent());
		}
		return b;
		
	}
	public boolean clickItemFromText(
			AccessibilityNodeInfo accessibilityNodeInfo, String text) {
		boolean b=false;
		if (accessibilityNodeInfo != null) {
				List<AccessibilityNodeInfo> ok_nodes = findItemFromText(
						accessibilityNodeInfo, text);
				if (ok_nodes != null && !ok_nodes.isEmpty()) {
					AccessibilityNodeInfo node;
					for (int i = 0; i < ok_nodes.size(); i++) {
						node = ok_nodes.get(i);
					b=	node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
							
					}

			}
		}
		return b;
	}
	public List<AccessibilityNodeInfo> findItemFromText(
			AccessibilityNodeInfo source, String text) {
		if (source != null) {
			List<AccessibilityNodeInfo> ok_nodes = source
					.findAccessibilityNodeInfosByText(text);
			if (ok_nodes==null&&source.getParent()!=null) {
				ok_nodes=source.getParent().findAccessibilityNodeInfosByText(text);
			}
			if (ok_nodes != null && !ok_nodes.isEmpty()) {
				
				for (AccessibilityNodeInfo a:ok_nodes) {
//					Log.e("findItemFromText", a+"=source=="+source.getChildCount());
				}
				
				return ok_nodes;
			}
		}
		return new ArrayList<AccessibilityNodeInfo>();
	}
	
	public	void listViewToLog(AccessibilityNodeInfo source){
		if (source==null) {
			return;
		}
		int cou=source.getChildCount();
		for (int i = 0; i < cou; i++) {
			if (source.getChild(i).getChildCount()>0) {
				listViewToLog(source.getChild(i));
			}
			
		}
	}
	
}
