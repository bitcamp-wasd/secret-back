import { Module } from '@nestjs/common';
import { MongooseModule } from '@nestjs/mongoose';
import { CommentSchema } from './comment.collection';
import { CommentController } from './comment.controller';
import { CommentService } from './comment.service';
import { ConfigModule } from 'src/module/config.module';
import { RestApiModule } from 'src/restApi/api.module';

import { ChallengeListSchema } from 'src/challengeList/challengeList.collection';

@Module({
  imports: [
    MongooseModule.forFeature([
      { name: 'Comment', schema: CommentSchema },
      { name: 'ChallengeList', schema: ChallengeListSchema },
    ]),
    ConfigModule,
    RestApiModule,
  ],
  controllers: [CommentController],
  providers: [CommentService],
})
export class CommentModule {}
