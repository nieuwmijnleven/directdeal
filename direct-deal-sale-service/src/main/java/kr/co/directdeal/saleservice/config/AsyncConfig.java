package kr.co.directdeal.saleservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * Configuration class to enable asynchronous processing and
 * provide a ThreadPoolTaskExecutor bean named "saleThreadPoolTaskExecutor"
 * for async task execution related to sale operations.
 *
 * Configures thread pool parameters such as core size, max size, queue capacity,
 * and thread name prefix.
 *
 * @author Cheol Jeon
 */
@EnableAsync
@Configuration
public class AsyncConfig {

    /**
     * Defines a ThreadPoolTaskExecutor bean with:
     * - core pool size: 10
     * - max pool size: 20
     * - queue capacity: 50
     * - thread name prefix: "SaleThreadPool-"
     *
     * This executor is used for handling asynchronous tasks annotated with @Async,
     * specifically for sale-related async operations.
     *
     * @return configured Executor instance
     */
    @Bean("saleThreadPoolTaskExecutor")
    public Executor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(20);
        executor.setQueueCapacity(50);
        executor.setThreadNamePrefix("SaleThreadPool-");
        executor.initialize();
        return executor;
    }
}
