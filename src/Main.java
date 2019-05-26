import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

public class Main {


    public static void main(String[] args) throws NoSuchAlgorithmException {
        BigInteger p = null, q = null, a = null, c, b, seed, x = null, y= null;
        BigInteger[] xy ;
        BigInteger privateKey;
        BigInteger[] publicKey;

        File ec192 = new File("src/ECP192.txt");
        try {
            Scanner Scan = new Scanner(ec192);
            p = new BigInteger(Scan.nextLine());
            q = new BigInteger(Scan.nextLine());
            seed = new BigInteger(Scan.nextLine(), 16);
            c = new BigInteger(Scan.nextLine(), 16);
            b = new BigInteger(Scan.nextLine(), 16);
            x = new BigInteger(Scan.nextLine(), 16);
            y = new BigInteger(Scan.nextLine(), 16);
            a = BigInteger.valueOf(-3);
            Scan.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        privateKey = key.PriKeyGeneration(q);

        xy = new BigInteger[2];
        xy[0] = x;
        xy[1] = y;


        publicKey = Signature.Pmulti(xy, p, a, privateKey);
        System.out.println("x: " + xy[0]);
        System.out.println("y: " + xy[1]);

        BigInteger[] SignPair = Signature.EcdsaSignGeneration(xy, a, q, p, privateKey);
        System.out.println(Signature.EcdsaSignVerification(SignPair, q, p, xy, a, publicKey));
        // System.out.println("Hello World!");
    }
}
