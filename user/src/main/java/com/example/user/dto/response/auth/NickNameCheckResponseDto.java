package com.example.user.dto.response.auth;

import com.example.user.common.ResponseCode;
import com.example.user.common.ResponseMessage;
import com.example.user.dto.response.ResponseDto;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
public class NickNameCheckResponseDto extends ResponseDto {

    private NickNameCheckResponseDto(){
        super();
    }

    public static ResponseEntity<NickNameCheckResponseDto> success(){
        NickNameCheckResponseDto responseBody = new NickNameCheckResponseDto();
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }

    public static ResponseEntity<ResponseDto> duplicateNickName(){
        ResponseDto responseBody = new ResponseDto(ResponseCode.DUPLICATE_NICKNAME, ResponseMessage.DUPLICATE_NICKNAME);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
    }
}
