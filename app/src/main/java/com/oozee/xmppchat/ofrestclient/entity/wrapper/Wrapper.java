package com.oozee.xmppchat.ofrestclient.entity.wrapper;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class Wrapper<T> {
    public T wrap(List<String> list, Class<T> clazz) {
        new BroadcastPresenceRoles().setBroadcastPresenceRoles(list);
        T t = null;
        try {
            t = clazz.newInstance();
            clazz.getMethod("set" + t.getClass().getSimpleName(), new Class[]{List.class}).invoke(list, new Object[0]);
            return t;
        } catch (InstantiationException e) {
            e.printStackTrace();
            return t;
        } catch (IllegalAccessException e2) {
            e2.printStackTrace();
            return t;
        } catch (InvocationTargetException e3) {
            e3.printStackTrace();
            return t;
        } catch (NoSuchMethodException e4) {
            e4.printStackTrace();
            return t;
        }
    }
}
