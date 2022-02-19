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
		try {
			for (String className : classList) {
				className = className.replace('.', '/');
				ClassReader reader = new ClassReader(className);

				ClassNode decompiled = new ClassNode();
				reader.accept(decompiled, ClassReader.EXPAND_FRAMES);
				classMap.put(className, decompiled);
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Error reading class definitions!");
			System.exit(1);
		}

		// This should be all we need for the parser to function
	}

	public ASMParser(InputStream[] classStreams) throws IOException {
		this.classMap = new HashMap<>();
		try {
			for (InputStream stream : classStreams) {
				ClassReader reader = new ClassReader(stream);

				ClassNode decompiled = new ClassNode();
				reader.accept(decompiled, ClassReader.EXPAND_FRAMES);

				// We still need the fully qualified class name
				String className = decompiled.name;
				classMap.put(className, decompiled);
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Error reading class definitions!");
			System.exit(1);
		}
	}

	/**
	 * This method is for the presentation layer's functionality.
	 * 
	 * @return String[] classNames
	 */
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

	/**
	 * Returns a list of compiler annotations for a method.
	 * 
	 * @param className  The name of the class where the method should reside in
	 * @param methodName THe name of the method to retrieve compiler annotations
	 *                   from
	 * @return A Set<String> of all of the annotations from the compiler for the
	 *         specified method
	 * 
	 */
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
		List<AnnotationNode> annotations = decompMethod.invisibleAnnotations;
		Set<String> annotationStrs = new HashSet<String>();

		if (annotations != null) {
			for (AnnotationNode annotation : annotations) {
				annotationStrs.add(annotation.toString());
			}
		}

		return annotationStrs;
	}

	/**
	 * Searches through the methods of a class, and finds the onces that are public
	 * facing, static access.
	 * 
	 * @param className the class to be searched for static methods
	 * @return List<String> of methods in the class with the static access modifier
	 */
	public List<String> getStaticMethods(String className) {
		if (!this.classMap.containsKey(className)) {
			throw new IllegalArgumentException("Error! The specified class was not found in the parsed class map.");
		}

		ClassNode decompiled = this.classMap.get(className);

		List<String> methodList = new ArrayList<>();

		for (MethodNode node : decompiled.methods) {
			if (node.access == Opcodes.ACC_PUBLIC + Opcodes.ACC_STATIC) {
				methodList.add(node.name);
			}
		}

		return methodList;
	}

	/**
	 * Determines if this class has a public facing constructor
	 * 
	 * @param className
	 * @return boolean. if the classes constructor is private - true. Otherwise
	 *         false
	 */
	public boolean isClassConstructorPrivate(String className) {
		ClassNode classNode = this.classMap.get(className);

		for (MethodNode method : classNode.methods) {
			if (method.name.equals("<init>")) {
				if (method.access == Opcodes.ACC_PRIVATE) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * searches the given class for fields that have private static access modifiers
	 * 
	 * @param className
	 * @return List<String> of fieldNames that are private static
	 */
	public List<String> getClassStaticPrivateFieldNames(String className) {
		List<String> fieldNames = new ArrayList<>();
		ClassNode classNode = this.classMap.get(className);

		for (FieldNode field : classNode.fields) {

			if (field.access == Opcodes.ACC_PRIVATE + Opcodes.ACC_STATIC) {
				fieldNames.add(field.name);
			}
		}

		return fieldNames;
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

	public Map<String, List<String>> getMethodNamesAndVariables(String className) {
		Map<String, List<String>> methodNames = new HashMap<>();
		ClassNode classNode = this.classMap.get(className);

		for (MethodNode method : classNode.methods) {
			if (method.localVariables == null) {
				methodNames.put(method.name, new ArrayList<String>());
			} else {
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
			} else {
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
		// className = className.replace('.', '/');
		try {
			ClassReader reader = new ClassReader(className);
			ClassNode decompiled = new ClassNode();
			reader.accept(decompiled, ClassReader.EXPAND_FRAMES);

			return decompiled.interfaces;
		} catch (IOException e) {
			System.out.println("Class Not Found: " + className);
			return null;
		}
	}

	public boolean compareMethodFromInterface(String className, String methodName, String interfaceName) {
		try {
			if (!this.classMap.keySet().contains(className)) {
				// System.out.println(className + " not found");
				ClassReader reader = new ClassReader(className);
				ClassNode decompiled = new ClassNode();
				reader.accept(decompiled, ClassReader.EXPAND_FRAMES);
				this.classMap.put(className, decompiled);
			}

			if (!this.classMap.keySet().contains(interfaceName)) {
				// System.out.println(interfaceName + " not found");
				ClassReader reader1 = new ClassReader(interfaceName);
				ClassNode decompiled1 = new ClassNode();
				reader1.accept(decompiled1, ClassReader.EXPAND_FRAMES);
				this.classMap.put(interfaceName, decompiled1);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		MethodNode original;
		MethodNode compare;
		try {
			original = getMethodNode(className, methodName);
		} catch (IllegalArgumentException e) {
			original = null;
			// System.out.println("Func not found in " + className + ", " + methodName );
		}

		try {
			compare = getMethodNode(interfaceName, methodName);
		} catch (IllegalArgumentException e) {
			compare = null;
			// System.out.println("Func not found in " + interfaceName + ", " + methodName);
		}

		if (original != null && compare != null) {
			if (original.desc.compareTo(compare.desc) == 0) {
				return true;
			} else {
				return false;
			}
		}
		return false;
	}

	public List<MethodCall> removeThis(List<MethodCall> list, List<String> names) {
		ArrayList<MethodCall> arrList = (ArrayList<MethodCall>) list;
		if (arrList.get(0).getInvokerName().compareTo("this") != 0) {
			return list;
		} else {
			List<MethodCall> newList = new ArrayList<>();
			for (int i = 0; i < arrList.size() - 1; i++) {
				newList.add(new MethodCall(
						arrList.get(i).getCalledMethodName(),
						arrList.get(i).getInvoker(),
						arrList.get(i + 1).getInvokerName(),
						arrList.get(i).getInvokedClass()));
			}
			if (names.size() > 0) {
				newList.add(new MethodCall(
						arrList.get(arrList.size() - 1).getCalledMethodName(),
						arrList.get(arrList.size() - 1).getInvoker(),
						names.get(names.size() - 1),
						arrList.get(arrList.size() - 1).getInvokedClass()));

			}
			return newList;
		}
	}

	public String getSignature(String className) {
		return (this.classMap.get(className).signature);
	}

	public String getSignatureNonEnum(String className) {
		return (this.classMap.get(className).access - 0x4000) < 0 ? this.getSignature(className) : null;
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

	/**
	 * Provides a list of MethodCall Objects corresponding to method calls within
	 * the specified method
	 * 
	 * @throws IllegalArgumentException If the method is not found in the specified
	 *                                  class
	 * @param className  The name of the class where the method should reside in
	 * @param methodName The name of the method to retrieve method call information
	 *                   from
	 * @return List of MethodCall Objects
	 * 
	 */
	public List<MethodCall> getMethodCalls(String className, String methodName) {
		List<MethodCall> methodCalls = new ArrayList<MethodCall>();
		MethodNode method = this.getMethodNode(className, methodName);
		Analyzer<SourceValue> analyzer = new Analyzer<SourceValue>(new SourceInterpreter());
		Set<String> newVars = new HashSet<String>();
		Set<String> fieldStructVars = new HashSet<String>();
		try {
			Frame<SourceValue>[] frames = analyzer.analyze(className, method);
			instructions: for (int i = 0; i < frames.length; i++) {
				AbstractInsnNode insn = method.instructions.get(i);
				if (insn.getType() == AbstractInsnNode.METHOD_INSN) {
					MethodInsnNode call = (MethodInsnNode) insn;
					if (call.getOpcode() == Opcodes.INVOKESPECIAL && call.name.equals("<init>")) {
						if (!call.owner.equals("java/lang/Object") &&
								call.getNext().getType() == AbstractInsnNode.VAR_INSN) {

							VarInsnNode newVar = (VarInsnNode) call.getNext();
							if (newVar.var < method.localVariables.size()) {
								newVars.add(method.localVariables.get(newVar.var).name);
							}
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
									if (call.owner.length() > 9 && call.owner.substring(0, 9).equals("java/util")
											&& call.getNext().getType() == AbstractInsnNode.TYPE_INSN) {
										if (call.getNext().getNext().getType() == AbstractInsnNode.VAR_INSN) {
											VarInsnNode fieldVar = (VarInsnNode) call.getNext().getNext();
											if (fieldVar.var < method.localVariables.size()) {
												fieldStructVars.add(method.localVariables.get(fieldVar.var).name);
											}
										}
									}
									continue instructions;
								case AbstractInsnNode.VAR_INSN:
									VarInsnNode varInsn = (VarInsnNode) insn2;

									Type[] arguments = Type.getArgumentTypes(method.desc);
									if (varInsn.var > 0 && varInsn.var < arguments.length + 1) {
										methodCalls.add(new MethodCall(((MethodInsnNode) insn).name,
												Invoker.PARAMETER,
												method.localVariables.get(varInsn.var).name,
												call.owner));
									} else if (varInsn.var >= method.localVariables.size()) {
										continue instructions;
									} else if (newVars.contains(method.localVariables.get(varInsn.var).name)) {
										methodCalls.add(new MethodCall(((MethodInsnNode) insn).name,
												Invoker.CONSTRUCTED,
												method.localVariables.get(varInsn.var).name,
												call.owner));
									} else {
										Invoker type = Invoker.RETURNED;
										if (fieldStructVars.contains(method.localVariables.get(varInsn.var).name)) {
											type = Invoker.FIELD;
										}
										methodCalls.add(new MethodCall(((MethodInsnNode) insn).name,
												type,
												method.localVariables.get(varInsn.var).name,
												call.owner));
									}
									continue instructions;
								case AbstractInsnNode.METHOD_INSN:
									Invoker type = Invoker.RETURNED;
									methodCalls.add(new MethodCall(((MethodInsnNode) insn).name,
											type,
											"",
											call.owner));
									continue instructions;
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

			if (betterTypeName.charAt(0) == 'L' && betterTypeName.charAt(betterTypeName.length() - 1) == ';') {
				betterTypeName = betterTypeName.substring(1, betterTypeName.length() - 1);
			}
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

	public boolean isInterface(String className) {
		return ((this.classMap.get(className).access & Opcodes.ACC_INTERFACE) == Opcodes.ACC_INTERFACE);
	}

	public boolean isAbstractClass(String className) {
		return (this.classMap.get(className).access & Opcodes.ACC_ABSTRACT) == Opcodes.ACC_ABSTRACT;
	}

	public boolean isEnum(String className) {
		return ((this.classMap.get(className).access & Opcodes.ACC_ENUM) == Opcodes.ACC_ENUM);
	}

	public boolean isFinal(String className) {
		return (this.classMap.get(className).access & Opcodes.ACC_FINAL) == Opcodes.ACC_FINAL;
	}

	public boolean allMethodsStatic(String className) {
		for (MethodNode method : this.classMap.get(className).methods) {
			if ((!method.name.equals("<init>") &&
					(method.access & Opcodes.ACC_STATIC) != Opcodes.ACC_STATIC)) {
				return false;
			}
		}
		return true;
	}
}
