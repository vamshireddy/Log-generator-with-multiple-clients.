import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;
import java.util.Arrays;
 
 
public class Master {
       
        public static byte[] getSeqNoArr(int seqNo)
        {
                ByteBuffer seqNobuff = ByteBuffer.allocate(4);
                seqNobuff.putInt(seqNo);
                return seqNobuff.array();
        }
       
        public static byte[] combine(byte b, byte[] arr1)
        {
                byte[] arr = new byte[1+arr1.length];
                arr[0] = b;
                for(int i=0;i<arr1.length;i++)
                {
                        arr[1+i] = arr1[i];
                }
                return arr;
        }
       
        public static byte[] combine(byte b, byte[] arr1, byte[] arr2)
        {
                byte[] arr = new byte[1+arr1.length+arr2.length];
                arr[0] = b;
                for(int i=0;i<arr1.length;i++)
                {
                        arr[1+i] = arr1[i];
                }
                for(int i=0;i<arr2.length;i++)
                {
                        arr[1+arr1.length+i] = arr2[i];
                }
                return arr;
        }
       
        public static void main(String args[])
        {
               
	    if( args.length != 4 )
	    {
		System.out.println("Args : IP, port, timeout in ms, no of clients");
            	System.exit(0);
	    }
                Integer c = 1;
                int count = Integer.parseInt(args[3]);
            DatagramSocket socket = null ;
            byte[] recvbuff;
            int dupCount = 0;
	    int port = Integer.parseInt(args[1]);
	    while( c <= count )
            {
                try
                {
                         int seqNo = 1234;
                         
                         recvbuff = new byte[1024];
                         
                         InetAddress host = InetAddress.getByName(args[0]);
                         socket = new DatagramSocket( c+20000) ;
                         socket.setSoTimeout(Integer.parseInt(args[2]));
                         
                         String msg = "hello Vamshi!";
                         byte[] seqNoArr = getSeqNoArr(seqNo);
                         byte type = 1;
                         
                         recvbuff = new byte[1024];
                         DatagramPacket recvPacket = new DatagramPacket(recvbuff, recvbuff.length);
                         DatagramPacket packet = null;
                         
                         byte[] regData = combine(type, seqNoArr);
                         
                         packet = new DatagramPacket( regData , regData.length, host, port ) ;
                         while( true )
                         {
                                 socket.send(packet);
                                 System.out.println("SENT: "+new String(regData));
                                 try
                                 {
                                         socket.receive(recvPacket);
                                 }
                                 catch( SocketTimeoutException e )
                                 {
					 dupCount++;
                                         continue;
                                 }
                                 break;
                         }
                         System.out.println("Sent register");
                         seqNo++;
                         
                         seqNoArr = getSeqNoArr(seqNo);
                         type = 2;
                         byte[] logData = combine(type, seqNoArr, msg.getBytes());
                         packet = new DatagramPacket( logData, logData.length, host, port );
                         
                         while( true )
                         {
                                 socket.send(packet);
                                 System.out.println("SENT: "+new String(logData));
                                 try
                                 {
                                         socket.receive(recvPacket);
                                 }
                                 catch( SocketTimeoutException e )
                                 {
					 dupCount++;
                                         continue;
                                 }
                                 break;
                         }
                         System.out.println("Sent the log");
                         seqNo++;
                         
                         seqNoArr = getSeqNoArr(seqNo);
                         type = 3;
                         
                         byte[] deregisterData = combine(type, seqNoArr);
                         packet = new DatagramPacket( deregisterData, deregisterData.length, host, port ) ;
                         while( true )
                         {
                                 socket.send(packet);
                                 System.out.println("SENT: "+new String(deregisterData));
                                 try
                                 {
                                         socket.receive(recvPacket);
                                 }
                                 catch( SocketTimeoutException e )
                                 {
					 dupCount++;
                                         continue;
                                 }
                                 break;
                         }
                         System.out.println("DONE");
                         socket.close();
                      }
                      catch( IOException e )
                      {
                          e.printStackTrace();
                      }
                      c++;
              }
	      System.out.println("Duplicates : "+dupCount);
        }
}
