package fr.inria.astor.core.solutionsearch.population;

import fr.inria.astor.core.entities.TestCaseVariantValidationResult;
import fr.inria.astor.core.entities.VariantValidationResult;
import fr.inria.astor.core.entities.ProgramVariant;
import fr.inria.astor.core.entities.ModificationPoint;

import java.util.List;
import java.util.Iterator;

import gumtree.spoon.AstComparator;
import gumtree.spoon.diff.Diff;
import gumtree.spoon.diff.operations.Operation;

import spoon.reflect.declaration.CtElement;
import spoon.reflect.visitor.Filter;

import java.lang.Math;

/**
 * Fitness function based on test suite execution combined with a distance function.
 * 
 * @author Darko Nedic, Falk Meyer-Eschenbach, Maike Basmer, Christopher Krizanovic, Serra Karadeniz
 *
 */
public class TestCaseAndDistanceFitnessFunction implements FitnessFunction {

	/**
	 * In this case the fitness value is associate to the failures: LESS FITNESS
	 * is better.
	 */
	public double calculateFitnessValue(ProgramVariant programVariant) {
		VariantValidationResult validationResult = programVariant.getValidationResult();
		
		if (validationResult == null)
			return this.getWorstMaxFitnessValue();

		TestCaseVariantValidationResult result = (TestCaseVariantValidationResult) validationResult;
		
		
		return result.getFailureCount();
	}
	
	
	public double calculateFitnessValue(ProgramVariant programVariant, ProgramVariant originalVariant) {
		VariantValidationResult validationResult = programVariant.getValidationResult();
		int modSize = 0;
		int origSize = 0;
		int changes = 0;
		double factor;
		double weight = 0.5;
		int difference;
		
		List<ModificationPoint> modifiedModPoints = programVariant.getModificationPoints();
		List<ModificationPoint> originalModPoints = originalVariant.getModificationPoints();
		
		Iterator<ModificationPoint> modIterator = modifiedModPoints.iterator();
		Iterator<ModificationPoint> origIterator = originalModPoints.iterator();
		
		while(origIterator.hasNext() && modIterator.hasNext()){
			ModificationPoint orig = origIterator.next();
			ModificationPoint mod = modIterator.next();

			List<CtElement> origElems = orig.getCodeElement().getElements(new Filter<CtElement>(){
				@Override
				public boolean matches(CtElement t){
					return true;
				}
				
			});

			List<CtElement> modElems = mod.getCodeElement().getElements(new Filter<CtElement>(){
				@Override
				public boolean matches(CtElement t){
					return true;
				}
				
			});

			modSize += modElems.size();
			origSize += origElems.size();
			

			
			AstComparator comparator = new AstComparator();
			Diff diff = comparator.compare(orig.getCodeElement(), mod.getCodeElement());
			//List<Operation> operations = diff.getRootOperations();
			List<Operation> operations = diff.getAllOperations();

			if(operations != null){
				changes += operations.size();
			} 
			
			//System.out.println("MAIKE: " + changes);
		}
		
		
		if (validationResult == null)
			return this.getWorstMaxFitnessValue();

		TestCaseVariantValidationResult result = (TestCaseVariantValidationResult) validationResult;
		
		//int casesExecuted = validationResult.getCasesExecuted();
		int casesExecuted = result.getCasesExecuted();

		//System.out.println("DARKO: " + result.getFailureCount() + "/" + result.getCasesExecuted());
		
		difference = Math.abs(modSize-origSize);
		factor = (changes == 0) ? 0 : 1 - (((double) difference) / changes);
		System.out.println("origSize:" + origSize + ", modSize: " + modSize + ", changes: " + changes);
		System.out.println("Result: " + (result.getFailureCount()) + ", NeuResult: " + (result.getFailureCount()*(1-weight)+result.getFailureCount()*weight*factor));
		return result.getFailureCount()*(1-weight)+result.getFailureCount()*weight*factor;
	}

	public double getWorstMaxFitnessValue() {

		return Double.MAX_VALUE;
	}
}
