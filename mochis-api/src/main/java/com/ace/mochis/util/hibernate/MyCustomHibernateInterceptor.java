package com.ace.mochis.util.hibernate;

import com.ace.mochis.util.RequestId;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.EmptyInterceptor;

public class MyCustomHibernateInterceptor extends EmptyInterceptor {
    /**
     *
     */
    private static final long serialVersionUID = 4700859296270083347L;
    private static final Logger logger = LogManager.getLogger(MyCustomHibernateInterceptor.class);

    public String onPrepareStatement(String sql) {
        String requestId = RequestId.getId();
        if (requestId != null) {
            logger.debug("/* " + requestId + " */ " + sql);
            return "/* " + requestId + " */ " + sql;
        } else {
            return sql;
        }
    }
}
