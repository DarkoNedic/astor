package fr.inria.astor.core.solutionsearch.population;

import fr.inria.astor.core.entities.TestCaseVariantValidationResult;
import fr.inria.astor.core.entities.VariantValidationResult;
import fr.inria.astor.core.entities.ProgramVariant;
import fr.inria.astor.core.entities.ModificationPoint;
import java.util.List;

/**
 * Fitness function based on test suite execution.
 * 
 * @author Matias Martinez
 *
 */
public class TestCaseFitnessFunction implements FitnessFunction {

	/**
	 * In this case the fitness value is associate to the failures: LESS FITNESS
	 * is better.
	 */
	public double calculateFitnessValue(ProgramVariant programVariant) {
		VariantValidationResult validationResult = programVariant.getValidationResult();
		
		List<ModificationPoint> modPoints = programVariant.getModificationPoints();
		
		/* for(ModificationPoint mod : modPoints){
			System.out.println("Modification point " + mod.getCodeElement().toString());
		}
		*/
		
		if (validationResult == null)
			return this.getWorstMaxFitnessValue();

		TestCaseVariantValidationResult result = (TestCaseVariantValidationResult) validationResult;
		return result.getFailureCount();
	}

	public double getWorstMaxFitnessValue() {

		return Double.MAX_VALUE;
	}
}
