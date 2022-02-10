package datasource;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.objectweb.asm.*;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

public class ASMParser {

    private Map<String, ClassNode> classMap;

    public ASMParser(String[] classList) throws IOException {
        for (String className : classList) {
            ClassReader reader = new ClassReader(className);

            ClassNode decompiled = new ClassNode();
            reader.accept(decompiled, ClassReader.EXPAND_FRAMES);
        }
    }

    public String getUserFriendlyName(String className) {
        ClassNode decompiled = this.classMap.get(className);
        return Type.getObjectType(decompiled.name).getClassName();
    }

    public String getSuperName(String className) {
        ClassNode decompiled = this.classMap.get(className);
        return decompiled.superName;
    }

    public String[] getInterfaces(String className) {
        ClassNode decompiled = this.classMap.get(className);
        String[] result = null;
        decompiled.interfaces.toArray(result);
        return result;
    }

    /**
     * Returns a list of methods contained in the specified decompiled class.
     * 
     * @param className The name of the class to retrieve methods from
     * @return An array of strings containing all names of the methods in the class.
     */

    public String[] getMethods(String className) {
        ClassNode decompiled = this.classMap.get(className);
        String[] result = null;

        List<String> methodList = new ArrayList<>();

        for (MethodNode node : decompiled.methods) {
            methodList.add(node.name);
        }

        methodList.toArray(result);
        return result;
    }

    /**
     * Returns a list of exceptions that exist in the signature of a method
     * 
     * @param className  The name of the class where the method exists in
     * @param methodName The name of the method to retrieve the exceptions from
     * @return An array of strings containing the names of methods in the class.
     */

    public String[] getMethodExceptionSignature(String className, String methodName) {
        ClassNode decompiled = this.classMap.get(className);

        // Kinda have to find the method in the first place, use basic search for now
        MethodNode decompMethod = null;
        for (MethodNode node : decompiled.methods) {
            if (node.name.equals("methodName")) {
                decompMethod = node;
            }
        }

        // Just a safeguard
        if (decompMethod == null) {
            throw new IllegalArgumentException("Error! Specified Method was not found in the class!");
        }

        String[] result = null;
        decompMethod.exceptions.toArray(result);
        return result;
    }

}
