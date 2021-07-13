package ge.eathub.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import ge.eathub.models.AccessToken;
import ge.eathub.security.Authenticator;
import ge.eathub.utils.AuthenticatorFactory;
import ge.eathub.utils.ObjectMapperFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/auth")
public class AuthenticationServlet extends HttpServlet {

    private final ObjectMapper mapper = ObjectMapperFactory.get();

    private final Authenticator authenticator = AuthenticatorFactory.get();


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Credentials credentials = mapper.readValue(req.getReader(), Credentials.class);

        if (authenticator.checkCredentials(credentials.getUsername(), credentials.getPassword())) {
            String token = authenticator.issueAccessToken(credentials.getUsername());
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.setContentType("application/json");
            mapper.writeValue(resp.getWriter(), new AccessToken(token));
//            resp.sendRedirect("/chat.html");
        } else {
            resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
            resp.setContentType("text/plain");
            resp.getWriter().write("Invalid credentials");
        }
    }


    private static class Credentials {

        private String username;

        private String password;

        public Credentials() {

        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }


}