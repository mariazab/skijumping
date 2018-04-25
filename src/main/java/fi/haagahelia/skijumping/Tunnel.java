package fi.haagahelia.skijumping;
import com.jcraft.jsch.*;

public class Tunnel {
   
   
    public void go() throws Exception{
        String host="";
        String user="username";
        String password="password";
        int port=22;
        
        int tunnelLocalPort=0000;
        String tunnelRemoteHost="";
        int tunnelRemotePort=3306;
        
        System.out.println("trying to connect...");
        JSch jsch=new JSch();
        Session session=jsch.getSession(user, host, port);
        session.setPassword(password);
        localUserInfo lui=new localUserInfo();
        session.setUserInfo(lui);
        session.connect();
        session.setPortForwardingL(tunnelLocalPort,tunnelRemoteHost,tunnelRemotePort);
        System.out.println("Connected");
    
    }
    
  class localUserInfo implements UserInfo{
    String passwd;
    public String getPassword(){ return passwd; }
    public boolean promptYesNo(String str){return true;}
    public String getPassphrase(){ return null; }
    public boolean promptPassphrase(String message){return true; }
    public boolean promptPassword(String message){return true;}
    public void showMessage(String message){}
  } 
}

