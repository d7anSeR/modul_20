package example.service;

import example.model.SocialNetwork;

import java.util.List;
import java.util.Optional;

public interface SocialNetworkService {

    void createSocialNetwork(SocialNetwork socialNetwork);

    Optional<SocialNetwork> getSocialNetwork(Long id);

    List<SocialNetwork> getAllSocialNetworks();

    boolean deleteSocialNetwork(Long id);
}
