import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.Serializable;
import java.security.*;

public class User implements Serializable {
    GenerateKeys keyGen;
    String name;
    private final PrivateKey privateKey;
    private final PublicKey publicKey;

    public User(String name){
        this.name = name;
        try {
            keyGen = new GenerateKeys(2048);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        privateKey = keyGen.getPrivateKey();
        publicKey = keyGen.getPublicKey();
    }

    /**
     * Create a transaction signed with a signature using the private key of the user
     */
    public Transaction createTransaction(int amount, User toUser,int uid){
        try {
            String message = name +" sent "+ amount +" VC to "+toUser.getName();
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] messageHash = digest.digest(message.getBytes());
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, privateKey);
            byte[] digitalSignature = cipher.doFinal(messageHash);

            return new Transaction(this, toUser, amount, uid, digitalSignature, false, message);

        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getName() {
        return name;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }
}
