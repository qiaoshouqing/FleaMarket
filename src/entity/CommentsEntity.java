package entity;

public class CommentsEntity {
	
	private int cid;
	private int uid;
	private int gid;
	private String uname;
	private String ccontent;
	private String ccreated_at;
	
	public CommentsEntity(int cid, int uid, int gid, String uname, String ccontent, String ccreated_at)
	{
		this.cid = cid;
		this.uid = uid;
		this.gid = gid;
		this.uname = uname;
		this.ccontent = ccontent;
		this.ccreated_at = ccreated_at;
	}
	public int getCid() {
		return cid;
	}
	public void setCid(int cid) {
		this.cid = cid;
	}
	public int getUid() {
		return uid;
	}
	public void setUid(int uid) {
		this.uid = uid;
	}
	public int getGid() {
		return gid;
	}
	public void setGid(int gid) {
		this.gid = gid;
	}
	public String getUname() {
		return uname;
	}
	public void setUname(String uname) {
		this.uname = uname;
	}
	public String getCcontent() {
		return ccontent;
	}
	public void setCcontent(String ccontent) {
		this.ccontent = ccontent;
	}
	public String getCcreated_at() {
		return ccreated_at;
	}
	public void setCcreated_at(String ccreated_at) {
		this.ccreated_at = ccreated_at;
	}
}
