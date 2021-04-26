package com.movo.rpc.core.common.constants;

/**
 * PRC用常量
 * @author Movo
 * @create 2021/4/1 8:53
 */
public class RpcConstant {
    /**
     * 编码
     */
    public static final String UTF_8 = "UTF-8";
    /**
     * Zookeeper服务注册地址
     */
    public static final String ZK_SERVICE_PATH = "/rpc";
    /**
     * 路径分隔符
     */
    public static final String PATH_DELIMITER = "/";
    /**
     * java序列化协议
     */
    public static final String PROTOCOL_JAVA = "java";
    /**
     * protobuf序列化协议
     */
    public static final String PROTOCOL_PROTOBUF = "protobuf";
    /**
     * kryo序列化协议
     */
    public static final String PROTOCOL_KRYO = "kryo";
    /**
     * 轮询
     */
    public static final String BALANCE_ROUND = "balanceRound";
    /**
     * 随机
     */
    public static final String BALANCE_RANDOM = "random";
    /**
     * 加权轮询
     */
    public static final String BALANCE_WEIGHT_ROUND = "balanceWeightRound";
    /**
     * 平滑加权轮询
     */
    public static final String BALANCE_SMOOTH_WEIGHT_ROUND = "smoothWeightRound";
}
