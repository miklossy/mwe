/*
* generated by Xtext
*/
package org.eclipse.emf.mwe2.language.ui.contentassist;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.mwe2.language.mwe2.Assignment;
import org.eclipse.emf.mwe2.language.mwe2.Component;
import org.eclipse.emf.mwe2.language.mwe2.DeclaredProperty;
import org.eclipse.emf.mwe2.language.mwe2.Module;
import org.eclipse.emf.mwe2.language.scoping.Mwe2ScopeProvider;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.xtext.common.types.JvmFeature;
import org.eclipse.xtext.common.types.JvmOperation;
import org.eclipse.xtext.common.types.JvmType;
import org.eclipse.xtext.common.types.xtext.ui.TypeMatchFilters;
import org.eclipse.xtext.resource.IEObjectDescription;
import org.eclipse.xtext.scoping.IScope;
import org.eclipse.xtext.ui.editor.contentassist.ContentAssistContext;
import org.eclipse.xtext.ui.editor.contentassist.ICompletionProposalAcceptor;

import com.google.common.base.Function;
import com.google.inject.Inject;

/**
 * see http://www.eclipse.org/Xtext/documentation/latest/xtext.html#contentAssist on how to customize content assistant
 */
public class Mwe2ProposalProvider extends AbstractMwe2ProposalProvider {
	
	@Inject
	private NamespaceAwareTypesProposalProvider typeProposalProvider;
	
	@Override
	public Mwe2ScopeProvider getScopeProvider() {
		return (Mwe2ScopeProvider) super.getScopeProvider();
	}
	
	@Override
	public void completeAssignment_Feature(EObject model,
			org.eclipse.xtext.Assignment assignment, ContentAssistContext context,
			ICompletionProposalAcceptor acceptor) {
		if (model instanceof Assignment && model.eContainer() instanceof Component) {
			createFeatureProposals((Component) model.eContainer(), context, acceptor);
		} else if (model instanceof Component) {
			createFeatureProposals((Component) model, context, acceptor);
		}
	}
	
	protected void createFeatureProposals(Component component,
			ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
		IScope scope = getScopeProvider().createComponentFeaturesScope(component);
		Iterable<IEObjectDescription> candidates = scope.getAllContents();
		Function<IEObjectDescription, ICompletionProposal> factory = getProposalFactory("ID", context);
		for (IEObjectDescription candidate: candidates) {
			if (!acceptor.canAcceptMoreProposals())
				return;
			acceptor.accept(factory.apply(candidate));
		}
	}
	
	@Override
	public void completeComponent_Type(EObject model,
			org.eclipse.xtext.Assignment assignment,
			ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
		if (model instanceof Component)
			model = model.eContainer();
		if (model instanceof Module) {
			typeProposalProvider.createTypeProposals(this, context, TypeMatchFilters.canInstantiate(), acceptor);	
		} else if (model instanceof Assignment) {
			Assignment attribute = (Assignment) model;
			if (attribute.getFeature() == null || attribute.getFeature().eIsProxy())
				return;
			JvmFeature feature = attribute.getFeature();
			if (feature instanceof JvmOperation) {
				JvmType parameterType = ((JvmOperation) feature).getParameters().get(0).getParameterType().getType();
				typeProposalProvider.createSubTypeProposals(parameterType, this, context, TypeMatchFilters.canInstantiate(), acceptor);
			}
		}
	}
	
	@Override
	protected String getDisplayString(EObject element, String qualifiedName,
			String shortName) {
		if (element instanceof DeclaredProperty) {
			return qualifiedName;
		}
		return super.getDisplayString(element, qualifiedName, shortName);
	}	
}