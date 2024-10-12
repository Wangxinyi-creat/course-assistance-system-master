package com.zhedian.common.util;


import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import javax.tools.SimpleJavaFileObject;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import java.net.URI;
import java.io.IOException;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JavaStringExecution {

    public static String cute(String str) {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        String name = extractClassName(str);
        JavaSourceFromString source = new JavaSourceFromString(name, str);
        Iterable<? extends JavaFileObject> compilationUnits = Arrays.asList(source);

        StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);
        JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, null, null, null, compilationUnits);

        boolean success = task.call();
        try {
            fileManager.close();
        } catch (IOException e) {
            System.out.println("e = " + e);
        }

        if (success) {
            Runtime runtime = Runtime.getRuntime();
            try {
                Process process = runtime.exec("java "+name);
                process.waitFor();
                String s = readProcessOutput(process);
                process.destroy();
                return s;
            } catch (Exception e) {
                System.out.println("e = " + e);
            }
        }
        return "";
    }

    public static String extractClassName(String javaCode) {
        // 正则表达式匹配public class后面的类名，但不包括包名
        Pattern pattern = Pattern.compile("public\\s+class\\s+(\\w+)");
        Matcher matcher = pattern.matcher(javaCode);

        if (matcher.find()) {
            return matcher.group(1); // 返回匹配到的类名
        } else {
            return ""; // 没有找到类名
        }
    }

    private static String readProcessOutput(Process process) throws IOException {
        java.util.Scanner scanner = new java.util.Scanner(process.getInputStream()).useDelimiter("\\A");
        return scanner.hasNext() ? scanner.next() : "";
    }

    public static class JavaSourceFromString extends SimpleJavaFileObject {
        final String code;

        JavaSourceFromString(String name, String code) {
            super(URI.create("string:///" + name.replace('.', '/') + Kind.SOURCE.extension), Kind.SOURCE);
            this.code = code;
        }

        @Override
        public CharSequence getCharContent(boolean ignoreEncodingErrors) {
            return code;
        }
    }

}
