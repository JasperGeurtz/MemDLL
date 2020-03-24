import com.sun.jna.Library;
import com.sun.jna.Native;
import org.jasperge.memdll.MemDLL;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;

public class Example {
    interface MyDLL extends Library {
        void someFunc();
    }

    public static void viaJNA() throws IOException {
        //get from resources and copy to temp folder
        File dllLocation = new File(System.getProperty("java.io.tmpdir"), "MyDLL.dll");
        Files.copy(MyDLL.class.getClassLoader().getResourceAsStream("MyDLL.dll"), dllLocation.toPath());

        // load dll
        System.load(dllLocation.getAbsolutePath());
        MyDLL myDLL = Native.load("MyDLL", MyDLL.class);
        myDLL.someFunc();
    }

    public static void viaMemDLL() throws URISyntaxException, IOException {
        // load dll directly, no copying
        URL dllUrl = MyDLL.class.getClassLoader().getResource("MyDLL.dll");
        MyDLL myDLL = MemDLL.load(dllUrl, MyDLL.class);
        myDLL.someFunc();
    }
}
