package main;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import presentation.PresentationLayer;

public class LinterMain {
	public static void main(String[] args) {
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

		PresentationLayer frontend = new PresentationLayer();
		frontend.setupAnalyzers(classList);
		frontend.runAnalyzers();
		frontend.vomitOutput(System.out);

	}

	private static String[] recurseDirectory(String path) {
		List<String> files = new ArrayList<>();
		File currentDir = new File(path);

		for (File child : currentDir.listFiles()) {
			if (Files.isSymbolicLink(child.toPath())) {
				throw new IllegalArgumentException(
						"Error, I found a symlink and I don't want to touch that yucky stuff >:P");
			} else if (child.isFile() && child.getName().contains(".class")) {
				files.add(child.getAbsolutePath());
			} else {
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
