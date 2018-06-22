package chatRoom3_0;

import java.io.*;
import java.net.*;

public class WriteThread  extends Thread{
    public Socket socket1 = null,socket2 = null;
    public String name,othername;
    public WriteThread(String name1,Socket socket1,String name2,Socket socket2) {
    	this.socket1 = socket1;
    	this.socket2 = socket2;
    	this.name = name1;
    	this.othername = name2;
    }
    public void run() {
    	try{
			String line;
			//��Socket����õ��������������PrintWriter����
			PrintWriter os2=new PrintWriter(socket2.getOutputStream());
			//��othername�Ŀͻ��˹���BufferedReader����
			BufferedReader sin1=new BufferedReader(new InputStreamReader(socket1.getInputStream()));
			//�ڱ�׼����ϴ�ӡ�ӿͻ���1������ַ���
			System.out.println(name + ":" + sin1.readLine());
			//�ӱ�׼�������һ�ַ���
			line=sin1.readLine();
			while(!line.equals("exist")) {
				while(!line.equals("bye")){//������ַ���Ϊ "bye"����ֹͣѭ��
					os2.println(name + ":" + line);//��ͻ���������ַ���
					os2.flush();//ˢ���������ʹClient�����յ����ַ���
					//��ϵͳ��׼����ϴ�ӡ���ַ���
					System.out.println(othername + ":" + line);
					line=sin1.readLine();//��ϵͳ��׼�������һ�ַ���
				}//����ѭ��
			os2.close(); //�ر�Socket�����
			}
			socket1.close(); //�ر�Socket			
		}catch(Exception e){
			System.out.println("Error:"+e);//��������ӡ������Ϣ
		}
    }
}