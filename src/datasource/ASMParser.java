package datasource;

import java.io.IOException;
import java.util.Map;

import org.objectweb.asm.*;
import org.objectweb.asm.tree.ClassNode;

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

}
