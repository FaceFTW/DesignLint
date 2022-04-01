package main;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import presentation.PresentationLayer;

public class LinterMain {
	public static void main(String[] args) {
		// Presentation layer will only expect a list of files, so check for dirs in
		// case
		List<String> classFilesToAnalyze = new ArrayList<>();
		int flags = 0;

		for (String string : args) {

			// There should be no beginning hyphen for the list of files, therefore we can
			// assume this is likely an actual path
			if (string.charAt(0) != '-') {
				getClassList(classFilesToAnalyze, string);
			} else {
				flags = setFlags(flags, string);
			}
		}

		// For now just test if we recurse properly
		// for (String path : classList) {
		// System.out.println(path);
		// }

		// Do a buncha bitmasking here
		String[] classList = new String[classFilesToAnalyze.size()];
		classFilesToAnalyze.toArray(classList);

		// If there are no analyzer flags set, enable all of them
		if ((flags & PresentationLayer.ALL_ANALYZERS) != 0x0) {
			flags = flags | PresentationLayer.ALL_ANALYZERS;
		}

		PresentationLayer frontend = new PresentationLayer(flags);
		frontend.setupAnalyzers(classList);
		frontend.runAnalyzers();
		frontend.vomitOutput(System.out);

	}

	private static void getClassList(List<String> classFilesToAnalyze, String string) {
		File pathListed = new File(string);

		if (pathListed.isDirectory()) {
			String[] recursed = recurseDirectory(string);
			if (recursed != null & recursed.length > 0) {
				classFilesToAnalyze.addAll(Arrays.asList(recursed));
			}
		} else if (pathListed.getName().contains(".class")) {
			classFilesToAnalyze.add(pathListed.getAbsolutePath());
		}

	}

	// Presentation layer will not determine what files to parse, only to parse
	// files
	// Hence directory recursion is defined here and not in the presentation layer
	private static String[] recurseDirectory(String path) {
		List<String> files = new ArrayList<>();
		File currentDir = new File(path);

		// We will check beforehand if this is a directory
		for (File child : currentDir.listFiles()) {
			// I am NOT PROCESSING SYMLINKS, THAT'S JUST STUPID
			if (Files.isSymbolicLink(child.toPath())) {
				throw new IllegalArgumentException(
						"Error, I found a symlink and I don't want to touch that yucky stuff >:P");
			} else if (child.isFile() && child.getName().contains(".class")) {
				// We will resolve paths as we find them
				files.add(child.getAbsolutePath());
			} else {
				// This *must* be a directory, so recurse
				// The end condition is when there are no more dirs to parse
				if (child.isDirectory()) {
					String[] recursed = recurseDirectory(child.getAbsolutePath());
					if (recursed != null && recursed.length > 0) {
						files.addAll(Arrays.asList(recursed));
					}
				}
			}
		}

		String[] result = new String[files.size()];
		files.toArray(result);
		return result;
	}

	private static int setFlags(int inFlags, String switchStr) {
		// All options will be single char/multi char (analyzers only)
		switch (switchStr.charAt(1)) {
			case 'v':
				// case 'V':
				return inFlags | PresentationLayer.VERBOSE_FLAG;
			case 'h':
				// case 'H':
				return inFlags | PresentationLayer.HELP_FLAG;

			case 'a':
				// Limited to if-else her because string compares
				// should also only be only two chars
				String analyzerFlag = switchStr.substring(2, switchStr.length());
				if (analyzerFlag.length() >= 3) {
					break;
				}

				// Now actual analyzer flag stuff
				if (analyzerFlag.equals("GN")) {
					return inFlags | PresentationLayer.GENERIC_NAME_ANALYZER_FLAG;
				}
				if (analyzerFlag.equals("VN")) {
					return inFlags | PresentationLayer.VAR_NAME_ANALYZER_FLAG;
				}
				if (analyzerFlag.equals("ET")) {
					return inFlags | PresentationLayer.EXCEPTION_THROWN_ANALYZER_FLAG;
				}
				if (analyzerFlag.equals("EH")) {
					return inFlags | PresentationLayer.EQUALS_HASHCODE_ANALYZER_FLAG;
				}
				if (analyzerFlag.equals("HC")) {
					return inFlags | PresentationLayer.HIGH_COUPLING_ANALYZER_FLAG;
				}
				if (analyzerFlag.equals("LK")) {
					return inFlags | PresentationLayer.PRINCIPLE_OF_LEAST_KNOWLEDGE_ANALYZER_FLAG;
				}
				if (analyzerFlag.equals("DR")) {
					return inFlags | PresentationLayer.DRY_ANALYZER_FLAG;
				}
				if (analyzerFlag.equals("CI")) {
					return inFlags | PresentationLayer.CODE_TO_INTERFACE_ANALYZER_FLAG;
				}
				if (analyzerFlag.equals("SI")) {
					return inFlags | PresentationLayer.SINGLETON_ANALYZER_FLAG;
				}
				if (analyzerFlag.equals("OA")) {
					return inFlags | PresentationLayer.OBJECT_ADAPTER_ANALYZER_FLAG;
				}
				if (analyzerFlag.equals("ST")) {
					return inFlags | PresentationLayer.STRATEGY_ANALYZER_FLAG;
				}
				if (analyzerFlag.equals("TM")) {
					return inFlags | PresentationLayer.TEMPLATE_METHOD_ANALYZER_FLAG;
				}

				// Passes to default if for some reason we didn't return
				// TODO should we throw an exception to indicate that param is bad????
			default:
				break;
		}
		// Passthr

		// TODO Add call to print help output

		return inFlags;
		// System.exit(1);
	}

}
