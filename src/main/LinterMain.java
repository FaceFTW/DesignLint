package main;

import java.io.File;
import java.io.IOException;
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

		for (String string : args) {
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

		String[] classList = new String[classFilesToAnalyze.size()];
		classFilesToAnalyze.toArray(classList);

		// For now just test if we recurse properly
		for (String path : classList) {
			System.out.println(path);
		}

		PresentationLayer frontend = new PresentationLayer();
		try {
			frontend.setupAnalyzers(classList);
			frontend.runAnalyzers();
			frontend.vomitOutput(System.out);
		} catch (IOException e) {
			e.printStackTrace();
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

}