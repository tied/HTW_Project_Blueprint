package ch.htwchur.confluence.blueprint.project;

import com.atlassian.confluence.plugins.createcontent.api.events.BlueprintPageCreateEvent;

import com.atlassian.confluence.plugins.createcontent.api.events.SpaceBlueprintCreateEvent;
import com.atlassian.confluence.plugins.createcontent.api.events.SpaceBlueprintHomePageCreateEvent;
import com.atlassian.event.api.EventListener;
import com.atlassian.event.api.EventPublisher;
import com.atlassian.plugin.ModuleCompleteKey;
import com.atlassian.plugin.spring.scanner.annotation.imports.ConfluenceImport;
import com.atlassian.user.Group;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import com.atlassian.confluence.security.SpacePermission;
import com.atlassian.confluence.spaces.Space;
import com.atlassian.confluence.user.ConfluenceUser;
import com.atlassian.confluence.user.UserAccessor;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

@Named
public class BlueprintListener implements InitializingBean, DisposableBean {

	@ConfluenceImport
	private EventPublisher eventPublisher;

	@ConfluenceImport
	UserAccessor userAccessor;

	private static final ModuleCompleteKey MY_BLUEPRINT_KEY = new ModuleCompleteKey("ch.htwchur.confluence.blueprint.project.htw_project_blueprint", "project-space-blueprint");
	private static final Logger log = LoggerFactory.getLogger(BlueprintListener.class);
	private static final String userFieldKey = "userMultiField";

	@Inject
	public BlueprintListener(EventPublisher eventPublisher,UserAccessor userAccessor) {
		this.eventPublisher = eventPublisher;
		this.userAccessor = userAccessor;
	}

	@EventListener
	public void onBlueprintCreateEvent(BlueprintPageCreateEvent event) {

//		ContentPermission editRestriction = new ContentPermission();
//        ContentPermissionManager cpm = new DefaultContentPermissionManager();
//        Collection&lt;ContentPermission&gt; cp = new ArrayList&lt;ContentPermission&gt;();
//        editRestriction = ContentPermission.createGroupPermission(ContentPermission.EDIT_PERMISSION,UserAccessor.GROUP_CONFLUENCE_ADMINS);
//        editRestriction.setCreationDate(event.getPage().getCreationDate());                
//        cp.add(editRestriction);
//        cpm.setContentPermissions(cp, event.getContent(),ContentPermission.EDIT_PERMISSION);  
	}


	@EventListener
	public void onBlueprintCreateEvent(SpaceBlueprintCreateEvent event) {

		Space space = event.getSpace();
		
		// permissionManager.createDefaultSpacePermissions(event.getSpace());
					List<SpacePermission> permissions = space.getPermissions();

					 List<SpacePermission> permissionToRemove = new ArrayList<SpacePermission>();
					 
					// remove editspace for confluence-usres
					for (SpacePermission perm : permissions) {
						if (perm.getGroup() == "confluence-users") {
							if ((perm.getType().equals(SpacePermission.COMMENT_PERMISSION)) || (perm.getType().equals(SpacePermission.REMOVE_OWN_CONTENT_PERMISSION))
									|| (perm.getType().equals(SpacePermission.VIEWSPACE_PERMISSION))) {
								// do nothing, keep this permissions
							} else {
								// remove all other permissions
									permissionToRemove.add(perm);
							//	space.removePermission(perm);
							}

						}
					}
					
					for(SpacePermission permToRemov : permissionToRemove) {
					space.removePermission(permToRemov);
					}
					
		
		if (event.getContext().containsKey(userFieldKey)) {

			String usersFieldContent = event.getContext().get(userFieldKey).toString();
			String[] users = usersFieldContent.split(",");

			List<String> permissionsToGive = new ArrayList<String>();
			permissionsToGive.add(SpacePermission.CREATEEDIT_PAGE_PERMISSION);
			permissionsToGive.add(SpacePermission.VIEWSPACE_PERMISSION);
			permissionsToGive.add(SpacePermission.EDITBLOG_PERMISSION);
			permissionsToGive.add(SpacePermission.REMOVE_OWN_CONTENT_PERMISSION);
			permissionsToGive.add(SpacePermission.EXPORT_SPACE_PERMISSION);
			permissionsToGive.add(SpacePermission.COMMENT_PERMISSION);
			permissionsToGive.add(SpacePermission.REMOVE_PAGE_PERMISSION);
			permissionsToGive.add(SpacePermission.REMOVE_BLOG_PERMISSION);
			permissionsToGive.add(SpacePermission.REMOVE_ATTACHMENT_PERMISSION);
			permissionsToGive.add(SpacePermission.CREATE_ATTACHMENT_PERMISSION);
			permissionsToGive.add(SpacePermission.REMOVE_COMMENT_PERMISSION);





			for (String username : users) {

				for (String permission : permissionsToGive) {
					ConfluenceUser confluenceUser = userAccessor.getUserByName(username);
					SpacePermission spacePerm = SpacePermission.createUserSpacePermission(permission, space, confluenceUser);
					space.addPermission(spacePerm);
				}

			}

			

		}

	}

	@EventListener
	public void onBlueprintCreateEvent(SpaceBlueprintHomePageCreateEvent event) {

	}

	@Override
	public void afterPropertiesSet() throws Exception {
		eventPublisher.register(this);
	}

	@Override
	public void destroy() throws Exception {
		eventPublisher.unregister(this);
	}

}
