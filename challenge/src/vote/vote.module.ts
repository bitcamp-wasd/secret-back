import { Module } from '@nestjs/common';
import { VoteService } from './vote.service';
import { MongooseModule } from '@nestjs/mongoose';
import { VoteSchema } from './vote.collection';
import { ChallengeModule } from 'src/challenge/challenge.module';

@Module({
  imports: [
    MongooseModule.forFeature([{ name: 'Vote', schema: VoteSchema }]),
    ChallengeModule,
  ],
  providers: [VoteService],
  exports: [VoteService],
})
export class VoteModule {}
