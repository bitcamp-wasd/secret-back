import { Module, NestModule } from '@nestjs/common';
import { SpringCloudConfig } from './config/cloud.config';
import { ConfigModule } from './module/config.module';

import mongoose from 'mongoose';
import { MongoModule } from './module/mongo.module';
import { ChallengeModule } from './challenge/challenge.module';
import { S3Module } from './aws/s3.module';
import { ChallengeListModule } from './challengeList/challengeList.module';
import { VoteModule } from './vote/vote.module';
import { TaskModule } from './schedule/task.module';

@Module({
  imports: [
    ConfigModule,
    MongoModule,
    ChallengeModule,
    S3Module,
    ChallengeListModule,
    VoteModule,
    TaskModule,
  ],
  controllers: [],
  providers: [SpringCloudConfig],
})
export class AppModule implements NestModule {
  configure(): any {
    mongoose.set('debug', true);
  }
}
