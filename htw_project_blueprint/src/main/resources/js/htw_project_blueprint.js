AJS.bind("blueprint.wizard-register.ready", function () {
	function submitProjectSpace(e, state) {
	    state.pageData.ContentPageTitle = state.pageData.name;
	    return Confluence.SpaceBlueprint.CommonWizardBindings.submit(e, state);
	}
	function preRenderProjectSpace(e, state) {
	    state.soyRenderContext['atlToken'] = AJS.Meta.get('atl-token');
	    state.soyRenderContext['showSpacePermission'] = false;
	}
	
	Confluence.Blueprint.setWizard('ch.htwchur.confluence.blueprint.project.htw_project_blueprint:project-space-blueprint-item', function(wizard) {
	    wizard.on("submit.projectSpaceId", submitProjectSpace);
	    wizard.on("pre-render.projectSpaceId", preRenderProjectSpace);
	    wizard.on("post-render.projectSpaceId", Confluence.SpaceBlueprint.CommonWizardBindings.postRender);
	    
	});
});




