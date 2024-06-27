package com.example.user.service.implement;

import com.example.user.entity.CustomOAuth2User;
import com.example.user.entity.UserEntity;
import com.example.user.entity.UserRankEntity;
import com.example.user.repository.UserRankRepository;
import com.example.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OAuth2UserServiceImpl extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final UserRankRepository userRankRepository;
    private final SecureRandom random = new SecureRandom();

    @Override
    public OAuth2User loadUser(OAuth2UserRequest request) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(request);
        String oauth2ClientName = request.getClientRegistration().getClientName();

//         try {
//
//         System.out.println(new
//                 ObjectMapper().writeValueAsString(oAuth2User.getAttributes()));
//
//         } catch (Exception exception) {
//         exception.printStackTrace();
//         }

        UserEntity userEntity = null;
        String email = null;
        String nickname = null;

        if (oauth2ClientName.equals("kakao")){
            email = oAuth2User.getAttributes().get("id") + "@kakao";
            nickname = generateRandomNickName("kakao_");
            UserRankEntity defaultRank = userRankRepository.findByMinPoint(0)
                    .orElseThrow(() -> new IllegalArgumentException("Default rank not found"));
            userEntity = new UserEntity(email, nickname, "kakao", defaultRank);
        }

        if (oauth2ClientName.equals("naver")){

            Map<String, String> responseMap = (Map<String, String>) oAuth2User.getAttributes().get("response");
            email = responseMap.get("email");
            nickname = generateRandomNickName("naver_");
            UserRankEntity defaultRank = userRankRepository.findByMinPoint(0)
                    .orElseThrow(() -> new IllegalArgumentException("Default rank not found"));
            userEntity = new UserEntity(email, nickname, "naver", defaultRank);
        }

        if(userRepository.existsByEmail(email)){
            throw new OAuth2AuthenticationException("이미 존재하는 이메일 입니다.");
        }

        userRepository.save(userEntity);

        return new CustomOAuth2User(email);

    }

    private String generateRandomNickName(String prefix) {

        String randomNumber;
        do {
            randomNumber = prefix + random.nextInt(1000000);
        }while (userRepository.existsByNickname(randomNumber));
        return randomNumber;
    }


}
