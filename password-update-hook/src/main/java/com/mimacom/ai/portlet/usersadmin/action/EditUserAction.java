package com.mimacom.ai.portlet.usersadmin.action;

import com.liferay.portal.UserPasswordException;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.struts.BaseStrutsPortletAction;
import com.liferay.portal.kernel.struts.StrutsPortletAction;
import com.liferay.portal.kernel.util.Constants;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.util.PortalUtil;
import com.mimacom.ai.portlet.action.util.UpdatePasswordUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.portlet.*;

public class EditUserAction extends BaseStrutsPortletAction {

    private static final Logger log = LoggerFactory.getLogger(EditUserAction.class);

    private static final String NEW_PASSWORD_PARAM_NAME = "password1";

    @Override
    public void processAction(StrutsPortletAction originalStrutsPortletAction, PortletConfig portletConfig, ActionRequest actionRequest, ActionResponse actionResponse) throws Exception
    {
        log.debug("process action..");

		boolean callOriginalMethod = true;
        String cmd = ParamUtil.get(actionRequest, Constants.CMD, StringPool.BLANK);

        if(log.isDebugEnabled()) {
            log.debug("CMD: " + cmd);
        }
        if(cmd.equals("update")) {

            String user = PortalUtil.getUser(actionRequest).getScreenName();
            String oldPassword = PortalUtil.getUserPassword(actionRequest);
            String newPassword = ParamUtil.get(actionRequest, NEW_PASSWORD_PARAM_NAME, StringPool.BLANK);

            if (!newPassword.equals(oldPassword)) {
                callOriginalMethod = UpdatePasswordUtil.updatePassword(user, oldPassword, newPassword);
                
                if( !callOriginalMethod ) {
					final UserPasswordException e = new UserPasswordException(UserPasswordException.PASSWORD_NOT_CHANGEABLE);
					
					SessionErrors.add(actionRequest, e.getClass(), e );
				}
            }
        }

 		if( callOriginalMethod ) {
			originalStrutsPortletAction.processAction(
                originalStrutsPortletAction, portletConfig, actionRequest,
                actionResponse);
		}
    }

    @Override
    public String render(StrutsPortletAction originalStrutsPortletAction, PortletConfig portletConfig, RenderRequest renderRequest, RenderResponse renderResponse) throws Exception {
        return originalStrutsPortletAction.render(portletConfig, renderRequest, renderResponse);
    }

}
