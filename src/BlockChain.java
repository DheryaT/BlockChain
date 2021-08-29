import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.*;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.util.*;

public class BlockChain implements Serializable {

    private final ArrayList<Block> chain;
    private final ArrayList<String> nChanges = new ArrayList<>();
    private final ArrayList<Transaction> pendingTransactions= new ArrayList<>();
    private int N =0;
    private int mID = 1;

    public BlockChain(){
        chain = new ArrayList<Block>();
    }

    /**
     * Adds a block to the blockchain
     */
    public synchronized void addBlock(Block block ){
        if(block.getPreviousHash().equals(getPreviousHash())) {
            clearTransactions();
            chain.add(block);
            long time = block.getTimeTaken();
            if(time < 60){
                N = N +1;
                nChanges.add("N was increased to "+N+"\n");
            }else{
                if(N>0) {
                    N = N - 1;
                    nChanges.add("N was decreased to "+N+"\n");
                }else{
                    nChanges.add("N stayed at "+N+"\n");
                }

            }

        }

    }

    /**
     * Gets the hash of the previous block
     */
    public synchronized String getPreviousHash(){
        if(!chain.isEmpty()) {
            return chain.get(chain.size() - 1).getHash();
        }else{
            return "0";
        }

    }

    /**
     * Adds a valid transaction to the list of transactions
     */
    public void addTransaction(Transaction transaction){

        if(verifyTransaction(transaction)&&validateBalance(transaction)){
            pendingTransactions.add(transaction);
            mID++;
        }else{
            System.out.println("Not valid transaction: "+transaction.getUid());
        }

    }

    /**
     * Verifies if signature was really signed by the users private key
     */
    public boolean verifyTransaction(Transaction msg){
        byte[] encryptedMessageHash = msg.getSignature();
        PublicKey pk = msg.getUser().getPublicKey();
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, pk);
            byte[] decryptedMessageHash = cipher.doFinal(encryptedMessageHash);
            MessageDigest md = null;
            md = MessageDigest.getInstance("SHA-256");
            byte[] newMessageHash = md.digest(msg.getTransaction().getBytes());
            return Arrays.equals(decryptedMessageHash, newMessageHash);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Verifies user trying to send has enough balance to send
     */
    public boolean validateBalance(Transaction msg){
        User user = msg.getUser();
        int received= 0;
        int sent = 0;
        for(Block b: chain){
            ArrayList<Transaction> bTrans = b.getData();
            Transaction reward = b.getReward();
            for(Transaction t: bTrans){
                if(t.getToUser().getName().equals(user.getName())){
                    received += t.getAmount();
                }
                if(t.getUser().getName().equals(user.getName())){
                    sent += t.getAmount();
                }

            }
            if(reward.getToUser().getName().equals(user.getName())){
                received+= reward.getAmount();
            }
        }
        sent += msg.getAmount();
        return ((received-sent) >= 0);
    }

    public ArrayList<Transaction> getTransactions(){
        ArrayList<Transaction> newT = new ArrayList<>();
        newT.addAll(pendingTransactions);
        return newT;
    }

    public void clearTransactions(){
        pendingTransactions.clear();
    }

    public int getSize(){
        return chain.size();
    }

    public int getN() {
        return N;
    }

    public int getmID() {
        return mID;
    }

    public synchronized int getNextId() {
        return chain.size() + 1;
    }

    public String toString(){
        StringBuilder str = new StringBuilder();
        for(int i = 0; i < chain.size(); i++){
            str.append(chain.get(i).toString());
            str.append(nChanges.get(i));
            str.append("\n");
        }
        return str.toString();
    }

}
