package com.xqmetting.server.cache;

import com.xqmetting.entity.SessionCache;
import org.springframework.stereotype.Component;

@Component
public class SessionCacheSupportImpl implements SessionCacheSupport{

    @Override
    public void save(SessionCache s) {

    }

    @Override
    public SessionCache get(String userId) {
        return null;
    }

    @Override
    public void remove(String userId) {

    }
}
