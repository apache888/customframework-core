package com.customframework.core;

import com.customframework.core.annotation.Autowired;
import com.customframework.core.annotation.Component;
import com.customframework.core.annotation.Repository;
import io.github.lukehutch.fastclasspathscanner.FastClasspathScanner;
import io.github.lukehutch.fastclasspathscanner.scanner.ScanResult;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created on 22.02.2018
 *
 * @author Roman Hayda
 */

public class AnnotationConfigApplicationContext implements ApplicationContext {

    private static final Map<String, Object> register = new ConcurrentHashMap<>();
    private static final Map<String, Class> definitions = new ConcurrentHashMap<>();

    //Map<InterfaceName, Map<MethodName, Query>>
    private static final Map<String, Map<String, String>> noImplIntrfs = new ConcurrentHashMap<>();

    //format com.mypkg.bin
    private String[] basePackages;

    public AnnotationConfigApplicationContext(String... basePackages) {
        this.basePackages = basePackages;

    }


    @Override
    public Object getBean(String name) {
        return register.get(name);
    }

    @Override
    public Object getBean(Class requiredType) {

            if (definitions.values().contains(requiredType)) {
                    return register.get(requiredType.getName());
            }
        return null;
    }

    @Override
    public Set<String> getBeanNames() {
        return register.keySet();
    }

    public void init() throws IllegalAccessException, ClassNotFoundException, InstantiationException {
        scan(basePackages);
        autowiredFields();

    }


    private void scan(String[] basePackages) throws ClassNotFoundException, IllegalAccessException, InstantiationException {

        FastClasspathScanner scanner = new FastClasspathScanner(basePackages);
        ScanResult result = scanner.scan();
        List<String> candidates = result.getNamesOfAllClasses();

//        System.out.println(candidates);

            for (String candidateName : candidates) {
                Class candidate = Class.forName(candidateName);
                if (candidate.isAnnotationPresent(Component.class)) {
                    register.put(candidateName, candidate.newInstance());
                    definitions.put(candidateName, candidate);
                }
                if (candidate.isAnnotationPresent(Repository.class)) {
                    register.put(candidateName, Proxy.newProxyInstance(candidate.getClassLoader(), new Class[]{candidate}, new InvocationHandler() {
                        @Override
                        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                            String result = null;
                            if (noImplIntrfs.containsKey(candidateName)) {
                                if (noImplIntrfs.get(candidateName).containsKey(method.getName())) {
                                    result = noImplIntrfs.get(candidateName).get(method.getName());
                                } else {
                                    throw new UnsupportedOperationException();
                                }
                            } else {
                                throw new ClassNotFoundException();
                            }
                            System.out.println(result);
                            return result;
                        }
                    }));
                    definitions.put(candidateName, candidate);

                    noImplIntrfs.put(candidateName, parseMethodNameToQuery(candidate.getDeclaredMethods()));
                }
            }
    }

    private void autowiredFields() throws IllegalAccessException {
        Collection<Object> beans = register.values();

        for (Object bean : beans) {
            Field[] fields = bean.getClass().getDeclaredFields();
            for (Field field : fields) {
                Autowired annotation = field.getAnnotation(Autowired.class);
                if (annotation != null) {
                    Object value = this.getBean(field.getType());
                    field.setAccessible(true);
                    field.set(bean, value);
                }
            }
        }
    }


    private Map<String, String> parseMethodNameToQuery(Method[] methods) {

        Map<String, String> queries = new HashMap<>();

        for (Method method : methods) {
            String methodName = method.getName();
            String query = parse(method.getGenericReturnType().getTypeName(), methodName);
            queries.put(methodName, query);
        }

        return queries;
    }

    private String parse(String classSimpleName, String methodName) {

        String tableAlias = classSimpleName.substring(0, 1).toLowerCase();
        StringBuilder builder = new StringBuilder();
        String part = null;
        String param = null;
        StringBuilder where = new StringBuilder("WHERE ");
        int paramCount = 1;

        if (methodName.startsWith("findBy")) {
            builder.append("SELECT * FROM ");
            builder.append(classSimpleName);
            builder.append(" ");
            builder.append(tableAlias);
            part = methodName.substring(methodName.indexOf("findBy")+6);
        } else {
            throw new UnsupportedOperationException();
        }

        if (part.contains("And")) {
            param = part.substring(0, part.indexOf("And"));

            where.append(tableAlias);
            where.append(".");
            where.append(param.toLowerCase());
            where.append(" = :?");
            where.append(paramCount);
            where.append(" AND ");

            paramCount++;

            param = part.substring(part.indexOf("And")+3);

            where.append(tableAlias);
            where.append(".");
            where.append(param.toLowerCase());
            where.append(" = :?");
            where.append(paramCount);

        } else if (part.contains("Or")) {
            param = part.substring(0, part.indexOf("Or"));

            where.append(tableAlias);
            where.append(".");
            where.append(param.toLowerCase());
            where.append(" = :?");
            where.append(paramCount);
            where.append(" OR ");

            paramCount++;

            param = part.substring(part.indexOf("Or")+2);

            where.append(tableAlias);
            where.append(".");
            where.append(param.toLowerCase());
            where.append(" = :?");
            where.append(paramCount);
        } else {

            where.append(tableAlias);
            where.append(".");
            where.append(part.toLowerCase());
            where.append(" = :?");
            where.append(paramCount);
        }

        builder.append(" ");
        builder.append(where);


        return  builder.toString();
    }






}
