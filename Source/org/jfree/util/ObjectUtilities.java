/*
 * Decompiled with CFR 0_115.
 */
package org.jfree.util;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.Collection;
import java.util.Iterator;
import org.jfree.util.Log;
import org.jfree.util.PublicCloneable;

public final class ObjectUtilities {
    public static final String THREAD_CONTEXT = "ThreadContext";
    public static final String CLASS_CONTEXT = "ClassContext";
    private static String classLoaderSource = "ThreadContext";
    private static ClassLoader classLoader;

    private ObjectUtilities() {
    }

    public static String getClassLoaderSource() {
        return classLoaderSource;
    }

    public static void setClassLoaderSource(String classLoaderSource) {
        ObjectUtilities.classLoaderSource = classLoaderSource;
    }

    public static boolean equal(Object o1, Object o2) {
        if (o1 == o2) {
            return true;
        }
        if (o1 != null) {
            return o1.equals(o2);
        }
        return false;
    }

    public static int hashCode(Object object) {
        int result = 0;
        if (object != null) {
            result = object.hashCode();
        }
        return result;
    }

    public static Object clone(Object object) throws CloneNotSupportedException {
        if (object == null) {
            throw new IllegalArgumentException("Null 'object' argument.");
        }
        if (object instanceof PublicCloneable) {
            PublicCloneable pc = (PublicCloneable)object;
            return pc.clone();
        }
        try {
            Method method = object.getClass().getMethod("clone", null);
            if (Modifier.isPublic(method.getModifiers())) {
                return method.invoke(object, null);
            }
        }
        catch (NoSuchMethodException e) {
            Log.warn("Object without clone() method is impossible.");
        }
        catch (IllegalAccessException e) {
            Log.warn("Object.clone(): unable to call method.");
        }
        catch (InvocationTargetException e) {
            Log.warn("Object without clone() method is impossible.");
        }
        throw new CloneNotSupportedException("Failed to clone.");
    }

    public static Collection deepClone(Collection collection) throws CloneNotSupportedException {
        if (collection == null) {
            throw new IllegalArgumentException("Null 'collection' argument.");
        }
        Collection result = (Collection)ObjectUtilities.clone(collection);
        result.clear();
        Iterator iterator = collection.iterator();
        while (iterator.hasNext()) {
            Object item = iterator.next();
            if (item != null) {
                result.add(ObjectUtilities.clone(item));
                continue;
            }
            result.add(null);
        }
        return result;
    }

    public static synchronized void setClassLoader(ClassLoader classLoader) {
        ObjectUtilities.classLoader = classLoader;
    }

    public static ClassLoader getClassLoader() {
        return classLoader;
    }

    public static synchronized ClassLoader getClassLoader(Class c) {
        if (classLoader != null) {
            return classLoader;
        }
        if ("ThreadContext".equals(classLoaderSource)) {
            ClassLoader threadLoader = Thread.currentThread().getContextClassLoader();
            return threadLoader;
        }
        ClassLoader applicationCL = c.getClassLoader();
        if (applicationCL == null) {
            return ClassLoader.getSystemClassLoader();
        }
        return applicationCL;
    }

    public static URL getResource(String name, Class c) {
        ClassLoader cl = ObjectUtilities.getClassLoader(c);
        return cl.getResource(name);
    }

    public static URL getResourceRelative(String name, Class c) {
        ClassLoader cl = ObjectUtilities.getClassLoader(c);
        String cname = ObjectUtilities.convertName(name, c);
        return cl.getResource(cname);
    }

    private static String convertName(String name, Class c) {
        if (name.startsWith("/")) {
            return name.substring(1);
        }
        while (c.isArray()) {
            c = c.getComponentType();
        }
        String baseName = c.getName();
        int index = baseName.lastIndexOf(46);
        if (index == -1) {
            return name;
        }
        String pkgName = baseName.substring(0, index);
        return pkgName.replace('.', '/') + "/" + name;
    }

    public static InputStream getResourceAsStream(String name, Class context) {
        URL url = ObjectUtilities.getResource(name, context);
        if (url == null) {
            return null;
        }
        try {
            return url.openStream();
        }
        catch (IOException e) {
            return null;
        }
    }

    public static InputStream getResourceRelativeAsStream(String name, Class context) {
        URL url = ObjectUtilities.getResourceRelative(name, context);
        if (url == null) {
            return null;
        }
        try {
            return url.openStream();
        }
        catch (IOException e) {
            return null;
        }
    }

    public static Object loadAndInstantiate(String className, Class source) {
        try {
            ClassLoader loader = ObjectUtilities.getClassLoader(source);
            Class c = loader.loadClass(className);
            return c.newInstance();
        }
        catch (Exception e) {
            return null;
        }
    }
}

