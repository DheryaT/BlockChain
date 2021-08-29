import java.io.Serializable;

public class Transaction implements Serializable {
    User user;
    User toUser;
    int amount;
    int uid;
    byte[] signature;
    boolean reward;
    String transaction;

    public Transaction(User user,User toUser, int amount, int uid, byte[] signature, Boolean reward, String transactionMsg) {
        this.user = user;
        this.toUser = toUser;
        this.amount = amount;
        this.uid = uid;
        this.signature = signature;
        this.reward= reward;
        this.transaction = transactionMsg;
    }

    public byte[] getSignature() {
        return signature;
    }

    public User getUser() {
        return user;
    }

    public int getAmount() {
        return amount;
    }

    public User getToUser() {
        return toUser;
    }

    public boolean isReward() {
        return reward;
    }

    public int getUid() {
        return uid;
    }

    public String getTransaction() { return transaction;}
}
