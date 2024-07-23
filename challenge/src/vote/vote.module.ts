import { Module } from '@nestjs/common';
import { VoteService } from './vote.service';
import { MongooseModule } from '@nestjs/mongoose';
import { VoteSchema } from './vote.collection';

@Module({
  imports: [MongooseModule.forFeature([{ name: 'Vote', schema: VoteSchema }])],
  providers: [VoteService],
  exports: [VoteService],
})
export class VoteModule {}
