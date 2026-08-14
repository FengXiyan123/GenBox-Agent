package com.baidu.fsg.uid.worker;

import com.baidu.fsg.uid.utils.ValuedEnum;

/**
 * 枚举定义。
 */
public enum WorkerNodeType implements ValuedEnum<Integer> {

    CONTAINER(1), ACTUAL(2);

    private final Integer type;

    private WorkerNodeType(Integer type) {
        this.type = type;
    }

    @Override
    public Integer value() {
        return type;
    }

}
