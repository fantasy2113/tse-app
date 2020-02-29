package de.jos.tselicence.config;

import de.jos.tselicence.core.Comp;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component(Comp.CONTEXT)
public class Context implements ApplicationContextAware {

    private static ApplicationContext appContext;

    public static <T> T getBean(Class<T> beanClass) {
        return appContext.getBean(beanClass);
    }

    public static Object getBean(String beanName) {
        return appContext.getBean(beanName);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        appContext = applicationContext;
    }
}
