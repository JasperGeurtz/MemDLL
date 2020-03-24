# MemDLL
Load native DLL's from memory instead of the filesystem like required by JNA/JNI.
For now **windows-x64** only.

Uses [MemoryModule](https://github.com/fancycode/MemoryModule) to load the `dll` from memory.

## Usecase

Often when using JNA with custom `dll`'s you need to manually extract the `dll` from your `jar`.
This library aims to avoid these unnecessary steps.


### Example Comparison

Using `MemDLL`
```Java
interface MyDLL {
    void someFunc();
}
// load dll directly from resources, no copying
URL dllUrl = MyDLL.class.getClassLoader().getResource("MyDLL.dll");
MyDLL myDLL = MemDLL.load(dllUrl, MyDLL.class);
myDLL.someFunc();
```

Using only `JNA`
```Java
interface MyDLL extends Library {
    void someFunc();
}

//get from resources and copy to temp folder
File dllLocation = new File(System.getProperty("java.io.tmpdir"), "MyDLL.dll");
Files.copy(MyDLL.class.getClassLoader().getResourceAsStream("MyDLL.dll"), dllLocation.toPath());

// load dll
System.load(dllLocation.getAbsolutePath());
MyDLL myDLL = Native.load("MyDLL.dll", MyDLL.class);
myDLL.someFunc();
```

## Usage

 - add it to your `gradle`/`maven` project via [JitPack](https://jitpack.io/#JasperGeurtz/MemDLL)
 - alternatively download the latest jar from the [releases](https://github.com/JasperGeurtz/MemDLL/releases)
