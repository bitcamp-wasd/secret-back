import { Prop, Schema, SchemaFactory } from '@nestjs/mongoose';
import * as mongoosePaginate from 'mongoose-paginate-v2';

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

const schema = SchemaFactory.createForClass(ChallengeList);
schema.set('timestamps', { createdAt: true });
schema.plugin(mongoosePaginate);
export const ChallengeListSchema = schema;
