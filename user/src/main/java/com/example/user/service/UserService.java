package com.example.user.service;

import com.example.user.common.ValidationUtil;
import com.example.user.component.Exception.DuplicateException;
import com.example.user.component.Exception.NicknameFormatException;
import com.example.user.component.Exception.PasswordFormatException;
import com.example.user.dto.info.*;
import com.example.user.dto.info.response.UserApiInfo;
import com.example.user.dto.info.response.UserListDto;
import com.example.user.dto.info.response.UserRankInfo;
import com.example.user.entity.UserEntity;
import com.example.user.entity.UserRankEntity;
import com.example.user.repository.UserRankRepository;
import com.example.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class UserService {

    private final UserRepository userRepository;
    private final UserRankRepository userRankRepository;
    private final ValidationUtil validationUtil;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private boolean isValidPassword(String password) {
        String passwordPattern = "^(?=.*[a-zA-Z])(?=.*[0-9])[a-zA-Z0-9]{8,13}$";
        return password != null && password.matches(passwordPattern);
    }

    @Transactional
    public void addUserPoints(Long userId, int points){

        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        //랭크 정보 데이터베이스에서 가져오기
        List<UserRankEntity> ranks = userRankRepository.findAll();
        user.addPoints(points, ranks);
        userRepository.save(user);
    }

    public UserInfoDto getUserInfo(Long userId){

        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        UserInfoDto userInfoDto = new UserInfoDto(
                userEntity.getEmail(),
                userEntity.getNickname(),
                userEntity.getRankId().getRankName(),
                userEntity.getPoint()
        );
        return userInfoDto;
    }

    public void updateUser(Long userId, UpdateUserInfoDto dto){

        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        String nickname = dto.getNickName();

        if (!validationUtil.isValidNickname(nickname)){
            throw new NicknameFormatException("닉네임 형식에 맞춰주세요");
        }

        if (!userEntity.getNickname().equals(nickname)){
            boolean isExistNickName = userRepository.existsByNickname(dto.getNickName());
            if (isExistNickName){
                throw new DuplicateException("Nickname already exists");
            }
        }

        String password = dto.getPassword();
        if (!isValidPassword(password)){
            throw new PasswordFormatException("비밀번호 형식에 맞춰주세요.");
        }

        userEntity.setNickname(dto.getNickName());
        userEntity.setPassword(passwordEncoder.encode(password));
        userRepository.save(userEntity);
    }


    public UserApiInfo getUserApiInfo(Long userId){

        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        UserApiInfo userApiInfo = new UserApiInfo(
                userEntity.getNickname(),
                userEntity.getRankId().getRankName()
        );
        return userApiInfo;
    }

    public UserRankInfo getRankApiInfo(Long userId){

        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        UserRankInfo userRankInfo = new UserRankInfo(
                userEntity.getNickname(),
                userEntity.getRankId().getImagePath()
        );
        return userRankInfo;
    }

    public List<UserListDto> getUserList(List<Long> userId){

        List<UserEntity> users = userRepository.findByUserIdIn(userId);

        List<UserListDto> userList = users.stream()
                .map(user -> new UserListDto(user.getUserId(), user.getNickname(), user.getRankId().getRankName()))
                .collect(Collectors.toList());

        return userList;
    }

}
