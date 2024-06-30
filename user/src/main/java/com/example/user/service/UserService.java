package com.example.user.service;

import com.example.user.dto.UpdateUserInfoDto;
import com.example.user.dto.UserInfoBannerDto;
import com.example.user.dto.UserInfoDto;
import com.example.user.entity.UserEntity;
import com.example.user.entity.UserRankEntity;
import com.example.user.repository.UserRankRepository;
import com.example.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserRankRepository userRankRepository;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public void addUserPoints(Long userId, int points){

        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        //랭크 정보 데이터베이스에서 가져오기
        List<UserRankEntity> ranks = userRankRepository.findAll();
        user.addPoints(points, ranks);
        userRepository.save(user);
    }

    public UserInfoBannerDto getUserInfoBanner(Long userId){

        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        UserInfoBannerDto userInfoBannerDto = new UserInfoBannerDto(
                userEntity.getNickname(),
                userEntity.getRankId().getRankName()
        );

        return userInfoBannerDto;
    }

    public UserInfoDto getUserInfo(Long userId){

        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        UserInfoDto userInfoDto = new UserInfoDto(
                userEntity.getEmail(),
                userEntity.getNickname(),
                userEntity.getPoint()
        );
        return userInfoDto;
    }

    public void updateUser((Long userId, UpdateUserInfoDto dto){

        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));


        if (!userEntity.getNickname().equals(dto.getNickName())){
            boolean isExistNickName = userRepository.existsByNickname(dto.getNickName());
            if (isExistNickName){
                throw new IllegalArgumentException("Nickname already exists");
            }
        }

        userEntity.setNickname(dto.getNickName());
        userEntity.setPassword(passwordEncoder.encode(dto.getPassword()));
        userRepository.save(userEntity);
    }
}
