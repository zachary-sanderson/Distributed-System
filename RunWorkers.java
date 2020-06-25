import java.net.*;
import java.io.*;

public class RunWorkers
{
    public static void main (String args[]){
        int iter = 0;
        Worker[] workers = new Worker[30];
        while (iter < 30){
            workers[iter] = new Worker();
        }
    }
}