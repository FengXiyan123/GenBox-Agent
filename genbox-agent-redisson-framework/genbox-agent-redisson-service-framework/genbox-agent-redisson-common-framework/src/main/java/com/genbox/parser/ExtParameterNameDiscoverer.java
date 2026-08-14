package com.genbox.parser;

import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.NativeDetector;

/**
 * 对DefaultParameterNameDiscoverer进行扩展，添加{@link LocalVariableTableParameterNameDiscoverer}。
 */
public class ExtParameterNameDiscoverer extends DefaultParameterNameDiscoverer {

    public ExtParameterNameDiscoverer() {
        super();
        if (!NativeDetector.inNativeImage()) {
            addDiscoverer(new LocalVariableTableParameterNameDiscoverer());
        }
    }
}
