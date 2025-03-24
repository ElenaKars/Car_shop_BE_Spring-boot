package de.aittr.g_52_shop.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public AmazonS3 doClient(DOProperties properties) {

        AWSCredentials credentials = new BasicAWSCredentials(
                properties.getAccessKey(),
                properties.getSecretKey()
        );

        AwsClientBuilder.EndpointConfiguration endpointConfig =
                new AwsClientBuilder.EndpointConfiguration(
                        properties.getEndpoint(),
                        properties.getRegion()
                );

        // Создаём специальный объект AmazonS3ClientBuilder, который
        // создаёт для нас объект клиента.
        // При этом мы в этот объект закладываем информацию о том,
        // где находятся сервисы ДО (endpointConfig) и какие реквизиты доступа
        // при этом нужно использовать, чтобы авторизоваться (credentials)
        AmazonS3ClientBuilder clientBuilder = AmazonS3ClientBuilder
                .standard()
                .withEndpointConfiguration(endpointConfig)
                .withCredentials(new AWSStaticCredentialsProvider(credentials));

        return clientBuilder.build();
    }
}
