package entity;

public class GoodsEntity {
	
	private int gid;
	private int uid;
	private String uname;
	private String uphone;
	private String gname;
	private String gprice;
	private String gdescribe;
	private String gis_sold;
	private String gsort;
	private String gimage;
	private String gcreated_at;
	private String ccount;


	public GoodsEntity(int gid, int uid, String uname, String uphone, String gname, String gprice, String gdescribe, String gis_sold, String gsort, String gimage, String gcreated_at, String ccount) {
		this.gid = gid;
		this.uid = uid;
		this.uname = uname;
		this.uphone = uphone;
		this.gname = gname;
		this.gprice = gprice;
		this.gdescribe = gdescribe;
		this.gis_sold = gis_sold;
		this.gsort = gsort;
		this.gimage = gimage;
		this.gcreated_at = gcreated_at;
		this.ccount = ccount;
	}
	
	
	public int getGid() {
		return gid;
	}
	public void setGid(int gid) {
		this.gid = gid;
	}
	public int getUid() {
		return uid;
	}
	public void setUid(int uid) {
		this.uid = uid;
	}
	public String getUname() {
		return uname;
	}
	public void setUname(String uname) {
		this.uname = uname;
	}
	public String getUphone() {
		return uphone;
	}
	public void setUphone(String uphone) {
		this.uphone = uphone;
	}
	public String getGname() {
		return gname;
	}
	public void setGname(String gname) {
		this.gname = gname;
	}
	public String getGprice() {
		return gprice;
	}
	public void setGprice(String gprice) {
		this.gprice = gprice;
	}
	public String getGdescribe() {
		return gdescribe;
	}
	public void setGdescribe(String gdescribe) {
		this.gdescribe = gdescribe;
	}
	public String getGis_sold() {
		return gis_sold;
	}
	public void setGis_sold(String gis_sold) {
		this.gis_sold = gis_sold;
	}
	public String getGsort() {
		return gsort;
	}
	public void setGsort(String gsort) {
		this.gsort = gsort;
	}
	public String getGimage() {
		return gimage;
	}
	public void setGimage(String gimage) {
		this.gimage = gimage;
	}
	public String getGcreated_at() {
		return gcreated_at;
	}
	public void setGcreated_at(String gcreated_at) {
		this.gcreated_at = gcreated_at;
	}
	public String getCcount() {
		return ccount;
	}
	public void setCcount(String ccount) {
		this.ccount = ccount;
	}
	

}
