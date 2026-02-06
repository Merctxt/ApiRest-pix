package com.dio.pix.config;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import java.util.HashMap;
import java.util.Map;

public class DotenvConfig implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        try {
            Dotenv dotenv = Dotenv.configure()
                    .ignoreIfMissing()
                    .load();

            ConfigurableEnvironment environment = applicationContext.getEnvironment();
            Map<String, Object> dotenvMap = new HashMap<>();

            dotenv.entries().forEach(entry -> {
                String key = entry.getKey();
                String value = entry.getValue();
                dotenvMap.put(key, value);
                System.setProperty(key, value);
            });

            environment.getPropertySources().addFirst(new MapPropertySource("dotenvProperties", dotenvMap));

            System.out.println("✅ Variáveis .env carregadas com sucesso!");
        } catch (Exception e) {
            System.err.println("⚠️ Erro ao carregar .env: " + e.getMessage());
        }
    }
}


