package domain.analyzer;

import datasource.ASMParser;
import domain.DomainAnalyzer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import domain.ErrType;
import domain.LinterError;
import domain.ReturnType;

public class TemplateMethodAnalyzer extends DomainAnalyzer {

    private ASMParser parser;

    //Parsed Data
    public Map<String, String> extendedClasses;
    public Map<String, List<List<String>>> abstractMethods;
    public Map<String, List<List<String>>> concreteMethods;
    public Map<String, Map<List<String>, List<String>>> abstractInConcrete;

    public List<LinterError> foundPatterns;

    public List<String> trashCollection;

    public TemplateMethodAnalyzer(ASMParser parser) {
        this.parser = parser;
        this.extendedClasses = new HashMap<>();
        this.abstractMethods = new HashMap<>();
        this.concreteMethods = new HashMap<>();
        this.abstractInConcrete = new HashMap<>();
        this.foundPatterns = new ArrayList<>();
    }


    public void getRelevantData(String[] classList) {
        for (String className : classList) {
            this.extendedClasses.put(className, this.parser.getSuperName(className));
            this.abstractMethods.put(className, this.parser.getAbstractMethods(className));
            this.concreteMethods.put(className, this.parser.getConcreteMethods(className));
        }

        for (String className : this.abstractMethods.keySet()) {
            this.abstractInConcrete.put(className, new HashMap<>());
            for (List<String> methodName : this.concreteMethods.get(className)) {
                this.abstractInConcrete.get(className).put(methodName,
                        this.parser.getAbstractMethodsInConcrete(className, methodName,
                                this.abstractMethods.get(className)));
            }
        }
    }


    public void analyzeData() {
        for (String className : this.abstractInConcrete.keySet()) {
            for (List<String> methodName : this.abstractInConcrete.get(className).keySet()) {
                    for (String subclassName : this.extendedClasses.keySet()) {
                        boolean allConcrete = true;
                        if (this.extendedClasses.get(subclassName).compareTo(className) == 0) {
                            for (String abstractMethod : this.abstractInConcrete.get(className).get(methodName)) {
                                boolean counter = false;

                                String abstractDesc = null;
                                for (int i = 0; i < this.abstractMethods.get(className).size(); i++) {
                                    if (this.abstractMethods.get(className).get(i).get(0).compareTo(abstractMethod) == 0) {
                                        abstractDesc = this.abstractMethods.get(className).get(i).get(1);
                                    }
                                }
                                for (int i = 0; i < this.concreteMethods.get(subclassName).size(); i++) {
                                    if (this.concreteMethods.get(subclassName).get(i).get(0).compareTo(abstractMethod) == 0 &&
                                            abstractDesc != null &&
                                    this.concreteMethods.get(subclassName).get(i).get(1).compareTo(abstractDesc) == 0) {
                                        counter = true;
                                    }
                                }
                                if (!counter) {
                                    allConcrete = false;
                                }
                            }
                            if (allConcrete && methodName.get(0).compareTo("<init>") != 0) {
                                    this.foundPatterns.add(new LinterError(className, methodName.get(0),
                                            "Template Method Pattern Found: " + methodName.get(0) + " (Subclass: " + subclassName + ")",
                                            ErrType.PATTERN));
                            }
                        }
                    }
            }
        }
    }


    public ReturnType composeReturnType() {
        return new ReturnType("TemplateMethodAnalyzer", this.foundPatterns);
    }

    public Map<String, String> getExtendedClasses() {
        return this.extendedClasses;
    }

    public Map<String, List<List<String>>> getAbstractMethods() {
        return this.abstractMethods;
    }

    public Map<String, List<List<String>>> getConcreteMethods() {
        return this.concreteMethods;
    }

    public Map<String, Map<List<String>, List<String>>> getAbstractInsideConcreteMethods() {
        return this.abstractInConcrete;
    }
}
