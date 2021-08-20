import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigInteger;

public class RSA {
    // 公钥 Public Key
    public int n = 0; // p*q
    public int e = 0;// co-prime number of λ(n) between 1 to  λ(n)
    // 私钥 Private Key
    private int d = 0; // e's modular multiplicative inverse `de === 1  (mode n)`

    public int encrypt(int m) {
        return BigInteger.valueOf(m).pow(e).mod(BigInteger.valueOf(n)).intValue();
    }

    private int decrypt(int m) {
        return BigInteger.valueOf(m).pow(d).mod(BigInteger.valueOf(n)).intValue();
    }

    private int sign(int m){
        return BigInteger.valueOf(m).pow(d).mod(BigInteger.valueOf(n)).intValue();
    }

    private int verify(int m){
        return BigInteger.valueOf(m).pow(e).mod(BigInteger.valueOf(n)).intValue();
    }

    /**
     * 用两个质数初始化公钥和私钥
     */
    void init(int p, int q){
        n = p*q;
        // λ(n)=lcm(λ(p),λ(q))=lcm(φ(p),φ(q))=lcm(p-1,q-1)
        int lambdaN = lcm(p-1,q-1);
        e = coprime(lambdaN);
        d = mmi(e, lambdaN);
        System.out.println("RSA p="+p+", q="+q+", n="+n+", lambda(n)="+lambdaN+", e="+e+", d="+d);
    }

    /**
     * 找一个互质数
     */
    int coprime(int n){
        int x = 3;
        for(;x<n;x++){
            boolean coprime = true;
            for(int i=2;i<=x;i++){
                if(n%i==0 && x% i==0){
                    coprime=false;
                    break;
                }
            }
            if(coprime)return x;
        }
        throw new IllegalArgumentException("Cannot find co-prime number for "+n);
    }

    /**
     * 最小公倍数
     */
    int lcm(int a, int b) {
        int x = 1;
        for(;x<=b;x++){
           if( a * x % b ==0) break;
        }
        return a*x;
    }

    /**
     * Modular Multiplicative Inverse
     * 模反数
     */
    int mmi(int e, int lambdaN) {
        int d = 2;
        for (; d < lambdaN; d++) {
            if (d * e % lambdaN == 1) break;
        }

        return d;
    }


    public static void main(String... args) throws Exception{
        RSA rsa = new RSA();
        rsa.init(17,23);
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            System.out.println("Please input a number:");
            int plain = Integer.valueOf(in.readLine());
            int encrypted = rsa.encrypt(plain);
            int decrypted = rsa.decrypt(encrypted);
            System.out.println("Encrypt/Decrypt: "+plain + "->" + encrypted + "->" + decrypted);
            int signature = rsa.sign(plain);
            int verify = rsa.verify(signature);
            System.out.println("Sign/Verify: "+plain + "->" + signature + "->" + verify);
        }
    }
}
