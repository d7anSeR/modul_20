package example.controller;

import example.model.SocialNetwork;
import example.service.SocialNetworkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/social-network")
public class SocialNetworkController {

    @Autowired
    private SocialNetworkService socialNetworkService;

    @PostMapping
    public ResponseEntity<SocialNetwork> saveSocialNetwork(@RequestBody SocialNetwork socialNetwork) {
        socialNetworkService.createSocialNetwork(socialNetwork);
        return new ResponseEntity<>(socialNetwork, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SocialNetwork> getSocialNetwork(@PathVariable Long id) {
        return ResponseEntity.of(socialNetworkService.getSocialNetwork(id));
    }

    @GetMapping
    public List<SocialNetwork> getAllSocialNetworks() {
        return socialNetworkService.getAllSocialNetworks();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSocialNetwork(@PathVariable Long id) {
        return socialNetworkService.deleteSocialNetwork(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}
