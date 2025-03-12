package com.project.social_network.service.interfaces;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public interface BaseRedisService {

  void set(String key, String value);

  void setWithTTL(String key, String value, long timeout, TimeUnit unit);

  void setTimeToLive(String key, long timeoutInDays);

  void hashSet(String key, String field, Object value);

  boolean hashExists(String key, String field);

  Object get(String key);

  Map<String, Object> getField(String key);

  Object hashGet(String key, String field);

  List<Object> hashGetFieldPrefix(String key, String fieldPrefix);

  Set<String> getFieldPrefixs(String key);

  void delete(String key);

  void delete(String key, String field);

  void delete(String key, List<String> fields);

}
