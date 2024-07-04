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

        if (oauth2ClientName.equals("kakao")) {
            // 카카오 사용자 정보 가져오기
            Map<String, Object> kakaoAccount = (Map<String, Object>) oAuth2User.getAttributes().get("kakao_account");
            email = (String) kakaoAccount.get("email");
            nickname = (String) kakaoAccount.get("profile_nickname");
            if (email == null) {
                email = oAuth2User.getName() + "@kakao.com";  // 이메일이 없는 경우 고유 id를 사용하여 이메일 생성
            }
            if (nickname == null) {
                nickname = generateRandomNickName("kakao_");
            }
        }

//        if (oauth2ClientName.equals("kakao")){
//            email = oAuth2User.getAttributes().get("id") + "@kakao";
//            nickname = generateRandomNickName("kakao_");
//
//        }

        if (oauth2ClientName.equals("naver")){

            Map<String, String> responseMap = (Map<String, String>) oAuth2User.getAttributes().get("response");
            email = responseMap.get("email");
            nickname = generateRandomNickName("naver_");
        }


        // 이미 등록된 사용자인지 확인
        userEntity = userRepository.findByEmail(email);
        if (userEntity != null) {
            // 이미 등록된 사용자이므로 로그인 처리
            return new CustomOAuth2User(email,userEntity.getUserId(), userEntity.getRole(), nickname);
        } else {
            // 새로운 사용자 등록
            UserRankEntity defaultRank = userRankRepository.findByMinPoint(0)
                    .orElseThrow(() -> new IllegalArgumentException("Default rank not found"));
            userEntity = new UserEntity(email, nickname, oauth2ClientName, defaultRank);
            userRepository.save(userEntity);
            return new CustomOAuth2User(email, userEntity.getUserId(), userEntity.getRole(), nickname);
        }
    }

    private String generateRandomNickName(String prefix) {

        String randomNumber;
        do {
            randomNumber = prefix + random.nextInt(1000000);
        }while (userRepository.existsByNickname(randomNumber));
        return randomNumber;
    }


}
