package com.github.microcatalog;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.ComponentScan;

@TestConfiguration
@ComponentScan("com.github.microcatalog.service.mapper")
public class MappersConfig {
}
