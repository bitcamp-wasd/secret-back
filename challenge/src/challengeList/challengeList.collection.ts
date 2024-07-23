import { Prop, Schema, SchemaFactory } from '@nestjs/mongoose';

@Schema({ collection: 'challengeList' })
export class ChallengeList {
  @Prop()
  videoPath: string;
  @Prop()
  thumbnailPath: string;
  @Prop()
  userId: number;
  @Prop()
  cnt: number;
}

export const ChallengeListSchema = SchemaFactory.createForClass(ChallengeList);
