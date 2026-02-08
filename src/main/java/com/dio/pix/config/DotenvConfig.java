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

            // Converte DATABASE_URL do Railway (postgresql://...) para JDBC (jdbc:postgresql://...)
            String databaseUrl = System.getenv("DATABASE_URL");
            if (databaseUrl != null && !databaseUrl.isEmpty()) {
                if (databaseUrl.startsWith("postgresql://")) {
                    databaseUrl = "jdbc:" + databaseUrl;
                } else if (!databaseUrl.startsWith("jdbc:")) {
                    databaseUrl = "jdbc:postgresql://" + databaseUrl;
                }
                System.setProperty("DATABASE_URL", databaseUrl);
                dotenvMap.put("DATABASE_URL", databaseUrl);
                System.out.println("✅ DATABASE_URL configurada: " + databaseUrl.replaceAll(":[^:@]+@", ":***@"));
            }

            environment.getPropertySources().addFirst(new MapPropertySource("dotenvProperties", dotenvMap));

            System.out.println("✅ Variáveis carregadas com sucesso!");
        } catch (Exception e) {
            System.err.println("⚠️ Erro ao carregar config: " + e.getMessage());
        }
    }
}


