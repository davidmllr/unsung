package de.berlin.htw.mueller.frontend.redirects;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import com.wrapper.spotify.requests.authorization.authorization_code.AuthorizationCodeRequest;
import de.berlin.htw.mueller.backend.spotify.Spotify;
import de.berlin.htw.mueller.frontend.MainLayout;
import org.apache.hc.core5.http.ParseException;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Route(value = "spotify/callback", layout = MainLayout.class)
public class SpotifyCallbackView extends VerticalLayout implements HasUrlParameter<String> {

    private final SpotifyApi spotifyApi;

    @Autowired
    public SpotifyCallbackView(Spotify spotify) {
        this.spotifyApi = spotify.getApi();
    }

    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter String parameter) {

        Location location = event.getLocation();
        QueryParameters queryParameters = location
                .getQueryParameters();

        Map<String, List<String>> parametersMap =
                queryParameters.getParameters();

        String code = parametersMap.get("code").get(0);
        System.out.println(code);

        final AuthorizationCodeRequest request = spotifyApi.authorizationCode(code)
                .build();
        authorize(request, event);
    }

    private void authorize(AuthorizationCodeRequest request, BeforeEvent event) {
        try {

            final AuthorizationCodeCredentials authorizationCodeCredentials = request.execute();

            // Set access and refresh token for further "spotifyApi" object usage
            spotifyApi.setAccessToken(authorizationCodeCredentials.getAccessToken());
            spotifyApi.setRefreshToken(authorizationCodeCredentials.getRefreshToken());

            System.out.println("Expires in: " + authorizationCodeCredentials.getExpiresIn());
            UI.getCurrent().getPage().setLocation("start");
        } catch (IOException | SpotifyWebApiException | ParseException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
