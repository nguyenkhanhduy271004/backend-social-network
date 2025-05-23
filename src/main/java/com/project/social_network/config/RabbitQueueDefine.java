package com.project.social_network.config;

import com.project.social_network.constant.JobQueue;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

@Service
public class RabbitQueueDefine {
  @Autowired
  @Qualifier("amqpAdmin")
  private AmqpAdmin rabbitAdminMain;

  @Bean
  public Queue incomingQueue() {
    for (String queueName : JobQueue.queueNameList) {
      Queue queue = QueueBuilder.durable(queueName)
          .withArgument("x-message-ttl", 86400000) // 24 hours in milliseconds
          .withArgument("x-max-length", 10000) // Maximum queue size
          .build();
      rabbitAdminMain.declareQueue(queue);
    }
    return null;
  }
}
