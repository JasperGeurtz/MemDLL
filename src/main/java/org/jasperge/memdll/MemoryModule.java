package org.jasperge.memdll;

import com.sun.jna.Library;
import com.sun.jna.Pointer;

interface MemoryModule extends Library {
    MemoryModule INSTANCE = MemoryModuleInstance.get();

    Pointer MemoryLoadLibrary(byte[] data, long size);

    Pointer MemoryGetProcAddress(Pointer dll, String name);
}
