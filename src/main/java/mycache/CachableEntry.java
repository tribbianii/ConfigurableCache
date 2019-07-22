package mycache;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class CachableEntry implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3089740212644542247L;
	private String key;
	private Object data;
	private long size;
	
	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		CachableEntry e=(CachableEntry)obj;
		return this.key.equals(e.key)&&this.data.equals(e.data)&&this.size==e.size;
	}
	
	
	
	
	public CachableEntry(String key, Object data) {
		//super();
		this.key = key;
		this.data = data;
		size=this.getObjectSize(data);
		
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	public long getSize() {
		return size;
	}
	public void setSize(long size) {
		this.size = size;
	}
	
	private long getObjectSize(Object o) {
		ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
		ObjectOutputStream objectOutputStream=null;
		
		try {
			objectOutputStream=new ObjectOutputStream(byteArrayOutputStream);
			objectOutputStream.writeObject(o);
			objectOutputStream.flush();
			return (long)byteArrayOutputStream.toByteArray().length;
			//System.out.println();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			try {
				objectOutputStream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return 0;
	}
}
