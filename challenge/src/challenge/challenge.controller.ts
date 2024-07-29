import {
  Body,
  Controller,
  Get,
  HttpException,
  HttpStatus,
  NotFoundException,
  Post,
  Query,
} from '@nestjs/common';
import { UserAuth } from 'src/auth/headers.decorator';
import { UserAuthDto } from 'src/auth/userAuth.dto';
import { ChallengeService } from './challenge.service';
import { Types } from 'mongoose';
import { NewChallengeRequestDto } from './dto/request/new-challenge.dto';
import { ChallengeBannerResponseDto } from './dto/response/challenge-data-res.dto';
import { CompleteChallengeDto } from './dto/response/challenge-complete-res.dto';

@Controller()
export class ChallengeController {
  constructor(private readonly challengeService: ChallengeService) {}

  /**
   * 관리자 기능: 챌린지 생성
   * @param userAuth
   * @param request
   * @returns
   */
  @Post('auth/new')
  async getAuth(
    @UserAuth('user') userAuth: UserAuthDto,
    @Body() newChallenge: NewChallengeRequestDto,
  ): Promise<Types.ObjectId> {
    if (!userAuth.isAdmin())
      throw new HttpException('권한 없음', HttpStatus.UNAUTHORIZED);

    const id: Types.ObjectId =
      await this.challengeService.newChallenge(newChallenge);

    return id;
  }

  @Get('banner')
  async banner(): Promise<ChallengeBannerResponseDto> {
    const banner = await this.challengeService.getBanner();
    return banner;
  }

  @Get('complete')
  async completelist(
    @Query('pageNumber') pageNumber: number,
  ): Promise<CompleteChallengeDto[]> {
    const list = await this.challengeService.getCompleteList(pageNumber);
    return list;
  }

  /**
   * 챌린지 pagination 기능
   * @param page
   * @returns
   */
  @Get('list')
  async getChallenges(@Query('pageNumber') page: number) {
    return (await this.challengeService.challengeList(page)).docs;
  }

  @Get('view')
  async getChallenge(@Query('challengeId') challengeId: string) {
    try {
      return await this.challengeService.getChallengeAndChallengeVideo(
        challengeId,
      );
    } catch (error) {
      throw new NotFoundException('존재하지 않는 챌린지 입니다.');
    }
  }
}
