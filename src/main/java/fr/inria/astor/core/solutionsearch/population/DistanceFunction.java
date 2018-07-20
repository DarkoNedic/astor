package fr.inria.astor.core.solutionsearch.population;

import fr.inria.astor.core.entities.VariantValidationResult;
import fr.inria.astor.core.solutionsearch.extension.AstorExtensionPoint;
import fr.inria.astor.core.entities.ProgramVariant;

/**
 * Fitness function
 * 
 * @author Matias Martinez
 *
 */
public interface DistanceFunction extends  AstorExtensionPoint {

	public double calculateDistance(ProgramVariant originalVariant, ProgramVariant programVariant);
	
}
