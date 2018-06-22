package chatRoom3_0;

import java.io.*;
import java.net.*;
import java.util.*;

public class MultiTalkServer{
	static int clientnum=0;
	static Hashtable<String,Socket> talk = new Hashtable<String,Socket>(); //��̬��Ա��������¼��ǰ�ͻ��ĸ���
	public static void main(String args[]) throws IOException {
		ServerSocket serverSocket=null;
		boolean listening=true;
		try{
			//����һ��ServerSocket�ڶ˿�4700�����ͻ�����
			serverSocket=new ServerSocket(4008); 			
		}catch(IOException e) {
			System.out.println("Could not listen on port:4008.");
			//��������ӡ������Ϣ
			System.exit(-1); //�˳�
		}
		while(listening){ //ѭ������
			if(clientnum != 0) {
				//�������ͻ����󣬸��ݵõ���Socket����Ϳͻ��������������̣߳�������֮
				Socket socket1 = serverSocket.accept();
				String line1,line2,line3,name,othername;
				//��Socket����õ�����������������Ӧ��BufferedReader����
				BufferedReader is=new BufferedReader(new 
						InputStreamReader(socket1.getInputStream()));
				//��Socket����õ��������������PrintWriter����
				PrintWriter os=new PrintWriter(socket1.getOutputStream());
				line1 = new String("������֣�");
				line2 = new String("�������������");
				line3 = new String("û�д��û�");
				//��ϵͳ��׼�����豸����BufferedReader����
				BufferedReader sin=new BufferedReader(new InputStreamReader(System.in));
				//�ڱ�׼����ϴ�ӡ�ӿͻ��˶�����ַ���
				os.println(line1);
				os.flush();
				name=is.readLine();
				talk.put(name, socket1);
				while(true) {
					os.println(line2);
					os.flush();
					othername = is.readLine();
					if(talk.containsKey(othername))
					{
						new WriteThread(name,talk.get(name),othername,talk.get(othername)).start();
						new ReadThread(name,talk.get(name),othername,talk.get(othername)).start();
						clientnum++; //���ӿͻ�����
						break;
					}
					else {
						os.println(line3);
						os.flush();
					}
				}
			}
			else
			{
				Socket socket1 = serverSocket.accept();
				String line1,line2,name;
				//��Socket����õ�����������������Ӧ��BufferedReader����
				BufferedReader is=new BufferedReader(new 
						InputStreamReader(socket1.getInputStream()));
				//��Socket����õ��������������PrintWriter����
				PrintWriter os=new PrintWriter(socket1.getOutputStream());
				line1 = new String("������֣�");
				line2 = new String("����������ֻ����һ���û����޷��������û����죬��ȴ�������");
				//��ϵͳ��׼�����豸����BufferedReader����
				os.println(line1);
				os.flush();
				name=is.readLine();
				talk.put(name, socket1);
				os.println(line2);
				os.flush();
				clientnum++; //���ӿͻ�����
			}
		}
		serverSocket.close(); //�ر�ServerSocket
	}
}