import java.math.BigInteger;
import java.util.Random;

public class key {


    public static BigInteger PriKeyGeneration(BigInteger n) {

        Random rand = new Random();
        BigInteger result;
        do {
            result = new BigInteger(n.bitLength(), rand);
        } while (result.compareTo(n) >= 0);
        System.out.println(result);
        return result;
    }



}
