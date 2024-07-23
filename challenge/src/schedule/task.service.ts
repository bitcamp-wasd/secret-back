import { Injectable } from '@nestjs/common';
import { Cron } from '@nestjs/schedule';
import { ChallengeService } from 'src/challenge/challenge.service';
import { ChallengeListService } from 'src/challengeList/challengeList.service';

@Injectable()
export class TaskService {
  constructor(
    private readonly challengeService: ChallengeService,
    private readonly challengeListService: ChallengeListService,
  ) {}

  @Cron('0 0 0 * * *')
  async checkCron() {}
}
