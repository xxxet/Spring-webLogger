package webLogger.service;

public class Visitor {

    //  private Integer id;
    private String ip;
    private String date;
    private String post;
    private String useragent;

    /*   public void setID(Integer id) {
     this.id = id;
     }*/
    /* public Integer getID() {
     return id;
     }*/
    public void setIp(String ip) {
        // System.out.println("setIP: " + ip);
        this.ip = ip;
    }

    public void setDate(String date) {
        //System.out.println("setDate: " + date);
        this.date = date;
    }

    public void setPost(String post) {
        // System.out.println("setpost: " + post);
        this.post = post;
    }
    
     void setUserAgent(String useragent) {
       this.useragent=useragent;
    }

    public String getIp() {
        return ip;
    }

    public String getDate() {
        return date;
    }

    public String getPost() {
        return post;
    }

    public  String getUserAgent() {
       return useragent;
    }
     
    @Override
    public String toString() {
        String s = ip + " " + date + " " + post + " " + useragent;
        //System.out.println("toString(): " + s);
        return s;
    }

}
