package com.genbox.database.mybatisplus;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import com.genbox.util.DateUtils;

import java.util.Date;
/**
 * 处理器。
 */
@Slf4j
public class MybatisPlusMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {

        this.strictInsertFill(metaObject, "createTime", DateUtils::now, Date.class);
        this.strictInsertFill(metaObject, "editTime", DateUtils::now, Date.class);
    }

    @Override
    public void updateFill(MetaObject metaObject) {

        this.strictUpdateFill(metaObject, "editTime", DateUtils::now, Date.class);
    }
}
