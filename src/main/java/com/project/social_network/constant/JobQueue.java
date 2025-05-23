package com.project.social_network.constant;

import java.util.Arrays;
import java.util.List;

public class JobQueue {
  public static final String QUEUE_DEV = "rabbit-queue";
  public static final List<String> queueNameList = Arrays.asList(QUEUE_DEV);
}
