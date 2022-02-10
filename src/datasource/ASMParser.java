package datasource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.objectweb.asm.*;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TryCatchBlockNode;

public class ASMParser {

    private Map<String, ClassNode> classMap;

    public ASMParser(String[] classList) throws IOException {
        for (String className : classList) {
            ClassReader reader = new ClassReader(className);

            ClassNode decompiled = new ClassNode();
            reader.accept(decompiled, ClassReader.EXPAND_FRAMES);
        }

        // This should be all we need for the parser to function
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
     * @throws IllegalArgumentException If the class specified is not being parsed
     * @param className The name of the class to retrieve methods from
     * @return An array of strings containing all names of the methods in the class.
     */

    public String[] getMethods(String className) {
        if (!this.classMap.containsKey(className)) {
            throw new IllegalArgumentException("Error! The specified class was not found in the parsed class map.");
        }

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
     * @throws IllegalArgumentException If the method is not found in the
     *                                  specified class
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

    /**
     * Returns a list of exceptions types that a method catches
     * 
     * @throws IllegalArgumentException If the method is not found in the specified
     *                                  class
     * @param className  The name of the class where the method should reside in
     * @param methodName THe name of the method to retrieve caught exception types
     *                   from
     * @return A string array of internal types for each exception type that is
     *         caught. Duplicates can occur
     * 
     */
    public String[] getMethodExceptionCaught(String className, String methodName) {
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

        List<TryCatchBlockNode> caughtExceptions = decompMethod.tryCatchBlocks;
        List<String> caughtExceptionTypes = new ArrayList<>();

        for (TryCatchBlockNode block : caughtExceptions) {
            caughtExceptionTypes.add(block.type);
        }

        String[] result = null;
        caughtExceptionTypes.toArray(result);
        return result;
    }

    public String[] getMethodCompilerAnnotations(String className, String methodName) {
    	ClassNode decompiled = this.classMap.get(className);
    	
    	MethodNode decompMethod = null;
        for (MethodNode node : decompiled.methods) {
            if (node.name.equals(methodName)) {
                decompMethod = node;
            }
        }
        
        if (decompMethod == null) {
            throw new IllegalArgumentException("Error! Specified Method was not found in the class!");
        }

        String[] result = null;
        // TODO Return annotations
        return null;
    }
    
}
