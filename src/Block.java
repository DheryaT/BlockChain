import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.time.*;
import java.security.MessageDigest;
import java.util.Date;

public class Block implements Serializable {
    int id;
    long timeStamp;
    int magicNumber;
    long timeTaken;
    String previousHash;
    String hash;
    User creator;
    Transaction reward;
    ArrayList<Transaction> data;

    public Block(int id, String previousHash, int zeros, User user, ArrayList<Transaction> transactions) {
        this.id = id;
        timeStamp = new Date().getTime();
        this.previousHash = previousHash;
        creator = user;
        data = transactions;
        String message = creator.getName()+" gets 100 VC";
        reward = new Transaction(null, creator, 100, 0, null, true, message);
        calculateHash(zeros);
    }

    /**
     * Calculates hash of the block that starts with amount of zeros specified by the parameter
     */
    public String calculateHash(int zeros){
        long startTime = System.currentTimeMillis();
        String start = "";
        for(int i =0 ; i < zeros; i++){
            start = start + "0";
        }
        Random rand = new Random();
        hash = "";
        String strID = String.valueOf(id);
        String strTime = String.valueOf(timeStamp);
        magicNumber = rand.nextInt();
        hash = applySha256(strID+reward.getTransaction()+ magicNumber +strTime + previousHash+ transactionsToString());

        while(!hash.startsWith(start)&&!start.equals("")) {
            magicNumber = rand.nextInt();
            hash = applySha256(strID + magicNumber +strTime + previousHash+data);
        }
        long endTime = System.currentTimeMillis();
        long duration = (endTime - startTime);
        timeTaken = duration/1000;
        return hash;
    }

    /**
     * Creates a string from the transactions list
     */
    public String transactionsToString(){
        String str = "";
        for(int i = 0; i < data.size(); i++){
            str = str + data.get(i).getTransaction()+"\n";
        }
        return str;
    }


    @Override
    public String toString(){
        String transaction = transactionsToString();
        if(transaction.equals("")){
            transaction = "No Transactions\n";
        }
        String str = "Block:\nCreated by miner # "+creator.getName()+"\n"+reward.getTransaction()+"\nId: "+ id +"\nTimestamp: "+ timeStamp +"\nMagic number: "+magicNumber+"\nHash of the previous block:\n"+previousHash+"\n"+"Hash of the block:\n"+hash+"\nBlock data: \n"+transaction+"Block was generating for "+timeTaken+" seconds\n";
        return str;
    }


    public static String applySha256(String input){
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            /* Applies sha256 to our input */
            byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte elem: hash) {
                String hex = Integer.toHexString(0xff & elem);
                if(hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        }
        catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Transaction getReward() {
        return reward;
    }

    public ArrayList<Transaction> getData() {
        return data;
    }

    public int getId() {
        return id;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public int getMagicNumber() {
        return magicNumber;
    }

    public long getTimeTaken() {
        return timeTaken;
    }

    public String getPreviousHash() {
        return previousHash;
    }

    public String getHash() {
        return hash;
    }
}
