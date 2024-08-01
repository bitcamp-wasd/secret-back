import { Module } from '@nestjs/common';
import { S3Module } from 'src/aws/s3.module';
import { ChallengeListController } from './challengeList.controller';
import { ChallengeListService } from './challengeList.service';
import { MongooseModule } from '@nestjs/mongoose';
import { ChallengeListSchema } from './challengeList.collection';
import { ChallengeModule } from 'src/challenge/challenge.module';
import { VoteModule } from 'src/vote/vote.module';
import { RestApiModule } from 'src/restApi/api.module';

@Module({
  imports: [
    S3Module,
    MongooseModule.forFeature([
      { name: 'ChallengeList', schema: ChallengeListSchema },
    ]),
    ChallengeModule,
    VoteModule,
    RestApiModule,
  ],
  controllers: [ChallengeListController],
  providers: [ChallengeListService],
  exports: [ChallengeListService],
})
export class ChallengeListModule {}
