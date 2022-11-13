import java.util.List;

import static jodd.util.ThreadUtil.sleep;

public class RedisTinder {
    private final static int USERS = 20;
    private final static int SLEEP = 100;
    private static void logRegular(String user){
        String log = "- На главной странице показваем пользователя " + user;
        System.out.println(log);
    }
    private static void logBuyer(String user){
        String log = "> Пользователь " + user + " оплатил платную услугу \n" +
                "- На главной станице показываем пользователя " + user;
        System.out.println(log);
    }

    public static void main(String[] args) {
        RedisStorage redis = new RedisStorage();
        redis.init();
        redis.createUsers(USERS);
        List<String> users = redis.listOfUsers();
        String buyerId1 = String.valueOf(1 + (int)(Math.random() * 20));
        String buyerId2 = String.valueOf(1 + (int)(Math.random() * 20));
        new Thread(() -> {
            for(;;){
                for(String user : users){
                    if(user.equals(buyerId1) || user.equals(buyerId2)){
                        logBuyer(user);
                    }
                    sleep(2 * SLEEP);
                }
            }
        }).start();

        new Thread(() -> {
            for (;;){
                for (String user : users){
                    if(!user.equals(buyerId1) && !user.equals(buyerId2)){
                        logRegular(user);
                    }
                    sleep(SLEEP);
                }
                System.out.println("\n");
            }
        }).start();

        redis.shutdown();
    }
}
