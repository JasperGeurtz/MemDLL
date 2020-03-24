package org.jasperge.memdll;

import com.sun.jna.Function;
import com.sun.jna.Pointer;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Load DLL's from memory instead of the filesystem [windows-x64 ONLY]
 */
public class MemDLL {
    private static MemoryModule mmi = MemoryModule.INSTANCE;

    /**
     * Load from resource URL
     */
    public static <T> T load(URL dllResource, Class<T> clazz) throws URISyntaxException, IOException {
        byte[] dllData = Files.readAllBytes(Paths.get(dllResource.toURI()));
        return load(dllData, clazz);
    }

    /**
     * Load from raw bytes
     */
    @SuppressWarnings("unchecked")
    public static <T> T load(byte[] dllBytes, Class<T> clazz) {
        Pointer dll = mmi.MemoryLoadLibrary(dllBytes, dllBytes.length);

        Map<String, Function> funcMap = new HashMap<>();
        for (Method m : clazz.getDeclaredMethods()) {
            Pointer fp = mmi.MemoryGetProcAddress(dll, m.getName());
            if (fp == null) {
                String argTypes = Arrays.stream(m.getParameterTypes()).map(Class::toString).collect(Collectors.joining(", "));
                throw new Error("method \"" + clazz.getName() + "::" + m.getName() + "(" + argTypes + ")\" not found");
            }
            funcMap.put(m.getName(), Function.getFunction(fp));
        }
        return (T) Proxy.newProxyInstance(
                clazz.getClassLoader(),
                new Class[] { clazz },
                (proxy, method, args) -> funcMap.get(method.getName()).invoke(method.getReturnType(), args));
    }
}
