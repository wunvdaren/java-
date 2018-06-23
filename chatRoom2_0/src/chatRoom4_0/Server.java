package chatRoom4_0;

import java.io.*;  
import java.net.*;  
import java.util.*;  

public class Server {
	
	private boolean isStart = false;  
    // �����socket  
    private ServerSocket ss = null;  
    // �ͻ���socket  
    private Socket socket = null;  
    // ����ͻ��˼���  
    Hashtable <String,ClientThread> chat = new Hashtable<String,ClientThread>();  
    
    
    public static void main(String[] args) {  
        new Server().start();  
    }  
  
    public void start() {  
        try {  
            // ����������  
            ss = new ServerSocket(4008);  
        } catch (BindException e) {  
            System.out.println("�˿�����ʹ����");  
            // �رճ���  
            System.exit(0);  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
  
        try {  
            isStart = true;  
            while (isStart) {  
                // ��������  
                socket = ss.accept();  
                System.out.println("one client connect");  
                // �����ͻ����߳�  
                ClientThread client = new ClientThread(socket);
                new Thread(client).start();  
                 
            }  
        } catch (Exception e) {  
            e.printStackTrace();  
        } finally {  
            // �رշ���  
            try {  
                ss.close();  
                isStart = false;
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
        }  
  
    }  
  
  
 
    private class ClientThread extends Thread {  
        // �ͻ���socket  
        private Socket socket = null;  
        // �ͻ���������  
        private BufferedReader dataInputStream = null;  
        // �ͻ��������  
        private PrintWriter dataOutputStream = null;  
        private boolean isConnect = false;  
        private String myname;
  
        public ClientThread(Socket socket) {  
            this.socket = socket;  
            try {  
                isConnect = true;  
                // ��ȡ�ͻ���������  
                dataInputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));  
                // ��ȡ�ͻ��������  
                dataOutputStream = new PrintWriter(socket.getOutputStream());  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
        }  
  
        // ��ͻ���Ⱥ����ת�������� 
         
        public void sendMessageToClients(String message) {  
            dataOutputStream.println(message);
			dataOutputStream.flush();  
        }  
        
        
        public void setname()  {
        	String line = new String("������������֣�");
        	
        	sendMessageToClients(line);
        	
        	try {
				myname = dataInputStream.readLine();
				System.out.println(myname);
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
        
        public String getname() {
        	return myname;
        }
  
          
        public void run() {  
            isConnect = true;  
            ClientThread c = null;
            String othername = null;
            setname();
            String name = getname();
            chat.put(name,this); 
            if(isConnect) {
            	while(true) {
            	    String line = new String("��������������Ķ���");
            	    sendMessageToClients(line);
            	    try {
            	    	othername =  dataInputStream.readLine();
            	    	if(chat.containsKey(othername)) {
            	    		String l = "��ȷ������";
            	    		sendMessageToClients(l);
            	    		break;
            	    		}
            	    	else {
            	    		String l = "�����û������ڣ��������룺";
            	    		sendMessageToClients(l);
            	    	}
            	    } catch (IOException e) {
            	    	e.printStackTrace();
            	    }
            	}
            }
            try {  
            	c = chat.get(othername);  
                while (isConnect) {  
                    // ��ȡ�ͻ��˴��ݵ�����  
                    String message = dataInputStream.readLine();  
                    c.sendMessageToClients(message); 
                }  
            } catch (EOFException e) {  
                System.out.println("client closed!"); 
                sendMessageToClients("client closed!");
            } catch (SocketException e) {  
                if (c != null) {  
                    chat.remove(c);  
                }  
                System.out.println("Client is Closed!!!!");  
                sendMessageToClients("client closed!");
            } catch (Exception e) {  
                e.printStackTrace();  
            } finally {  
                // �ر������Դ  
                try {  
                    if (dataInputStream != null) {  
                        dataInputStream.close();  
                    }  
                    if (socket != null) {  
                        socket.close();  
                        socket = null;  
                    }  
                } catch (IOException e) {  
                    e.printStackTrace();  
                }  
            }  
        }  
    }  
 
}