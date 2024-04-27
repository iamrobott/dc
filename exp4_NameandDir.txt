import java.io.*;
import java.net.*;

public class NameResolution {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Enter the website URL (like google.com) to resolve its Name to Address: ");
        String name = br.readLine();
        try {
            InetAddress ip = InetAddress.getByName(name);
            System.out.println("\nIP Address: " + ip.getHostAddress());
        } catch (UnknownHostException e) {
            System.err.println("\n\n No such Host is present ... ");
            System.out.println("\n Try Again !!");
        }
    }
}