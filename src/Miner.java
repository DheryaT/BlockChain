import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class Miner implements Callable<Block>, Serializable {
    BlockChain blockChain;
    Block b;
    int N;

    public Miner(BlockChain blockChain, int n) {
        this.blockChain = blockChain;
        N = n;
    }

    /**
     * Method to mine a block
     */
   @Override
    public Block call() throws Exception {
        ArrayList<Transaction> data = blockChain.getTransactions();
        User user= new User("miner"+ Thread.currentThread().getId());
        b = new Block(blockChain.getNextId(), blockChain.getPreviousHash(), N, user,data);
        return b;
    }
}
