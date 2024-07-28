import { Body, Controller, Get, Post, Query } from '@nestjs/common';
import { ChallengeListService } from './challengeList.service';
import { challengeUploadDto } from './dto/request/challenge-upload.dto';
import { UserAuth } from 'src/auth/headers.decorator';
import { UserAuthDto } from 'src/auth/userAuth.dto';
import { ChallengeUploadResponseDto } from './dto/response/challenge-upload-res.dto';
import { VoteRequestDto } from 'src/vote/dto/request/voteRequest.dto';

@Controller()
export class ChallengeListController {
  constructor(private readonly challengeListService: ChallengeListService) {}

  /**
   * 해당하는 챌린지에 동영상 업로드 기능 참가
   * @param userAuth
   * @param challenge
   * @returns
   */
  @Post('auth/upload')
  async uploadChallenge(
    @UserAuth('user') userAuth: UserAuthDto,
    @Body() challenge: challengeUploadDto,
  ) {
    const presignedUrl: ChallengeUploadResponseDto =
      await this.challengeListService.uploadChallenge(userAuth, challenge);
    return presignedUrl;
  }

  @Post('auth/vote')
  async voteChallenge(
    @UserAuth('user') userAuth: UserAuthDto,
    @Body() voteRequestDto: VoteRequestDto,
  ) {
    await this.challengeListService.voteChallengeList(userAuth, voteRequestDto);
  }

  @Get('watch')
  async watch(@Query('videoId') videoId: string) {
    return await this.challengeListService.findByChallengeVideo(videoId);
  }

  @Post('auth/vote/check')
  async checkVote(
    @UserAuth('user') userAuth: UserAuthDto,
    @Body() voteRequestDto: VoteRequestDto,
  ) {
    return await this.challengeListService.checkVote(userAuth, voteRequestDto);
  }
}
