package com.genbox.ai;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import com.genbox.exception.DefaultExceptionHandler;

import static org.assertj.core.api.Assertions.assertThat;

class GenBoxAgentBusinessChatApplicationTest {

    @Test
    void importsGlobalExceptionHandlerFromCommonModule() {
        Import imports = GenBoxAgentBusinessChatApplication.class.getAnnotation(Import.class);

        assertThat(imports).isNotNull();
        assertThat(imports.value()).contains(DefaultExceptionHandler.class);
    }
}
