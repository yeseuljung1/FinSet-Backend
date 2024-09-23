package security.handelr;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import security.account.domain.CustomUser;
import security.dto.AuthResultDTO;
import security.dto.UserInfoDTO;
import security.util.JsonResponse;
import security.util.JwtProcessor;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Log4j
@Component
@RequiredArgsConstructor
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtProcessor jwtProcessor;


    private AuthResultDTO makeAuthResult(CustomUser user) {
        String username=user.getUsername();
        String token=jwtProcessor.generateToken(username);

        return new AuthResultDTO(token, UserInfoDTO.of(user.getMember()));

    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        CustomUser user=(CustomUser) authentication.getPrincipal();

        AuthResultDTO result = makeAuthResult(user);
        JsonResponse.send(response,result);
    }


}
