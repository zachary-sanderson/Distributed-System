import java.net.*;
import java.io.*;

public class Worker{
    static final int nRe = 300;
    static final int nIm = 200;
    static final int z_Re_max = 1;
    static final int z_Re_min = -2;
    static final int z_Im_max = -1;
    static final int z_Im_min = 1;
    public Worker (){
        Socket s=null;
        try{
            int ServerPort=7896;
            s = new Socket("localhost",ServerPort);
            DataInputStream in = new DataInputStream(s.getInputStream());
            DataOutputStream out = new DataOutputStream(s.getOutputStream());
            int re = in.readInt();
            System.out.println("Received: " + re);
            int[] im = DoWork(re);
            out.writeInt(im.length);
            for (int i = 0; i < im.length; i++){
                out.writeInt(im[i]);
            }
        } catch (UnknownHostException e) {System.out.println("Sock: " + e.getMessage());
        } catch (EOFException e) {System.out.println("EOF: " + e.getMessage());
        } catch (IOException e) {System.out.println("IO: " + e.getMessage());
        } finally {if (s != null) try {s.close();
            } catch (IOException e) {System.out.println("close: " + e.getMessage());}}
    }
    
    private static int[] DoWork(int re){
        int [] mandelbrot = new int[nIm];
        for (int j=0; j<mandelbrot.length; j++){
            float z0_Re = ((float)re/(float)nRe) * (z_Re_max - z_Re_min) + z_Re_min;
            float z0_Im = ((float)j/(float)nIm) * (z_Im_max - z_Im_min) + z_Im_min;
            float z_Re = z0_Re;
            float z_Im = z0_Im;
                
            int niter = 0;
            mandelbrot[j] = niter;
            while (niter < 100){
                float z_sq_re = z_Re*z_Re - z_Im*z_Im; // Re(z^2)
                float z_sq_im = (float) 2.0*z_Re*z_Im; // Im (z^2)
                float mod_z_sq_sq = z_sq_re*z_sq_re + z_sq_im*z_sq_im; // |z^2|^2
                if ( mod_z_sq_sq > 4 ) break; // Equivalent to |z^2|>2
                z_Re = z_sq_re + z0_Re; // Update z to value at next iteration
                z_Im = z_sq_im + z0_Im;
                mandelbrot[j] = niter;
                niter++; // Update iteration counter
            }
        }
        return mandelbrot;
    }
}