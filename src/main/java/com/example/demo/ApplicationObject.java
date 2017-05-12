package com.example.demo;

public class ApplicationObject {
	
	//private final long id;
    //private final String content;
    private final String linkerd;
    private final String namerd;
    private final String linkerd_tcp;
    private final String linkerd_viz;
    
    public ApplicationObject(String linkerd, String namerd, String linkerd_tcp, String linkerd_viz) {
    	this.linkerd = linkerd;
    	this.namerd = namerd;
    	this.linkerd_tcp = linkerd_tcp;
    	this.linkerd_viz = linkerd_viz;
    	
    }
	
    public String getLinkerd() {
		return linkerd;
	}
	public String getNamerd() {
		return namerd;
	}
	
	public String getLinkerd_tcp() {
		return linkerd_tcp;
	}
	
	public String getLinkerd_viz() {
		return linkerd_viz;
	}
    
    /*public ApplicationObject(long id, String content) {
        this.id = id;
        this.content = content;
    }

    public long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }*/
    
   
}
