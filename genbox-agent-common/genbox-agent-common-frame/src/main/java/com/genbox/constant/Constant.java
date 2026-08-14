package com.genbox.constant;

/**
 * 常量类。
 */
public class Constant {

    public static final String PREFIX_DISTINCTION_NAME = "prefix.distinction.name";

    public static final String DEFAULT_PREFIX_DISTINCTION_NAME = "genbox-agent";

    public static final String SPRING_INJECT_PREFIX_DISTINCTION_NAME = "${"+PREFIX_DISTINCTION_NAME+":"+DEFAULT_PREFIX_DISTINCTION_NAME+"}";

}
