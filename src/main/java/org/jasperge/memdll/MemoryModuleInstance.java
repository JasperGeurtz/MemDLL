package org.jasperge.memdll;

import com.sun.jna.Native;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;

class MemoryModuleInstance {
    static String tmpdir = System.getProperty("java.io.tmpdir");
    static MemoryModule get() {
        String dllName = "MemoryModule"; //System.getProperty("sun.arch.data.model").equals("64") ? "MemoryModule64" : "org.jasperge.memdll.MemoryModule";

        File temp = new File(tmpdir, dllName + ".dll");
        if (!temp.exists()) {
            try (InputStream is = MemoryModule.class.getClassLoader().getResourceAsStream(dllName + ".dll")) {
                Files.copy(is, temp.toPath());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.load(temp.getAbsolutePath());
        return Native.load(dllName, MemoryModule.class);
    }
}
