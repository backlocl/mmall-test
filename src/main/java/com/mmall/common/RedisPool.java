package com.mmall.common;

import com.mmall.util.PropertiesUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
@SuppressWarnings("all")
public class RedisPool {

    private static JedisPool pool=null;
    private static Integer maxTool= Integer.parseInt(PropertiesUtil.getProperty("redis.max.total","20"));
    private static Integer maxIdle=Integer.parseInt(PropertiesUtil.getProperty("redis.max.idle","20"));;
    private static Integer minIdle=Integer.parseInt(PropertiesUtil.getProperty("redis.min.idle","20"));;
    private static Boolean testOnBorrow=Boolean.parseBoolean(PropertiesUtil.getProperty("redis.test.borrow","true"));
    private static Boolean testOnreturn=Boolean.parseBoolean(PropertiesUtil.getProperty("redis.test.return","true"));


    private static String redisIp = PropertiesUtil.getProperty("redis1.ip");
    private static Integer redisPort = Integer.parseInt(PropertiesUtil.getProperty("redis1.port","6379"));
    private static void initPoll(){
        JedisPoolConfig config = new JedisPoolConfig();

        config.setMaxTotal(maxTool);
        config.setMaxIdle(maxIdle);
        config.setMinIdle(minIdle);

        config.setTestOnBorrow(testOnBorrow);
        config.setTestOnReturn(testOnreturn);

        config.setBlockWhenExhausted(true);

        pool=new JedisPool(config,redisIp,redisPort,1000*2);
    }

    static {
        initPoll();
    }

    public static Jedis getJedis(){
        return pool.getResource();
    }

    public static void returnBrokenResource(Jedis jedis){
        pool.returnBrokenResource(jedis);
    }

    public static void returnResource(Jedis jedis){
        pool.returnResource(jedis);
    }

    public static void main(String[] args) {
        Jedis jedis = pool.getResource();
        jedis.set("key","value");
        returnResource(jedis);
    }
}
