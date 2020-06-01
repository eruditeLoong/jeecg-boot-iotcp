package org.jeecg.common.constant;

/**
 * @author: huangxutao
 * @date: 2019-06-14
 * @description: 缓存常量
 */
public interface CacheConstant {

	/**
	 * 字典信息缓存
	 */
    public static final String SYS_DICT_CACHE = "sys:cache:dict";
	/**
	 * 表字典信息缓存
	 */
    public static final String SYS_DICT_TABLE_CACHE = "sys:cache:dictTable";

	/**
	 * 数据权限配置缓存
	 */
    public static final String SYS_DATA_PERMISSIONS_CACHE = "sys:cache:permission:datarules";

	/**
	 * 缓存用户信息
	 */
	public static final String SYS_USERS_CACHE = "sys:cache:user";

	/**
	 * 全部部门信息缓存
	 */
	public static final String SYS_DEPARTS_CACHE = "sys:cache:depart:alldata";


	/**
	 * 全部部门ids缓存
	 */
	public static final String SYS_DEPART_IDS_CACHE = "sys:cache:depart:allids";


    /**
     * 测试缓存key
     */
    public static final String TEST_DEMO_CACHE = "test:demo";

    /**
     * 字典信息缓存
     */
    public static final String SYS_DYNAMICDB_CACHE = "sys:cache:dbconnect:dynamic:";

	/**
	 * 设备模型的数据解析对象缓存
	 */
	public static final String IOT_MODEL_DATA_PROTOCOL_CACHE = "iot:cache:model:data:protocol";

	/**
	 * 数据解析，动态加载解析类
	 */
	public static final String IOT_LOAD_JAR_METHOD_CACHE = "iot:cache:load:jar:method";

	/**
	 * 构建功能数据结构缓存
	 */
	public static final String IOT_BUILD_FUNC_DATA_STRUCTURE_CACHE = "iot:cache:function:build:data:structure";

	/**
	 * 设备实例数据节点
	 */
	public static final String IOT_DEVICE_INSTANCE_DATA_NODES_CACHE = "iot:cache:device:instance:data:nodes";
}
