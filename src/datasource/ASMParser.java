package datasource;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;
import org.objectweb.asm.tree.analysis.Analyzer;
import org.objectweb.asm.tree.analysis.AnalyzerException;
import org.objectweb.asm.tree.analysis.Frame;
import org.objectweb.asm.tree.analysis.SourceInterpreter;
import org.objectweb.asm.tree.analysis.SourceValue;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class ASMParser {

	private Map<String, ClassNode> classMap;

	public ASMParser(String[] classList) throws IOException {
		this.classMap = new HashMap<String, ClassNode>();
		for (String className : classList) {
			className = className.replace('.', '/');
			ClassReader reader = new ClassReader(className);

			ClassNode decompiled = new ClassNode();
			reader.accept(decompiled, ClassReader.EXPAND_FRAMES);
			classMap.put(className, decompiled);
		}

		// This should be all we need for the parser to function
	}

	public ASMParser(InputStream[] classStreams) throws IOException {
		this.classMap = new HashMap<>();
		for (InputStream stream : classStreams) {
			ClassReader reader = new ClassReader(stream);

			ClassNode decompiled = new ClassNode();
			reader.accept(decompiled, ClassReader.EXPAND_FRAMES);

			// We still need the fully qualified class name
			String className = decompiled.name;
			classMap.put(className, decompiled);
		}
	}

	// This method is for the presentation layer's functionality
	public String[] getParsedClassNames() {
		String[] classNames = new String[this.classMap.size()];
		this.classMap.keySet().toArray(classNames);

		return classNames;
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
		List<String> interfaces = decompiled.interfaces;
		String[] result = new String[interfaces.size()];
		interfaces.toArray(result);
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

		List<String> methodList = new ArrayList<>();

		for (MethodNode node : decompiled.methods) {
			methodList.add(node.name);
		}

		String[] result = new String[methodList.size()];

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
			if (node.name.equals(methodName)) {
				decompMethod = node;
			}
		}

		// Just a safeguard
		if (decompMethod == null) {
			throw new IllegalArgumentException("Error! Specified Method was not found in the class!");
		}

		List<String> exceptions = new ArrayList<String>();
		exceptions = decompMethod.exceptions;

		String[] result = new String[exceptions.size()];
		exceptions.toArray(result);
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
			if (node.name.equals(methodName)) {
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

		String[] result = new String[caughtExceptionTypes.size()];
		caughtExceptionTypes.toArray(result);
		return result;
	}

	public Set<String> getMethodCompilerAnnotations(String className, String methodName) {
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
		List<AnnotationNode> annotations = decompMethod.invisibleAnnotations;
		Set<String> annotationStrs = new HashSet<String>();

		if (annotations != null) {
			for (AnnotationNode annotation : annotations) {
				annotationStrs.add(annotation.toString());
			}
		}

		return annotationStrs;
	}

	public ArrayList<String> getClassFieldNames(String className) {
		ArrayList<String> fieldNames = new ArrayList<>();
		ClassNode classNode = this.classMap.get(className);

		for (FieldNode field : classNode.fields) {
			if ((field.access & Opcodes.ACC_STATIC) == 0) {
				fieldNames.add(field.name);
			}
		}

		return fieldNames;
	}

	public ArrayList<String> getGlobalNames(String className) {
		ArrayList<String> globalNames = new ArrayList<>();
		ClassNode classNode = this.classMap.get(className);

		for (FieldNode field : classNode.fields) {
			if ((field.access & Opcodes.ACC_STATIC) != 0) {
				globalNames.add(field.name);
			}
		}

		return globalNames;
	}

	public Map<String, ArrayList<String>> getMethodNames(String className) {
		Map<String, ArrayList<String>> methodNames = new HashMap<>();
		ClassNode classNode = this.classMap.get(className);

		for (MethodNode method : classNode.methods) {
			ArrayList<String> methodVar = new ArrayList<>();
			for (LocalVariableNode local : method.localVariables) {
				if (local.name.compareTo("this") != 0)
					methodVar.add(local.name);
			}
			methodNames.put(method.name, methodVar);
		}

		return methodNames;
	}

	public void getMethod(String className) {
		ClassNode decompiled = this.classMap.get(className);

		for (MethodNode node : decompiled.methods) {
			// TableSwitchInsnNode table = node.visitJumpInsn(Opcodes.TABLESWITCH, new
			// Label());
		}

	}

	public String getSignature(String className) {
		return (this.classMap.get(className).signature);
	}

	private MethodNode getMethodNode(String className, String methodName) {
		ClassNode decompiled = this.classMap.get(className);
		List<MethodNode> methods = decompiled.methods;
		MethodNode method = null;
		for (MethodNode mNode : methods) {
			if (mNode.name.equals(methodName)) {
				method = mNode;
				break;
			}
		}
		if (method == null) {
			throw new IllegalArgumentException("Error! Specified Method was not found in the class!");
		}
		return method;
	}
	
	public List<MethodCall> getMethodCalls(String className, String methodName) {
		List<MethodCall> methodCalls = new ArrayList<MethodCall>();
		MethodNode method = this.getMethodNode(className, methodName);
		Analyzer<SourceValue> analyzer = new Analyzer<SourceValue>(new SourceInterpreter());
		try {
			Frame<SourceValue> [] frames = analyzer.analyze(className, method);
			for(int i = 0; i < frames.length; i++) {
				AbstractInsnNode insn = method.instructions.get(i);
				if (insn.getType() == AbstractInsnNode.METHOD_INSN) {
					for(int j = 0; j < frames[i].getStackSize(); j++) {
						SourceValue value = (SourceValue) frames[i].getStack(j);
						for(AbstractInsnNode insn2 : value.insns) {
							switch(insn2.getType()) {
								case AbstractInsnNode.FIELD_INSN:
									methodCalls.add(new MethodCall(((MethodInsnNode) insn).name, 
										    Invoker.FIELD, 
										    ((FieldInsnNode) insn2).name));
									break;
								case AbstractInsnNode.VAR_INSN:
									VarInsnNode varInsn = (VarInsnNode) insn2;
									Type [] arguments = Type.getArgumentTypes(method.desc);
									if(varInsn.var > 0 && varInsn.var < arguments.length + 1) {
										methodCalls.add(new MethodCall(((MethodInsnNode) insn).name, 
											    Invoker.PARAMETER, 
											    method.localVariables.get(varInsn.var).name));
									} else {
										methodCalls.add(new MethodCall(((MethodInsnNode) insn).name, 
											    Invoker.INITIALIZED, 
											    method.localVariables.get(varInsn.var).name));
									}
									break;
								case AbstractInsnNode.METHOD_INSN:
									methodCalls.add(new MethodCall(((MethodInsnNode) insn).name, 
										    Invoker.RETURNED, 
										    ""));
									break;
								default:
									break;
							}		
						}
					}
				}
			}
		} catch (AnalyzerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		return methodCalls;
	}

}
