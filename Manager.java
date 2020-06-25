import java.net.*;
import java.io.*;
import java.util.*;
import com.panayotis.gnuplot.JavaPlot;

class Manager{
    public static void main (String args[]){
        try{
            int serverPort = 7896;
            ServerSocket listenSocket = new ServerSocket(serverPort);
            Workload workload = new Workload();
            while (workload.finished.size() < 300){
                Socket clientSocket = listenSocket.accept();
                Connection c = new Connection(clientSocket, workload);
            } 
            listenSocket.close();
        } catch (IOException e) {System.out.println("Listen:" + e.getMessage() );}
    }
}

class Workload{
    final int nRe = 300;
    final int nIm = 200;
    int[][] mandelbrot = new int[nRe][nIm];
    Queue<Integer> toDo = new LinkedList<Integer>();
    Queue<Integer> finished = new LinkedList<Integer>();
    public Workload(){
        int index = 0;
        for (int i = 0; i < nRe; i++){
           toDo.add(i);
           for (int j =0; j < nIm; j++){
               mandelbrot[i][j] = 0;
           }
        }
    }
    
    public void Update(int re, int[] im){
        mandelbrot[re] = im;
        finished.add(re);
    }
}

class Connection extends Thread {
    DataInputStream in;
    DataOutputStream out;
    Socket clientSocket;
    int re;
    int iter;
    Workload workload;
    public Connection (Socket aClientSocket, Workload workload){
        try{
            clientSocket = aClientSocket;
            in = new DataInputStream( clientSocket.getInputStream() );
            out = new DataOutputStream( clientSocket.getOutputStream() );
            this.workload = workload;
            this.start();
        } catch (IOException e) {System.out.println("Connection: " + e.getMessage() );}
    }
    
    public void run(){
        try{
            if (workload.toDo.size() > 0){
                re = workload.toDo.poll();
                out.writeInt(re);
                int len = in.readInt();
                int[] im = new int[len];
                System.out.println(len);
                for (int i = 0; i < im.length; i++){
                     im[i] = in.readInt();
                     System.out.println(im[i]);
                }
                workload.Update(re, im);
            }
        } catch (EOFException e) {System.out.println("EOF: " + e.getMessage() );
        } catch (IOException e) {System.out.println("IO: " + e.getMessage() );
        } finally { try {clientSocket.close(); 
        }catch (IOException e){System.out.println("close: " + e.getMessage());}}
    }
}