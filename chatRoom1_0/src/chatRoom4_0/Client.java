package chatRoom4_0;

import java.io.*;
import java.net.*;

public class Client {
	
	
	private Socket socket = null;  
    // ���������  
    private DataOutputStream dataOutputStream = null;  
    // ����������  
    private DataInputStream dataInputStream = null;  
    private boolean isConnect = false;  
    Thread tReceive = new Thread(new ReceiveThread());  
    Thread tSend = new Thread(new sendThread());
    String name;
    int flag = 0;
    
    public static void main(String[] args) throws IOException {  
        Client chatClient = new Client();  
        chatClient.connect();
        while(!chatClient.GetIsConnect()) {
        	if(chatClient.getf() == 2) {
        		chatClient.connect();
        		chatClient.CloseSocket();
        		chatClient.BreakConnect();
            }	
        }
        
	}
    
    //���ӷ�����
    public void connect() {  
        try {  
            // �½����������  
            socket = new Socket("192.168.1.102", 4008);  
            // ��ȡ�ͻ��������  
            dataOutputStream = new DataOutputStream(socket.getOutputStream());  
            dataInputStream = new DataInputStream(socket.getInputStream());  
            System.out.println("���Ϸ����");  
            isConnect = true;  
            tReceive.start();
            tSend.start();
        } catch (UnknownHostException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
    }  
    
    public int getf() {
    	return flag;
    }
    
    public void BreakConnect() {
    	isConnect = false;
    }
    
    public void CloseSocket() throws IOException {
    	socket.close();
    }

    
    public boolean GetIsConnect() {
    	return isConnect;
    }
    private class sendThread extends Thread {
    	public void run() {
    		try {  
    			while (isConnect) {
    				String text = new BufferedReader(new InputStreamReader(System.in)).readLine();
    				dataOutputStream.writeUTF(text);  
    				dataOutputStream.flush(); 
                    if (text.equals("bye")) {  
                    	//dataOutputStream.close();
                    	socket.shutdownOutput();
                    	flag++;
                    	break;
                    }
    			}
            } catch (IOException e1) {  
                e1.printStackTrace(); 
            }  
    	}
    }
    
  
    private class ReceiveThread extends Thread {  
    	
        public void run() {  
            try {  
                while (isConnect) {  
                    String message = dataInputStream.readUTF();  
                    if (!message.equals("bye")) {  
                          System.out.println(message);
                    }  
                    else {
                    	System.out.println(message);
                    	//dataInputStream.close();
                    	socket.shutdownInput();
                    	flag++;
                    	break;
                    }
                }  
            } catch (IOException e) {  
                System.out.println("You close the client!");
            }  
        }  
  
    }  
    
    
}