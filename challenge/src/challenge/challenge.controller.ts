import {
  Body,
  Controller,
  Get,
  HttpException,
  HttpStatus,
  Post,
  Query,
} from '@nestjs/common';
import { UserAuth } from 'src/auth/headers.decorator';
import { UserAuthDto } from 'src/auth/userAuth.dto';
import { ChallengeService } from './challenge.service';
import { Types } from 'mongoose';
import { NewChallengeRequestDto } from './dto/request/new-challenge.dto';

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

  /**
   * 챌린지 pagination 기능
   * @param page
   * @returns
   */
  @Get('list')
  async getChallenges(@Query('pageNumber') page: number) {
    return (await this.challengeService.challengeList(page)).docs;
  }

  @Get('auth/test')
  async getTest(
    @UserAuth('user') userAuth: UserAuthDto,
    @Query('test') test: string,
  ) {
    try {
      await this.challengeService.chekcUserDuplicate(userAuth, test);
      return await this.challengeService.getChallengeAndChallengeVideo(test);
    } catch (error) {
      throw error;
    }
  }

  @Get('view')
  async getChallenge(@Query('challengeId') challengeId: string) {
    return await this.challengeService.getChallengeAndChallengeVideo(
      challengeId,
    );
  }
}
