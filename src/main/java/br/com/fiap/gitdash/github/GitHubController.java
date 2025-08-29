package br.com.fiap.gitdash.github;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;

import java.util.List;

@Controller
public class GitHubController {

    private final GitHubService gitHubService;

    public GitHubController(GitHubService gitHubService) {
        this.gitHubService = gitHubService;
    }

    @GetMapping("/")
    public String getUserInfo(Model model, @RegisteredOAuth2AuthorizedClient("github") OAuth2AuthorizedClient authorizedClient, @AuthenticationPrincipal OAuth2User principal) {

        String tokenValue = authorizedClient.getAccessToken().getTokenValue();
        List<RepositoryInfo> repos = gitHubService.getUserRepositories(tokenValue);
        String username = principal.getName();
        String avatarUrl = principal.getAttribute("avatar_url");
        String htmlUrl = principal.getAttribute("html_url");

        model.addAttribute("repos", repos);
        model.addAttribute("name", username);
        model.addAttribute("html_url", htmlUrl);
        model.addAttribute("avatar_url", avatarUrl);

        return "user";
    }

    @GetMapping("/login")
    public String index() {
        return "login";
    }

    @GetMapping("/logout")
    public String logout() {
        return "logout";
    }
}