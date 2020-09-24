package de.berlin.htw.mueller.frontend.redirects;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.VaadinSession;
import de.berlin.htw.mueller.frontend.MainLayout;
import twitter4j.Twitter;
import twitter4j.TwitterException;

import java.util.List;
import java.util.Map;

@Route(value = "twitter/callback", layout = MainLayout.class)
public class TwitterCallbackView extends VerticalLayout implements HasUrlParameter<String> {

    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter String parameter) {

        Location location = event.getLocation();
        QueryParameters queryParameters = location
                .getQueryParameters();

        Map<String, List<String>> parametersMap =
                queryParameters.getParameters();

        String oauthToken = parametersMap.get("oauth_token").get(0);
        System.out.println(oauthToken);

        String oauthVerifier = parametersMap.get("oauth_verifier").get(0);
        System.out.println(oauthVerifier);

        try {
            verify(oauthVerifier);
        } catch (TwitterException e) {
            e.printStackTrace();
        }

    }

    /**
     *
     * @param oauthVerifier
     * @throws TwitterException
     */
    private void verify(String oauthVerifier) throws TwitterException {
        Twitter twitter = VaadinSession.getCurrent().getAttribute(Twitter.class);
        twitter.getOAuthAccessToken(oauthVerifier);
        VaadinSession.getCurrent().setAttribute("twitter.requestToken", null);
        VaadinSession.getCurrent().setAttribute("twitter.authorized", true);
        UI.getCurrent().getPage().setLocation("start");
    }
}
