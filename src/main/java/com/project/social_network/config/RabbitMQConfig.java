package com.project.social_network.config;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.context.support.GenericWebApplicationContext;
import org.springframework.amqp.rabbit.listener.ConditionalRejectingErrorHandler;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.amqp.rabbit.connection.ConnectionNameStrategy;
import org.springframework.amqp.rabbit.connection.SimplePropertyValueConnectionNameStrategy;

@Configuration
@EnableRabbit
@EnableAutoConfiguration(exclude = RabbitAutoConfiguration.class)
public class RabbitMQConfig {

  @Autowired
  private GenericWebApplicationContext context;

  @Value("${spring.rabbitmq.host}")
  private String rabitHost;

  @Value("${spring.rabbitmq.port}")
  private int rabitPort;

  @Value("${spring.rabbitmq.username}")
  private String rabitUsername;

  @Value("${spring.rabbitmq.password}")
  private String rabitPassword;

  @Value("${spring.rabbitmq.virtual-host}")
  private String rabitVirtualHost;

  // @Value("${spring.rabbitmq.virtual-host-job}")
  // private String rabitVirtualHostJob;

  @Bean
  public MessageConverter jsonMessageConverter() {
    Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter();
    return converter;
  }

  @Bean
  public ConnectionNameStrategy connectionNameStrategy() {
    return new SimplePropertyValueConnectionNameStrategy("spring.application.name");
  }

  private CachingConnectionFactory getCachingConnectionFactoryCommon() {
    CachingConnectionFactory connectionFactory = new CachingConnectionFactory(this.rabitHost, this.rabitPort);
    connectionFactory.setUsername(this.rabitUsername);
    connectionFactory.setPassword(this.rabitPassword);
    connectionFactory.setVirtualHost(this.rabitVirtualHost);

    // Configure connection recovery
    connectionFactory.setPublisherReturns(true);
    connectionFactory.setPublisherConfirmType(CachingConnectionFactory.ConfirmType.CORRELATED);
    connectionFactory.setConnectionNameStrategy(connectionNameStrategy());

    return connectionFactory;
  }

  @Primary
  @Bean
  public AmqpAdmin amqpAdmin(ConnectionFactory connectionFactory) {
    RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);
    rabbitAdmin.setAutoStartup(true);
    return rabbitAdmin;
  }

  @Primary
  @Bean
  public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
    RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
    rabbitTemplate.setMessageConverter(jsonMessageConverter());

    // Configure retry
    RetryTemplate retryTemplate = new RetryTemplate();
    ExponentialBackOffPolicy backOffPolicy = new ExponentialBackOffPolicy();
    backOffPolicy.setInitialInterval(1000);
    backOffPolicy.setMultiplier(2.0);
    backOffPolicy.setMaxInterval(10000);
    retryTemplate.setBackOffPolicy(backOffPolicy);

    SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy();
    retryPolicy.setMaxAttempts(3);
    retryTemplate.setRetryPolicy(retryPolicy);

    rabbitTemplate.setRetryTemplate(retryTemplate);
    return rabbitTemplate;
  }

  @Primary
  @Bean
  public ConnectionFactory connectionFactory() {
    return getCachingConnectionFactoryCommon();
  }

  @Primary
  @Bean("rabbitListenerContainerFactory")
  public SimpleRabbitListenerContainerFactory containerFactory(ConnectionFactory connectionFactory) {
    final SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
    factory.setConnectionFactory(connectionFactory);
    factory.setMessageConverter(jsonMessageConverter());

    // Configure error handling
    factory.setErrorHandler(new ConditionalRejectingErrorHandler());
    factory.setDefaultRequeueRejected(false);

    // Configure concurrency
    factory.setConcurrentConsumers(3);
    factory.setMaxConcurrentConsumers(10);

    // Configure prefetch
    factory.setPrefetchCount(1);

    return factory;
  }

  // @Bean(name = "amqpAdminJob")
  // public AmqpAdmin amqpAdminJob() {
  // RabbitAdmin rabbitAdmin = new RabbitAdmin(this.rabbitConnectionJob());
  // return rabbitAdmin;
  // }
  //
  // @Bean("rabbitConnectionJob")
  // public ConnectionFactory rabbitConnectionJob() {
  // CachingConnectionFactory connectionFactory =
  // this.getCachingConnectionFactoryCommon();
  // connectionFactory.setVirtualHost(this.rabitVirtualHostJob);
  // return connectionFactory;
  // }
  //
  // @Bean("rabbitTemplateJob")
  // public RabbitTemplate rabbitTemplateJob(@Qualifier("rabbitConnectionJob")
  // ConnectionFactory connectionFactory) {
  // return new RabbitTemplate(connectionFactory);
  // }
  //
  // @Bean("containerFactoryJob")
  // public SimpleRabbitListenerContainerFactory
  // containerFactoryJob(@Qualifier("rabbitConnectionJob") ConnectionFactory
  // connectionFactory) {
  // final SimpleRabbitListenerContainerFactory factory = new
  // SimpleRabbitListenerContainerFactory();
  // factory.setDefaultRequeueRejected(false);
  // factory.setConnectionFactory(connectionFactory);
  // factory.setDefaultRequeueRejected(false);
  // return factory;
  // }

}
