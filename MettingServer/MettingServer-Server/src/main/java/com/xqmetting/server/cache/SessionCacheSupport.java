package com.xqmetting.server.cache;

import com.xqmetting.entity.SessionCache;

public interface SessionCacheSupport {

    void save(SessionCache s);

    SessionCache get(String userId);

    void remove(String userId);

}
