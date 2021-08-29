
import java.io.*;

class SerializationUtils implements Serializable{

    /**
     * Save blockchain object to given file
     */
    public void save(Object b){
        try {
            File file = new File("src/blockchain.txt");
            serialize(b, file);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Loads and returns blockchain object from src directory
     */
    public BlockChain load(){
        BlockChain b = null;
        try {
            File file = new File("src/blockchain.txt");
            b = (BlockChain) deserialize(file);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        if(b==null){
            return new BlockChain();
        }else{
            return b;
        }

    }

    /**
     * Serialize the given object to the file
     */
    private static void serialize(Object obj, File fileName) throws IOException {
        FileOutputStream fos = new FileOutputStream(fileName);
        BufferedOutputStream bos = new BufferedOutputStream(fos);
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(obj);
        oos.close();
    }

    /**
     * Deserialize to an object from the file
     */
    private static Object deserialize(File fileName) throws IOException, ClassNotFoundException {
        FileInputStream fis = new FileInputStream(fileName);
        BufferedInputStream bis = new BufferedInputStream(fis);
        ObjectInputStream ois = new ObjectInputStream(bis);
        Object obj = ois.readObject();
        ois.close();
        return obj;
    }


}
