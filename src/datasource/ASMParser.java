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

	public List<String> getClassFieldNames(String className) {
		List<String> fieldNames = new ArrayList<>();
		ClassNode classNode = this.classMap.get(className);

		for (FieldNode field : classNode.fields) {
			if ((field.access & Opcodes.ACC_STATIC) == 0) {
				fieldNames.add(field.name);
			}
		}

		return fieldNames;
	}

	public List<String> getGlobalNames(String className) {
		List<String> globalNames = new ArrayList<>();
		ClassNode classNode = this.classMap.get(className);

		for (FieldNode field : classNode.fields) {
			if ((field.access & Opcodes.ACC_STATIC) != 0) {
				globalNames.add(field.name);
			}
		}

		return globalNames;
	}

	public Map<String, List<String>> getMethodNames(String className) {
		Map<String, List<String>> methodNames = new HashMap<>();
		ClassNode classNode = this.classMap.get(className);

		for (MethodNode method : classNode.methods) {
			if (method.localVariables == null) {
				methodNames.put(method.name, new ArrayList<String>());
			}
			else {
				ArrayList<String> methodVar = new ArrayList<>();
				for (LocalVariableNode local : method.localVariables) {
					if (local.name.compareTo("this") != 0)
						methodVar.add(local.name);
				}
				methodNames.put(method.name, methodVar);
			}
		}

		return methodNames;
	}

	public List<String> getClassFieldTypes(String className) {
		List<String> fieldTypes = new ArrayList<>();
		ClassNode classNode = this.classMap.get(className);
		if (classNode == null) {
			System.out.println("Node not found");
			return null;
		}

		for (FieldNode field : classNode.fields) {
				fieldTypes.add(field.desc);
		}

		return fieldTypes;
	}

	public Map<String, List<String>> getMethodVarTypes(String className) {
		Map<String, List<String>> methodNames = new HashMap<>();
		ClassNode classNode = this.classMap.get(className);

		for (MethodNode method : classNode.methods) {
			if (method.localVariables == null) {
				methodNames.put(method.name, new ArrayList<String>());
			}
			else {
				ArrayList<String> methodVar = new ArrayList<>();
				for (LocalVariableNode local : method.localVariables) {
					if (local.name.compareTo("this") != 0)
						methodVar.add(local.desc);
				}
				methodNames.put(method.name, methodVar);
			}
		}

		return methodNames;
	}

	public List<String> getInterfacesWithoutMap(String className) {
		className = className.replace('.', '/');
		try {
			ClassReader reader = new ClassReader(className);
			ClassNode decompiled = new ClassNode();
			reader.accept(decompiled, ClassReader.EXPAND_FRAMES);

			return decompiled.interfaces;
		}
		catch (IOException e) {
			System.out.println("Class Not Found: " + className);
			return null;
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
		Set<String> newVars = new HashSet<String>();
		try {
			Frame<SourceValue>[] frames = analyzer.analyze(className, method);
			for (int i = 0; i < frames.length; i++) {
				AbstractInsnNode insn = method.instructions.get(i);
				if (insn.getType() == AbstractInsnNode.METHOD_INSN) {
					MethodInsnNode call = (MethodInsnNode) insn;
					if (call.getOpcode() == Opcodes.INVOKESPECIAL && call.name.equals("<init>")) {
						if (!call.owner.equals("java/lang/Object") &&
								call.getNext().getType() == AbstractInsnNode.VAR_INSN) {
							VarInsnNode newVar = (VarInsnNode) call.getNext();
							newVars.add(method.localVariables.get(newVar.var).name);
						}
					}
					for (int j = 0; j < frames[i].getStackSize(); j++) {
						SourceValue value = (SourceValue) frames[i].getStack(j);
						for (AbstractInsnNode insn2 : value.insns) {
							switch (insn2.getType()) {
								case AbstractInsnNode.FIELD_INSN:
									methodCalls.add(new MethodCall(((MethodInsnNode) insn).name,
											Invoker.FIELD,
											((FieldInsnNode) insn2).name,
											call.owner));
									break;
								case AbstractInsnNode.VAR_INSN:
									VarInsnNode varInsn = (VarInsnNode) insn2;

									Type[] arguments = Type.getArgumentTypes(method.desc);
									if (varInsn.var > 0 && varInsn.var < arguments.length + 1) {
										methodCalls.add(new MethodCall(((MethodInsnNode) insn).name,
												Invoker.PARAMETER,
												method.localVariables.get(varInsn.var).name,
												call.owner));
									} else if (newVars.contains(method.localVariables.get(varInsn.var).name)) {
										methodCalls.add(new MethodCall(((MethodInsnNode) insn).name,
												Invoker.CONSTRUCTED,
												method.localVariables.get(varInsn.var).name,
												call.owner));
									} else {
										methodCalls.add(new MethodCall(((MethodInsnNode) insn).name,
												Invoker.RETURNED,
												method.localVariables.get(varInsn.var).name,
												call.owner));
									}
									break;
								case AbstractInsnNode.METHOD_INSN:
									methodCalls.add(new MethodCall(((MethodInsnNode) insn).name,
											Invoker.RETURNED,
											"",
											call.owner));
									break;
								default:
									break;
							}
						}
					}
				}
			}
		} catch (AnalyzerException e) {
			e.printStackTrace();
		}

		return methodCalls;
	}

	/**
	 * Determines all of the types used by fields of a specified parsed class.
	 * This does not actually associate any information about what field has what
	 * type, rather it just gives an unordered list of all field types.
	 * 
	 * @param className The name of the class to process
	 * @return An array of unique strings containing the internal type names of
	 *         fields
	 */
	public String[] getFieldTypeNames(String className) {
		Set<String> types = new HashSet<>();
		ClassNode decompiled = this.classMap.get(className);

		// For each field, add the type if it isn't already in the list
		for (FieldNode field : decompiled.fields) {
			// Parse the description (Signature tends to be wonky and variable w/ generics);
			String internalTypeName = field.desc;
			String betterTypeName = Type.getType(internalTypeName).getInternalName();

			// Each dimension of an array is indicated with a left bracket
			// Only the type should count toward the coupling, so remove them if they exist
			betterTypeName = betterTypeName.replace("[", "");

			// Primitives in the JVM are single letter types, so we can filter them out
			// after removing any array identifiers by checking string length

			// We also can ignore duplicate detection by using a Set, instead of a list
			if (betterTypeName.length() > 1) {
				types.add(betterTypeName);
			}
		}

		String[] result = new String[types.size()];
		types.toArray(result);
		return result;
	}

	/**
	 * Determines all of the types used by method returns of a specified parsed
	 * class. This does not actually associate any information about what field has
	 * what type, rather it just gives an unordered list of all return types.
	 * 
	 * @param className The name of the class to process
	 * @return An array of unique strings containing the internal type names of
	 *         method returns
	 */

	public String[] getAllMethodReturnTypes(String className) {
		Set<String> types = new HashSet<>();
		ClassNode decompiled = this.classMap.get(className);

		// For each field, add the type if it isn't already in the list
		for (MethodNode method : decompiled.methods) {
			// Get the method data
			String betterTypeName = Type.getReturnType(method.desc).getInternalName();

			// We get parameter types from the description, so do some regex matching

			// Method descriptors will have parenthesis, therefore we need to remove them
			// We parse parameter types in a different method as well
			betterTypeName = betterTypeName.replaceAll("\\(.*\\)", "");

			// Each dimension of an array is indicated with a left bracket
			// Only the type should count toward the coupling, so remove them if they exist
			betterTypeName = betterTypeName.replace("[", "");

			// Primitives in the JVM are single letter types, so we can filter them out
			// after removing any array identifiers by checking string length

			// We also can ignore duplicate detection by using a Set, instead of a list
			if (betterTypeName.length() > 1) {
				types.add(betterTypeName);
			}
		}

		String[] result = new String[types.size()];
		types.toArray(result);
		return result;
	}

	/**
	 * Determines all of the types used by method parameters of a specified parsed
	 * class. This does not actually associate any information about what parameter
	 * has what type, rather it just gives an unordered list of all parameter types.
	 * 
	 * @param className The name of the class to process
	 * @return An array of unique strings containing the internal type names of
	 *         method parameters
	 */
	public String[] getAllMethodParameterTypes(String className) {
		Set<String> types = new HashSet<>();
		ClassNode decompiled = this.classMap.get(className);

		// For each field, add the type if it isn't already in the list
		for (MethodNode method : decompiled.methods) {
			// Get the Parameter data
			for (Type paramType : Type.getArgumentTypes(method.desc)) {
				String betterTypeName = "";
				if (paramType.getSort() == Type.ARRAY) {
					betterTypeName = paramType.getClassName();
					betterTypeName = betterTypeName.replace("[]", "");
					betterTypeName = betterTypeName.replace(".", "/");
				} else {
					betterTypeName = paramType.getInternalName();
					betterTypeName = betterTypeName.replace("[", "");
				}
				// Each dimension of an array is indicated with a left bracket
				// Only the type should count toward the coupling, so remove them if they exist

				// Just in case parse the type again (apparently arrays can screw this up)
				// betterTypeName = Type.getType(betterTypeName).getInternalName();

				// Primitives in the JVM are single letter types, so we can filter them out
				// after removing any array identifiers by checking string length

				// We also can ignore duplicate detection by using a Set, instead of a list
				if (betterTypeName.length() > 1) {
					types.add(betterTypeName);
				}
			}

		}

		String[] result = new String[types.size()];
		types.toArray(result);
		return result;
	}

	public String[] getAllMethodBodyTypes(String className) {
		Set<String> types = new HashSet<>();
		ClassNode decompiled = this.classMap.get(className);

		// For each field, add the type if it isn't already in the list
		for (MethodNode method : decompiled.methods) {
			for (AbstractInsnNode instruction : method.instructions) {
				String betterTypeName = "";

				switch (instruction.getType()) {
					case AbstractInsnNode.METHOD_INSN:
						betterTypeName = ((MethodInsnNode) instruction).owner;
						break;
					case AbstractInsnNode.FIELD_INSN:
						betterTypeName = Type.getType(((FieldInsnNode) instruction).desc).getInternalName();
						String ownerTypeName = ((FieldInsnNode) instruction).owner;
						ownerTypeName = ownerTypeName.replace("[", "");
						if (ownerTypeName.length() > 1) {
							types.add(ownerTypeName);
						}
						break;

					default:
						break;
				}

				// Each dimension of an array is indicated with a left bracket
				// Only the type should count toward the coupling, so remove them if they
				// exist
				betterTypeName = betterTypeName.replace("[", "");

				// Primitives in the JVM are single letter types, so we can filter them out
				// after removing any array identifiers by checking string length

				// We also can ignore duplicate detection by using a Set, instead of a list
				if (betterTypeName.length() > 1) {
					types.add(betterTypeName);
				}
			}

		}

		String[] result = new String[types.size()];
		types.toArray(result);
		return result;
	}

	public String[] getAllMethodLocalTypes(String className) {
		Set<String> types = new HashSet<>();
		ClassNode decompiled = this.classMap.get(className);

		// For each field, add the type if it isn't already in the list
		for (MethodNode method : decompiled.methods) {
			if (method.localVariables != null) {
				for (LocalVariableNode local : method.localVariables) {

					String betterTypeName = "";
					if (Type.getType(local.desc).getSort() == Type.ARRAY) {
						betterTypeName = Type.getType(local.desc).getClassName();
						betterTypeName = betterTypeName.replace("[]", "");
						betterTypeName = betterTypeName.replace(".", "/");
					} else {
						betterTypeName = Type.getType(local.desc).getInternalName();
						betterTypeName = betterTypeName.replace("[", "");
					}

					Type.getType(local.desc).getInternalName();

					// Each dimension of an array is indicated with a left bracket
					// Only the type should count toward the coupling, so remove them if they
					// exist
					betterTypeName = betterTypeName.replace("[", "");

					// Primitives in the JVM are single letter types, so we can filter them out
					// after removing any array identifiers by checking string length

					// We also can ignore duplicate detection by using a Set, instead of a list
					if (betterTypeName.length() > 1) {
						types.add(betterTypeName);
					}
				}
			}

		}

		String[] result = new String[types.size()];
		types.toArray(result);
		return result;
	}

	public String[] getExtendsImplementsTypes(String className) {
		Set<String> types = new HashSet<>();
		ClassNode decompiled = this.classMap.get(className);

		// Null check
		if (decompiled.interfaces != null) {
			for (String interfaceType : decompiled.interfaces) {
				// These are explicit class names in the first place, so just add them
				types.add(interfaceType);
			}
		}

		String[] result = new String[types.size()];
		types.toArray(result);
		return result;
	}
}
