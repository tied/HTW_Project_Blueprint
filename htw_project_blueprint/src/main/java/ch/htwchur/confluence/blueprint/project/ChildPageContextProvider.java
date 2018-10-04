package ch.htwchur.confluence.blueprint.project;

import com.atlassian.confluence.plugins.createcontent.api.contextproviders.AbstractBlueprintContextProvider;
import com.atlassian.confluence.plugins.createcontent.api.contextproviders.BlueprintContext;

import  com.atlassian.confluence.plugins.createcontent.api.contextproviders.BlueprintContextKeys;


public class ChildPageContextProvider extends AbstractBlueprintContextProvider
{
    @Override
    protected BlueprintContext updateBlueprintContext(BlueprintContext context)
    {
    	context.put(BlueprintContextKeys.NO_PAGE_TITLE_PREFIX.key(), "true");

        return context;
    }
}
