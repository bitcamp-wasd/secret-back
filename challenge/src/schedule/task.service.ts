import { Injectable, Logger } from '@nestjs/common';
import { Cron } from '@nestjs/schedule';
import { ChallengeService } from 'src/challenge/challenge.service';
import { ChallengeListService } from 'src/challengeList/challengeList.service';

@Injectable()
export class TaskService {
  private readonly logger: Logger = new Logger(TaskService.name);
  constructor(
    private readonly challengeService: ChallengeService,
    private readonly challengeListService: ChallengeListService,
  ) {}

  /**
   * 매 12시 투표 가능 변경
   */
  @Cron('0 0 0 * * *')
  async VoteStart() {
    this.logger.debug('12시 투표 시작');
    this.challengeService.startVoteChallenge();
  }

  @Cron('0 0 10 * * *')
  async endChallenge() {
    this.logger.debug('챌린지 종료');
    this.challengeService.endChallenge();
  }
}
