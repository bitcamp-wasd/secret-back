import { Module } from '@nestjs/common';
import { MongooseModule } from '@nestjs/mongoose';
import { ChallengeSchema } from './challenge.collection';
import { ChallengeController } from './challenge.controller';
import { ChallengeService } from './challenge.service';
import { RestApiModule } from 'src/restApi/api.module';
import { ConfigModule } from 'src/module/config.module';

@Module({
  imports: [
    MongooseModule.forFeature([{ name: 'Challenge', schema: ChallengeSchema }]),
    RestApiModule,
    ConfigModule,
  ],
  controllers: [ChallengeController],
  providers: [ChallengeService],
  exports: [ChallengeService],
})
export class ChallengeModule {}
