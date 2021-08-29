import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main implements Serializable {
    public static void main(String[] args) {
        SerializationUtils util = new SerializationUtils();
        //BlockChain blockChain = new BlockChain(); - If you want to start a new BLock chain
        BlockChain blockChain = util.load();

        User tom = new User("Tom");
        User jeff = new User("Jeff");

        List<User> rewarded = new ArrayList<>(); //

        for (int i = 0; i < 5; i++) {

            if(i == 0){
                blockChain.addTransaction(tom.createTransaction(10, jeff,blockChain.getmID() ));
            }

            int size = blockChain.getSize();
            int poolSize = 1;
            ExecutorService executor = Executors.newFixedThreadPool(poolSize);
            List<Miner> miners = new ArrayList<Miner>();

            for (int j = 0; j < poolSize; j++) {
                miners.add(new Miner(blockChain, blockChain.getN()));
            }
            Block nBlock = null;
            try {
                nBlock = executor.invokeAny(miners);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

            blockChain.addBlock(nBlock);
            rewarded.add(nBlock.creator);

            executor.shutdownNow();

        }

        System.out.println(blockChain.toString());
        util.save(blockChain);

    }
}
