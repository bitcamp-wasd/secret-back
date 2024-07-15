import {
  Controller,
  HttpException,
  HttpStatus,
  Post,
  Req,
} from '@nestjs/common';
import { UserAuth } from 'src/auth/headers.decorator';
import { UserAuthDto } from 'src/auth/userAuth.dto';
import { ChallengeService } from './challenge.service';

@Controller()
export class ChallengeController {
  constructor(private readonly challengeService: ChallengeService) {}

  @Post('auth/new')
  getAuth(
    @UserAuth('user') userAuth: UserAuthDto,
    @Req() request: Request,
  ): UserAuthDto {
    if (!userAuth.isAdmin())
      throw new HttpException('권한 없음', HttpStatus.UNAUTHORIZED);

    this.challengeService.newChallenge(request.body);

    return userAuth;
  }
}
