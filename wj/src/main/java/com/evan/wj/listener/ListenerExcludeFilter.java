package com.evan.wj.listener;

import org.springframework.boot.context.TypeExcludeFilter;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;

/**
 * 用于测试时，停止监听
 **/
public class ListenerExcludeFilter extends TypeExcludeFilter {
    private static final String UDP_SERVER_LISTENER = "com.evan.wj.listener.InitBootContextListener";

    @Override
    public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory) {
        if(UDP_SERVER_LISTENER.equals(metadataReader.getClassMetadata().getClassName())){
            return true;
        }
        return false;
    }

    @Override
    public boolean equals(Object o) {
        return o != null && getClass() == o.getClass();
    }

    @Override
    public int hashCode() {
        return 1;
    }
}
