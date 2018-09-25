/**
 * 
 */
package cn.ourwill.huiyizhan.utils;

/**
 * @author thomasong
 */
public enum ResultEnum {
	SUCCESS(0,"成功"),ERROR(-1,"失败"),RELOGIN(401,"重新登录");
	private int code;
	private String name;
	private ResultEnum(int code, String name) {
		this.code = code;
		this.name = name;
	}
	
	
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
	
	

}
