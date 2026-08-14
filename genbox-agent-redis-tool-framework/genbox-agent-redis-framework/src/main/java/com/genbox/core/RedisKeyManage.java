package com.genbox.core;


import lombok.Getter;

/**
 * 配置类。
 */
@Getter
public enum RedisKeyManage {
    
    USER_INFO_KEY("user:info:%s","用户id","value为UserInfo类型","k"),
    DOCUMENT_INDEX_BUILD_PROGRESS("document:index-build:progress:%s", "文档索引构建任务id", "value为DocumentIndexBuildProgressVo类型", "axing"),
    DOCUMENT_PARSE_ROUTE_PROGRESS("document:parse-route:progress:%s", "文档解析路由任务id", "value为DocumentParseRouteProgressVo类型", "axing"),
   
   
    ;

    /**
     * key值
     * */
    private final String key;

    /**
     * key的说明
     * */
    private final String keyIntroduce;

    /**
     * value的说明
     * */
    private final String valueIntroduce;

    /**
     * 作者
     * */
    private final String author;

    RedisKeyManage(String key, String keyIntroduce, String valueIntroduce, String author){
        this.key = key;
        this.keyIntroduce = keyIntroduce;
        this.valueIntroduce = valueIntroduce;
        this.author = author;
    }

    public static RedisKeyManage getRc(String keyCode) {
        for (RedisKeyManage re : RedisKeyManage.values()) {
            if (re.key.equals(keyCode)) {
                return re;
            }
        }
        return null;
    }
    
}
