import { Module } from '@nestjs/common';
import { ScheduleModule } from '@nestjs/schedule';
import { TaskService } from './task.service';
import { ChallengeModule } from 'src/challenge/challenge.module';
import { ChallengeListModule } from 'src/challengeList/challengeList.module';

@Module({
  imports: [ScheduleModule.forRoot(), ChallengeModule, ChallengeListModule],
  providers: [TaskService],
})
export class TaskModule {}
