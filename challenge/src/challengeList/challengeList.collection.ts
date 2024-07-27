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
  @Prop()
  title: string;

  @Prop()
  category: string;

  @Prop()
  description: string;

  @Prop()
  length: number;
}

export const ChallengeListSchema = SchemaFactory.createForClass(ChallengeList);
