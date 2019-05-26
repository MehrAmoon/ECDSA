import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class Signature {

    public static String message = "Mehrnaz";

//    public static BigInteger[] PDoubling(BigInteger[] xy, BigInteger p, BigInteger aa) {
//
//        BigInteger[] value = new BigInteger[2];
//        BigInteger i, j;
//        i = xy[0].multiply(xy[0]).multiply(BigInteger.valueOf(3)).add(aa);
//        j = (xy[1].multiply(BigInteger.valueOf(2))).modInverse(p);
//        i = (i.multiply(j)).mod(p);
//        j = i.multiply(i);
//        j = (j.subtract(xy[0].multiply(BigInteger.valueOf(2)))).mod(p);
//        value[0] = j;
//        value[1] = (i.multiply(xy[0].subtract(j))).subtract(xy[1]).mod(p);
//
//        return value;
//    }

//    public static BigInteger[] PAddition(BigInteger[] PVal, BigInteger[] xy, BigInteger p) {
//
//        BigInteger[] value = new BigInteger[2];
//        BigInteger xx = (xy[0].subtract(PVal[0]));
//        BigInteger yy = (xy[1].subtract(PVal[1]));
//        xx = xx.modInverse(p);
//        yy = yy.multiply(xx).mod(p);
//        xx = yy.multiply(yy);
//        xx = ((xx.subtract(PVal[0])).subtract(xy[0])).mod(p);
//        value[0] = xx;
//        value[1] = (xx.multiply(PVal[0].subtract(yy))).subtract(xy[1]).mod(p);
//
//        return value;
//    }

    public static BigInteger[] PAddition(BigInteger[] PVal, BigInteger[] xy, BigInteger a, BigInteger p) {
        BigInteger x, y, s;
        BigInteger[] value = new BigInteger[2];
//        System.out.println(PVal[0]);
//        System.out.println(PVal[1]);
        if (PVal[0].equals(p.add(BigInteger.ONE)) && PVal[1].equals(p.add(BigInteger.ONE))) {
            if (PVal[0].equals(xy[0]) && PVal[1].equals(xy[1])) {
                x = p.add(BigInteger.ONE);
                y = p.add(BigInteger.ONE);
            } else {
                x = xy[0];
                y = xy[1];
            }
        } else if (xy[0].equals(p.add(BigInteger.ONE)) && xy[1].equals(p.add(BigInteger.ONE))) {
            x = PVal[0];
            y = PVal[1];
        } else if (PVal[0].equals(xy[0]) && PVal[1].equals(xy[1])) {
            if (PVal[1].multiply(BigInteger.valueOf(2)).gcd(p).equals(BigInteger.ONE)) {
                s = (PVal[0].pow(2).multiply(BigInteger.valueOf(3)).add(a))
                        .multiply(PVal[1].multiply(BigInteger.valueOf(2)).modInverse(p)).mod(p); // (3x1^2+a)/2y1 (mod p)
                x = s.pow(2).subtract(PVal[0]).subtract(xy[0]).mod(p);  //s^2 - x1 - x2 (mod p)
                y = s.multiply(PVal[0].subtract(x)).subtract(PVal[1]).mod(p);  //s(x1-x3) - y1 (mod p)
            } else {
                x = p.add(BigInteger.ONE);
                y = p.add(BigInteger.ONE);
            }
        } else {
            if (xy[0].subtract(PVal[0]).gcd(p).equals(BigInteger.ONE)) {
                s = (xy[1].subtract(PVal[1]))
                        .multiply(xy[0].subtract(PVal[0]).modInverse(p)).mod(p); // (y2-y1)/(x2-x1) (mod p)
                x = s.pow(2).subtract(PVal[0]).subtract(xy[0]).mod(p);  //s^2 - x1 - x2 (mod p)
                y = s.multiply(PVal[0].subtract(x)).subtract(PVal[1]).mod(p);  //s(x1-x3) - y1 (mod p)
            } else {
                x = p.add(BigInteger.ONE);
                y = p.add(BigInteger.ONE);
            }
        }

        value[0] = x;
        value[1] = y;
        return value;

    }

    public static BigInteger[] Pmulti(BigInteger[] xy, BigInteger p, BigInteger a, BigInteger mult) {
        BigInteger[] PVal = new BigInteger[2];
        BigInteger x = p.add(BigInteger.ONE);
        BigInteger y = p.add(BigInteger.ONE);

        PVal[0] = x;
        PVal[1] = y;
//        boolean set = false;
        String DBin = mult.toString(2);
        int DBinLength = DBin.length();

        for (int i = 0; i < DBinLength; i++) {
            PVal = PAddition(PVal, PVal, a, p);
            if (DBin.charAt(i) == '1') {
                PVal = PAddition(PVal, xy, a, p);

            }
//            xy = PDoubling(xy, p, a);
        }
        return PVal;
    }


    public static BigInteger[] EcdsaSignGeneration(BigInteger[] xy, BigInteger a, BigInteger q, BigInteger p, BigInteger privKey) throws NoSuchAlgorithmException {

        BigInteger r, e, s, k, Kinv, ke;
        BigInteger[] kg;
        do {
            ke = new BigInteger(q.bitLength(), new Random());
        } while (!ke.gcd(p).equals(BigInteger.ONE));

//        do {
//            do {
//                k = BigInteger.valueOf(1);
        kg = Pmulti(xy, p, a, ke);
        r = kg[0];
//
//            } while (r.compareTo(BigInteger.ZERO) == 0);
        MessageDigest messD = MessageDigest.getInstance("SHA-256");
        byte[] hash = messD.digest(message.getBytes(StandardCharsets.UTF_8));
        s = r.multiply(privKey).add(new BigInteger(hash))
                .multiply(ke.modInverse(q)).mod(q);
//            Kinv = k.modInverse(q);
//            s = (Kinv.multiply(e.add(privKey.multiply(r)))).mod(q);

        // xy = PDoubling(xy, q, a);

//        } while (s.compareTo(BigInteger.ZERO) == 0);

        kg[0] = r;
        kg[1] = s;
        System.out.println("kg0: " + kg[0]);
        System.out.println("kg1: " + kg[1]);
        System.out.println("Mess Signed");
        return kg;
    }

    public static boolean EcdsaSignVerification(BigInteger[] sign, BigInteger q, BigInteger p, BigInteger[] xy, BigInteger a, BigInteger[] pubKey) throws NoSuchAlgorithmException {
        BigInteger r = sign[0];
        BigInteger s = sign[1];

        System.out.println("r: " + r);
        System.out.println("s: " + s);
        System.out.println("q: " + q);
//        System.out.println("com: "+ s.compareTo(q));


//        if (r.compareTo(q) >= 0) {
//            System.out.println("Message NOT VERIFIED");
//            return false;
//        }
//        if (s.compareTo(q) >= 0) {
//            System.out.println("Message NOT VERIFIED");
//            return false;
//        }
String Mes="Mehrnaz";
        MessageDigest messD = MessageDigest.getInstance("SHA-256");
        byte[] hashh = messD.digest(Mes.getBytes(StandardCharsets.UTF_8));

      //  BigInteger e = new BigInteger(hashh);
        BigInteger w = s.modInverse(q);

        BigInteger u1 = w.multiply(new BigInteger(hashh)).mod(q);
        BigInteger u2 = w.multiply(r).mod(q);

        BigInteger[] X = PAddition(Pmulti(xy, p, a, u1), Pmulti(pubKey, p, a, u2), a, p);

        BigInteger v = X[0];

        System.out.println("v: " + v);
        System.out.println("r: " + r);
        System.out.println("com: " + v.compareTo(r.mod(q)));

        if (v.compareTo(r.mod(q)) == 0) {
            System.out.println("Message VERIFIED");
            return true;
        }
else {System.out.println("Message NOT VERIFIED");
            return false;}

    }

}