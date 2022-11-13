import org.redisson.Redisson;
import org.redisson.api.RList;
import org.redisson.api.RedissonClient;
import org.redisson.client.RedisConnectionException;
import org.redisson.config.Config;
import java.util.ArrayList;
import java.util.List;
import static java.lang.System.out;

public class RedisStorage {
    private RedissonClient redisClient;
    private RList<String> users;
    private final static String KEY = "TINDER";
    void init(){
        Config config = new Config();
        config.useSingleServer().setAddress("redis://127.0.0.1:6379");
        try{
            redisClient = Redisson.create(config);
        } catch (RedisConnectionException ex){
            out.println("Не удалось подключиться к серверу Redis");
            out.println(ex.getMessage());
        }
        users = redisClient.getList(KEY);
    }
    void shutdown(){
        redisClient.shutdown();
    }
    void createUsers(int amount){
        for (int u = 1; u <= amount; u++){
            users.add(String.valueOf(u));
        }
    }
    List<String> listOfUsers(){
        return new ArrayList<>(users);
    }


}
